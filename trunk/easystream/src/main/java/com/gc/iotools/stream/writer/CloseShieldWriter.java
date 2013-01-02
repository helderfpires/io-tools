package com.gc.iotools.stream.writer;
/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * A <code>CloseShieldWriter</code> wraps some other <code>Writer</code>,
 * which it uses as its basic sink of data. The class
 * <code>CloseShieldWriter</code> pass all requests to the contained writer,
 * except the {@linkplain #close()} method that is not to the underlying
 * stream.
 * </p>
 * <p>
 * This class is typically used in cases where a <code>Writer</code> needs to
 * be passed to a component that wants to explicitly close the stream even if
 * other components still want to write data on it.
 * </p>
 *
 * @author dvd.smnt
 * @since 1.2.8
 * @param <T>
 *            Type of the Writer passed in the constructor.
 * @version $Id$
 */
public class CloseShieldWriter<T extends Writer> extends Writer {
	private int closeCount = 0;

	private final Writer source;

	/**
	 * Construct a <code>CloseShieldWriter</code> that forwards the calls to
	 * the source Writer passed in the constructor.
	 *
	 * @param source
	 *            original Writer
	 * @param <T> a T object.
	 */
	public CloseShieldWriter(final T source) {
		if (source == null) {
			throw new IllegalArgumentException("Source Writer can't be null");
		}
		this.source = source;
	}

	/** {@inheritDoc} */
	@Override
	public Writer append(final char c) throws IOException {
		return this.source.append(c);
	}

	/** {@inheritDoc} */
	@Override
	public Writer append(final CharSequence csq) throws IOException {
		return this.source.append(csq);
	}

	/** {@inheritDoc} */
	@Override
	public Writer append(final CharSequence csq, final int start,
			final int end) throws IOException {
		return this.source.append(csq, start, end);
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
	}

	/** {@inheritDoc} */
	@Override
	public void flush() throws IOException {
		this.source.flush();
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
	 * Returns the wrapped (original) <code>Writer</code> passed in the
	 * constructor.
	 * </p>
	 *
	 * @return The original <code>Writer</code> passed in the constructor
	 */
	public T getWrappedWriter() {
		@SuppressWarnings("unchecked")
		final T result = (T) this.source;
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.source.toString();
	}

	/** {@inheritDoc} */
	@Override
	public void write(final char[] cbuf) throws IOException {
		this.source.write(cbuf);
	}

	/** {@inheritDoc} */
	@Override
	public void write(final char[] cbuf, final int off, final int len)
			throws IOException {
		this.source.write(cbuf, off, len);
	}

	/** {@inheritDoc} */
	@Override
	public void write(final int c) throws IOException {
		this.source.write(c);
	}

	/** {@inheritDoc} */
	@Override
	public void write(final String str) throws IOException {
		this.source.write(str);
	}

	/** {@inheritDoc} */
	@Override
	public void write(final String str, final int off, final int len)
			throws IOException {
		this.source.write(str, off, len);
	}
}
