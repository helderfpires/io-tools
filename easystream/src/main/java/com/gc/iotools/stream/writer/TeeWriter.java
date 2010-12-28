package com.gc.iotools.stream.writer;

/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * <p>
 * Copies the data that is written to this class to the <code>Writer(s)</code>
 * passed in the constructor. It also collect statistics on the operations
 * done (time spent writing to the internal writers, amount of data written).
 * </p>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * 	 InputStream source=... //some data to be read.
 *   StringWriter destination1= new StringWriter();
 *   StringWriter destination2= new StringWriter();
 *   
 *   TeeWriter tee =  new TeeWriter(destination1,destination2);
 *   org.apache.commons.io.IOUtils.copy(source,tee);
 *   tee.close();
 *   //at this point both destination1 and destination2 contains the same characters.
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.2.7
 */
public class TeeWriter extends Writer {

	/**
	 * <code>True</code> when {@link #close()} is invoked. Prevents data from
	 * being written to the destination <code>Writer(s)</code> after
	 * {@link #close()} has been invoked.
	 */
	protected boolean closeCalled = false;
	private final boolean[] copyEnabled;

	/**
	 * The destination <code>Writer(s)</code> where data is written.
	 */
	protected final Writer[] destinations;
	private long size = 0;
	private final long[] writeTime;

	/**
	 * <p>
	 * Creates a <code>TeeWriter</code> and saves its arguments, the
	 * <code>destinations</code> for later use.
	 * </p>
	 * <p>
	 * This constructor allow to specify multiple <code>Writer</code> to which
	 * the data will be copied.
	 * </p>
	 * 
	 * @since 1.2.4
	 * @param destinations
	 *            Data written to this<code>Writer</code> are copied to all
	 *            the <code>destinations</code>.
	 */
	public TeeWriter(final Writer... destinations) {
		checkDestinations(destinations);
		this.writeTime = new long[destinations.length];
		this.destinations = destinations;
		this.copyEnabled = new boolean[destinations.length];
		Arrays.fill(this.copyEnabled, true);
	}

	private void checkDestinations(final Writer... destinations) {
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			for (int i = 0; i < this.destinations.length; i++) {
				final Writer stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.close();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
	}

	/**
	 * <p>
	 * Allow to switch off the copy to the underlying streams. Setting the
	 * parameter to false will disable the copy to all the underlying streams
	 * at once.
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
	 * Allow to switch off the copy to the underlying streams, selectively
	 * enabling or disabling copy on some specific stream.
	 * </p>
	 * <p>
	 * The copy is enabled by default. Each element in the array correspond to
	 * an <code>OutputStream</code> passed in the constructor. If the
	 * correspondent element in the array passed as a parameter is set to
	 * <code>true</code> the copy will be enabled.It can be invoked multiple
	 * times.
	 * </p>
	 * 
	 * @since 1.2.9
	 * @param enable
	 *            whether to copy or not the bytes to the underlying stream.
	 */
	public final void enableCopy(final boolean[] enable) {
		if (enable == null) {
			throw new IllegalArgumentException("Enable array can't be null");
		}
		if (enable.length != this.copyEnabled.length) {
			throw new IllegalArgumentException("Enable array must be of "
					+ "the same size of the OutputStream array passed "
					+ "in the constructor. Array size [" + enable.length
					+ "] streams [" + this.copyEnabled.length + "]");
		}
		for (int i = 0; i < enable.length; i++) {
			this.copyEnabled[i] = enable[i];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws IOException {
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				final Writer stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.flush();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
	}

	/**
	 * <p>
	 * This method returns the size in bytes of the data written to this
	 * Writer. It can be used to collect statistics on the write operations.
	 * </p>
	 * 
	 * @return size in bytes of the data written to the <code>Writers</code>.
	 */
	public final long getSize() {
		return this.size;
	}

	/**
	 * <p>
	 * Return the time spent writing to the destination <code>Writer(s)</code>
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
	 */
	@Override
	public void write(final char[] b) throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final Writer stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b);
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size += b.length;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final char[] b, final int off, final int len)
			throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final Writer stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b, off, len);
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size += len;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final int b) throws IOException {
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final Writer stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b);
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size++;
		}
	}
}
