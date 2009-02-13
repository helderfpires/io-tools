package com.gc.iotools.stream.base;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInputStreamWrapper extends InputStream {
	private static final int SKIP_SIZE = 8192;
	protected final InputStream source;

	protected boolean closeCalled;

	protected AbstractInputStreamWrapper(final InputStream source) {
		this.source = source;
	}

	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			closeOnce();
		}
	}

	public abstract int innerRead(byte[] b, int off, int len)
			throws IOException;

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
		byte[] buf = new byte[SKIP_SIZE];
		while (curPos < n && readLen >= 0) {
			readLen = this.read(buf);
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
		} else if ((off < 0) || (off > b.length) || (len < 0)
				|| ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (this.closeCalled) {
			throw new IllegalStateException("Stream already closed");
		}
	}

	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[1];
		final int n = this.read(buf);
		final int result = (n > 0 ? buf[0] : n);
		return result;
	}
	
	protected abstract void closeOnce() throws IOException;
}
