package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

public class DecoderHelperStream extends InputStream {
	
	@Override
	public void mark(int readlimit) {
		final int markLimit = (int) (readlimit * ratio) + offset + 1;
		this.baseStream.mark(markLimit);
	}

	@Override
	public boolean markSupported() {
		return this.baseStream.markSupported();
	}

	@Override
	public void reset() throws IOException {
		this.baseStream.reset();
	}

	@Override
	public int read() throws IOException {
		return this.decodedStream.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.decodedStream.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.decodedStream.read(b);
	}

	private final InputStream baseStream;
	private final InputStream decodedStream;
	private final float ratio;
	private final int offset;

	public DecoderHelperStream(InputStream originalStream,
			InputStream decodedStream, float ratio, int offset) {
		this.baseStream = originalStream;
		this.decodedStream = decodedStream;
		this.ratio = ratio;
		this.offset = offset;
	}

}
