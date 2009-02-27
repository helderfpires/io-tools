package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008,2009 Davide Simonetti
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.base.ExecutorServiceFactory;

/**
 * <p>
 * This class is an <code>OutputStream</code> that, when extended, allows to
 * read the data written to it from the <code>InputStream</code> inside the
 * method {@linkplain #doRead()}.
 * </p>
 * <p>
 * To use this class you must extend it and implement the method
 * {@linkplain #doRead()}. Inside this method place the logic that needs to read
 * the data from the <code>InputStream</code>. Then the data can be written to
 * this class that implements <code>OutputStream</code>.
 * </p>
 * <p>
 * The {@linkplain #doRead()} call executes in another thread, so there is no
 * warranty on when it will start and when it will end. Special care must be
 * taken in passing variables to it: all the arguments must be final and inside
 * {@linkplain #doRead()} you shouldn't change the variables of the outer class.
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
 * 		// read from InputStream into a string. Data will be written to the 
 * 		// outer class later (oStream2IStream.write). 
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
 *            Type returned by the method {@link #getResults()} after the thread
 *            has finished.
 * @since 1.0
 * @author dvd.smnt
 */
public abstract class OutputStreamToInputStream<T> extends OutputStream {
	/**
	 * This class executes in the second thread.
	 * 
	 * @author dvd.smnt
	 * 
	 */
	private final class DataConsumer implements Callable<T> {

		private final InputStream inputstream;

		DataConsumer(final InputStream istream) {
			this.inputstream = istream;
		}

		public T call() throws Exception {
			T processResult;
			try {
				processResult = doRead(this.inputstream);
			} finally {
				emptyInputStream();
			}
			return processResult;
		}


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
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(OutputStreamToInputStream.class);

	private boolean closeCalled = false;
	private final boolean joinOnClose;
	private final PipedOutputStream wrappedPipedOS;
	private final Future<T> writingResult;

	/**
	 * <p>
	 * Creates a new <code>OutputStreamToInputStream</code>. It uses the default
	 * {@link ExecutionModel#THREAD_PER_INSTANCE} thread instantiation strategy.
	 * This means that a new thread is created for every instance of
	 * <code>OutputStreamToInputStream</code>.
	 * </p>
	 * 
	 * @throws IOException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream() throws IOException {
		this(true, ExecutionModel.THREAD_PER_INSTANCE);
	}

	/**
	 * 
	 * @param joinOnClose
	 * @param executionModel
	 * @throws IOException
	 */
	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutionModel executionModel) throws IOException {
		this(joinOnClose, ExecutorServiceFactory.getExecutor(executionModel));
	}

	/**
	 * 
	 * @param joinOnClose
	 *            if <code>true</code> the internal thread will be joined when
	 *            close is invoked.
	 * @param executorService
	 *            Service for executing the internal thread.
	 * @throws IOException
	 *             Exception thrown if pipe can't be created.
	 */
	public OutputStreamToInputStream(final boolean joinOnClose,
			final ExecutorService executorService) throws IOException {
		if (executorService == null) {
			throw new IllegalArgumentException(
					"executor service can't be null");
		}
		this.wrappedPipedOS = new PipedOutputStream();
		final PipedInputStream pipedIS = new PipedInputStream(
				this.wrappedPipedOS);

		final DataConsumer executingProcess = new DataConsumer(pipedIS);
		this.joinOnClose = joinOnClose;
		this.writingResult = executorService.submit(executingProcess);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.wrappedPipedOS.close();
			if (this.joinOnClose) {
				// waiting for thread to finish..
				try {
					this.writingResult.get();
				} catch (final ExecutionException e) {
					final IOException e1 = new IOException(
							"Problem producing data");
					e1.initCause(e.getCause());
					throw e1;

				} catch (final InterruptedException e) {
					final IOException e1 = new IOException(
							"Waiting of the thread has been interrupted");
					e1.initCause(e);
					throw e1;
				}
			}
		}
	}

	public final void close(final long timeout, final TimeUnit tu)
			throws IOException, InterruptedException, ExecutionException,
			TimeoutException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.wrappedPipedOS.close();
			if (this.joinOnClose) {
				this.writingResult.get(timeout, tu);
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void flush() throws IOException {
		this.wrappedPipedOS.flush();
	}

	/**
	 * <p>
	 * This method returns the result of the method {@link #doRead(InputStream)}
	 * and ensure the previous method is over.
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
	 *                Thrown if the method {@linkplain #doRead()} threw an
	 *                Exception. The <code>getCause()</code> returns the
	 *                original Exception.
	 * @throws IllegalStateException
	 *             When it is called before the method {@link #close()} has been
	 *             called.
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
		this.wrappedPipedOS.write(bytes);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void write(final byte[] bytes, final int offset,
			final int length) throws IOException {
		this.wrappedPipedOS.write(bytes, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void write(final int bytetowr) throws IOException {
		this.wrappedPipedOS.write(bytetowr);
	}

	/**
	 * This method has to be implemented to use this class. It allows to
	 * retrieve the data written to the outer <code>OutputStream</code> from the
	 * <code>InputStream</code> passed as a parameter.
	 * 
	 * @param istream
	 *            The InputStream where the data can be retrieved.
	 * @return Optionally returns a result of the elaboration.
	 * @throws Exception
	 *             If an <code>Exception</code> occurs during the elaboration it
	 *             can be thrown. Will be returned in the method
	 *             {@link #getResults()}.
	 */
	protected abstract T doRead(InputStream istream) throws Exception;

}
