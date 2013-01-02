package com.gc.iotools.stream.reader;
/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * <p>
 * A <code>CloseOnceReader</code> wraps some other <code>Reader</code>, which
 * it uses as its basic source of data. The class <code>CloseOnceReader</code>
 * pass all requests to the contained input stream, except the
 * {@linkplain #close()} method that is passed only one time to the underlying
 * stream.
 * </p>
 * <p>
 * Multiple invocation of the <code>close()</code> method will result in only
 * one invocation of the same method on the underlying stream. This is useful
 * with some buggy <code>Reader</code> that don't allow <code>close()</code>
 * to be called multiple times.
 * </p>
 *
 * @author dvd.smnt
 * @since 1.2.7
 * @param <T>
 *            Type of the Reader passed in the constructor.
 * @version $Id$
 */
public class CloseOnceReader<T extends Reader> extends Reader {
	private int closeCount = 0;

	private final Reader source;

	/**
	 * Construct a <code>CloseOnceReader</code> that forwards the calls to the
	 * source Reader passed in the constructor.
	 *
	 * @param source
	 *            original Reader
	 */
	public CloseOnceReader(final T source) {
		if (source == null) {
			throw new IllegalArgumentException("Source reader can't be null");
		}
		this.source = source;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Multiple invocation of this method will result in only one invocation
	 * of the <code>close()</code> on the underlying stream.
	 * </p>
	 */
	@Override
	public void close() throws IOException {
		this.closeCount++;
		if (this.closeCount <= 1) {
			this.source.close();
		}
	}

	/**
	 * Returns the number of time that close was called.
	 *
	 * @return Number of times that close was called
	 */
	public int getCloseCount() {
		return this.closeCount;
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>Reader</code> passed in the
	 * constructor.
	 * </p>
	 *
	 * @return The original <code>Reader</code> passed in the constructor
	 */
	public T getWrappedReader() {
		@SuppressWarnings("unchecked")
		final T result = (T) this.source;
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void mark(final int readAheadLimit) throws IOException {
		this.source.mark(readAheadLimit);
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		return this.source.markSupported();
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		return this.source.read();
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] cbuf) throws IOException {
		return this.source.read(cbuf);
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] cbuf, final int off, final int len)
			throws IOException {
		return this.source.read(cbuf, off, len);
	}

	/** {@inheritDoc} */
	@Override
	public int read(final CharBuffer target) throws IOException {
		return this.source.read(target);
	}

	/** {@inheritDoc} */
	@Override
	public boolean ready() throws IOException {
		return this.source.ready();
	}

	/** {@inheritDoc} */
	@Override
	public void reset() throws IOException {
		this.source.reset();
	}

	/** {@inheritDoc} */
	@Override
	public long skip(final long n) throws IOException {
		return this.source.skip(n);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.source.toString();
	}
}
