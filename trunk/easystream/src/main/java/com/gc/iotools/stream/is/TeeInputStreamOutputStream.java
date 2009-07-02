package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gc.iotools.stream.base.AbstractInputStreamWrapper;
import com.gc.iotools.stream.base.EasyStreamConstants;

/**
 * <p>
 * Copies the data from the underlying <code>InputStream</code> to the
 * <code>OutputStream(s)</code> passed in the constructor. The data copied are
 * similar to the underlying <code>InputStream</code>.
 * </p>
 * <p>
 * When the method <code>{@link #close()}</code> is invoked all the bytes
 * remaining in the underlying <code>InputStream</code> are copied to the
 * <code>OutputStream</code>. This behavior is different from this class and
 * {@code TeeInputStream} in Apache commons-io.
 * </p>
 * <p>
 * Bytes skipped are in any case copied to the <code>OutputStream</code>. Mark
 * and reset of the outer <code>InputStream</code> doesn't affect the data
 * copied to the <code>OutputStream</code>.
 * </p>
 * <p>
 * It is also possible to copy the data to multiple <code>OutputStreams</code>
 * at once.
 * </p>
 * <p>
 * Sample usage:
 * </p>
 * 
 * <pre>
 * 	 InputStream source=... //some data to be readed.
 *   ByteArrayOutputStream destination1= new ByteArrayOutputStream();
 *   ByteArrayOutputStream destination2= new ByteArrayOutputStream();
 *   
 *   TeeOutputStream tee=
 *                          new TeeOutputStream(source,destination1);
 *   org.apache.commons.io.IOUtils.copy(tee,destination2);
 *   tee.close();
 *   //at this point both destination1 and destination2 contains the same bytes.
 *   byte[] bytes=destination1.getBytes();
 * </pre>
 * 
 * @see org.apache.commons.io.input.TeeInputStream
 * @author dvd.smnt
 * @since 1.0.6
 */
public class TeeInputStreamOutputStream extends AbstractInputStreamWrapper {

	private final long[] writeTime;

	private long markPosition = 0;

	private long destinationPosition = 0;

	private long sourcePosition = 0;
	/**
	 * The destination <code>OutputStream</code> where data is written.
	 */
	protected final OutputStream[] destinations;
	/**
	 * If <code>true</code> <code>source</code> and <code>destination</code>
	 * streams are closed when {@link #close()} is invoked.
	 */
	protected final boolean closeStreams;

	/**
	 * <p>
	 * Creates a <code>TeeOutputStream</code> and saves its argument, the input
	 * stream <code>source</code> and the <code>OutputStream</code>
	 * <code>destination</code> for later use.
	 * </p>
	 * <p>
	 * When the method {@link #close()} it is invoked the remaining content of
	 * the <code>source</code> stream is copied to the <code>destination</code>
	 * and the <code>source</code> and <code>destination</code> streams are
	 * closed.
	 * </p>
	 * 
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination) {
		this(source, destination, true);
	}

	/**
	 * Creates a <code>TeeOutputStream</code> and saves its argument, the input
	 * stream <code>source</code> and the output stream <code>destination</code>
	 * for later use.
	 * 
	 * @since 1.2.0
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 * @param closeCalled
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying streams
	 *            will not be called (it must be invoked externally).
	 * 
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination, final boolean closeStreams) {
		this(source, closeStreams, new OutputStream[] { destination });
	}

	/**
	 * <p>
	 * Creates a <code>TeeOutputStream</code> and saves its argument, the input
	 * stream <code>source</code> and the output stream <code>destination</code>
	 * for later use.
	 * </p>
	 * <p>
	 * This constructor allow to specify multiple <code>OutputStream</code> to
	 * which the data will be copied.
	 * </p>
	 * 
	 * @since 1.2.3
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param closeCalled
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying streams
	 *            will not be called (it must be invoked externally).
	 * @param destinations
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final boolean closeStreams, final OutputStream... destinations) {
		super(source);
		if (destinations == null) {
			throw new IllegalArgumentException(
					"Destinations OutputStream can't be null");
		}
		if (destinations.length == 0) {
			throw new IllegalArgumentException(
					"At least one destination OutputStream must be specified");
		}
		for (OutputStream destination : destinations) {
			if (destination == null) {
				throw new IllegalArgumentException(
						"One of the outputstreams in the array is null");
			}
		}
		this.writeTime = new long[destinations.length + 1];
		this.destinations = destinations;
		this.closeStreams = closeStreams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int available() throws IOException {
		return this.source.available();
	}

	/**
	 * <p>
	 * This method is called when the method {@link #close()} is invoked. It
	 * copies all the data eventually remaining in the source
	 * <code>InputStream</code> passed in the constructor to the destination
	 * <code>OutputStream</code>.
	 * </p>
	 * <p>
	 * The standard behavior is to close both the underlying
	 * <code>InputStream</code> and <code>OutputStream</code>. When the class
	 * was constructed with the parameter {@link TeeOutputStream#closeCalled
	 * closeCalled} set to false the underlying streams must be closed
	 * externally.
	 * 
	 * @throws IOException
	 *             thrown when a IO problem occurs in reading or writing the
	 *             data.
	 * @see #close()
	 */
	@Override
	public void closeOnce() throws IOException {

		IOException e1 = null;
		try {
			final byte[] buffer = new byte[EasyStreamConstants.SKIP_BUFFER_SIZE];
			while (innerRead(buffer, 0, buffer.length) > 0) {
				// empty block: just throw bytes away
			}
		} catch (final IOException e) {
			e1 = new IOException(
					"It's not possible to copy to the destination OutputStream.");
			e1.initCause(e);
		}
		if (this.closeStreams) {
			this.source.close();
			for (int i = 0; i < destinations.length; i++) {
				long start = System.currentTimeMillis();
				destinations[i].close();
				getWriteTime()[i] += System.currentTimeMillis() - start;
			}
		}
		if (e1 != null) {
			throw e1;
		}
	}

