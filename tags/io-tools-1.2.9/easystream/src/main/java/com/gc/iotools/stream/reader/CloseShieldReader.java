package com.gc.iotools.stream.reader;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

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
 */
public class CloseShieldReader<T extends Reader> extends Reader {
	private int closeCount = 0;

	private final Reader source;

	/**
	 * Construct a <code>CloseShieldReader</code> that forwards the calls to
	 * the source Reader passed in the constructor.
	 * 
	 * @param source
	 *            original Reader
	 */
	public CloseShieldReader(final T source) {
		if (source == null) {
			throw new IllegalArgumentException("Source reader can't be null");
		}
		this.source = source;
	}

	/**
	 * <p>
	 * Multiple invocation of this method will result in only one invocation
	 * of the <code>close()</code> on the underlying stream.
	 * </p>
	 * 
	 * @throws IOException
	 *             Exception thrown if some error happens into the underlying
	 *             stream.
	 */
	@Override
	public void close() throws IOException {
		this.closeCount++;
	}

	/**
	 * Returns the number of time that close was called.
	 * 
	 * @see com.gc.iotools.stream.is.inspection.DiagnosticReader.java
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

	@Override
	public void mark(final int readAheadLimit) throws IOException {
		this.source.mark(readAheadLimit);
	}

	@Override
	public boolean markSupported() {
		return this.source.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.source.read();
	}

	@Override
	public int read(final char[] cbuf) throws IOException {
		return this.source.read(cbuf);
	}

	@Override
	public int read(final char[] cbuf, final int off, final int len)
			throws IOException {
		return this.source.read(cbuf, off, len);
	}

	@Override
	public int read(final CharBuffer target) throws IOException {
		return this.source.read(target);
	}

	@Override
	public boolean ready() throws IOException {
		return this.source.ready();
	}

	@Override
	public void reset() throws IOException {
		this.source.reset();
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.source.skip(n);
	}

	@Override
	public String toString() {
		return this.source.toString();
	}
}
