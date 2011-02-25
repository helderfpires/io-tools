package com.gc.iotools.stream.is;
/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * A <code>CloseOnceInputStream</code> contains some other input stream, which
 * it uses as its basic source of data. The class
 * <code>CloseOnceInputStream</code> pass all requests to the contained input
 * stream, except the {@linkplain #close()} method that is passed only one
 * time to the underlying stream.
 * </p>
 * <p>
 * Multiple invocation of the <code>close()</code> method will result in only
 * one invocation of the same method on the underlying stream. This is useful
 * with some buggy <code>InputStream</code> that don't allow
 * <code>close()</code> to be called multiple times.
 * </p>
 *
 * @author dvd.smnt
 * @since 1.2.6
 * @param <T>
 *            Type of the InputStream passed in the constructor.
 * @version $Id$
 */
public class CloseOnceInputStream<T extends InputStream> extends
		FilterInputStream {
	private int closeCount = 0;

	/**
	 * Construct a <code>CloseOnceInputStream</code> that forwards the calls
	 * to the source InputStream passed in the constructor.
	 *
	 * @param source
	 *            original InputStream
	 */
	public CloseOnceInputStream(final T source) {
		super(source);
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
			super.close();
		}
	}

	/**
	 * Returns the number of time that close was called.
	 *
	 * @see com.gc.iotools.stream.is.inspection.DiagnosticInputStream
	 * @return Number of times that close was called
	 */
	public int getCloseCount() {
		return this.closeCount;
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>InputStream</code> passed in the
	 * constructor.
	 * </p>
	 *
	 * @return The original <code>InputStream</code> passed in the constructor
	 */
	public T getWrappedInputStream() {
		@SuppressWarnings("unchecked")
		final T result = (T) super.in;
		return result;
	}
}
