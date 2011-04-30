package com.gc.iotools.fmt.decoders;

import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * This class is for compose multiple decoders. It is intended for internal
 * use only, shouldn't be added directly.
 */
public class CompositeDecoder implements Decoder {

	private final Decoder[] decoders;

	public CompositeDecoder(final Decoder[] decoders) {
		this.decoders = decoders;
	}

	@Override
	public InputStream decode(final InputStream inStream) throws IOException {
		InputStream curStream = inStream;
		for (final Decoder decoder : this.decoders) {
			curStream = decoder.decode(curStream);
		}
		return curStream;
	}

	@Override
	public FormatEnum getFormat() {
		throw new UnsupportedOperationException("Shouldn't be called");
	}

}
