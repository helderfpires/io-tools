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
 * A <code>CloseOnceWriter</code> wraps some other <code>Writer</code>
 * preventing multiple invocations of the method {@link #close()}.
 * </p>
 * <p>
 * It forwards all requests to the contained writer, except the
 * {@linkplain #close()} method that is passed only one time to the underlying
 * stream.
 * </p>
 * <p>
 * This is useful with some non conforming <code>Writer</code> that don't allow
 * <code>close()</code> to be called multiple times.
 * </p>
 * 
 * @see java.io.Writer#close()
 * @author Gabriele Contini
 * @since 1.2.14
 * @param <T>
 *            Type of the Writer passed in the constructor.
 * @version $Id: CloseOnceWriter.java 527 2014-02-24 19:29:50Z
 *          gabriele.contini@gmail.com $
 */
public class CloseOnceWriter<T extends Writer> extends FilterWriter {
	private int closeCount = 0;

	/**
	 * Construct a <code>CloseOnceWriter</code> that forwards the calls to the
	 * source Writer passed in the constructor.
	 * 
	 * @param source
	 *            original Writer
	 */
	public CloseOnceWriter(final T source) {
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
		synchronized (this) {
			this.closeCount++;
			if (this.closeCount > 1) {
				return;
			}
		}
		this.out.close();
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
		final T result = (T) super.out;
		return result;
	}

}
