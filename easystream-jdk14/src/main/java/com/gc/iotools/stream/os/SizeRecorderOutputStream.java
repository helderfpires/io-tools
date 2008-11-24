package com.gc.iotools.stream.os;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO: javadoc, junits
 * 
 * @author dvd.smnt
 * 
 */
public class SizeRecorderOutputStream extends OutputStream {

	private final OutputStream innerOs;
	private boolean closeCalled;
	private long size = 0;

	/**
	 * @param innerOs
	 */
	public SizeRecorderOutputStream(final OutputStream innerOs) {
		this.innerOs = innerOs;
	}

	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.innerOs.close();
		}
	}

	public void flush() throws IOException {
		this.innerOs.flush();
	}

	public long getSize() {
		return this.size;
	}

	public void write(final byte[] b) throws IOException {
		this.innerOs.write(b);
		this.size += b.length;
	}

	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		this.innerOs.write(b, off, len);
		this.size += len;
	}

	public void write(final int b) throws IOException {
		this.innerOs.write(b);
		this.size++;
	}

}
