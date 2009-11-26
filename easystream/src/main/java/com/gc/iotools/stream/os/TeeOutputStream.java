package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */
import java.io.IOException;
import java.io.OutputStream;

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
 */
public class TeeOutputStream extends OutputStream {

	private long size = 0;
	private final long[] writeTime;

	/**
	 * <code>True</code> when {@link #close()} is invoked. Prevents data from
	 * being written to the destination <code>OutputStream(s)</code> after
	 * {@link #close()} has been invoked.
	 */
	protected boolean closeCalled = false;
	/**
	 * The destination <code>OutputStream(s)</code> where data is written.
	 */
	protected final OutputStream[] destinations;

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
	 *            Data written to this<code>OutputStream</code> are copied to
	 *            all the <code>destinations</code>.
	 */
	public TeeOutputStream(final OutputStream... destinations) {
		checkDestinations(destinations);
		this.writeTime = new long[destinations.length];
		this.destinations = destinations;
	}

	private void checkDestinations(final OutputStream... destinations) {
		if (destinations == null) {
			throw new IllegalArgumentException(
					"Destinations OutputStream can't be null");
		}
		if (destinations.length == 0) {
			throw new IllegalArgumentException(
					"At least one destination OutputStream must be specified");
		}
		for (final OutputStream destination : destinations) {
			if (destination == null) {
				throw new IllegalArgumentException(
						"One of the outputstreams in the array is null");
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
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.close();
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @return time spent writing on the destination <code>OutputStreams</code>.
	 */
	public long[] getWriteTime() {
		return this.writeTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final byte[] b) throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.write(b);
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
			this.size += b.length;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		if (b == null) {
			throw new NullPointerException("Array of bytes can't be null");
		}
		if (!this.closeCalled) {
			for (int i = 0; i < this.destinations.length; i++) {
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.write(b, off, len);
				this.writeTime[i] += System.currentTimeMillis() - start;
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
				final OutputStream stream = this.destinations[i];
				final long start = System.currentTimeMillis();
				stream.write(b);
				this.size++;
				this.writeTime[i] += System.currentTimeMillis() - start;
			}
			this.size++;
		}
	}
}
