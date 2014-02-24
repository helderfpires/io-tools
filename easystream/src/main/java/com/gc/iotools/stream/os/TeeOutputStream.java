package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * <p>
 * Copies the data that is written to this class to the
 * <code>OutputStream(s)</code> passed in the constructor. It also collect
 * statistics on the operations done (time spent writing to the internal
 * streams, amount of data written).
 * </p>
 * <p>
 * Usage:
 * </p>
 *
 * <pre>
 * 	 InputStream source=... //some data to be read.
 *   ByteArrayOutputStream destination1= new ByteArrayOutputStream();
 *   ByteArrayOutputStream destination2= new ByteArrayOutputStream();
 *
 *   TeeOutputStream tee =  new TeeOutputStream(destination1,destination2);
 *   org.apache.commons.io.IOUtils.copy(source,tee);
 *   tee.close();
 *   //at this point both destination1 and destination2 contains the same bytes.
 * </pre>
 *
 * @author dvd.smnt
 * @since 1.2.4
 * @version $Id$
 */
public class TeeOutputStream extends OutputStream {

	/**
	 * <code>True</code> when {@link #close()} is invoked. Prevents data from
	 * being written to the destination <code>OutputStream(s)</code> after
	 * {@link #close()} has been invoked.
	 */
	protected boolean closeCalled = false;
	private final boolean[] copyEnabled;
	/**
	 * The destination <code>OutputStream(s)</code> where data is written.
	 */
	protected final OutputStream[] destinations;

	private long size = 0;
	private final long[] writeTime;

	/**
	 * <p>
	 * Creates a <code>TeeOutputStream</code> and saves its arguments, the
	 * <code>destinations</code> for later use.
	 * </p>
	 * <p>
	 * This constructor allow to specify multiple <code>OutputStream</code> to
	 * which the data will be copied.
	 * </p>
	 *
	 * @since 1.2.4
	 * @param destinations
	 *            Data written to this <code>OutputStream</code> are copied to
	 *            all the <code>destinations</code>.
	 */
	public TeeOutputStream(final OutputStream... destinations) {
		checkDestinations(destinations);
		this.writeTime = new long[destinations.length];
		this.destinations = destinations;
		this.copyEnabled = new boolean[destinations.length];
		Arrays.fill(this.copyEnabled, true);
	}

	private void checkDestinations(final OutputStream... dests) {
		if (dests == null) {
			throw new IllegalArgumentException(
					"Destinations OutputStream can't be null");
		}
		if (dests.length == 0) {
			throw new IllegalArgumentException(
					"At least one destination OutputStream must be specified");
		}
		for (final OutputStream destination : dests) {
			if (destination == null) {
				throw new IllegalArgumentException(
						"One of the outputstreams in the array is null");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			for (int i = 0; i < this.destinations.length; i++) {
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.close();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
	}

	/**
	 * <p>
	 * Allow to switch off the copy to the underlying streams. The copy is
	 * enabled by default. Setting the parameter to false will disable the
	 * copy to all the underlying streams at once.
	 * </p>
	 * <p>
	 * If you need more fine grained control you should use
	 * {@link #enableCopy(boolean[])} .
	 * </p>
	 *
	 * @since 1.2.8
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

	/** {@inheritDoc} */
	@Override
	public void flush() throws IOException {
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.flush();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
	}

	/**
	 * <p>
	 * Returns the <code>OutputStream</code>(s) passed in the constructor.
	 * </p>
	 *
	 * @since 1.2.9
	 * @return Array of OutputStream passed in the constructor.
	 */
	public final OutputStream[] getDestinationStreams() {
		return this.destinations;
	}

	/**
	 * <p>
	 * This method returns the size in bytes of the data written to this
	 * OutputStream. It can be used to collect statistics on the write
	 * operations.
	 * </p>
	 *
	 * @return size in bytes of the data written to the
	 *         <code>OutputStreams</code>.
	 */
	public final long getSize() {
		return this.size;
	}

	/**
	 * <p>
	 * Return the time spent writing to the destination
	 * <code>OutputStream(s)</code> in milliseconds.
	 * </p>
	 * <p>
	 * The returned array has one element for each <code>OutputStream</code>
	 * passed in the constructor.
	 * </p>
	 *
	 * @return time spent writing on the destination
	 *         <code>OutputStreams</code>.
	 */
	public long[] getWriteTime() {
		return this.writeTime;
	}

	/** {@inheritDoc} */
	@Override
	public void write(final byte[] b) throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final OutputStream stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b);
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size += b.length;
		} else {
			throw new IOException("Stream already closed.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final OutputStream stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b, off, len);
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size += len;
		} else {
			throw new IOException("Stream already closed.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(final int b) throws IOException {
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				if (this.copyEnabled[i]) {
					final OutputStream stream = this.destinations[i];
					final long start = System.currentTimeMillis();
					stream.write(b);
					this.size++;
					this.writeTime[i] += System.currentTimeMillis() - start;
				}
			}
			this.size++;
		} else {
			throw new IOException("Attempt to write to a closed stream.");
		}
	}
}
