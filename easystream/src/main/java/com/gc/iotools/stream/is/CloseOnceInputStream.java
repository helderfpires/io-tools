package com.gc.iotools.stream.is;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author dvd.smnt
 * @since 1.2.6
 */
public class CloseOnceInputStream extends FilterInputStream {
	private int closeCount = 0;

	public CloseOnceInputStream(final InputStream source) {
		super(source);
	}

	@Override
	public void close() throws IOException {
		this.closeCount++;
		if (this.closeCount <= 1) {
			super.close();
		}
	}

	public int getCloseCount() {
		return this.closeCount;
	}

}
