package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;

import net.iharder.Base64;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Decodes Base64 encoded streams.
 * 
 * @author dvd.smnt
 * @since 1.0.7
 */
public class Base64Decoder implements Decoder {
	private final byte[] skipBuffer = new byte[8192];

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream decode(final InputStream istream) {
		// Issue:
		// https://sourceforge.net/tracker/?func=detail&aid=3239968&group_id=23200&atid=377736

		final InputStream decoded = new Base64.InputStream(istream) {

			@Override
			public long skip(final long n) throws IOException {
				long remaining = n;
				int skipped = 0;
				while (remaining > 0 && skipped >= 0) {
					skipped = read(Base64Decoder.this.skipBuffer, 0,
							(int) Math.min(
									Base64Decoder.this.skipBuffer.length,
									remaining));
					if (skipped > 0) {
						remaining -= skipped;
					}
				}
				return n - remaining;
			}

		};
		return decoded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

}
