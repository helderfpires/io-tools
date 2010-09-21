package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008,2010 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.EasyStreamConstants;
import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;
import com.gc.iotools.stream.utils.LogUtils;

/**
 * <p>
 * This class allow to read from an <code>InputStream</code> the data who has
 * been written to an <code>OutputStream</code> (performs an
 * <code>OutputStream</code> -> <code>InputStream</code> conversion).
 * </p>
 * <p>
 * More detailiy it is an <code>OutputStream</code> that, when extended,
 * allows to read the data written to it from the <code>InputStream</code>
 * inside the method {@linkplain #doRead(InputStream)}.
 * </p>
 * <p>
 * To use this class you must extend it and implement the method
 * {@linkplain #doRead(InputStream)}. Inside this method place the logic that
 * needs to read the data from the <code>InputStream</code>. Then the data can
 * be written to this class that implements <code>OutputStream</code>. When
 * {@linkplain #close()} method is called on the outer
 * <code>OutputStream</code> an EOF is generated in the
 * <code>InputStream</code> passed in the {@linkplain #doRead(InputStream)}.
 * </p>
 * <p>
 * The {@linkplain #doRead(InputStream)} call executes in another thread, so
 * there is no warranty on when it will start and when it will end. Special
 * care must be taken in passing variables to it: all the arguments must be
 * final and inside {@linkplain #doRead(InputStream)} you shouldn't change the
 * variables of the outer class.
 * </p>
 * <p>
 * Any Exception threw inside the {@linkplain #doRead(InputStream)} method is
 * propagated to the outer <code>OutputStream</code> on the next
 * <code>write</code> operation.
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
 * OutputStreamToInputStream&lt;String&gt; oStream2IStream = 
 * new OutputStreamToInputStream&lt;String&gt;() {
 * 	protected String doRead(final InputStream istream) throws Exception {
 * 		// Users of this class should place all the code that need to read data
 *      // from the InputStream in this method. Data available through the 
 *      // InputStream passed as a parameter is the data that is written to the 
 * 		// OutputStream oStream2IStream through its write method.  
 * 		final String result = IOUtils.toString(istream);
 * 		return result + &quot; was processed.&quot;;
 * 	}
 * };
 * try {
 * 	// some data is written to the OutputStream, will be passed to the method
 * 	// doRead(InputStream i) above and after close() is called the results 
 * 	// will be available through the getResults() method.  
 * 	oStream2IStream.write(&quot;test&quot;.getBytes());
 * } finally {
 * 	// don't miss the close (or a thread would not terminate correctly).
 * 	oStream2IStream.close();
 * }
 * String result = oStream2IStream.getResults();
 * //result now contains the string &quot;test was processed.&quot;
 * </pre></code>
 * 
 * @param <T>
 *            Type returned by the method {@link #getResults()} after the
 *            thread has finished.
 * @since 1.0
 * @author dvd.smnt
 */
public abstract class OutputStreamToInputStream<T> extends OutputStream {
	/**
	 * This class executes in the second thread.
	 * 
	 * @author dvd.smnt
	 */
	private final class DataConsumer implements Callable<T> {

		private final InputStream inputstream;

		DataConsumer(final InputStream istream) {
			this.inputstream = istream;
		}

		public synchronized T call() throws Exception {
			T processResult;
			try {
				// avoid the internal class close the stream.
				final CloseShieldInputStream istream = new CloseShieldInputStream(
						this.inputstream);
				processResult = doRead(istream);
			} catch (final Exception e) {
				OutputStreamToInputStream.this.abort = true;
				throw e;
			} finally {
				emptyInputStream();
				this.inputstream.close();
			}
			return processResult;
		}

