package com.gc.iotools.stream.base;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Provides common functionality to the easystream library:
 * <ul>
 * <li>holds a source InputStream</li>
 * <li>provide a {@linkplain #closeOnce()} method that's called exactly once.</li>
 * <li>provide an implementation of {@linkplain AbstractInputStreamWrapper#skip(final long n) } and
 * {@linkplain #read()} methods.</li>
 * <li>Keeps track of the position in the source stream over mark and reset.</li>
 * </ul>
 * </p>
 *
 * @author dvd.smnt
 * @since 1.2.0
 * @version $Id$
 */
public abstract class AbstractInputStreamWrapper extends InputStream {

	protected boolean closeCalled;

	protected final InputStream source;

	/**
	 * <p>Constructor for AbstractInputStreamWrapper.</p>
	 *
	 * @param source the {@link java.io.InputStream} that is wrapped by this stream.
	 */
	protected AbstractInputStreamWrapper(final InputStream source) {
		if (source == null) {
			throw new IllegalArgumentException(
					"InputStream source can't be null.");
		}
		this.source = source;
	}

	private void checkReadArguments(final byte[] b, final int off,
			final int len) {
		if (b == null) {
			throw new NullPointerException(
					"Buffer for read(b,off,len) is null");
		} else if (off < 0) {
			throw new IndexOutOfBoundsException(
					"read(b,off,len) called with off < 0 ");
		} else if ((off > b.length) || (len < 0) || ((off + len) > b.length)
				|| ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (this.closeCalled) {
			throw new IllegalStateException("Stream already closed");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			closeOnce();
		}
	}

	/**
	 * <p>closeOnce</p>
	 *
	 * @throws java.io.IOException if any error occurs reading the bytes.
	 */
	protected abstract void closeOnce() throws IOException;

	/**
	 * <p>innerRead</p>
	 *
	 * @param b an array of byte.
	 * @param off a int.
	 * @param len a int.
	 * @return a int.
	 * @throws java.io.IOException if any error occurs reading the bytes.
	 */
	protected abstract int innerRead(byte[] b, int off, int len)
			throws IOException;

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[1];
		final int n = this.read(buf);
		final int result = (n > 0 ? buf[0] & 0xff : n);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		checkReadArguments(b, off, len);
		int result = 0;
		if (len > 0) {
			result = innerRead(b, off, len);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public long skip(final long n) throws IOException {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Skip was called with a negative skip size[" + n + "]");
		}
		long curPos = 0;
		int readLen = 0;
		final byte[] buf = new byte[EasyStreamConstants.SKIP_BUFFER_SIZE];
		while ((curPos < n) && (readLen >= 0)) {
			readLen = innerRead(buf, 0,
					(int) Math.min(buf.length, n - curPos));
			if (readLen > 0) {
				curPos += readLen;
			}
		}
		return curPos;
	}
}
