package org.goglecode.iotools.streams;

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

/**
 * <p>
 * While data are read from InputStream they're also written to the OutputStream
 * passed in the constructor.
 * </p>
 * <p>
 * Internal InputStream is closed when method close() is invoked. OutputStream
 * must be closed outside this class.
 * </p>
 * 
 * @since Sep 28, 2008
 */
public final class TeeInputStreamOutputStream extends InputStream {

	private boolean closed = false;

	private final OutputStream destination;

	private final InputStream source;

	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination) {
		this.source = source;
		this.destination = destination;
	}

	public int available() throws IOException {
		return this.source.available();
	}

	/**
	 * Copy the remaining data to the OutputStream and Closes the inner
	 * InputStream.
	 */
	public void close() throws IOException {
		if (!this.closed) {
			IOException e1 = null;
			this.closed = true;
			try {
				int n = 0;
				final byte[] buffer = new byte[8192];
				while ((n = this.source.read(buffer)) > 0) {
					this.destination.write(buffer, 0, n);
				}
			} catch (final IOException e) {
				e1 = new IOException(
						"It's not possible to copy to the OutputStream");
				e1.initCause(e);
			}
			this.source.close();
			if (e1 != null) {
				throw e1;
			}
		}
	}

	public void mark(final int readlimit) {
	}

	public boolean markSupported() {
		return false;
	}

	public int read() throws IOException {
		final int result = this.source.read();
		if (result >= 0) {
			this.destination.write(result);
		}
		return result;
	}

	public int read(final byte[] b) throws IOException {
		final int result = this.source.read(b);
		if (result > 0) {
			this.destination.write(b, 0, result);
		}
		return result;
	}

	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.source.read(b, off, len);
		if (result > 0) {
			this.destination.write(b, off, result);
		}
		return result;
	}

	public void reset() throws IOException {
		throw new IOException("Reset not supported");
	}

	public long skip(final long n) throws IOException {
		throw new UnsupportedOperationException("Skip is not supported by ["
				+ TeeInputStreamOutputStream.class + "]");
	}

}
