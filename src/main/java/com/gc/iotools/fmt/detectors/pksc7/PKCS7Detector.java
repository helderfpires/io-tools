package com.gc.iotools.fmt.detectors.pksc7;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

import com.gc.iotools.fmt.base.AbstractFormatDetector;
import com.gc.iotools.fmt.base.FormatEnum;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

/**
 * Class for detecting DER and BER encoded PKCS7 files.
 * 
 * @author dvd.smnt
 */
public class PKCS7Detector extends AbstractFormatDetector {
	private static final Log LOGGER = LogFactory.getLog(PKCS7Detector.class);

	public PKCS7Detector() {
		super(90, FormatEnum.PKCS7);
	}

	public boolean detect(final byte[] readedBytes) {
		final InputStream buffer = new ByteArrayInputStream(readedBytes);
		boolean result = false;
		try {
			final ASN1Reader pkcsHdrRead = new ASN1Reader(buffer);
			pkcsHdrRead.check(PKCSObjectIdentifiers.signedData);
			result = true;
		} catch (final FormatException e) {
			LOGGER.debug("PKCS7 not recognized" + "Exception (normal) ["
					+ e.getMessage() + "]");
		} catch (final IOException e) {
			LOGGER.warn("PKCS7 not recognized for an I/O exception", e);
		} catch (final Throwable e) {
			LOGGER.warn("PKCS7 not recognized for unexpected exception.", e);
		}
		return result;
	}
}
