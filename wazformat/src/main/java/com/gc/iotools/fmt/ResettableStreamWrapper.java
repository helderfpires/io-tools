package com.gc.iotools.fmt;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.ResettableInputStream;

/**
 * Helps in mark and reset of decoded streams. Mark and reset are done on
 * baseStream. Reads are done on decodedStream.
 * 
 * @since 1.2.0
 * @author dvd.smnt
 * @see Decoder
 */

public class ResettableStreamWrapper extends ResettableInputStream {

	private final ResettableInputStream baseStream;

	private InputStream decodedStream;

	private final Decoder decoder;

	public ResettableStreamWrapper(
			final ResettableInputStream originalStream, final Decoder decoder) {
		this.baseStream = originalStream;
		// this.decodedStream = decodedStream;
		this.decoder = decoder;
	}

	private void checkInitialized() throws IOException {
		if (this.decodedStream == null) {
			this.baseStream.resetToBeginning();
			this.decodedStream = this.decoder.decode(this.baseStream);
		}
	}

	@Override
	public void close() throws IOException {
		// this.baseStream.close();
		this.decodedStream = null;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read() throws IOException {
		checkInitialized();
		return this.decodedStream.read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		checkInitialized();
		return this.decodedStream.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		checkInitialized();
		return this.decodedStream.read(b, off, len);
	}

	@Override
	public void resetToBeginning() throws IOException {
		this.decodedStream = null;
		this.baseStream.resetToBeginning();
	}

	@Override
	public long skip(final long size) throws IOException {
		checkInitialized();
		return this.decodedStream.skip(size);
	}

}
