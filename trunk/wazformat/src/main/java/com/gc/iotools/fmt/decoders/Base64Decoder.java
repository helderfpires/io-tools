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
		final InputStream decoded = new com.gc.iotools.stream.utils.Base64.InputStream(
				istream);
		return decoded;
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

	public int getOffset() {
		return 4;
	}

	public float getRatio() {
		return 4 / 3f;
	}

}
