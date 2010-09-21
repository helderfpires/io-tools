package com.gc.iotools.stream.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Slow OutputStream for test purposes
 * 
 * @author dvd.smnt
 */
public class SlowOutputStream extends FilterOutputStream {

	private final long wait;

	public SlowOutputStream(final long wait, final OutputStream innerStream) {
		super(innerStream);
		this.wait = wait;
	}

	@Override
	public void close() throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (super.out != null) {
			super.out.close();
		}
	}

	@Override
	public void flush() throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (super.out != null) {
			super.out.flush();
		}
	}

	public OutputStream getRawStream() {
		return super.out;
	}

	@Override
	public void write(final byte[] b) throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (super.out != null) {
			super.out.write(b);
		}
	}

	@Override
	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (super.out != null) {
			super.out.write(b, off, len);
		}
	}

	@Override
	public void write(final int b) throws IOException {
		try {
			Thread.sleep(this.wait);
		} catch (final InterruptedException e) {
			throw new IOException("Sleep interrupted", e);
		}
		if (super.out != null) {
			super.out.write(b);
		}
	}

}
