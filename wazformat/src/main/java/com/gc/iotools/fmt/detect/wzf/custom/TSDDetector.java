package com.gc.iotools.fmt.detect.wzf.custom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.detect.wzf.DefiniteLengthModule;

/**
 * Class for detecting TimeStampedData files (RFC 5544).
 * 
 * @author gboccardo
 */
public class TSDDetector implements DefiniteLengthModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TSDDetector.class);


	@Override
	public boolean detect(final byte[] readBytes) {
		final InputStream buffer = new ByteArrayInputStream(readBytes);
		boolean result = false;
		try {
			final ASN1Reader pkcsHdrRead = new ASN1Reader(buffer);
			pkcsHdrRead.check(PKCSObjectIdentifiers.id_ct_timestampedData);
			result = true;
		} catch (final Exception e) {
			LOGGER.debug("TSD not recognized. Exception: [" + e.getMessage()
					+ "]");
		}
		return result;
	}

	@Override
	public FormatId getDetectedFormat() {
		return new FormatId(FormatEnum.TSD, null);
	}

	@Override
	public int getDetectLength() {
		return 4096;
	}

	@Override
	public void init(final FormatId fenum, final String param) {
		//empty block. Do nothing here
	}
}
