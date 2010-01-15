package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.BufferedInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64InputStream;

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
		final InputStream decoded = new BufferedInputStream(
				new Base64InputStream(istream));
		return decoded;
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

}
