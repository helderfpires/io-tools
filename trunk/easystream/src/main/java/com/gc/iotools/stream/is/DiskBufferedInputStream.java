/**
 * 
 */
package com.gc.iotools.stream.is;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gc.iotools.stream.utils.StreamUtils;

/**
 * InputStream that guarantee the reset().
 * 
 * Very similar to java.io.BufferInputStream but when the buffer size exceed the
 * threshold size it copy it to a temporary file;
 * 
 * @author dvd.smnt
 * @since 1.0.9
 */
public class DiskBufferedInputStream extends InputStream {
	private static final Log log = LogFactory
			.getLog(DiskBufferedInputStream.class);
	private InputStream current;
	/*
	 * Position of cursor into the marked stream if 0 the cursor is at the
	 * beginning
	 */
	private long currentPosition = 0;
	private OutputStream fileOs;
	private boolean markForever = false;
	private int markLimit = 0;
	/*
	 * -1 no marking <br> 0 the mark is at the beginning of the marking stream
	 * <br> >0 need a skip because the mark was in the middle of the marking
	 * stream (multiple marks)<br>
	 */
	private long markPos = 0;
	private InputStream original;
	private File tempFile = null;
	private final int threshold;

	/**
	 * 
	 * @param bis
	 * @param threshold
	 */
	public DiskBufferedInputStream(final InputStream bis, final int threshold) {
		this.original = bis;
		this.current = bis;
		this.threshold = threshold;
	}

	@Override
	public int available() throws IOException {
		return this.current.available();
	}

	@Override
	public void close() throws IOException {
		cleanup();
		this.current.close();
		this.original.close();
	}

	@Override
	public synchronized void mark(final int readlimit) {
		this.markForever = readlimit < 0;
		final int effectiveReadLimit = (this.markForever ? this.threshold
				: Math.min(readlimit, this.threshold));
		this.current = new BufferedInputStream(this.original);
		this.current.mark(effectiveReadLimit);
		this.markLimit = readlimit;
		this.markPos = 0;
		if (this.tempFile != null) {
			this.markPos = this.currentPosition;
		}
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
		/*
		 * occorre gestire il seguente caso: 1) lettura fino a threshold
		 * (len=min(tres-currentPosition,len)) 2) quando len = treshold copia su
		 * file.
		 */
		if (this.currentPosition == this.threshold) {
			// switch to disk mode
			this.tempFile = File.createTempFile("iotools-diskbufferis", ".tmp");
			this.tempFile.deleteOnExit();
			this.current.reset();
			// TODO: better copy;
			final byte[] buf = new byte[(int) this.currentPosition];
			StreamUtils.tryReadFully(this.current, buf, 0,
					(int) this.currentPosition);
			this.fileOs = new FileOutputStream(this.tempFile);
			this.fileOs.write(buf);
		}
		int effectiveLen = len;
		if (this.threshold - currentPosition > 0) {
			effectiveLen = Math.min(len,
					(int) (this.threshold - currentPosition));
		}
		final int n = this.current.read(b, off, effectiveLen);
		if (((this.currentPosition + n) > this.markLimit)
				&& (this.markLimit > 0)) {
			// delete all marks
			this.current = this.original;
			cleanup();
			markPos = -1;
		}
		if ((n > 0) && (this.markLimit != 0)) {
			this.currentPosition += n;
			if (this.fileOs != null) {
				// copy the data to the file
				this.fileOs.write(b, off, n);
			}
		}
		return n;
	}

	@Override
	public synchronized void reset() throws IOException {
		if (this.current instanceof BufferedInputStream) {
			this.current.reset();
		} else if (this.fileOs != null) {
			this.fileOs.close();
			this.fileOs = null;
			this.current = new SequenceInputStream(new FileInputStream(
					this.tempFile), this.original);
			this.original = this.current;
			this.original.skip(this.markPos);
		} else {
			throw new IOException("why we're here?");
		}
	}

	@Override
	public long skip(final long n) throws IOException {
		throw new UnsupportedOperationException("not yet implemented");
		// return this.original.skip(n);
	}

	private void cleanup() {
		if (this.fileOs != null) {
			try {
				this.fileOs.close();
			} catch (final IOException e) {
				DiskBufferedInputStream.log.debug("Exception closing the file",
						e);
			}
			this.fileOs = null;
		}

		if (this.tempFile != null) {
			final boolean del = this.tempFile.delete();
			if (!del) {
				DiskBufferedInputStream.log.warn("can not delete ["
						+ this.tempFile.getName() + "]");
			}
			this.tempFile = null;
		}

	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
	}

}
