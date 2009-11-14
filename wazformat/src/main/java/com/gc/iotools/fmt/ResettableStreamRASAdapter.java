package com.gc.iotools.fmt;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.fmt.base.ResettableInputStream;
import com.gc.iotools.stream.is.RandomAccessInputStream;
import com.gc.iotools.stream.store.OnOffStore;
import com.gc.iotools.stream.store.Store;
import com.gc.iotools.stream.store.ThresholdStore;

public class ResettableStreamRASAdapter extends ResettableInputStream {
	private final RandomAccessInputStream ras;
	private boolean closeCalled = false;

	public ResettableStreamRASAdapter(final InputStream source) {
		final ThresholdStore ts = new ThresholdStore(128 * 1024);
		final OnOffStore os = new OnOffStore(ts);
		this.ras = new RandomAccessInputStream(source, os);
	}

	@Override
	public int available() throws IOException {
		return this.ras.available();
	}

	@Override
	public void close() throws IOException {
		if (!closeCalled) {
			closeCalled = true;
			this.ras.close();
		}
	}

	public void enable(final boolean enable) {
		final OnOffStore store = (OnOffStore) this.ras.getStore();
		store.enable(enable);
	}

	public Store getStore() {
		return this.ras.getStore();
	}

	public boolean isCloseCalled() {
		return this.closeCalled;
	}

	@Override
	public void mark(final int readLimit) {
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
	public int read(final byte[] b) throws IOException {
		return this.ras.read(b);
	}

	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.ras.read(b, off, len);
	}

	@Override
	public void reset() throws IOException {
		this.ras.reset();
	}

	@Override
	public void resetToBeginning() throws IOException {
		this.ras.seek(0);
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.ras.skip(n);
	}
}
