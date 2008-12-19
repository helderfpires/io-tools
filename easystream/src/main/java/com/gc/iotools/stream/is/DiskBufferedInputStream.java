/**
 * 
 */
package com.gc.iotools.stream.is;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * InputStream that guarantee the reset().
 * 
 * Very similar to java.io.BufferInputStream but when the buffer size exceed the
 * threshold size it copy it to a temporary file;
 * 
 * @author dvd.smnt
 *@since 1.0.9
 */
public class DiskBufferedInputStream extends InputStream {
	private final InputStream bis;
	private File tempFile;
	private OutputStream fileOs;
	private final int threshold;
	private long markPos;
	private boolean markForever = false;
	private int markLimit = -1;

	/**
	 * 
	 * @param bis
	 * @param threshold
	 */
	public DiskBufferedInputStream(final InputStream bis, final int threshold) {
		this.bis = new BufferedInputStream(bis);
		this.threshold = threshold;
	}

	@Override
	public int available() throws IOException {
		return this.bis.available();
	}

	@Override
	public void close() throws IOException {
		cleanup();
		this.bis.close();
	}

	@Override
	public synchronized void mark(final int readlimit) {
		this.markForever = readlimit < 0;
		final int effectiveReadLimit = (this.markForever ? this.threshold
				: Math.min(readlimit, this.threshold));
		this.bis.mark(effectiveReadLimit);
		this.markLimit = readlimit;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException("b.length[" + b.length
					+ "] offset[" + off + "] length[" + len + "");
		} else if (len == 0) {
			return 0;
		}
		if (this.markPos + len > this.threshold
				&& this.markPos <= this.threshold) {
			this.tempFile = File.createTempFile("iotools-diskbufferis", ".tmp");
		}
		final int n = this.bis.read(b, off, len);
		if (n > 0 && (this.markLimit != 0)) {
			this.markPos += n;
		}
		return n;
	}

	@Override
	public synchronized void reset() throws IOException {
		// TODO Auto-generated method stub
		super.reset();
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.bis.skip(n);
	}

	private void cleanup() {

	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
	}

}
