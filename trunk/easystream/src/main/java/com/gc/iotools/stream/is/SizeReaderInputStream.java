package com.gc.iotools.stream.is;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Counts the bytes of the <code>InputStream</code> passed in the constructor.
 * It can be used to determine the size of a document passed as a stream. This
 * is possible only after the stream has been fully processed (by other parts of
 * the application).
 * </p>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * SizeReaderInputStream srIstream = new SizeReaderInputStream(originalStream);
 * //performs all the application operation on stream
 * performTasksOnStream(srIstream);
 * srIstream.close();
 * long size = srIstream.getSize();
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.0.6
 */
public class SizeReaderInputStream extends InputStream {

	private static final int BUF_SIZE = 32768;

	private boolean closeCalled = false;

	private final boolean fullReadOnClose;
	private final InputStream innerStream;
	private long markPosition = 0;
	private long size = 0;

	public SizeReaderInputStream(final InputStream istream) {
		this(istream, true);
	}

	/**
	 * Constructs an <code>SizeReaderInputStream</code> and allow to specify
	 * actions to do on close.
	 * 
	 * @param istream
	 *            Stream whose bytes must be counted.
	 * @param fullReadOnClose
	 *            if <i>true</i> after the close the inner stream is read
	 *            completely and the effective size of the inner stream is
	 *            calculated.
	 */
	public SizeReaderInputStream(final InputStream istream,
			final boolean fullReadOnClose) {
		if (istream == null) {
			throw new IllegalArgumentException("InputStream can't be null");
		}
		this.innerStream = istream;
		this.fullReadOnClose = fullReadOnClose;
	}

	@Override
	public int available() throws IOException {
		return this.innerStream.available();
	}

	/**
	 * Closes the inner stream. If <code>fullReadOnClose</code> was set in the
	 * constructor it also count all the bytes of the underlying stream.
	 * 
	 * @see InputStream#close()
	 * @exception IOException
	 *                if an I/O error occurs reading the whole content of the
	 *                stream.
	 */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			// avoid multiple calls to close();
			this.closeCalled = true;
			try {
				if (this.fullReadOnClose) {
					final byte[] buffer = new byte[BUF_SIZE];
					while (this.read(buffer) >= 0) {
						// Do nothing, just throw away the bytes and count them.
					}
				}
			} finally {
				this.innerStream.close();
			}
		}
	}

	/**
	 * Returns the bytes read until now or total length of the stream if the
	 * <code>{@link close}</code> method has been called or EOF was reached.
	 * 
	 * @return bytes read until now or the total length of the stream if close()
	 *         was called.
	 */
	public long getSize() {
		return this.size;
	}

	public boolean isFullReadOnClose() {
		return this.fullReadOnClose;
	}

	@Override
	public void mark(final int readlimit) {
		this.innerStream.mark(readlimit);
		this.markPosition = this.size;
	}

	@Override
	public boolean markSupported() {
		return this.innerStream.markSupported();
	}

	@Override
	public int read() throws IOException {
		final int readed = this.innerStream.read();
		if (readed >= 0) {
			this.size++;
		}
		return readed;
	}

	@Override
	public int read(final byte[] b) throws IOException {
		final int readed = this.innerStream.read(b);
		if (readed >= 0) {
			this.size += readed;
		}
		return readed;
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int readed = this.innerStream.read(b, off, len);
		if (readed >= 0) {
			this.size += readed;
		}
		return readed;
	}

	@Override
	public void reset() throws IOException {
		this.innerStream.reset();
		this.size = this.markPosition;
	}

	@Override
	public long skip(final long n) throws IOException {
		final long skipSize = this.innerStream.skip(n);
		this.size += skipSize;
		return skipSize;
	}

}
