package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008-2009, Davide Simonetti
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

import com.gc.iotools.stream.base.AbstractInputStreamWrapper;

/**
 * <p>
 * Copies the data from the underlying <code>InputStream</code> to the
 * <code>OutputStream</code> passed in the constructor. The data copied are
 * similar to the underlying <code>InputStream</code>.
 * </p>
 * <p>
 * When the method <code>{@link #close()}</code> is invoked all the bytes
 * remaining in the underlying <code>InputStream</code> are copied to the
 * <code>OutputStream</code>. This behavior is different from this class and
 * <code>org.apache.commons.io.input.TeeInputStream</code>.
 * </p>
 * <p>
 * Bytes skipped are copied to the <code>OutputStream</code> while
 * <code>mark</code> and <code>reset</code> are not supported at the moment.
 * </p>
 * <p>
 * Sample usage:
 * </p>
 * 
 * <pre>
 * 	 InputStream source=... //some data to be readed.
 *   ByteArrayOutputStream destination1= new ByteArrayOutputStream();
 *   ByteArrayOutputStream destination2= new ByteArrayOutputStream();
 *   
 *   TeeInputStreamOutputStream tee=
 *                          new TeeInputStreamOutputStream(source,destination1);
 *   org.apache.commons.io.IOUtils.copy(tee,destination2);
 *   tee.close();
 *   //at this point both destination1 and destination2 contains the same bytes.
 *   byte[] bytes=destination1.getBytes();
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.0.6
 */
public class TeeInputStreamOutputStream extends AbstractInputStreamWrapper {
	/**
	 * Buffer size used in skip() operations.
	 */
	private static final int BUF_SIZE = 8192;

	protected final OutputStream destination;

	private long innerStreamPosition;
	/**
	 * If <code>true</code> <code>source</code> and <code>destination</code>
	 * streams are closed when {@link #close()} is invoked.
	 */
	protected final boolean closeStreams;

	/**
	 * <p>
	 * Creates a <code>TeeInputStreamOutputStream</code> and saves its argument,
	 * the input stream <code>source</code> and the <code>OutputStream</code>
	 * <code>destination</code> for later use.
	 * </p>
	 * <p>
	 * When the method {@link #close()} it is invoked the remaining content of
	 * the <code>source</code> stream is copied to the <code>destination</code>
	 * and the <code>source</code> and <code>destination</code> streams are
	 * closed.
	 * </p>
	 * 
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination) {
		this(source, destination, true);
	}

	/**
	 * Creates a <code>TeeInputStreamOutputStream</code> and saves its argument,
	 * the input stream <code>source</code> and the output stream
	 * <code>destination</code> for later use.
	 * 
	 * @since 1.2
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 * @param closeStreams
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying streams
	 *            will not be called (it must be invoked externally).
	 * 
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination, final boolean closeStreams) {
		super(source);
		this.destination = destination;
		this.closeStreams = closeStreams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int available() throws IOException {
		return this.source.available();
	}

	/**
	 * This method copy all the data eventually remaining in the internal
	 * <code>InputStream</code> to the <code>OutputStream</code>. The standard
	 * behavior is to close closes the underlying <code>InputStream</code> and
	 * <code>OutputStream</code>. When the class was constructed with the
	 * parameter {@link TeeInputStreamOutputStream#closeStreams closeStreams}
	 * set to false the underlying streams must be closed externally.
	 * 
	 * @throws IOException
	 *             thrown when a IO problem occurs in reading or writing the
	 *             data.
	 */
	@Override
	public void closeOnce() throws IOException {

		IOException e1 = null;
		try {
			int n = 0;
			final byte[] buffer = new byte[BUF_SIZE];
			while ((n = this.source.read(buffer)) > 0) {
				this.destination.write(buffer, 0, n);
			}
		} catch (final IOException e) {
			e1 = new IOException(
					"It's not possible to copy to the OutputStream");
			e1.initCause(e);
		}
		if (this.closeStreams) {
			this.source.close();
			this.destination.close();
		}
		if (e1 != null) {
			throw e1;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark(final int readlimit) {
		this.innerStreamPosition=position;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return this.source.markSupported();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		final int result = this.source.read();
		if (result >= 0) {
			this.destination.write(result);
		}
		return result;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b) throws IOException {
		final int result = this.source.read(b);
		if (result > 0) {
			this.destination.write(b, 0, result);
		}
		return result;
	}

	@Override
	public int innerRead(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.source.read(b, off, len);
		if (result > 0) {
			this.destination.write(b, off, result);
		}
		return result;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() throws IOException {
		throw new IOException("Reset not supported");
	}

}
