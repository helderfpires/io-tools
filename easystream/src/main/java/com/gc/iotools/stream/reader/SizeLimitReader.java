package com.gc.iotools.stream.reader;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */

import java.io.IOException;
import java.io.Reader;

/**
 * A {@link java.io.Reader} wrapper that will read only a definite number of
 * bytes from the underlying stream.
 *
 * @see java.io.Reader
 * @author dvd.smnt
 * @since 1.2.7
 * @version $Id$
 */
public class SizeLimitReader extends Reader {

	private boolean closed = false;
	/**
	 * The number of bytes that have been read from the {@link #in} stream.
	 * Read methods should check to ensure that currentPosition never exceeds
	 * {@link #maxSize}.
	 */
	protected long currentPosition = 0;

	/**
	 * The underlying stream from which data are read. All methods are
	 * forwarded to it, after checking that the {@linkplain #maxSize} size
	 * that hasn't been reached.
	 */
	protected Reader in;

	/**
	 * The position in the stream when {@linkplain #mark} was called. It's
	 * used to reset the current position when {@linkplain #reset} is called.
	 */
	protected long markPosition = 0;

	/**
	 * The number of bytes to read at most from the {@link #in} stream. Read
	 * methods should check to ensure that the number of bytes read never
	 * exceeds maxSize.
	 */
	protected final long maxSize;

	/**
	 * <p>
	 * Create a new <code>SizeLimitReader</code> from another stream given a
	 * size limit.
	 * </p>
	 * <p>
	 * Bytes are read from the underlying stream until size limit is reached.
	 * </p>
	 *
	 * @param in
	 *            The underlying input stream from where the data is read.
	 * @param maxCharacterSize
	 *            the max number of bytes to allow to be read from the
	 *            underlying stream.
	 */
	public SizeLimitReader(final Reader in, final long maxCharacterSize) {
		this.in = in;
		this.maxSize = maxCharacterSize;
	}

	/**
	 * <p>
	 * Close the underlying stream. Calling this method may make data on the
	 * underlying stream unavailable.
	 * </p> {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		if (!this.closed) {
			// some buggy inputStream doesn't support close() call twice.
			this.closed = true;
			this.in.close();
		}
	}

	/**
	 * Get the maximum number of bytes left to read before the limit, set in
	 * the constructor, is reached.
	 *
	 * @return The number of bytes that (at a maximum) are left to be taken
	 *         from this stream.
	 */
	public long getBytesLeft() {
		return this.maxSize - this.currentPosition;
	}

	/**
	 * Get the number of bytes actually read from this stream.
	 *
	 * @return number of bytes that have already been taken from this stream.
	 */
	public long getBytesRead() {
		return this.currentPosition;
	}

	/**
	 * Get the number of total bytes (including bytes already read) that can
	 * be read from this stream (as set in the constructor).
	 *
	 * @return Maximum bytes that can be read until the size limit runs out
	 */
	public long getMaxSize() {
		return this.maxSize;
	}

	/** {@inheritDoc} */
	@Override
	public void mark(final int readlimit) throws IOException {
		this.in.mark(readlimit);
		this.markPosition = this.currentPosition;
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		return this.in.markSupported();
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		int result;
		if (this.currentPosition >= this.maxSize) {
			result = -1;
		} else {
			result = this.in.read();
			if (result >= 0) {
				this.currentPosition++;
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] cbuf) throws IOException {
		return this.read(cbuf, 0, cbuf.length);
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] cbuf, final int off, final int len)
			throws IOException {
		int bytesReaded;
		if (this.currentPosition >= this.maxSize) {
			bytesReaded = -1;
		} else {
			final int n = (int) Math.min(getBytesLeft(), len);
			bytesReaded = this.in.read(cbuf, off, n);
			if (bytesReaded > 0) {
				this.currentPosition += bytesReaded;
			}
		}
		return bytesReaded;
	}

	/** {@inheritDoc} */
	@Override
	public void reset() throws IOException {
		this.in.reset();
		this.currentPosition = this.markPosition;
		this.markPosition = 0;
	}

	/** {@inheritDoc} */
	@Override
	public long skip(final long n) throws IOException {
		long result;
		if (this.currentPosition >= this.maxSize) {
			result = -1;
		} else {
			result = this.in.skip(Math.min(n, getBytesLeft()));
			if (result > 0) {
				this.currentPosition += result;
			}
		}
		return result;
	}
}
