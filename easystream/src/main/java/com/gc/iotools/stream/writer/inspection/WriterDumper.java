package com.gc.iotools.stream.writer.inspection;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>
 * This class act as a filter, simply forwarding the calls to the
 * <code>Writer</code> passed in the constructor. Doing so it also keeps track
 * of the data written to the underlying stream. This is useful for logging
 * purposes.
 * </p>
 * <p>
 * <b>WARNING</b>: data written to this <code>Writer</code> are kept in
 * memory, so this class should be used when the maximum size of the character
 * data is known in advance, and it is "small" compared to memory size. In
 * case this is not possible this class should be instantiated limiting the
 * data that can be dumped {@link #WriterDumper(sink, maxDumpSize)}.
 * </p>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * 	 Reader source=... //some data to be read.
 *   Writer destination1= new StringWriter();
 *
 *   WriterDumper dumper = new WriterDumper(destination1);
 *   org.apache.commons.io.IOUtils.copy(source, dumper);
 *   dumper.close();
 *   String data= dumper.getData();
 *   //at this point "data" and destination1.toString() contains the same string.
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.2.9
 * @version $Id$
 */
public class WriterDumper<T extends Writer> extends FilterWriter {
	/** Constant <code>INDEFINITE_SIZE=-1L</code> */
	public static final long INDEFINITE_SIZE = -1L;

	private long currentSize = 0;

	private final StringWriter dataDumpStream = new StringWriter();
	private boolean dumpEnabled = true;
	private final long maxDumpSize;

	/**
	 * <p>
	 * Constructor for WriterDumper. Completely record the stream for an
	 * indefinite size into memory.
	 * </p>
	 * 
	 * @param sink
	 *            the underlying stream that must be dumped.
	 */
	public WriterDumper(final T sink) {
		this(sink, -1);
	}

	/**
	 * <p>
	 * Constructor for WriterDumper.
	 * </p>
	 * 
	 * @param sink
	 *            the underlying stream that must be dumped.
	 * @param maxDumpSize
	 *            the maximum size of the dump.
	 */
	public WriterDumper(final T sink, final long maxDumpSize) {
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
	 * Allow to switch off the copy to the internal character buffer. The copy
	 * is enabled by default.
	 * </p>
	 * 
	 * @param enable
	 *            a boolean.
	 */
	public void enableDump(final boolean enable) {
		this.dumpEnabled = enable;
	}

	/**
	 * <p>
	 * Returns the data that was written until now to the internal character
	 * buffer. This corresponds to the data written to the internal
	 * <code>Writer</code> passed in the constructor if
	 * <code>maxDumpSize</code> was not reach and data dump was not disabled
	 * (calling <code>enableDump(false)</code>).
	 * </p>
	 * 
	 * @return the data that was written until now to the Writer
	 */
	public final String getData() {
		return this.dataDumpStream.toString();
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>Writer</code> passed in the
	 * constructor.
	 * </p>
	 * 
	 * @return The original <code>Writer</code> passed in the constructor
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
			result = this.currentSize < this.maxDumpSize;
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void write(final char[] b, final int off, final int len)
			throws IOException {
		this.out.write(b, off, len);
		if (this.dumpEnabled && maxSizeNotReached()) {
			int lenght;
			if (this.maxDumpSize == INDEFINITE_SIZE) {
				lenght = len;
			} else {
				lenght = (int) Math.min(len, this.maxDumpSize
						- this.currentSize);
			}
			this.currentSize += lenght;
			this.dataDumpStream.write(b, off, lenght);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(final int b) throws IOException {
		super.write(b);
		if (this.dumpEnabled && maxSizeNotReached()) {
			this.dataDumpStream.write(b);
			this.currentSize++;
		}
	}
}
