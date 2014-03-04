package com.gc.iotools.stream.writer;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * A <code>CloseShieldWriter</code> wraps some other <code>Writer</code>, which
 * it uses as its basic sink of data. The class <code>CloseShieldWriter</code>
 * delegates all requests to the contained writer, except the
 * {@linkplain #close()} method that is not to the underlying stream.
 * </p>
 * <p>
 * This class is typically used in cases where a <code>Writer</code> needs to be
 * passed to a component that wants to explicitly close the stream but other
 * components still need to write data on it.
 * </p>
 * 
 * @author dvd.smnt
 * @since 1.2.8
 * @param <T>
 *            Type of the Writer passed in the constructor.
 * @version $Id: CloseShieldWriter.java 527 2014-02-24 19:29:50Z
 *          gabriele.contini@gmail.com $
 */
public class CloseShieldWriter<T extends Writer> extends FilterWriter {
	private int closeCount = 0;

	/**
	 * Construct a <code>CloseShieldWriter</code> that forwards the calls to the
	 * source Writer passed in the constructor.
	 * 
	 * @param source
	 *            original Writer
	 * @param <T>
	 *            a T object.
	 */
	public CloseShieldWriter(final T source) {
		super(source);
		if (source == null) {
			throw new IllegalArgumentException("Source Writer can't be null");
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
	 * Returns the wrapped (original) <code>Writer</code> passed in the
	 * constructor.
	 * </p>
	 * 
	 * @return The original <code>Writer</code> passed in the constructor
	 */
	public T getWrappedWriter() {
		@SuppressWarnings("unchecked")
		final T result = (T) this.out;
		return result;
	}

}
