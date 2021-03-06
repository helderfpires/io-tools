package com.gc.iotools.stream.reader;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import com.gc.iotools.stream.base.EasyStreamConstants;

/**
 * <p>
 * Copies the data from the underlying <code>Reader</code> to the
 * <code>Writer(s)</code> passed in the constructor. The data copied are
 * similar to the underlying <code>Reader</code>.
 * </p>
 * <p>
 * When the method <code>{@link #close()}</code> is invoked all the bytes
 * remaining in the underlying <code>Reader</code> are copied to the
 * <code>Writer(s)</code>. This behavior is different from this class and
 * {@code TeeInputStream} in Apache commons-io.
 * </p>
 * <p>
 * Bytes skipped are in any case copied to the <code>Writer</code>. Mark and
 * reset of the outer <code>Reader</code> doesn't affect the data copied to
 * the <code>Writer(s)</code>, that remain similar to the <code>Reader</code>
 * passed in the constructor.
 * </p>
 * <p>
 * It also calculate some statistics on the read/write operations.
 * {@link #getWriteTime()} returns the time spent writing to the Writers,
 * {@link #getReadTime()} returns the time spent reading from the Reader and
 * {@link #getWriteSize()} returns the amount of data written
 * to a single <code>Writer</code> until now.
 * </p>
 * <p>
 * Sample usage:
 * </p>
 *
 * <pre>
 * 	 Reader source=... //some data to be read.
 *   StringWriter destination1= new StringWriter();
 *   StringWriter destination2= new StringWriter();
 *
 *   TeeReaderWriter tee = new TeeReaderWriter(source,destination1);
 *   org.apache.commons.io.IOUtils.copy(tee,destination2);
 *   tee.close();
 *   //at this point both destination1 and destination2 contains the same bytes
 *   //in destination1 were put by TeeReaderWriter while in
 *   //destination2 they were copied by IOUtils.
 *   StringBuffer buffer=destination1.getBuffer();
 * </pre>
 *
 * @see org.apache.commons.io.input.TeeReader
 * @author dvd.smnt
 * @since 1.2.7
 * @version $Id$
 */
public class TeeReaderWriter extends Reader {

	/**
	 * If <code>true</code> <code>source</code> and <code>destination</code>
	 * streams are closed when {@link #close()} is invoked.
	 */
	protected final boolean closeStreams;

	protected final boolean[] copyEnabled;

	private long destinationPosition = 0;

	/**
	 * The destination <code>Writer</code>s where data is written.
	 */
	protected final Writer[] destinations;
	private long markPosition = 0;
	private long readTime = 0;
	/**
	 * The source <code>Reader</code> where the data comes from.
	 */
	protected final Reader source;
	private long sourcePosition = 0;
	private final long[] writeTime;

	/**
	 * <p>
	 * Creates a <code>TeeInputStreamWriter</code> and saves its argument, the
	 * input stream <code>source</code> and the output stream
	 * <code>destination</code> for later use.
	 * </p>
	 * <p>
	 * This constructor allow to specify multiple <code>Writer</code> to which
	 * the data will be copied.
	 * </p>
	 *
	 * @since 1.2.7
	 * @param source
	 *            The underlying <code>Reader</code>
	 * @param closeStreams
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying
	 *            streams will not be called (it must be invoked externally).
	 * @param destinations
	 *            Data read from <code>source</code> are also written to this
	 *            <code>Writer</code>.
	 */
	public TeeReaderWriter(final Reader source, final boolean closeStreams,
			final Writer... destinations) {
		this.source = source;
		if (destinations == null) {
			throw new IllegalArgumentException(
					"Destinations Writer can't be null");
		}
		if (destinations.length == 0) {
			throw new IllegalArgumentException(
					"At least one destination Writer must be specified");
		}
		for (final Writer destination : destinations) {
			if (destination == null) {
				throw new IllegalArgumentException(
						"One of the Writers in the array is null");
			}
		}
		this.writeTime = new long[destinations.length];
		this.destinations = destinations;
		this.closeStreams = closeStreams;
		this.copyEnabled = new boolean[destinations.length];
		Arrays.fill(this.copyEnabled, true);
	}

