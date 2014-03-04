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
 * A <code>CloseOnceReader</code> wraps some other <code>Reader</code>, which it
 * uses as its basic source of data. The class <code>CloseOnceReader</code> pass
 * all requests to the contained input stream, except the {@linkplain #close()}
 * method that is passed only one time to the underlying stream.
 * </p>
 * <p>
 * Multiple invocation of the <code>close()</code> method will result in only
 * one invocation of the same method on the underlying stream. This is useful
 * with some buggy <code>Reader</code> that don't allow <code>close()</code> to
 * be called multiple times.
 * </p>
 * 
 * @author dvd.smnt
 * @since 1.2.7
 * @param <T>
 *            Type of the Reader passed in the constructor.
 * @version $Id: CloseOnceReader.java 527 2014-02-24 19:29:50Z
 *          gabriele.contini@gmail.com $
 */
public class CloseOnceReader<T extends Reader> extends FilterReader {
	private int closeCount = 0;

	/**
	 * Construct a <code>CloseOnceReader</code> that forwards the calls to the
	 * source Reader passed in the constructor.
	 * 
	 * @param source
	 *            original Reader
	 */
	public CloseOnceReader(final T source) {
		super(source);
		if (source == null) {
			throw new IllegalArgumentException("Source reader can't be null");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Multiple invocation of this method will result in only one invocation of
	 * the <code>close()</code> on the underlying stream.
	 * </p>
	 */
	@Override
	public void close() throws IOException {
		synchronized (this) {
			this.closeCount++;
			if (this.closeCount > 1) {
				return;
			}
		}
		this.in.close();
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
