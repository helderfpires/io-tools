package com.gc.iotools.stream.reader;
/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * <p>
 * A <code>CloseShieldReader</code> wraps some other <code>Reader</code>,
 * which it uses as its basic source of data. The class
 * <code>CloseShieldReader</code> pass all requests to the contained stream,
 * except the {@linkplain #close()} method that is not to the underlying
 * stream.
 * </p>
 * <p>
 * This class is typically used in cases where a <code>Reader</code> needs to
 * be passed to a component that wants to explicitly close the stream even if
 * more input would still be available to other components.
 * </p>
 *
 * @author dvd.smnt
 * @since 1.2.8
 * @param <T>
 *            Type of the Reader passed in the constructor.
 * @version $Id$
 */
public class CloseShieldReader<T extends Reader> extends FilterReader {
	private int closeCount = 0;


	/**
	 * Construct a <code>CloseShieldReader</code> that forwards the calls to
	 * the source Reader passed in the constructor.
	 *
	 * @param source
	 *            original Reader
	 * @param <T> a T object.
	 */
	public CloseShieldReader(final T source) {
		super(source);
		if (source == null) {
			throw new IllegalArgumentException("Source reader can't be null");
		}
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
		final T result = (T) this.in;
		return result;
	}

	
}
