package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.InputStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Decodes Base64 encoded streams.
 * 
 * @author dvd.smnt
 * @since 1.0.7
 */
public class Base64Decoder implements Decoder {
	/**
	 * {@inheritDoc}
	 */
	public InputStream decode(final InputStream istream) {
		InputStream decoded = new com.gc.iotools.stream.utils.Base64.InputStream(
				istream);
		return new DecoderHelperStream(istream, decoded, 4 / 3f, 4);
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

}
