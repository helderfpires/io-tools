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
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;

/**
 * <p>
 * This class allow to read the data written to an OutputStream from an
 * InputStream.
 * </p>
 * <p>
 * To use this class you must subclass it and implement the abstract method
 * {@link produce}. The data who is produced inside this function can be written
 * to the <code>OutputStream</code> passed as a parameter. Later it can be read
 * back from from the implementing class (whose ancestor is
 * <code>java.io.InputStream</code> ).
 * </p>
 * 
 * <pre>
 * final String dataId=//id of some data.
 * final InputStreamFromOutputStream isos = new InputStreamFromOutputStream() {
 *   &#064;Override
 *   public void produce(final OutputStream dataSink) throws Exception {
 *      //call your application function who produces the data here
 *      produceMydata(dataId,dataSink)
 *   }
 * };
 *  //now you can read from the InputStream the data that was written to the 
 *  //OutputStream
 * 	byte[] readed=IOUtils.toByteArray(isos);
 * </pre>
 * <p>
 * This class encapsulates a pipe and a <code>Thread</code>, hiding the
 * complexity of using them.
 * </p>
 * 
 * @author dvd.smnt
 * @since 1.0
 */
public abstract class InputStreamFromOutputStream extends InputStream {

	private final class DataProducerRunnable implements Runnable {

		private IOException exception = null;

		private final Log LOGGER = LogFactory
				.getLog(InputStreamFromOutputStream.DataProducerRunnable.class);

		private String name = null;

		private OutputStream outputStream = null;

		DataProducerRunnable(final String threadName, final OutputStream ostream) {
			this.outputStream = ostream;
			this.name = threadName;
		}

		public void run() {
			final String threadName = getName();
			InputStreamFromOutputStream.ACTIVE_THREAD_NAMES.add(threadName);
			try {
				produce(this.outputStream);
			} catch (final Throwable e) {
				this.LOGGER.error("Error during data production.", e);
				this.exception = new IOException(
						"Error producing data for class ["
								+ getClass().getName() + "]");
				this.exception.initCause(e);
			} finally {
				closeStream();
				InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
						.remove(threadName);
				this.LOGGER.debug("thread [" + getName() + "] closed");
			}
		}

		private void closeStream() {
			try {
				this.outputStream.close();
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					this.LOGGER.debug("Stream already closed");
				} else {
					this.LOGGER.error("IOException closing OutputStream"
							+ " Thread might be locked", e);
				}
			} catch (final Throwable t) {
				this.LOGGER.error("Error closing InputStream"
						+ " Thread might be locked", t);
			}
		}

		final String getName() {
			return this.name;
		}
	}

	private static final List<String> ACTIVE_THREAD_NAMES = Collections
			.synchronizedList(new ArrayList<String>());

	private static final Log LOG = LogFactory
			.getLog(InputStreamFromOutputStream.class);

	public static final String[] getActiveThreadNames() {
		final String[] result;
		synchronized (InputStreamFromOutputStream.ACTIVE_THREAD_NAMES) {
			result = InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
					.toArray(new String[InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
							.size()]);
		}
		return result;
	}

	private boolean closeCalled = false;

	private final DataProducerRunnable executingRunnable;

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

	public InputStreamFromOutputStream(final ExecutionModel tmodel) {
		this(ExecutorServiceFactory.getExecutor(tmodel));
	}

	public InputStreamFromOutputStream(final Executor executor) {
		final String callerId = getCaller();
		PipedOutputStream pipedOS = null;
		try {
			this.pipedIS = new PipedInputStream();
			pipedOS = new PipedOutputStream(this.pipedIS);
		} catch (final IOException e) {
			throw new RuntimeException("Error during pipe creaton", e);
		}
		this.executingRunnable = new DataProducerRunnable(callerId, pipedOS);
		final String tName = this.executingRunnable.getName();
		executor.execute(this.executingRunnable);
		InputStreamFromOutputStream.LOG.debug("thread invoked by[" + tName
				+ "] queued for start.");
	}

	@Override
	public final void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.pipedIS.close();
		}
	}

	@Override
	public final int read() throws IOException {
		final int result = this.pipedIS.read();
		if ((result < 0) && (this.executingRunnable.exception != null)) {
			throw this.executingRunnable.exception;
		}
		return result;
	}

	@Override
	public final int read(final byte[] b) throws IOException {
		final int result = this.pipedIS.read(b);
		if ((result < 0) && (this.executingRunnable.exception != null)) {
			throw this.executingRunnable.exception;
		}
		return result;
	}

	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.pipedIS.read(b, off, len);
		if ((result < 0) && (this.executingRunnable.exception != null)) {
			throw this.executingRunnable.exception;
		}
		return result;

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
	 * This method must be implemented to produce the data that must be read
	 * from the external <code>InputStream</code>.
	 * </p>
	 * <p>
	 * Special care must be paid passing arguments to this method because it
	 * executes in another thread.
	 * </p>
	 * 
	 * @param dataSink
	 * @throws Exception
	 */
	protected abstract void produce(final OutputStream dataSink)
			throws Exception;

}
