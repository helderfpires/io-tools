package com.gc.iotools.stream.writer;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.EasyStreamConstants;
import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;
import com.gc.iotools.stream.reader.CloseShieldReader;
import com.gc.iotools.stream.utils.LogUtils;

/**
 * <p>
 * This class allow to read from an <code>Reader</code> the data who has been
 * written to an <code>Writer</code> (performs an <code>Writer</code> ->
 * <code>Reader</code> conversion).
 * </p>
 * <p>
 * More detailiy it is an <code>Writer</code> that, when extended, allows to
 * read the data written to it from the <code>Reader</code> inside the method
 * {@linkplain #doRead(Reader)}.
 * </p>
 * <p>
 * To use this class you must extend it and implement the method
 * {@linkplain #doRead(Reader)}. Inside this method place the logic that needs
 * to read the data from the <code>Reader</code>. Then the data can be written
 * to this class that implements <code>Writer</code>. When
 * {@linkplain #close()} method is called on the outer <code>Writer</code> an
 * EOF is generated in the <code>Reader</code> passed in the
 * {@linkplain #doRead(Reader)}.
 * </p>
 * <p>
 * The {@linkplain #doRead(Reader)} call executes in another thread, so there
 * is no warranty on when it will start and when it will end. Special care
 * must be taken in passing variables to it: all the arguments must be final
 * and inside {@linkplain #doRead(Reader)} you shouldn't change the variables
 * of the outer class.
 * </p>
 * <p>
 * Any Exception threw inside the {@linkplain #doRead(Reader)} method is
 * propagated to the outer <code>Writer</code> on the next <code>write</code>
 * operation.
 * </p>
 * <p>
 * The method {@link #getResults()} suspend the outer thread and wait for the
 * read from the internal stream is over. It returns when the
 * <code>doRead()</code> terminates and has produced its result.
 * </p>
 * <p>
 * Some sample code:
 * </p>
 * <code>
 * <pre>
 * WriterToReader&lt;String&gt; oStream2Reader =
 * new WriterToReader&lt;String&gt;() {
 * 	protected String doRead(final Reader reader) throws Exception {
 * 		// Users of this class should place all the code that need to read data
 *      // from the Reader in this method. Data available through the
 *      // Reader passed as a parameter is the data that is written to the
 * 		// Writer oStream2Reader through its write method.
 * 		final String result = IOUtils.toString(reader);
 * 		return result + &quot; was processed.&quot;;
 * 	}
 * };
 * try {
 * 	// some data is written to the Writer, will be passed to the method
 * 	// doRead(Reader i) above and after close() is called the results
 * 	// will be available through the getResults() method.
 * 	oStream2Reader.write(&quot;test&quot;.getBytes());
 * } finally {
 * 	// don't miss the close (or a thread would not terminate correctly).
 * 	oStream2Reader.close();
 * }
 * String result = oStream2Reader.getResults();
 * //result now contains the string &quot;test was processed.&quot;
 * </pre></code>
 *
 * @param <T>
 *            Type returned by the method {@link #getResults()} after the
 *            thread has finished.
 * @since 1.2.7
 * @author dvd.smnt
 * @version $Id$
 */
public abstract class WriterToReader<T> extends Writer {
	/**
	 * This class executes in the second thread.
	 * 
	 * @author dvd.smnt
	 */
	private final class DataConsumer implements Callable<T> {

		private final Reader reader;

		DataConsumer(final Reader reader) {
			this.reader = reader;
		}

		@Override
		public synchronized T call() throws Exception {
			T processResult;
			try {
				// avoid the internal class close the stream.
				final CloseShieldReader<Reader> reader = new CloseShieldReader<Reader>(
						this.reader);
				processResult = doRead(reader);
			} catch (final Exception e) {
				WriterToReader.this.abort = true;
				throw e;
			} finally {
				emptyReader();
				this.reader.close();
			}
			return processResult;
		}

