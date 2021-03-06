package com.gc.iotools.stream.utils;

import java.io.IOException;
import java.io.Writer;

/**
 * Slow OutputStream for test purposes
 * 
 * @author dvd.smnt
 */
public class SlowWriter extends Writer {
	private final long wait;
	private final Writer writer;

	public SlowWriter(final long wait, final Writer innerStream) {
		this.writer = innerStream;
		this.wait = wait;
	}

	@Override
	public void close() throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (this.writer != null) {
			this.writer.close();
		}
	}

	@Override
	public void flush() throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (this.writer != null) {
			this.writer.flush();
		}
	}

	public Writer getRawStream() {
		return this.writer;
	}

	@Override
	public void write(final char[] b) throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (this.writer != null) {
			this.writer.write(b);
		}
	}

	@Override
	public void write(final char[] b, final int off, final int len)
			throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (this.writer != null) {
			this.writer.write(b, off, len);
		}
	}

	@Override
	public void write(final int b) throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (this.writer != null) {
			this.writer.write(b);
		}
	}

}
