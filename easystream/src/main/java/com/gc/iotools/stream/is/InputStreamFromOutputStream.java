package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.EasyStreamConstants;
import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;
import com.gc.iotools.stream.utils.LogUtils;

/**
 * <p>
 * This class allow to read the data written to an OutputStream from an
 * InputStream.
 * </p>
 * <p>
 * To use this class you must subclass it and implement the abstract method
 * {@linkplain #produce(OutputStream)}. The data who is produced inside this
 * function can be written to the sink <code>OutputStream</code> passed as a
 * parameter. Later it can be read back from from the
 * <code>InputStreamFromOutputStream</code> class (whose ancestor is
 * <code>java.io.InputStream</code> ).
 * </p>
 * 
 * <pre>
 * final String dataId=//id of some data.
 * final InputStreamFromOutputStream&lt;String&gt; isos
 *                          = new InputStreamFromOutputStream&lt;String&gt;() {
 *   &#064;Override
 *   public String produce(final OutputStream dataSink) throws Exception {
 *      //call your application function who produces the data here
 *      //WARNING: we're in another thread here, so this method shouldn't
 *      //write any class field or make assumptions on the state of the class.
 *      return produceMydata(dataId,dataSink)
 *   }
 * };
 *  try {
 *    //now you can read from the InputStream the data that was written to the
 *    //dataSink OutputStream
 *     byte[] read=IOUtils.toByteArray(isos);
 *     //Use data here
 *   } catch (final IOException e) {
 *     //Handle exception here
 *   } finally {
 *   isos.close();
 * }
 * //You can get the result of produceMyData after the stream has been closed.
 * String resultOfProduction = isos.getResult();
 * </pre>
 * <p>
 * This class encapsulates a pipe and a <code>Thread</code>, hiding the
 * complexity of using them. It is possible to select different strategies for
 * allocating the internal thread or even specify the
 * {@linkplain ExecutorService} for thread execution.
 * </p>
 * 
 * @param <T>
 *            Optional result returned by the function
 *            {@linkplain #produce(OutputStream)} after the data has been
 *            written. It can be obtained calling the
 *            {@linkplain #getResult()}
 * @see ExecutionModel
 * @author dvd.smnt
 * @since 1.0
 */
public abstract class InputStreamFromOutputStream<T> extends PipedInputStream {
	/**
	 * This inner class run in another thread and calls the
	 * {@link #produce(OutputStream)} method.
	 * 
	 * @author dvd.smnt
	 */
	private final class DataProducer implements Callable<T> {

		private final String name;

		private final OutputStream outputStream;

		DataProducer(final String threadName, final OutputStream ostream) {
			this.outputStream = ostream;
			this.name = threadName;
		}

		@Override
		public T call() throws Exception {
			final String threadName = getName();
			T result;
			InputStreamFromOutputStream.ACTIVE_THREAD_NAMES.add(threadName);
			InputStreamFromOutputStream.LOG.debug("thread [" + threadName
					+ "] started.");
			try {
				result = produce(this.outputStream);
			} finally {
				closeStream();
				InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
						.remove(threadName);
				InputStreamFromOutputStream.LOG.debug("thread [" + threadName
						+ "] closed.");
			}
			return result;
		}

		private void closeStream() {
			try {
				this.outputStream.close();
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					InputStreamFromOutputStream.LOG
							.debug("Stream already closed");
				} else {
					InputStreamFromOutputStream.LOG.error(
							"IOException closing OutputStream"
									+ " Thread might be locked", e);
				}
			} catch (final Throwable t) {
				InputStreamFromOutputStream.LOG.error(
						"Error closing InputStream"
								+ " Thread might be locked", t);
			}
		}

