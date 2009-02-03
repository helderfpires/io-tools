package com.gc.iotools.fmt.decoders;

import java.io.InputStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

public class Base64Decoder implements Decoder {

	public InputStream decode(final InputStream istream) {
		InputStream decoded = new com.gc.iotools.stream.utils.Base64.InputStream(
				istream);
		return new DecoderHelperStream(istream, decoded, 4 / 3f, 4);
	}

	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

}
