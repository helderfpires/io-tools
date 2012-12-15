package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedDataParser;
import org.bouncycastle.cms.CMSTypedStream;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Extract original document from a PKCS#7 signed document.
 * 
 * @author dvd.smnt
 * @since 1.2.0
 * @see Decoder
 */
public class Pkcs7Decoder implements Decoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream decode(final InputStream istream) throws IOException {
		CMSSignedDataParser sdp;
		try {
			sdp = new CMSSignedDataParser(istream);
		} catch (final CMSException e) {
			final IOException e1 = new IOException(
					"Error parsing PKCS7 content");
			e1.initCause(e);
			throw e1;
		}
		final CMSTypedStream ts = sdp.getSignedContent();
		return ts.getContentStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FormatEnum getFormat() {
		return FormatEnum.PKCS7;
	}

}
