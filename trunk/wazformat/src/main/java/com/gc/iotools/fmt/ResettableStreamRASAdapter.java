package com.gc.iotools.fmt;

import java.io.IOException;

import com.gc.iotools.fmt.base.ResettableInputStream;
import com.gc.iotools.stream.is.RandomAccessInputStream;
import com.gc.iotools.stream.store.Store;

final class ResettableStreamRASAdapter extends ResettableInputStream {
	private final RandomAccessInputStream ras;
	
	public ResettableStreamRASAdapter(RandomAccessInputStream ras) {
		super();
		this.ras = ras;
	}

	@Override
	public void resetToBeginning() throws IOException {
		ras.seek(0);
	}

	@Override
	public int available() throws IOException {
		return this.ras.available();
	}

	@Override
	public void close() throws IOException {
		this.ras.close();
	}

	public Store getStore() {
		return this.ras.getStore();
	}

	@Override
	public void mark(int readLimit) {
		this.ras.mark(readLimit);
	}

	@Override
	public boolean markSupported() {
		return this.ras.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.ras.read();
	}

	@Override
	public final int read(byte[] b, int off, int len) throws IOException {
		return this.ras.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.ras.read(b);
	}

	@Override
	public void reset() throws IOException {
		this.ras.reset();
	}

	@Override
	public long skip(long n) throws IOException {
		return this.ras.skip(n);
	}



}
