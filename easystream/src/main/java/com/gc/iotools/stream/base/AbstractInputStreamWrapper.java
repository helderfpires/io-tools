package com.gc.iotools.stream.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Provides common functionality to the easystream library:
 * <ul>
 * <li>holds a source InputStream</li>
 * <li>provide a {@linkplain #closeOnce()} method that's called exactly once.</li>
 * <li>provide an implementation of {@linkplain #skip()} and
 * {@linkplain #read()} methods.</li>
 * <li>Keeps track of the position in the source stream over mark and reset.</li>
 * </ul>
 * </p>
 * 
 * @author dvd.smnt
 * @since 1.2
 */
public abstract class AbstractInputStreamWrapper extends InputStream {

	private static final int SKIP_SIZE = 8192;
	protected final InputStream source;

	protected boolean closeCalled;

	protected AbstractInputStreamWrapper(final InputStream source) {
		if (source == null) {
			throw new IllegalArgumentException(
					"InputStream source can't be null.");
		}
		this.source = source;
	}

	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			closeOnce();
		}
	}

	protected abstract int innerRead(byte[] b, int off, int len)
			throws IOException;

	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[1];
		final int n = this.read(buf);
		final int result = (n > 0 ? buf[0] & 0xff : n);
		return result;
	}

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

	@Override
	public long skip(final long n) throws IOException {
		long curPos = 0;
		int readLen = 0;
		final byte[] buf = new byte[SKIP_SIZE];
		while ((curPos < n) && (readLen >= 0)) {
			readLen = this.read(buf, 0, (int) Math
					.min(buf.length, n - curPos));
			if (readLen > 0) {
				curPos += readLen;
			}
		}
		return curPos;
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

	protected abstract void closeOnce() throws IOException;
}
