package com.gc.iotools.stream.os.inspection;

/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * <p>
 * Copies the data that is written to this class to the
 * <code>OutputStream</code> passed in the constructor. It also keep track of
 * the data written to the underlying stream in a byte array that can be read
 * at any time. This is useful for logging purposes.
 * </p>
 * <p>
 * <b>WARNING</b>: data written to this <code>OutputStream</code> are kept in
 * memory, so this class should be used when the maximum size of the data is
 * known in advance, and it is "small" compared to memory size. In case this
 * is not possible this class should be instantiated limiting the data that
 * can be dumped {@link #OutputStreamStreamDumper(sink, maxDumpSize)}.
 * </p>
 * <p>
 * Usage:
 * </p>
 *
 * <pre>
 * 	 InputStream source=... //some data to be read.
 *   OutputStream destination1= new ByteArrayOutputStream();
 *
 *   OutputStreamStreamDumper dumper = new OutputStreamStreamDumper(destination1);
 *   org.apache.commons.io.IOUtils.copy(source, dumper);
 *   dumper.close();
 *   byte[] data= dumper.getData();
 *   //at this point both destination1 and destination2 contains the same bytes.
 * </pre>
 *
 * @author dvd.smnt
 * @since 1.2.9
 * @version $Id$
 */
public class OutputStreamDumper<T extends OutputStream> extends
		FilterOutputStream {
	/** Constant <code>INDEFINITE_SIZE=-1L</code> */
	public static final long INDEFINITE_SIZE = -1L;

	private final ByteArrayOutputStream dataDumpStream = new ByteArrayOutputStream();

	private boolean dumpEnabled = true;
	private final long maxDumpSize;

	/**
	 * <p>Constructor for OutputStreamDumper.</p>
	 *
	 * @param sink 
	 * 		the underlying stream that must be dumped.
	 */
	public OutputStreamDumper(final T sink) {
		this(sink, -1);
	}

	/**
	 * <p>Constructor for OutputStreamDumper.</p>
	 *
	 * @param sink 
	 * 		the underlying stream that must be dumped.
	 * @param maxDumpSize the maximum size of the dump.
	 */
	public OutputStreamDumper(final T sink, final long maxDumpSize) {
		super(sink);
		this.maxDumpSize = maxDumpSize;
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		super.close();
	}

	/**
	 * <p>
	 * Allow to switch off the copy to the internal byte array. The copy is
	 * enabled by default.
	 * </p>
	 *
	 * @param enable a boolean.
	 */
	public void enableDump(final boolean enable) {
		this.dumpEnabled = enable;
	}

	/**
	 * <p>
	 * Returns the data that was written until now to the internal byte array.
	 * This corresponds to the data written to the internal
	 * <code>OutputStream</code> passed in the constructor if
	 * <code>maxDumpSize</code> was not reach and data dump was not disabled
	 * (calling <code>enableDump(false)</code>).
	 * </p>
	 *
	 * @return the data that was written until now to the OutputStream
	 */
	public final byte[] getData() {
		return this.dataDumpStream.toByteArray();
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>OutputStream</code> passed in the
	 * constructor.
	 * </p>
	 *
	 * @return The original <code>OutputStream</code> passed in the
	 *         constructor
	 */
	@SuppressWarnings("unchecked")
	public final T getWrappedStream() {
		return (T) this.out;
	}

	private boolean maxSizeNotReached() {
		boolean result;
		if (this.maxDumpSize == INDEFINITE_SIZE) {
			result = true;
		} else {
			final long currentSize = this.dataDumpStream.size();
			result = currentSize < this.maxDumpSize;
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		this.out.write(b, off, len);
		if (this.dumpEnabled && maxSizeNotReached()) {
			int lenght;
			if (this.maxDumpSize == INDEFINITE_SIZE) {
				lenght = len;
			} else {
				final long currentPosition = this.dataDumpStream.size();
				lenght = (int) Math.min(len, this.maxDumpSize
						- currentPosition);
			}
			this.dataDumpStream.write(b, off, lenght);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(final int b) throws IOException {
		super.write(b);
		if (this.dumpEnabled && maxSizeNotReached()) {
			this.dataDumpStream.write(b);
		}
	}
}
