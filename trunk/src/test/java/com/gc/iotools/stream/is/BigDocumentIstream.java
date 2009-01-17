package com.gc.iotools.stream.is;

import java.io.InputStream;

/**
 * 
 * @since 1.0.9
 * @version $Revision: 1 $
 */
class BigDocumentIstream extends InputStream {
	private long _lenght = 0;
	private long _postion = 0;

	public BigDocumentIstream(final long length) {
		this._lenght = length;
	}

	@Override
	public int read() {
		if (this._postion >= this._lenght) {
			return -1;
		}
		final int result = (int) (this._postion % 256);
		this._postion++;
		return result;
	}

	@Override
	public synchronized void reset() {
		this._postion = 0;
	}
}