	@Override
	public int innerRead(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.source.read(b, off, len);
		if (result > 0) {
			if (this.sourcePosition + result > this.destinationPosition) {
				final int newLen = (int) (this.sourcePosition + result - this.destinationPosition);
				final int newOff = off + (result - newLen);
				for (int i = 0; i < destinations.length; i++) {
					long start = System.currentTimeMillis();
					destinations[i].write(b, newOff, newLen);
					getWriteTime()[i] += System.currentTimeMillis() - start;
				}
				this.destinationPosition += newLen;
			}
			this.sourcePosition += result;
		}
		return result;
	}

	/**
	 * <p>
	 * Marks the current position in this input stream. A subsequent call to the
	 * <code>reset</code> method repositions this stream at the last marked
	 * position so that subsequent reads re-read the same bytes.
	 * </p>
	 * 
	 * @param readLimit
	 *            the maximum limit of bytes that can be read before the mark
	 *            position becomes invalid.
	 * @see #reset()
	 * @see java.io.InputStream#mark(int)
	 * @since 1.2.0
	 */
	@Override
	public void mark(final int readLimit) {
		this.source.mark(readLimit);
		this.markPosition = this.sourcePosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return this.source.markSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		final int result = this.source.read();
		if (result >= 0) {
			this.sourcePosition++;
			if (this.sourcePosition > this.destinationPosition) {
				for (int i = 0; i < destinations.length; i++) {
					long start = System.currentTimeMillis();
					destinations[i].write(result);
					getWriteTime()[i] += System.currentTimeMillis() - start;
				}
				this.destinationPosition++;
			}
		}
		return result;
	}

	/**
	 * <p>
	 * Repositions this stream to the position at the time the <code>mark</code>
	 * method was last called on this input stream.
	 * </p>
	 * <p>
	 * After <code>reset()</code> method is called the data is not copied
	 * anymore to the destination <code>OutputStream</code> until the position
	 * where <code>reset</code> was issued is reached again. This ensures the
	 * data copied to the destination <code>OutputStream</code> reflects the
	 * data contained in the source InputStream (the one passed in the
	 * constructor).
	 * </p>
	 * 
	 * @see #mark(int)
	 * @see java.io.InputStream#reset()
	 * @exception IOException
	 *                If the source stream has an exception in calling reset().
	 * @since 1.2.0
	 */
	@Override
	public synchronized void reset() throws IOException {
		this.source.reset();
		this.sourcePosition = this.markPosition;
	}

	/**
	 * <p>
	 * Return the time spent writing on the destination
	 * <code>OutputStream(s)</code> in milliseconds.
	 * </p>
	 * <p>
	 * The returned array has one element for each <code>OutputStream</code>
	 * passed in the constructor.
	 * </p>
	 * 
	 * @deprecated
	 * @return time spent writing on the destination <code>OutputStreams</code>.
	 * @see getStats
	 */
	@Deprecated
	public long[] getWriteTime() {
		return writeTime;
	}

	/**
	 * <p>
	 * Return an array of long representing the time spent reading from the
	 * <code>InputStream</code> or writing on the destination
	 * <code>OutputStream(s)</code>. Time unit is milliseconds.
	 * </p>
	 * <p>
	 * The returned array has is has one element for each
	 * <code>OutputStream</code> passed in the constructor.
	 * </p>
	 * 
	 */
	public long[] getTimeSpent() {
		return writeTime;
	}
}
