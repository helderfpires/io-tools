package com.gc.iotools.stream.is;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Counts the bytes from the inputStream passed in the constructor. It can be
 * used to determine the size of a document passed as a stream. This is possible
 * only after the stream has been fully processed (by other parts of the
 * application).
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
 * 
 */
public class SizeReaderInputStream extends InputStream {

	private final InputStream innerStream;

	private final boolean fullReadOnClose;
	private boolean closeCalled = false;
	private long size = 0;
	private long markPosition = 0;

	public SizeReaderInputStream(final InputStream istream) {
		this(istream, true);
	}

	/**
	 * Constructs an SizeReaderInputStream and allow to specify actions to do on
	 * close.
	 * 
	 * @param istream
	 *            Stream whose bytes must be counted.
	 * @param fullReadOnClose
	 *            if <i>true</i> after the close the inner stream is fully
	 *            readed and the total size is calculated.
	 */
	public SizeReaderInputStream(final InputStream istream,
			final boolean fullReadOnClose) {
		this.innerStream = istream;
		this.fullReadOnClose = fullReadOnClose;
	}

	public int available() throws IOException {
		return this.innerStream.available();
	}

	public void close() throws IOException {
		if (!this.closeCalled) {
			// avoid multiple calls to close();
			this.closeCalled = true;
			try {
				if (this.fullReadOnClose) {
					final byte[] buffer = new byte[32768];
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
	 * Returns the bytes readed until now or total length of the stream if the
	 * close() method has been called or EOF was reached.
	 * 
	 * @return bytes readed until now or the total lenght of the stream if
	 *         close() was called.
	 */
	public long getSize() {
		return this.size;
	}

	public boolean isFullReadOnClose() {
		return this.fullReadOnClose;
	}

	public void mark(final int readlimit) {
		this.innerStream.mark(readlimit);
		this.markPosition = this.size;
	}

	public boolean markSupported() {
		return this.innerStream.markSupported();
	}

	public int read() throws IOException {
		final int readed = this.innerStream.read();
		if (readed >= 0) {
			this.size++;
		}
		return readed;
	}

	public int read(final byte[] b) throws IOException {
		final int readed = this.innerStream.read(b);
		if (readed >= 0) {
			this.size += readed;
		}
		return readed;
	}

	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int readed = this.innerStream.read(b, off, len);
		if (readed >= 0) {
			this.size += readed;
		}
		return readed;
	}

	public void reset() throws IOException {
		this.innerStream.reset();
		this.size = this.markPosition;
	}

	public long skip(final long n) throws IOException {
		final long skipSize = this.innerStream.skip(n);
		this.size += skipSize;
		return skipSize;
	}

}
