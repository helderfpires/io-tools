package org.iotools.streams;

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
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO: code example, FIXME: JoinOnClose is not working, but definitely is
 * needed
 * 
 * @since 0ct 3, 2008
 * @author dvd.smnt
 * @version $Revision: 1 $
 */
public abstract class OutputStreamToInputStream extends OutputStream {

	private final class DataConsumerRunnable implements Runnable {
		private InvocationTargetException exception = null;

		private final InputStream inputstream;

		private Object processResult = null;

		DataConsumerRunnable(final String callerId, final InputStream istream) {
			this.inputstream = istream;
		}

		public void run() {
			try {
				this.processResult = doRead(this.inputstream);
			} catch (final Throwable e) {
				OutputStreamToInputStream.LOG.error("Problem processing.", e);
				this.exception = new InvocationTargetException(e);
			} finally {
				emptyInputStream();
			}
		}

		/**
		 * 
		 */
		private void emptyInputStream() {
			boolean closed = false;
			try {
				while ((this.inputstream.read()) >= 0) {
					// empty block
				}
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					OutputStreamToInputStream.LOG
							.debug("Stream already closed");
					closed = true;
				} else {
					OutputStreamToInputStream.LOG.error(
							"IOException while empty InputStream a "
									+ "thread can be locked", e);
				}
			} catch (final Throwable t) {
				OutputStreamToInputStream.LOG.error(
						"Error while empty InputStream a "
								+ "thread can be locked", t);
			}
			tryCloseIs(closed);
		}

		private void tryCloseIs(final boolean closed) {
			if (!closed) {
				try {
					this.inputstream.close();
				} catch (final Throwable e) {
					OutputStreamToInputStream.LOG.error(
							"Error closing Inputstream", e);
				}
			}
		}

		Object getResults() throws InvocationTargetException {
			if (this.exception != null) {
				throw this.exception;
			}
			return this.processResult;
		}
	}

	private static final Log LOG = LogFactory
			.getLog(OutputStreamToInputStream.class);

	private boolean closeCalled = false;

	private final DataConsumerRunnable executingProcess;

	private final boolean joinOnClose;

	private final PipedOutputStream wrappedPipedOS;

	public OutputStreamToInputStream() throws IOException {
		this(false, ExecutionModel.THREAD_PER_INSTANCE);
	}

	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutionModel em) throws IOException {
		this(joinOnClose, ExecutorServiceFactory.getExecutor(em));
	}

	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutorService executor) throws IOException {
		final String callerId = getCaller();
		this.wrappedPipedOS = new PipedOutputStream();
		final PipedInputStream pipedIS = new PipedInputStream(
				this.wrappedPipedOS);
		this.executingProcess = new DataConsumerRunnable(callerId, pipedIS);
		this.joinOnClose = joinOnClose;
		executor.execute(this.executingProcess);
	}

	@Override
	public final void close() throws IOException {
		this.closeCalled = true;
		this.wrappedPipedOS.close();
		if (this.joinOnClose) {
			// try {
			// this.executingProcess.join();
			// } catch (final InterruptedException e) {
			// final IOException ioe = new IOException(
			// "errore durante la close()");
			// ioe.initCause(e);
			// throw ioe;
			// }
			throw new UnsupportedOperationException(
					"Join on close not yet implemented");
		}
	}

	@Override
	public final void flush() throws IOException {
		this.wrappedPipedOS.flush();
	}

	// public final Object getResults() throws InvocationTargetException,
	// InterruptedException {
	// tryClose();
	// this.executingProcess.join();
	// return this.executingProcess.getResults();
	// }
	//
	// public final Object getResults(final long timeoutMillis)
	// throws InvocationTargetException, InterruptedException {
	// tryClose();
	// this.executingProcess.join(timeoutMillis);
	// return this.executingProcess.getResults();
	// }

	@Override
	public final void write(final byte[] bytes) throws IOException {
		this.wrappedPipedOS.write(bytes);
	}

	@Override
	public final void write(final byte[] bytes, final int offset,
			final int length) throws IOException {
		this.wrappedPipedOS.write(bytes, offset, length);
	}

	@Override
	public final void write(final int bytetowr) throws IOException {
		this.wrappedPipedOS.write(bytetowr);
	}

	private String getCaller() {
		final Exception exception = new Exception();
		final StackTraceElement[] stes = exception.getStackTrace();
		final StackTraceElement caller = stes[3];
		final String result = getClass().getName().substring(
				getClass().getPackage().getName().length() + 1)
				+ "callBy:" + caller.toString();
		OutputStreamToInputStream.LOG.debug("Open [" + result + "]");
		return result;
	}

	protected abstract Object doRead(InputStream istream) throws Exception;

}
