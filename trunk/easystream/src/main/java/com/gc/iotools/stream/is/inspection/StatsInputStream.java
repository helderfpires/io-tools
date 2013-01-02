package com.gc.iotools.stream.is.inspection;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.base.EasyStreamConstants;
import com.gc.iotools.stream.utils.LogUtils;
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
 * <li>The average bytes read each read() call.</li>
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
 * @version $Id$
 */
public class StatsInputStream extends InputStream {

	private static Map<String, Long> instanceNumber = new HashMap<String, Long>();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatsInputStream.class);
	private static Map<String, BigInteger> totalBytes = new HashMap<String, BigInteger>();
	private static Map<String, BigInteger> totalRead = new HashMap<String, BigInteger>();
	private static Map<String, Long> totalTime = new HashMap<String, Long>();
	private final boolean automaticLog;

	private final String callerId;
	private final StatsInputStream chainStream;
	private boolean closeCalled = false;
	private final boolean fullReadOnClose;
	private final InputStream innerStream;
	private long markPosition = 0;
	private long numberRead = 0;
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
	 * when the <code>StatsInputStream</code> is closed or finalized.
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
		this(istream, fullReadOnClose, automaticLog, null);
	}

	/**
	 * <p>
	 * Constructs an <code>SizeReaderInputStream</code> and allow to specify
	 * actions to do on close.
	 * </p>
	 * <p>
	 * If automaticLog is <code>true</code> the statistics will be written
	 * when the <code>StatsInputStream</code> is closed or finalized.
	 * </p>
	 * <p>
	 * Indicates another <code>StatsInputStream</code> to chain with. The aim
	 * is to test performances of a single <code>InputStream</code> in a chain
	 * of multiple <code>InputStreams</code>. You should put the
	 * <code>InputStream</code> to be tested between two
	 * <code>StatsInputStream</code> and chain the two together.
	 * <p>
	 * <code>
	 *  InputStream source = //source of data
	 * 	StatsInputStream sourceStats = new StatsInputStream(source);
	 * 	InputStream toBeTested = new InputStreamToBeTested(stis);
	 * 	StatsInputStream wrapperStis=new StatsInputStream(toBeTested, false, false, sourceStats);
	 * </code>
	 * <p>
	 * This will allow to produce statistics of the single
	 * <code>InputStream</code> to be tested, in a way independent from the
	 * source. Times spent will be the difference between the times from the
	 * source and times on the final wrapper.
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
	 * @param chainStream
	 *            The <code>InputStream</code> to chain.
	 * @since 1.3.0
	 */
	public StatsInputStream(final InputStream istream,
			final boolean fullReadOnClose, final boolean automaticLog,
			final StatsInputStream chainStream) {
		if (istream == null) {
			throw new IllegalArgumentException("InputStream can't be null");
		}
		this.innerStream = istream;
		this.fullReadOnClose = fullReadOnClose;
		this.automaticLog = automaticLog;
		this.callerId = LogUtils.getCaller(this.getClass());
		this.chainStream = chainStream;
		addToMapL(instanceNumber, 1);

	}

	private void addToMap(final Map<String, BigInteger> map, final long value) {
		if (!map.containsKey(this.callerId)) {
			map.put(this.callerId, BigInteger.valueOf(value));
		} else {
			final BigInteger mvalue = map.get(this.callerId);
			mvalue.add(BigInteger.valueOf(value));
		}
	}

	private void addToMapL(final Map<String, Long> map, final long value) {
		if (!map.containsKey(this.callerId)) {
			map.put(this.callerId, value);
		} else {
			final long mvalue = map.get(this.callerId);
			map.put(this.callerId, mvalue + value);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int available() throws IOException {
		return this.innerStream.available();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Closes the inner stream. If <code>fullReadOnClose</code> was set in the
	 * constructor it also count all the bytes of the underlying stream.
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
				final long timeElapsed = System.currentTimeMillis() - start;
				this.time += timeElapsed;
				addToMapL(totalTime, timeElapsed);
			}

		}
	}

	/** {@inheritDoc} */
	@Override
	protected void finalize() throws Throwable {
		if (this.automaticLog) {
			logCurrentStatistics();
		}
		super.finalize();
	}

	/**
	 * <p>
	 * Returns the average bytes per read.
	 * </p>
	 * <p>
	 * If this parameter is near 1 means that too many calls are made to read
	 * the <code>InputStream</code> bringing a loss of performances for large
	 * amount of data. Access to this <code>InputStream</code> should be made
	 * trough a <code>BufferedInputStream</code> with a reasonable buffer
	 * size.
	 * </p>
	 * <p>
	 * WARN: This measure is not accurate in case of mark and reset.
	 * </p>
	 *
	 * @return The average bytes per read().
	 */
	public float getAverageBytePerRead() {
		return (this.size * 1.0f) / this.numberRead;
	}

	/**
	 * Returns the reading bit rate in KB per second of this single instance.
	 *
	 * @return The KB/Sec bitRate of the stream.
	 */
	public float getBitRate() {
		return (this.size / EasyStreamConstants.ONE_KILOBYTE)
				/ TimeUnit.SECONDS.convert(getTime(), TimeUnit.MILLISECONDS);
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
	 * Number of calls to <code>int read()</code> ,
	 * <code>int read(byte[])</code> and <code>int read(byte[],int,int)</code>
	 * methods.
	 *
	 * @return Long representing the number of calls to read() methods.
	 */
	public long getNumberRead() {
		return this.numberRead;
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
		long time2 = this.time;
		if (this.chainStream != null) {
			time2 -= this.chainStream.getTime();
		}
		return time2;
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
		if (tu == null) {
			throw new IllegalArgumentException("TimeUnit can't be null");
		}
		long convertedTime = tu.convert(this.time, TimeUnit.MILLISECONDS);
		if (this.chainStream != null) {
			convertedTime -= this.chainStream.getTime(tu);
		}
		return convertedTime;
	}

	/**
	 * Total count of calls to <code>int read()</code>,
	 * <code>int read(byte[])</code> and <code>int read(byte[],int,int)</code>
	 * methods, made by this instance over the subsequent calls.
	 *
	 * @return Long representing the number of calls to read() methods.
	 */
	public long getTotalNumberRead() {
		final BigInteger totalReadBytes = StatsInputStream.totalRead
				.get(this.callerId);
		return (totalReadBytes == null) ? 0 : totalReadBytes.longValue();
	}

	/**
	 * <p>
	 * Returns the total time (in milliseconds) spent until now waiting for
	 * reading from the internal <code>InputStream</code> by the instances
	 * (identified by their constructor position).
	 * </p>
	 *
	 * @param tu
	 *            Unit to measure the time.
	 * @return time spent in waiting.
	 */
	public long getTotalTime(final TimeUnit tu) {
		if (tu == null) {
			throw new IllegalArgumentException("TimeUnit can't be null");
		}
		final Long totalTimeLocal = StatsInputStream.totalTime
				.get(this.callerId);
		final long timeMs = (totalTimeLocal == null) ? 0 : totalTimeLocal;
		long convertedTotalTime = tu.convert(timeMs, TimeUnit.MILLISECONDS);
		if (this.chainStream != null) {
			convertedTotalTime = convertedTotalTime
					- this.chainStream.getTotalTime(tu);
		}
		return convertedTotalTime;
	}

	private void internallogCurrentStatistics(
			final boolean addNotClosedWarning) {
		final StringBuffer message = new StringBuffer("Time spent[");
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

	/** {@inheritDoc} */
	@Override
	public void mark(final int readlimit) {
		final long start = System.currentTimeMillis();
		this.innerStream.mark(readlimit);
		this.markPosition = this.size;
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		return this.innerStream.markSupported();
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read();
		if (read >= 0) {
			this.size++;
			addToMap(totalBytes, 1);
		}
		addToMap(totalRead, 1);
		this.numberRead++;
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
		return read;
	}

	/** {@inheritDoc} */
	@Override
	public int read(final byte[] b) throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read(b);
		if (read >= 0) {
			this.size += read;
			addToMap(totalBytes, read);
		}
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
		this.numberRead++;
		addToMap(totalRead, 1);
		return read;
	}

	/** {@inheritDoc} */
	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		final long start = System.currentTimeMillis();
		final int read = this.innerStream.read(b, off, len);
		if (read >= 0) {
			this.size += read;
			addToMap(totalBytes, read);
		}
		this.numberRead++;
		addToMap(totalRead, 1);
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
		return read;
	}

	/** {@inheritDoc} */
	@Override
	public void reset() throws IOException {
		final long start = System.currentTimeMillis();
		this.innerStream.reset();
		this.size = this.markPosition;
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
	}

	/** {@inheritDoc} */
	@Override
	public long skip(final long n) throws IOException {
		final long start = System.currentTimeMillis();
		final long skipSize = this.innerStream.skip(n);
		this.size += skipSize;
		final long timeElapsed = System.currentTimeMillis() - start;
		this.time += timeElapsed;
		addToMapL(totalTime, timeElapsed);
		return skipSize;
	}

}
