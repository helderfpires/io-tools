package com.gc.iotools.stream.reader;

import java.io.IOException;
import java.io.Reader;

/**
 * @version $Revision: 1 $
 */
public class BigDocumentReader extends Reader {
	private static final int MODULO = 256;
	private long lenght = 0;
	private long markPos = 0;
	private long postion = 0;

	public BigDocumentReader(final long length) {
		this.lenght = length;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void mark(final int threshold) {
		this.markPos = this.postion;
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

	@Override
	public int read(final char[] cbuf, final int off, final int len)
			throws IOException {

		if (cbuf == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > cbuf.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int c = read();
		if (c == -1) {
			return -1;
		}
		cbuf[off] = (char) c;

		int i = 1;
		for (; i < len; i++) {
			c = read();
			if (c == -1) {
				break;
			}
			cbuf[off + i] = (char) c;
		}

		return i;

	}

	@Override
	public synchronized void reset() {
		this.postion = this.markPos;
	}

	public synchronized void resetToBeginning() {
		this.postion = 0;
	}
}