		String getName() {
			return this.name;
		}

	}

	/**
	 * Collection for debugging purpose containing names of threads (name is
	 * calculated from the instantiation line. See <code>getCaller()</code>.
	 */
	private static final List<String> ACTIVE_THREAD_NAMES = Collections
			.synchronizedList(new ArrayList<String>());

	/**
	 * The default pipe buffer size for the newly created pipes.
	 */
	private static int defaultPipeSize = EasyStreamConstants.DEFAULT_PIPE_SIZE;
	private static final Logger LOG = LoggerFactory
			.getLogger(InputStreamFromOutputStream.DataProducer.class);

	/**
	 * This method can be used for debugging purposes to get a list of the
	 * currently active threads.
	 * 
	 * @return Array containing names of the threads currently active.
	 */
	public static final String[] getActiveThreadNames() {
		final String[] result;
		synchronized (InputStreamFromOutputStream.ACTIVE_THREAD_NAMES) {
			result = InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
					.toArray(new String[0]);
		}
		return result;
	}



	/**
	 * Set the size for the pipe circular buffer for the newly created
	 * <code>InputStreamFromOutputStream</code>. Default is 4096 bytes.
	 * 
	 * @since 1.2.2
	 * @param defaultPipeSize
	 *            the default pipe buffer size in bytes.
	 */
	public static void setDefaultPipeSize(final int defaultPipeSize) {
		InputStreamFromOutputStream.defaultPipeSize = defaultPipeSize;
	}

	private boolean closeCalled = false;
	private final Future<T> futureResult;
	private final boolean joinOnClose;


	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> with a
	 * THREAD_PER_INSTANCE thread strategy.
	 * </p>
	 * 
	 * @see ExecutionModel#THREAD_PER_INSTANCE
	 */
	public InputStreamFromOutputStream() {
		this(ExecutionModel.THREAD_PER_INSTANCE);
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * choose the thread allocation strategy he likes.
	 * </p>
	 * <p>
	 * This class executes the produce method in a thread created internally.
	 * </p>
	 * 
	 * @since 1.2.2
	 * @see ExecutionModel
	 * @param executionModel
	 *            Defines how the internal thread is allocated.
	 * @param joinOnClose
	 *            If <code>true</code> the {@linkplain #close()} method will
	 *            also wait for the internal thread to finish.
	 */
	public InputStreamFromOutputStream(final boolean joinOnClose,
			final ExecutionModel executionModel) {
		this(joinOnClose, ExecutorServiceFactory.getExecutor(executionModel));
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * specify the ExecutorService that will execute the
	 * {@linkplain #produce(OutputStream)} method.
	 * </p>
	 * 
	 * @since 1.2.2
	 * @see ExecutorService
	 * @param executor
	 *            Defines the ExecutorService that will allocate the the
	 *            internal thread.
	 * @param joinOnClose
	 *            If <code>true</code> the {@linkplain #close()} method will
	 *            also wait for the internal thread to finish.
	 */
	public InputStreamFromOutputStream(final boolean joinOnClose,
			final ExecutorService executor) {
		this(joinOnClose, executor, defaultPipeSize);
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * specify the <code>ExecutorService</code> that will execute the
	 * {@linkplain #produce(OutputStream)} method and the pipe buffer size.
	 * </p>
	 * <p>
	 * Using this method the default size is ignored.
	 * </p>
	 * 
	 * @since 1.2.6
	 * @see ExecutorService
	 * @param executor
	 *            Defines the ExecutorService that will allocate the the
	 *            internal thread.
	 * @param joinOnClose
	 *            If <code>true</code> the {@linkplain #close()} method will
	 *            also wait for the internal thread to finish.
	 * @param pipeBufferSize
	 *            The size of the pipe buffer to allocate.
	 */
	public InputStreamFromOutputStream(final boolean joinOnClose,
			final ExecutorService executor, final int pipeBufferSize) {
		super(pipeBufferSize);
		final String callerId = LogUtils.getCaller(this.getClass());
		this.joinOnClose = joinOnClose;
		PipedOutputStream pipedOS = null;
		try {
			pipedOS = new PipedOutputStream(this);
		} catch (final IOException e) {
			throw new RuntimeException("Error during pipe creaton", e);
		}
		final Callable<T> executingCallable = new DataProducer(callerId,
				pipedOS);
		this.futureResult = executor.submit(executingCallable);
		InputStreamFromOutputStream.LOG.debug(
				"thread invoked by[{}] queued for start.", callerId);
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * choose the thread allocation strategy he likes.
	 * </p>
	 * <p>
	 * This class executes the produce method in a thread created internally.
	 * </p>
	 * 
	 * @see ExecutionModel
	 * @param executionModel
	 *            Defines how the internal thread is allocated.
	 */
	public InputStreamFromOutputStream(final ExecutionModel executionModel) {
		this(false, executionModel);
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * specify the ExecutorService that will execute the
	 * {@linkplain #produce(OutputStream)} method.
	 * </p>
	 * 
	 * @see ExecutorService
	 * @param executor
	 *            Defines the ExecutorService that will allocate the the
	 *            internal thread.
	 */
	public InputStreamFromOutputStream(final ExecutorService executor) {
		this(false, executor);
	}

	/**
	 * <p>
	 * This method is called just before the {@link #close()} method
	 * completes, and after the eventual join with the internal thread.
	 * </p>
	 * <p>
	 * It is an extension point designed for applications that need to perform
	 * some operation when the <code>InputStream</code> is closed.
	 * </p>
	 * 
	 * @since 1.2.9
	 * @throws IOException
	 *             threw when the subclass wants to communicate an exception
	 *             during close.
	 */
	protected void afterClose() throws IOException {
		// extension point;
	}

	private void checkException() throws IOException {
		try {
			this.futureResult.get();
		} catch (final ExecutionException e) {
			final Throwable t = e.getCause();
			final IOException e1 = new IOException("Exception producing data");
			e1.initCause(t);
			throw e1;
		} catch (final InterruptedException e) {
			final IOException e1 = new IOException("Thread interrupted");
			e1.initCause(e);
			throw e1;

		}
	}

	/** {@inheritDoc} */
	@Override
	public final void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			super.close();
			if (this.joinOnClose) {
				try {
					getResult();
				} catch (final Exception e) {
					final IOException e1 = new IOException(
							"The internal stream threw exception");
					e1.initCause(e);
					throw e1;
				}
			}
			afterClose();
		}
	}

	/**
	 * <p>
	 * Returns the object that was previously returned by the
	 * {@linkplain #produce(OutputStream)} method. It performs all the
	 * synchronization operations needed to read the result and waits for the
	 * internal thread to terminate.
	 * </p>
	 * <p>
	 * This method must be called after the method {@linkplain #close()},
	 * otherwise an IllegalStateException is thrown.
	 * </p>
	 * 
	 * @since 1.2.0
	 * @return Object that was returned by the
	 *         {@linkplain #produce(OutputStream)} method.
	 * @throws java.lang.Exception
	 *             If the {@linkplain #produce(OutputStream)} method threw an
	 *             java.lang.Exception this method will throw again the same
	 *             exception.
	 * @throws java.lang.IllegalStateException
	 *             If the {@linkplain #close()} method hasn't been called yet.
	 */
	public T getResult() throws Exception {
		if (!this.closeCalled) {
			throw new IllegalStateException(
					"getResult() called before close()."
							+ "This method can be called only "
							+ "after the stream has been closed.");
		}
		T result;
		try {
			result = this.futureResult.get();
		} catch (final ExecutionException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof Exception) {
				throw (Exception) cause;
			}
			throw e;
		}
		return result;
	}

	/**
	 * <p>
	 * This method must be implemented by the user of this class to produce
	 * the data that must be read from the external <code>InputStream</code>.
	 * </p>
	 * <p>
	 * Special care must be paid passing arguments to this method or setting
	 * global fields because it is executed in another thread.
	 * </p>
	 * <p>
	 * The right way to set a field variable is to return a value in the
	 * <code>produce</code>and retrieve it in the getResult().
	 * </p>
	 * 
	 * @return The implementing class can use this to return a result of data
	 *         production. The result will be available through the method
	 *         {@linkplain #getResult()}.
	 * @param sink
	 *            the implementing class should write its data to this stream.
	 * @throws java.lang.Exception
	 *             the exception eventually thrown by the implementing class
	 *             is returned by the {@linkplain #read()} methods.
	 * @see #getResult()
	 */
	protected abstract T produce(final OutputStream sink) throws Exception;

	/** {@inheritDoc} */
	@Override
	public final int read() throws IOException {
		final int result = super.read();
		if (result < 0) {
			checkException();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = super.read(b, off, len);
		if (result < 0) {
			checkException();
		}
		return result;
	}

}
