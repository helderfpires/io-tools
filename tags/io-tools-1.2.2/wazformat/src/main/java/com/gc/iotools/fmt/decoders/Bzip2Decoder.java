package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.bzip2.CBZip2InputStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Extract data from bzip2 encoded stream.
 * 
 * @since 1.2.2
 * @author dvd.smnt
 * @see Decoder
 */
public class Bzip2Decoder implements Decoder {
	private static final int START_SIZE = 4096;

	/**
	 * {@inheritDoc}
	 */
	public InputStream decode(final InputStream istream) throws IOException {
		final InputStream decoded = new CBZip2InputStream(istream);
		return decoded;
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum getFormat() {
		return FormatEnum.GZ;
	}

}
