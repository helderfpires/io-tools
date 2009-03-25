package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

/**
 * Helps in mark and reset of decoded streams.
 * 
 * @since 1.2.0
 * @author dvd.smnt
 * @see Decoder
 */
public class DecoderHelperStream extends InputStream {

	private final InputStream baseStream;

	private final InputStream decodedStream;

	private final float ratio;

	private final int offset;

	public DecoderHelperStream(final InputStream originalStream,
			final InputStream decodedStream, final float ratio,
			final int offset) {
		this.baseStream = originalStream;
		this.decodedStream = decodedStream;
		this.ratio = ratio;
		this.offset = offset;
	}

	@Override
	public void mark(final int readlimit) {
		final int markLimit = (int) (readlimit * this.ratio) + this.offset
				+ 1;
		this.baseStream.mark(markLimit);
	}

	@Override
	public boolean markSupported() {
		return this.baseStream.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.decodedStream.read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return this.decodedStream.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.decodedStream.read(b, off, len);
	}

	@Override
	public void reset() throws IOException {
		this.baseStream.reset();
	}

}
