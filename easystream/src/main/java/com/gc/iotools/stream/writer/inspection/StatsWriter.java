package com.gc.iotools.stream.writer.inspection;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

import com.gc.iotools.stream.utils.StreamUtils;

/**
 * <p>
 * A delegating <code>Writer</code> that gather statistics on the
 * <code>Writer</code> passed in the constructor.
 * </p>
 * <p>
 * It can be used to read:
 * <ul>
 * <li>The number of the characters written to the underlying character stream.</li>
 * <li>The time spent writing the character.</li>
 * <li>The bandwidth of the underlying stream.</li>
 * </ul>
 * </p>
 * <p>
 * Full statistics are available after the stream has been fully processed (by
 * other parts of the application), or after invoking the method
 * {@linkplain #close()} while partial statistics are available on the fly.
 * </p>
 * 
 * @author Gabriele Contini
 * @since 1.2.14
 * @version $Id: StatsWriter.java 527 2014-02-24 19:29:50Z
 *          gabriele.contini@gmail.com $
 */
public class StatsWriter extends Writer {

	private boolean closeCalled;
	private final Writer innerOs;
	private long size = 0;
	private long time = 0;

	/**
	 * Creates a new <code>StatsWriter</code> with the given destination
	 * character stream.
	 * 
	 * @param destination
	 *            Destination stream where data are written.
	 */
	public StatsWriter(final Writer destination) {
		this.innerOs = destination;
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			final long start = System.currentTimeMillis();
			this.innerOs.close();
			this.time += System.currentTimeMillis() - start;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void flush() throws IOException {
		final long start = System.currentTimeMillis();
		this.innerOs.flush();
		this.time += System.currentTimeMillis() - start;
	}

	/**
	 * <p>
	 * Returns a string representation of the writing bit rate formatted with a
	 * convenient unit. The unit will change trying to keep not more than 3
	 * digits.
	 * </p>
	 * 
	 * @return The characterRate of the stream.
	 */
	public String getBitRateString() {
		return StreamUtils.getRateString(this.size, this.time);
	}

	/**
	 * Returns the number of characters written until now.
	 * 
	 * @return return the number of characters written until now.
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * <p>
	 * Returns the time spent waiting for the internal stream to write the data.
	 * </p>
	 * 
	 * @param tu
	 *            Unit to measure the time.
	 * @return time spent in waiting.
	 */
	public long getTime(final TimeUnit tu) {
		return tu.convert(this.time, TimeUnit.MILLISECONDS);
	}

	/** {@inheritDoc} */
	@Override
	public void write(final char[] b) throws IOException {
		final long start = System.currentTimeMillis();
		this.innerOs.write(b);
		this.time += System.currentTimeMillis() - start;
		this.size += b.length;
	}

	/** {@inheritDoc} */
	@Override
	public void write(final char[] b, final int off, final int len)
			throws IOException {
		final long start = System.currentTimeMillis();
		this.innerOs.write(b, off, len);
		this.time += System.currentTimeMillis() - start;
		this.size += len;
	}

	/** {@inheritDoc} */
	@Override
	public void write(final int b) throws IOException {
		final long start = System.currentTimeMillis();
		this.innerOs.write(b);
		this.time += System.currentTimeMillis() - start;
		this.size++;
	}

}
