package com.gc.iotools.stream.is.inspection;

/*
 * Copyright (c) 2008,2009 Davide Simonetti. This source code is released
 * under the BSD License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.EasyStreamConstants;
import com.gc.iotools.stream.utils.StreamUtils;

/**
 * <p>
 * Gather some statistics of the <code>InputStream</code> passed in the
 * constructor.
 * </p>
 * <p>
 * It can be used to read:
 * <ul>
 * <li>The size of the internal stream.</li>
 * <li>The time spent reading the bytes.</li>
 * <li>The raw bandwidth of the underlying stream, calculated excluding the
 * time spent by the external process to elaborate the data.</li>
 * </ul>
 * </p>
 * <p>
 * Full statistics are available after the stream has been fully processed (by
 * other parts of the application), or after invoking the method
 * {@linkplain #close()} while partial statistics are available on the fly.
 * </p>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * StatsInputStream srIstream = new StatsInputStream(originalStream);
 * //performs all the application operation on stream
 * performTasksOnStream(srIstream);
 * srIstream.close();
 * long size = srIstream.getSize();
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.2.1
 */
public class StatsInputStream extends InputStream {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatsInputStream.class);

	private boolean areStatsLogged = false;

	private final boolean automaticLog;
	private boolean closeCalled = false;
	private final boolean fullReadOnClose;
	private final InputStream innerStream;
	private long markPosition = 0;
	private long size = 0;
	private long time = 0;

	/**
	 * <p>
	 * Constructs an <code>SizeReaderInputStream</code>. When
	 * {@linkplain #close()} is called the underlying stream will be closed.
	 * No further read will be done.
	 * </p>
	 * 
	 * @param source
	 *            Stream whose statistics must be calculated.
	 */
	public StatsInputStream(final InputStream source) {
		this(source, false);
	}

	/**
	 * Constructs an <code>SizeReaderInputStream</code> and allow to specify
	 * actions to do on close.
	 * 
	 * @param istream
	 *            Stream whose bytes must be counted.
	 * @param fullReadOnClose
	 *            if <i>true</i> after the close the inner stream is read
	 *            completely and the effective size of the inner stream is
	 *            calculated.
	 */
	public StatsInputStream(final InputStream istream,
			final boolean fullReadOnClose) {
		this(istream, fullReadOnClose, false);
	}

	/**
	 * <p>
	 * Constructs an <code>SizeReaderInputStream</code> and allow to specify
	 * actions to do on close.
	 * </p>
	 * <p>
	 * If automaticLog is <code>true</code> the statistics will be written
	 * when the <code>SizeReaderInputStream</code> is closed or finalized.
	 * </p>
	 * 
	 * @param istream
	 *            Stream whose bytes must be counted.
	 * @param fullReadOnClose
	 *            if <i>true</i> after the close the inner stream is read
	 *            completely and the effective size of the inner stream is
	 *            calculated.
	 * @param automaticLog
	 *            if <code>true</code> statistics will be automatically
	 *            written when the stream is closed or finalized.
	 * @since 1.2.7
	 */
	public StatsInputStream(final InputStream istream,
			final boolean fullReadOnClose, final boolean automaticLog) {
		if (istream == null) {
			throw new IllegalArgumentException("InputStream can't be null");
		}
		this.innerStream = istream;
		this.fullReadOnClose = fullReadOnClose;
		this.automaticLog = automaticLog;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int available() throws IOException {
		return this.innerStream.available();
	}

	/**
	 * Closes the inner stream. If <code>fullReadOnClose</code> was set in the
	 * constructor it also count all the bytes of the underlying stream.
	 * 
	 * @see InputStream#close()
	 * @exception IOException
	 *                if an I/O error occurs reading the whole content of the
	 *                stream.
	 */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			final long start = System.currentTimeMillis();
			// avoid multiple calls to close();
			this.closeCalled = true;
			try {
				if (this.fullReadOnClose) {
					final byte[] buffer = new byte[EasyStreamConstants.SKIP_BUFFER_SIZE];
					while (this.read(buffer) >= 0) {
						// Do nothing, just throw away the bytes and count
						// them.
					}
				}
			} finally {
				this.innerStream.close();
				this.time += System.currentTimeMillis() - start;
				if (this.automaticLog && !this.areStatsLogged) {
					logCurrentStatistics();
					this.areStatsLogged = true;
				}
			}

		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.automaticLog && !this.areStatsLogged) {
			logCurrentStatistics();
		}
		super.finalize();
	}

	/**
	 * Returns the reading bit rate in KB per second.
	 * 
	 * @return The KB/Sec bitRate of the stream.
	 */
	public float getBitRate() {
		return (this.size / EasyStreamConstants.ONE_KILOBYTE)
				/ TimeUnit.SECONDS.convert(this.time, TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the reading bit rate formatted with a convenient unit.
	 * 
	 * @return The bitRate of the stream.
	 */
	public String getBitRateString() {
		return StreamUtils.getRateString(this.size, this.time);
	}

	/**
	 * <p>
	 * Returns the number of bytes read until now from the internal
	 * <code>InputStream</code> or total length of the stream if the
	 * <code>{@link #close()}</code> method has been called or EOF was
	 * reached.
	 * </p>
	 * <p>
	 * Calculation refers to the original size of the internal
	 * <code>InputStream</code>. If {@linkplain #mark(int)} and
	 * {@linkplain #reset()} are called, the extra data read after the
	 * {@linkplain #reset()} is not taken in account, until the
	 * <code>mark</code> position is reached again.
	 * </p>
	 * 
	 * @return bytes read until now or the total length of the stream if
	 *         close() was called.
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * <p>
	 * Returns the time (in milliseconds) spent until now waiting for reading
	 * from the internal <code>InputStream</code>.
	 * </p>
	 * 
	 * @return time spent in waiting in milliseconds.
	 */
	public long getTime() {
		return this.time;
	}

	/**
	 * <p>
	 * Returns the time spent until now waiting for the internal stream to
	 * respond.
	 * </p>
	 * 
	 * @param tu
	 *            Unit to measure the time.
	 * @return time spent in waiting.
	 */
	public long getTime(final TimeUnit tu) {
		return tu.convert(this.time, TimeUnit.MILLISECONDS);
	}

	private void internallogCurrentStatistics(
			final boolean addNotClosedWarning) {
		StringBuffer message = new StringBuffer("Time spent[");
		message.append(getTime());
		message.append("]ms, bytes read [");
		message.append(getSize());
		message.append("] at [");
		message.append(getBitRate());
		message.append("].");
		if (addNotClosedWarning) {
			message.append("The stream is being finalized and "
					+ "close() was not called.");
		}
		LOGGER.info(message.toString());
	}

	/**
	 * Returns the behavior of the close method. If true when close is invoked
	 * a full read of the stream will be performed.
	 * 
	 * @return Whether a full read will be performed on the invocation of
	 *         {@linkplain #close()} method.
	 */
	public boolean isFullReadOnClose() {
		return this.fullReadOnClose;
	}

	/**
	 * Logs the current statistics.
	 */
	public void logCurrentStatistics() {
		internallogCurrentStatistics(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark(final int readlimit) {
		final long start = System.currentTimeMillis();
		this.innerStream.mark(readlimit);
		this.markPosition = this.size;
		this.time += System.currentTimeMillis() - start;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return this.innerStream.markSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read();
		if (read >= 0) {
			this.size++;
		}
		this.time += System.currentTimeMillis() - start;
		return read;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b) throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read(b);
		if (read >= 0) {
			this.size += read;
		}
		this.time += System.currentTimeMillis() - start;
		return read;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read(b, off, len);
		if (read >= 0) {
			this.size += read;
		}
		this.time += System.currentTimeMillis() - start;
		return read;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() throws IOException {
		final long start = System.currentTimeMillis();
		this.innerStream.reset();
		this.size = this.markPosition;
		this.time += System.currentTimeMillis() - start;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long skip(final long n) throws IOException {
		final long start = System.currentTimeMillis();
		final long skipSize = this.innerStream.skip(n);
		this.size += skipSize;
		this.time += System.currentTimeMillis() - start;
		return skipSize;
	}

}
