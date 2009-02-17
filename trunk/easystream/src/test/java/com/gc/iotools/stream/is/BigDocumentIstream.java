package com.gc.iotools.stream.is;

import java.io.InputStream;

/**
 * 
 * @since 1.0.9
 * @version $Revision: 1 $
 */
class BigDocumentIstream extends InputStream {
	private static final int MODULO = 256;
	private long lenght = 0;
	private long postion = 0;
	private long markPos = 0;

	public BigDocumentIstream(final long length) {
		this.lenght = length;
	}

	@Override
	public int read() {
		if (this.postion >= this.lenght) {
			return -1;
		}
		final int result = (int) (this.postion % MODULO);
		this.postion++;
		return result;
	}


	public synchronized void resetToBeginning() {
		this.postion = 0;
	}

	@Override
	public synchronized void mark(final int threshold) {
		this.markPos = this.postion;
	}

	 @Override
	public synchronized void reset() {
		this.postion = this.markPos;
	}
}
