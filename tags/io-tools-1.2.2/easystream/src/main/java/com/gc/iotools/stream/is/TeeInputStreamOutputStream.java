package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gc.iotools.stream.base.AbstractInputStreamWrapper;

/**
 * <p>
 * Copies the data from the underlying <code>InputStream</code> to the
 * <code>OutputStream</code> passed in the constructor. The data copied are
 * similar to the underlying <code>InputStream</code>.
 * </p>
 * <p>
 * When the method <code>{@link #close()}</code> is invoked all the bytes
 * remaining in the underlying <code>InputStream</code> are copied to the
 * <code>OutputStream</code>. This behavior is different from this class and
 * {@code TeeInputStream} in Apache commons-io.
 * </p>
 * <p>
 * Bytes skipped are copied to the <code>OutputStream</code>.
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
 *   TeeInputStreamOutputStream tee=
 *                          new TeeInputStreamOutputStream(source,destination1);
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
	/**
	 * Buffer size used in skip() operations.
	 */
	private static final int BUF_SIZE = 8192;

	private long markPosition = 0;

	private long destinationPosition = 0;

	private long sourcePosition = 0;
	/**
	 * The destination <code>OutputStream</code> where data is written.
	 */
	protected final OutputStream destination;
	/**
	 * If <code>true</code> <code>source</code> and <code>destination</code>
	 * streams are closed when {@link #close()} is invoked.
	 */
	protected final boolean closeStreams;

	/**
	 * <p>
	 * Creates a <code>TeeInputStreamOutputStream</code> and saves its argument,
	 * the input stream <code>source</code> and the <code>OutputStream</code>
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
	 * Creates a <code>TeeInputStreamOutputStream</code> and saves its argument,
	 * the input stream <code>source</code> and the output stream
	 * <code>destination</code> for later use.
	 * 
	 * @since 1.2.0
	 * @param source
	 *            The underlying <code>InputStream</code>
	 * @param destination
	 *            Data read from <code>source</code> are also written to this
	 *            <code>OutputStream</code>.
	 * @param closeStreams
	 *            if <code>true</code> the <code>destination</code> will be
	 *            closed when the {@link #close()} method is invoked. If
	 *            <code>false</code> the close method on the underlying streams
	 *            will not be called (it must be invoked externally).
	 * 
	 */
	public TeeInputStreamOutputStream(final InputStream source,
			final OutputStream destination, final boolean closeStreams) {
		super(source);
		this.destination = destination;
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
	 * was constructed with the parameter
	 * {@link TeeInputStreamOutputStream#closeStreams closeStreams} set to false
	 * the underlying streams must be closed externally.
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
			final byte[] buffer = new byte[BUF_SIZE];
			while (innerRead(buffer, 0, BUF_SIZE) > 0) {
				// empty block: just throw bytes away
			}
		} catch (final IOException e) {
			e1 = new IOException(
					"It's not possible to copy to the destination OutputStream.");
			e1.initCause(e);
		}
		if (this.closeStreams) {
			this.source.close();
			this.destination.close();
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
				this.destination.write(b, newOff, newLen);
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
				this.destination.write(result);
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
	 * After <code>reset</code> method is called the data is not copied anymore
	 * to the destination OutputStream until the position where
	 * <code>reset</code> was issued is reached again. This ensures the data
	 * copied to the destination OutputStream reflects the data contained in the
	 * source InputStream (the one passed in the constructor).
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

}