		private void emptyReader() {
			try {
				final char[] buffer = new char[EasyStreamConstants.SKIP_BUFFER_SIZE];
				while (this.reader.read(buffer) >= 0) {
					;
					// empty block: just throw bytes away
				}
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					WriterToReader.LOG.debug("Stream already closed");

				} else {
					WriterToReader.LOG.error(
							"IOException while empty Reader a "
									+ "thread can be locked", e);
				}
			} catch (final Throwable e) {
				WriterToReader.LOG.error("IOException while empty Reader a "
						+ "thread can be locked", e);
			}
		}
	}

	// Default timeout in milliseconds.
	private static final int DEFAULT_TIMEOUT = 15 * 60 * 1000;

	/**
	 * The default pipe buffer size for the newly created pipes.
	 */
	private static int defaultPipeSize = EasyStreamConstants.DEFAULT_PIPE_SIZE;

	private static final Logger LOG = LoggerFactory
			.getLogger(WriterToReader.class);



	/**
	 * Set the size for the pipe circular buffer. This setting has effect for
	 * the newly created <code>WriterToReader</code>. Default is 4096 bytes.
	 *
	 * @since 1.2.3
	 * @param defaultPipeSize
	 *            The default pipe buffer size in bytes.
	 */
	public static void setDefaultPipeSize(final int defaultPipeSize) {
		WriterToReader.defaultPipeSize = defaultPipeSize;
	}

	private boolean abort = false;
	private boolean closeCalled = false;
	private final boolean joinOnClose;
	private final PipedWriter pipedWriter;
	private final Future<T> writingResult;

	/**
	 * <p>
	 * Creates a new <code>WriterToReader</code>. It uses the default
	 * {@link ExecutionModel#THREAD_PER_INSTANCE} thread instantiation
	 * strategy. This means that a new thread is created for every instance of
	 * <code>WriterToReader</code>.
	 * </p>
	 * <p>
	 * When the {@linkplain #close()} method is called this class wait for the
	 * internal thread to terminate.
	 * </p>
	 *
	 * @throws java.lang.IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public WriterToReader() {
		this(true, ExecutionModel.THREAD_PER_INSTANCE);
	}

	/**
	 * <p>
	 * Creates a new <code>WriterToReader</code>. It let the user specify the
	 * thread instantiation strategy and what will happen upon the invocation
	 * of <code>close()</code> method.
	 * </p>
	 * <p>
	 * If <code>joinOnClose</code> is <code>true</code> when the
	 * <code>close()</code> method is invoked this class will wait for the
	 * internal thread to terminate.
	 * </p>
	 *
	 * @see ExecutionModel
	 * @param joinOnClose
	 *            if <code>true</code> the internal thread will be joined when
	 *            close is invoked.
	 * @param executionModel
	 *            The strategy for allocating threads.
	 * @throws java.lang.IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public WriterToReader(final boolean joinOnClose,
			final ExecutionModel executionModel) {
		this(joinOnClose, ExecutorServiceFactory.getExecutor(executionModel));
	}

	/**
	 * <p>
	 * Creates a new <code>WriterToReader</code>. It let the user specify the
	 * thread instantiation service and what will happen upon the invocation
	 * of <code>close()</code> method.
	 * </p>
	 * <p>
	 * If <code>joinOnClose</code> is <code>true</code> when the
	 * <code>close()</code> method is invoked this class will wait for the
	 * internal thread to terminate.
	 * </p>
	 *
	 * @since 1.2.6
	 * @param joinOnClose
	 *            if <code>true</code> the internal thread will be joined when
	 *            close is invoked.
	 * @param executorService
	 *            Service for executing the internal thread.
	 * @throws java.lang.IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public WriterToReader(final boolean joinOnClose,
			final ExecutorService executorService) {
		this(joinOnClose, executorService, defaultPipeSize);
	}

	/**
	 * <p>
	 * Creates a new <code>WriterToReader</code>. It let the user specify the
	 * thread instantiation service and what will happen upon the invocation
	 * of <code>close()</code> method.
	 * </p>
	 * <p>
	 * If <code>joinOnClose</code> is <code>true</code> when the
	 * <code>close()</code> method is invoked this class will wait for the
	 * internal thread to terminate.
	 * </p>
	 * <p>
	 * It also let the user specify the size of the pipe buffer to allocate.
	 * </p>
	 *
	 * @since 1.2.6
	 * @param joinOnClose
	 *            if <code>true</code> the internal thread will be joined when
	 *            close is invoked.
	 * @param executorService
	 *            Service for executing the internal thread.
	 * @param pipeBufferSize
	 *            The size of the pipe buffer to allocate.
	 * @throws java.lang.IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public WriterToReader(final boolean joinOnClose,
			final ExecutorService executorService, final int pipeBufferSize) {
		if (executorService == null) {
			throw new IllegalArgumentException(
					"executor service can't be null");
		}
		final String callerId = LogUtils.getCaller(getClass());
		this.pipedWriter = new PipedWriter();
		final PipedReader pipedIS = new PipedReader(pipeBufferSize);
		try {
			pipedIS.connect(this.pipedWriter);
		} catch (final IOException e) {
			throw new IllegalStateException("Error during pipe creaton", e);
		}
		final DataConsumer executingProcess = new DataConsumer(pipedIS);
		this.joinOnClose = joinOnClose;
		LOG.debug("invoked by[{}] queued for start.", callerId);
		this.writingResult = executorService.submit(executingProcess);
	}

	/** {@inheritDoc} */
	@Override
	public final void close() throws IOException {
		internalClose(this.joinOnClose, TimeUnit.MILLISECONDS,
				DEFAULT_TIMEOUT);
	}

	/**
	 * When this method is called the internal thread is always waited for
	 * completion.
	 *
	 * @param timeout
	 *            maximum time to wait for the internal thread to finish.
	 * @param tu
	 *            Time unit for the timeout.
	 * @throws java.io.IOException
	 *             Threw if some problem (timeout or internal exception)
	 *             occurs. see the <code>getCause()</code> method for the
	 *             explanation.
	 */
	public final void close(final long timeout, final TimeUnit tu)
			throws IOException {
		internalClose(true, tu, timeout);
	}

	/**
	 * <p>
	 * This method has to be implemented to use this class. It allows to
	 * retrieve the data written to the outer <code>Writer</code> from the
	 * <code>Reader</code> passed as a parameter.
	 * </p>
	 * <p>
	 * Any exception eventually threw inside this method will be propagated to
	 * the external <code>Writer</code>. When the next
	 * {@linkplain #write(char[])} operation is called an
	 * <code>IOException</code> will be thrown and the original exception can
	 * be accessed calling the getCause() method on the IOException. It will
	 * also be available by calling the method {@link #getResults()}.
	 * </p>
	 *
	 * @param reader
	 *            The Reader where the data can be retrieved.
	 * @return Optionally returns a result of the elaboration.
	 * @throws java.lang.Exception
	 *             If an <code>java.lang.Exception</code> occurs during the elaboration
	 *             it can be thrown. It will be propagated to the external
	 *             <code>Writer</code> and will be available calling the
	 *             method {@link #getResults()}.
	 */
	protected abstract T doRead(Reader reader) throws Exception;

	/** {@inheritDoc} */
	@Override
	public final void flush() throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedWriter.flush();
		}
	}

	/**
	 * <p>
	 * This method returns the result of the method {@link #doRead(Reader)}
	 * and ensure the previous method is over.
	 * </p>
	 * <p>
	 * This method suspend the calling thread and waits for the function
	 * {@link #doRead(Reader)} to finish. It returns when the
	 * <code>doRead()</code> terminates and has produced its result.
	 * </p>
	 * <p>
	 * It must be called after the method {@link #close()} otherwise a
	 * <code>IllegalStateException</code> is thrown.
	 * </p>
	 *
	 * @exception InterruptedException
	 *                Thrown when the thread is interrupted.
	 * @exception ExecutionException
	 *                Thrown if the method {@linkplain #doRead(Reader)} threw
	 *                an Exception. The <code>getCause()</code> returns the
	 *                original Exception.
	 * @throws java.lang.IllegalStateException
	 *             When it is called before the method {@link #close()} has
	 *             been called.
	 * @return the object returned from the doRead() method.
	 * @throws InterruptedException if the running thread is interrupted.
	 * @throws ExecutionException if the internal method launched an exception.
	 * @throws IllegalStateException if {@link #close()} was not called before.
	 */
	public final T getResults() throws InterruptedException,
			ExecutionException {
		if (!this.closeCalled) {
			throw new IllegalStateException("Method close() must be called"
					+ " before getResults");
		}
		return this.writingResult.get();
	}

	private void internalClose(final boolean join, final TimeUnit timeUnit,
			final long timeout) throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.pipedWriter.close();
			if (join) {
				// waiting for thread to finish..
				try {
					this.writingResult.get(timeout, timeUnit);
				} catch (final ExecutionException e) {
					final IOException e1 = new IOException(
							"The doRead() threw exception. Use "
									+ "getCause() for details.");
					e1.initCause(e.getCause());
					throw e1;
				} catch (final InterruptedException e) {
					final IOException e1 = new IOException(
							"Waiting of the thread has been interrupted");
					e1.initCause(e);
					throw e1;
				} catch (final TimeoutException e) {
					if (!this.writingResult.isDone()) {
						this.writingResult.cancel(true);
					}
					final IOException e1 = new IOException(
							"Waiting for the internal "
									+ "thread to finish took more than ["
									+ timeout + "] " + timeUnit);
					e1.initCause(e);
					throw e1;
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void write(final char[] bytes) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedWriter.write(bytes);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void write(final char[] bytes, final int offset,
			final int length) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedWriter.write(bytes, offset, length);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void write(final int chartowrite) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedWriter.write(chartowrite);
		}
	}

}