	/**
	 * <p>
	 * Creates a <code>TeeReaderWriter</code> and saves its argument, the
	 * input stream <code>source</code> and the <code>Writer</code>
	 * <code>destination</code> for later use.
	 * </p>
	 * <p>
	 * When the method {@link #close()} it is invoked the remaining content of
	 * the <code>source</code> stream is copied to the
	 * <code>destination</code> and the <code>source</code> and
	 * <code>destination</code> streams are closed.
	 * </p>
	 *
	 * @param source
	 *            The underlying <code>Reader</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>Writer</code>.
	 */
	public TeeReaderWriter(final Reader source, final Writer destination) {
		this(source, destination, true);
	}

	/**
	 * Creates a <code>TeeReaderWriter</code> and saves its argument, the
	 * input stream <code>source</code> and the output stream
	 * <code>destination</code> for later use.
	 *
	 * @since 1.2.7
	 * @param source
	 *            The underlying <code>Reader</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>Writer</code>.
	 * @param closeStreams
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying
	 *            streams will not be called (it must be invoked externally).
	 */
	public TeeReaderWriter(final Reader source, final Writer destination,
			final boolean closeStreams) {
		this(source, closeStreams, new Writer[] { destination });
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This method is called when the method {@link #close()} is invoked. It
	 * copies all the data eventually remaining in the source
	 * <code>Reader</code> passed in the constructor to the destination
	 * <code>Writer</code>.
	 * </p>
	 * <p>
	 * The standard behavior is to close both the underlying
	 * <code>Reader</code> and <code>Writer(s)</code>. When the class was
	 * constructed with the parameter {@link #closeStreams} 
	 * set to false the underlying streams must be closed
	 * externally.
	 * @see #close()
	 */
	@Override
	public void close() throws IOException {

		IOException e1 = null;
		try {
			final char[] buffer = new char[EasyStreamConstants.SKIP_BUFFER_SIZE];
			while (read(buffer, 0, buffer.length) > 0) {
				// empty block: just throw bytes away
			}
		} catch (final IOException e) {
			e1 = new IOException(
					"Incomplete data was written to the destination "
							+ "Writer(s).");
			e1.initCause(e);
		}
		if (this.closeStreams) {
			final long startr = System.currentTimeMillis();
			this.source.close();
			this.readTime += System.currentTimeMillis() - startr;
			for (int i = 0; i < this.destinations.length; i++) {
				final long start = System.currentTimeMillis();
				this.destinations[i].close();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
		if (e1 != null) {
			throw e1;
		}
	}

	/**
	 * <p>
	 * Allow to switch off the copy to the underlying
	 * <code>OutputStream</code>s. Setting the parameter to false will disable
	 * the copy to all the underlying streams at once.
	 * </p>
	 * <p>
	 * If you need more fine grained control you should use
	 * {@link #enableCopy(boolean[])} .
	 * </p>
	 *
	 * @since 1.2.9
	 * @param enable
	 *            whether to copy or not the bytes to the underlying stream.
	 */
	public final void enableCopy(final boolean enable) {
		Arrays.fill(this.copyEnabled, enable);
	}

	/**
	 * <p>
	 * Allow to switch off the copy to the underlying
	 * <code>OutputStream</code>s, selectively enabling or disabling copy to
	 * some specific stream.
	 * </p>
	 * <p>
	 * The copy is enabled by default. Each element in the array correspond to
	 * an <code>OutputStream</code> passed in the constructor. If the
	 * correspondent element in the array passed as a parameter is set to
	 * <code>true</code> the copy will be enabled. It can be invoked multiple
	 * times.
	 * </p>
	 *
	 * @since 1.2.9
	 * @param enable
	 *            whether to copy or not the bytes to the underlying
	 *            <code>OutputStream</code>s.
	 */
	public final void enableCopy(final boolean[] enable) {
		if (enable == null) {
			throw new IllegalArgumentException("Enable array can't be null");
		}
		if (enable.length != this.copyEnabled.length) {
			throw new IllegalArgumentException("Enable array must be of "
					+ "the same size of the Writer array passed "
					+ "in the constructor. Array size [" + enable.length
					+ "] streams [" + this.copyEnabled.length + "]");
		}
		for (int i = 0; i < enable.length; i++) {
			this.copyEnabled[i] = enable[i];
		}
	}

	/**
	 * <p>
	 * Returns the number of milliseconds spent reading from the
	 * <code>source</code> <code>Reader</code>.
	 * </p>
	 *
	 * @return number of milliseconds spent reading from the
	 *         <code>source</code> .
	 * @since 1.2.7
	 */
	public long getReadTime() {
		return this.readTime;
	}

	/**
	 * <p>
	 * Returns the number of bytes written until now to a single destination
	 * <code>Writer</code>.
	 * </p>
	 * <p>
	 * This number is not affected by any of the mark and reset that are made
	 * on this {@linkplain TeeReaderWriter} and reflects only the number of
	 * bytes written.
	 * </p>
	 *
	 * @return number of bytes written until now to a single
	 *         <code>destination</code>.
	 * @since 1.2.7
	 */
	public long getWriteSize() {
		return this.destinationPosition;
	}

	/**
	 * <p>
	 * Return the time spent writing on the destination <code>Writer(s)</code>
	 * in milliseconds.
	 * </p>
	 * <p>
	 * The returned array has one element for each <code>Writer</code> passed
	 * in the constructor.
	 * </p>
	 *
	 * @return time spent writing on the destination <code>Writers</code>.
	 */
	public long[] getWriteTime() {
		return this.writeTime;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Marks the current position in this input stream. A subsequent call to
	 * the <code>reset</code> method repositions this stream at the last
	 * marked position so that subsequent reads re-read the same bytes.
	 * </p>
	 * @see #reset()
	 * @see java.io.Reader#mark(int)
	 * @since 1.2.7
	 */
	@Override
	public void mark(final int readLimit) throws IOException {
		this.source.mark(readLimit);
		this.markPosition = this.sourcePosition;
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		return this.source.markSupported();
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		final long startr = System.currentTimeMillis();
		final int result = this.source.read();
		this.readTime += System.currentTimeMillis() - startr;

		if (result >= 0) {
			this.sourcePosition++;
			if (this.sourcePosition > this.destinationPosition) {
				for (int i = 0; i < this.destinations.length; i++) {
					if (this.copyEnabled[i]) {
						final long start = System.currentTimeMillis();
						this.destinations[i].write(result);
						getWriteTime()[i] += System.currentTimeMillis()
								- start;
					}
				}
				this.destinationPosition++;
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] b, final int off, final int len)
			throws IOException {
		final long startr = System.currentTimeMillis();
		final int result = this.source.read(b, off, len);
		this.readTime += System.currentTimeMillis() - startr;

		if (result > 0) {
			if (this.sourcePosition + result > this.destinationPosition) {
				final int newLen = (int) (this.sourcePosition + result - this.destinationPosition);
				final int newOff = off + (result - newLen);
				for (int i = 0; i < this.destinations.length; i++) {
					if (this.copyEnabled[i]) {
						final long start = System.currentTimeMillis();
						this.destinations[i].write(b, newOff, newLen);
						getWriteTime()[i] += System.currentTimeMillis()
								- start;
					}
				}
				this.destinationPosition += newLen;
			}
			this.sourcePosition += result;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Repositions this stream to the position at the time the
	 * <code>mark</code> method was last called on this input stream.
	 * </p>
	 * <p>
	 * After <code>reset()</code> method is called the data is not copied
	 * anymore to the destination <code>Writer</code> until the position where
	 * <code>reset</code> was issued is reached again. This ensures the data
	 * copied to the destination <code>Writer</code> reflects the data
	 * contained in the source Reader (the one passed in the constructor).
	 * </p>
	 * @see #mark(int)
	 * @see java.io.Reader#reset()
	 * @exception IOException
	 *                If the source stream has an exception in calling
	 *                reset().
	 * @since 1.2.7
	 */
	@Override
	public synchronized void reset() throws IOException {
		this.source.reset();
		this.sourcePosition = this.markPosition;
	}

}
