package com.gc.iotools.fmt.decoders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.cms.TimeStampedData;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Decoder for TSD (TimeStampedData) documents.
 * 
 * @author gboccardo Jan 4, 2011
 * @see Decoder
 */
public class TSDDecoder implements Decoder {

	@Override
	public InputStream decode(final InputStream tsdIS) throws IOException {
		TimeStampedData tsdParser;
		try {
			// TODO: the document is read completely in memory. Make a stream
			// detection instead
			final byte[] byteArray = IOUtils.toByteArray(tsdIS);
			tsdParser = TimeStampedData.getInstance(byteArray);
		} catch (final IOException e) {
			final IOException e1 = new IOException(
					"Error parsing TSD content");
			e1.initCause(e);
			throw e1;
		}

		return new BufferedInputStream(tsdParser.getContent()
				.getOctetStream());
	}

	@Override
	public FormatEnum getFormat() {
		return FormatEnum.TSD;
	}

}
