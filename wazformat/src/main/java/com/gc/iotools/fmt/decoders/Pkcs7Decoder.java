package com.gc.iotools.fmt.decoders;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
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
 * @since 1.2
 * @see Decoder
 */
public class Pkcs7Decoder implements Decoder {
	private static final int START_SIZE = 4096;

	/**
	 * {@inheritDoc}
	 */
	public InputStream decode(final InputStream istream) throws IOException {
		CMSSignedDataParser sdp;
		try {
			sdp = new CMSSignedDataParser(istream);
		} catch (CMSException e) {
			IOException e1 = new IOException("Error parsing PKCS7 content");
			e1.initCause(e);
			throw e1;
		}
		CMSTypedStream ts = sdp.getSignedContent();
		return new DecoderHelperStream(istream, ts.getContentStream(), 1,
				START_SIZE);
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum getFormat() {
		return FormatEnum.PKCS7;
	}

}
