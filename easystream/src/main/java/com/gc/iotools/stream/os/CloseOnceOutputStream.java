package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * A decorating <code>OutputStream</code> that prevents multiple invocations of
 * the {@linkplain #close()} method on the underlying stream.
 * </p>
 * <p>
 * Multiple invocation of the <code>close()</code> method will result in only
 * one invocation of the same method on the underlying stream. This is useful
 * with some non standard <code>OutputStream</code> that don't allow
 * <code>close()</code> to be called multiple times.
 * </p>
 * 
 * @author Gabriele Contini
 * @since 1.2.14
 * @param <T>
 *            Type of the OutputStream passed in the constructor.
 * @version $Id: CloseOnceOutputStream.java 523 2013-01-02 15:46:17Z
 *          gabriele.contini@gmail.com $
 */
public class CloseOnceOutputStream<T extends OutputStream> extends
		FilterOutputStream {
	private int closeCount = 0;

	/**
	 * Construct a <code>CloseOnceOutputStream</code> that forwards the calls to
	 * the source OutputStream passed in the constructor.
	 * 
	 * @param source
	 *            original OutputStream
	 */
	public CloseOnceOutputStream(final T source) {
		super(source);
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
		super.close();
	}

	/**
	 * Returns the number of time that close was called.
	 * 
	 * @see com.gc.iotools.stream.is.inspection.DiagnosticOutputStream
	 * @return Number of times that close was called
	 */
	public int getCloseCount() {
		return this.closeCount;
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>OutputStream</code> passed in the
	 * constructor.
	 * </p>
	 * 
	 * @return The original <code>OutputStream</code> passed in the constructor
	 */
	public T getWrappedOutputStream() {
		@SuppressWarnings("unchecked")
		final T result = (T) super.out;
		return result;
	}
}
