package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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

import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;

/**
 * <p>
 * This class allow to read the data written to an OutputStream from an
 * InputStream.
 * </p>
 * <p>
 * To use this class you must subclass it and implement the abstract method
 * {@link #produce(OutputStream))}. The data who is produced inside this
 * function can be written to the <code>OutputStream</code> passed as a
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
 *     byte[] readed=IOUtils.toByteArray(isos);
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
 * complexity of using them.
 * </p>
 * 
 * @param <T>
 *            Optional result returned by the function
 *            {@linkplain #produce(OutputStream))} after the data has been
 *            written. It can be obtained calling the {@linkplain #getResult()}
 * 
 * @see ExecutionModel
 * @author dvd.smnt
 * @since 1.0
 */
public abstract class InputStreamFromOutputStream<T> extends InputStream {
	private static final int DEFAULT_PIPE_SIZE = 1024;

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
	 * Extends PipedInputStream to allow set the default buffer size.
	 * 
	 */
	private final class MyPipedInputStream extends PipedInputStream {

		MyPipedInputStream(final int bufferSize) {
			super.buffer = new byte[bufferSize];
		}
	}

	private static final List<String> ACTIVE_THREAD_NAMES = Collections
			.synchronizedList(new ArrayList<String>());

	private static final Logger LOG = LoggerFactory
			.getLogger(InputStreamFromOutputStream.DataProducer.class);
	/**
	 * The default pipe buffer size for the newly created pipes.
	 */
	private static int defaultPipeSize = DEFAULT_PIPE_SIZE;

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
	 * <code>InputStreamFromOutputStream</code>. Default is 1024 bytes.
	 * 
	 * @since 1.2.0
	 * @param defaultPipeSize
	 *            the default pipe buffer size in bytes.
	 */
	public static void setDefaultBufferSize(final int defaultPipeSize) {
		InputStreamFromOutputStream.defaultPipeSize = defaultPipeSize;
	}

	private boolean closeCalled = false;

	private final Future<T> futureResult;

	private final PipedInputStream pipedIS;

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> with a
	 * THREAD_PER_INSTANCE thread strategy.
	 * </p>
	 * 
	 * @see ExecutionModel.THREAD_PER_INSTANCE
	 */
	public InputStreamFromOutputStream() {
		this(ExecutionModel.THREAD_PER_INSTANCE);
	}

	/**
	 * <p>
	 * It creates a <code>InputStreamFromOutputStream</code> and let the user
	 * set the {@link ExecutionModel} he likes.
	 * </p>
	 * 
	 * @param executionModel
	 *            The preferred ExecutionModel.
	 * @see ExecutionModel
	 */
	public InputStreamFromOutputStream(final ExecutionModel executionModel) {
		this(ExecutorServiceFactory.getExecutor(executionModel));
	}

	public InputStreamFromOutputStream(final ExecutorService executor) {
		final String callerId = getCaller();
		PipedOutputStream pipedOS = null;
		try {
			this.pipedIS = new MyPipedInputStream(defaultPipeSize);
			pipedOS = new PipedOutputStream(this.pipedIS);
		} catch (final IOException e) {
			throw new RuntimeException("Error during pipe creaton", e);
		}
		final Callable<T> executingCallable = new DataProducer(callerId,
				pipedOS);
		this.futureResult = executor.submit(executingCallable);
		InputStreamFromOutputStream.LOG.debug("thread invoked by[" + callerId
				+ "] queued for start.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.pipedIS.close();
		}
	}

	/**
	 * <p>
	 * Returns the object that was
	 * </p>
	 * <p>
	 * This method must be called after the method {@linkplain #close()},
	 * otherwise an IllegalStateException is thrown. It waits for the method
	 * {@link #produce(OutputStream)} to terminate and returns the result
	 * produced here.
	 * </p>
	 * 
	 * @since 1.2.0
	 * @return Object that was returned by the
	 *         {@linkplain #produce(OutputStream)} method.
	 * @throws Exception
	 *             If the {@linkplain #produce(OutputStream)} method threw an
	 *             Exception this method will throw again the same exception.
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
			} else {
				throw e;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int read() throws IOException {
		final int result = this.pipedIS.read();
		if (result < 0) {
			checkException();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.pipedIS.read(b, off, len);
		if (result < 0) {
			checkException();
		}
		return result;
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

	private String getCaller() {
		final Exception exception = new Exception();
		final StackTraceElement[] stes = exception.getStackTrace();
		boolean found = false;
		StackTraceElement caller = null;
		for (int i = 0; (i < stes.length) && !found; i++) {
			caller = stes[i];
			found = !this.getClass().equals(caller.getClass());
		}

		final String result = getClass().getName().substring(
				getClass().getPackage().getName().length() + 1)
				+ "callBy:" + caller.toString();
		InputStreamFromOutputStream.LOG.debug("OpenedBy [" + result + "]");
		return result;
	}

	/**
	 * <p>
	 * This method must be implemented by the user of this class to produce the
	 * data that must be read from the external <code>InputStream</code>.
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
	 *         {@linkplain getResult()}.
	 * @param dataSink
	 *            the implementing class should write data to this stream.
	 * @throws Exception
	 *             the exception eventually thrown by the implementing class is
	 *             returned by the {@linkplain #read()} methods.
	 * @see #getResult()
	 */
	protected abstract T produce(final OutputStream dataSink)
			throws Exception;

}