		private void emptyInputStream() {
			try {
				final byte[] buffer = new byte[EasyStreamConstants.SKIP_BUFFER_SIZE];
				while (this.inputstream.read(buffer) >= 0) {
					;
					// empty block: just throw bytes away
				}
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					OutputStreamToInputStream.LOG
							.debug("Stream already closed");

				} else {
					OutputStreamToInputStream.LOG.error(
							"IOException while empty InputStream a "
									+ "thread can be locked", e);
				}
			} catch (final Throwable e) {
				OutputStreamToInputStream.LOG.error(
						"IOException while empty InputStream a "
								+ "thread can be locked", e);
			}
		}
	}

	/**
	 * Extends PipedInputStream to allow set the default buffer size.
	 */
	private final class MyPipedInputStream extends PipedInputStream {

		MyPipedInputStream(final int bufferSize) {
			super.buffer = new byte[bufferSize];
		}
	}

	// Default timeout in milliseconds.
	private static final int DEFAULT_TIMEOUT = 15 * 60 * 1000;

	/**
	 * The default pipe buffer size for the newly created pipes.
	 */
	private static int defaultPipeSize = EasyStreamConstants.DEFAULT_PIPE_SIZE;

	private static final Logger LOG = LoggerFactory
			.getLogger(OutputStreamToInputStream.class);

	/**
	 * <p>
	 * Set the size for the pipe circular buffer. This setting has effect for
	 * the newly created <code>OutputStreamToInputStream</code>. Default is
	 * 4096 bytes.
	 * </p>
	 * <p>
	 * Will be removed in the 1.3 release. Use
	 * {@link #setDefaultPipeSize(int)} instead.
	 * </p>
	 * 
	 * @since 1.2.0
	 * @param defaultPipeSize
	 *            The default pipe buffer size in bytes.
	 * @deprecated
	 * @see #setDefaultPipeSize(int)
	 */
	@Deprecated
	public static void setDefaultBufferSize(final int defaultPipeSize) {
		OutputStreamToInputStream.defaultPipeSize = defaultPipeSize;
	}

	/**
	 * Set the size for the pipe circular buffer. This setting has effect for
	 * the newly created <code>OutputStreamToInputStream</code>. Default is
	 * 4096 bytes.
	 * 
	 * @since 1.2.3
	 * @param defaultPipeSize
	 *            The default pipe buffer size in bytes.
	 */

	public static void setDefaultPipeSize(final int defaultPipeSize) {
		OutputStreamToInputStream.defaultPipeSize = defaultPipeSize;
	}

	private boolean abort = false;
	private boolean closeCalled = false;
	private final boolean joinOnClose;
	private final PipedOutputStream pipedOs;
	private final Future<T> writingResult;

	/**
	 * <p>
	 * Creates a new <code>OutputStreamToInputStream</code>. It uses the
	 * default {@link ExecutionModel#THREAD_PER_INSTANCE} thread instantiation
	 * strategy. This means that a new thread is created for every instance of
	 * <code>OutputStreamToInputStream</code>.
	 * </p>
	 * <p>
	 * When the {@linkplain #close()} method is called this class wait for the
	 * internal thread to terminate.
	 * </p>
	 * 
	 * @throws IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream() {
		this(true, ExecutionModel.THREAD_PER_INSTANCE);
	}

	/**
	 * <p>
	 * Creates a new <code>OutputStreamToInputStream</code>. It let the user
	 * specify the thread instantiation strategy and what will happen upon the
	 * invocation of <code>close()</code> method.
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
	 * @throws IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutionModel executionModel) {
		this(joinOnClose, ExecutorServiceFactory.getExecutor(executionModel));
	}

	/**
	 * <p>
	 * Creates a new <code>OutputStreamToInputStream</code>. It let the user
	 * specify the thread instantiation service and what will happen upon the
	 * invocation of <code>close()</code> method.
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
	 * @throws IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutorService executorService) {
		this(joinOnClose, executorService, defaultPipeSize);
	}

	/**
	 * <p>
	 * Creates a new <code>OutputStreamToInputStream</code>. It let the user
	 * specify the thread instantiation service and what will happen upon the
	 * invocation of <code>close()</code> method.
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
	 * @throws IllegalStateException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutorService executorService, final int pipeBufferSize) {
		if (executorService == null) {
			throw new IllegalArgumentException(
					"executor service can't be null");
		}
		String callerId = LogUtils.getCaller(getClass());
		this.pipedOs = new PipedOutputStream();
		final PipedInputStream pipedIS = new MyPipedInputStream(
				pipeBufferSize);
		try {
			pipedIS.connect(this.pipedOs);
		} catch (IOException e) {
			throw new IllegalStateException("Error during pipe creaton", e);
		}
		final DataConsumer executingProcess = new DataConsumer(pipedIS);
		this.joinOnClose = joinOnClose;
		LOG.debug("invoked by[{}] queued for start.", callerId);
		this.writingResult = executorService.submit(executingProcess);
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @throws IOException
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
	 * This method is called just before the close method completes, and after
	 * the eventual join with the internal thread.
	 * </p>
	 * <p>
	 * It is an extension point designed for applications that need to perform
	 * some operation when the <code>OutputStream</code> is closed.
	 * </p>
	 * @since 1.2.9
	 */
	protected void afterClose() {
		// extension point;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void flush() throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedOs.flush();
		}
	}

	/**
	 * <p>
	 * This method returns the result of the method
	 * {@link #doRead(InputStream)} and ensure the previous method is over.
	 * </p>
	 * <p>
	 * This method suspend the calling thread and waits for the function
	 * {@link #doRead(InputStream)} to finish. It returns when the
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
	 *                Thrown if the method {@linkplain #doRead(InputStream)}
	 *                threw an Exception. The <code>getCause()</code> returns
	 *                the original Exception.
	 * @throws IllegalStateException
	 *             When it is called before the method {@link #close()} has
	 *             been called.
	 * @return the object returned from the doRead() method.
	 */
	public final T getResults() throws InterruptedException,
			ExecutionException {
		if (!this.closeCalled) {
			throw new IllegalStateException("Method close() must be called"
					+ " before getResults");
		}
		return this.writingResult.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void write(final byte[] bytes) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedOs.write(bytes);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void write(final byte[] bytes, final int offset,
			final int length) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedOs.write(bytes, offset, length);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void write(final int bytetowr) throws IOException {
		if (this.abort) {
			// internal thread is already aborting. wait for short time.
			internalClose(true, TimeUnit.SECONDS, 1);
		} else {
			this.pipedOs.write(bytetowr);
		}
	}

	private void internalClose(final boolean join, final TimeUnit timeUnit,
			final long timeout) throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.pipedOs.close();
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
			afterClose();
		}
	}

	/**
	 * <p>
	 * This method has to be implemented to use this class. It allows to
	 * retrieve the data written to the outer <code>OutputStream</code> from
	 * the <code>InputStream</code> passed as a parameter.
	 * </p>
	 * <p>
	 * Any exception eventually threw inside this method will be propagated to
	 * the external <code>OutputStream</code>. When the next
	 * {@linkplain #write(byte[])} operation is called an
	 * <code>IOException</code> will be thrown and the original exception can
	 * be accessed calling the getCause() method on the IOException. It will
	 * also be available by calling the method {@link #getResults()}.
	 * </p>
	 * 
	 * @param istream
	 *            The InputStream where the data can be retrieved.
	 * @return Optionally returns a result of the elaboration.
	 * @throws Exception
	 *             If an <code>Exception</code> occurs during the elaboration
	 *             it can be thrown. It will be propagated to the external
	 *             <code>OutputStream</code> and will be available calling the
	 *             method {@link #getResults()}.
	 */
	protected abstract T doRead(InputStream istream) throws Exception;

}
