package org.iotools.streams;

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

	@Override
	public int available() throws IOException {
		return this.source.available();
	}

	/**
	 * Copy the remaining data to the OutputStream and Closes the inner
	 * InputStream.
	 */
	@Override
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

	@Override
	public void mark(final int readlimit) {
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read() throws IOException {
		final int result = this.source.read();
		if (result >= 0) {
			this.destination.write(result);
		}
		return result;
	}

	@Override
	public int read(final byte[] b) throws IOException {
		final int result = this.source.read(b);
		if (result > 0) {
			this.destination.write(b, 0, result);
		}
		return result;
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.source.read(b, off, len);
		if (result > 0) {
			this.destination.write(b, off, result);
		}
		return result;
	}

	@Override
	public void reset() throws IOException {
		throw new IOException("Reset not supported");
	}

	@Override
	public long skip(final long n) throws IOException {
		throw new UnsupportedOperationException("Skip is not supported by ["
				+ TeeInputStreamOutputStream.class + "]");
	}

}
