package org.googlecode.iotools.fmt.base;

import org.apache.commons.lang.enums.ValuedEnum;

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
 * Enum of detected formats. Some format is "simple", some other is just a way
 * of encoding another kind of content.
 * 
 * If a user need to support a new format he must extend this class. It can't be
 * a Java 5 enum because (AFAIK) they can't be extended.
 * 
 * @since oct 24, 2008
 * @author dvd.smnt
 */
public class FormatEnum extends ValuedEnum {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5685026597452193393L;

	public static final int BASE64_INT = 0;
	public static final FormatEnum BASE64 = new FormatEnum("base64",
			BASE64_INT, "text");
	public static final int GIF_INT = 100;
	public static final FormatEnum GIF = new FormatEnum("gif", GIF_INT,
			"image/gif");
	public static final int JPEG_INT = 200;
	public static final FormatEnum JPEG = new FormatEnum("jpeg", JPEG_INT,
			"image/jpeg");
	public static final int M7M_INT = 300;
	public static final FormatEnum M7M = new FormatEnum("m7m", M7M_INT,
			"multipart/mixed");
	public static final int PDF_INT = 400;
	public static final FormatEnum PDF = new FormatEnum("pdf", PDF_INT,
			"application/pdf");
	public static final int RTF_INT = 500;
	public static final FormatEnum RTF = new FormatEnum("rtf", RTF_INT,
			"text/rtf");
	public static final int PEM_INT = 600;
	public static final FormatEnum PEM = new FormatEnum("pem", PEM_INT,
			"application/pkcs7-signature");
	public static final int PKCS7_INT = 700;
	public static final FormatEnum PKCS7 = new FormatEnum("pkcs7", PKCS7_INT,
			"application/pkcs7-signature");
	public static final int TIMESTAMP_INT = 800;
	public static final FormatEnum TIMESTAMP = new FormatEnum("timestamp",
			TIMESTAMP_INT, "application/pkcs7-signature");
	public static final int UNKNOWN_INT = 900;
	public static final FormatEnum UNKNOWN = new FormatEnum("unknown",
			UNKNOWN_INT, "application");
	public static final int XML_INT = 1000;
	public static final FormatEnum XML = new FormatEnum("xml", XML_INT,
			"text/xml");
	public static final int ZIP_INT = 1100;
	public static final FormatEnum ZIP = new FormatEnum("zip", ZIP_INT,
			"application/zip");

	private final String mimeType;

	/**
	 * constructor is protected to allow extensions.
	 * 
	 * @param enumName
	 * @param enumInt
	 */
	protected FormatEnum(final String enumName, final int enumInt,
			final String mime) {
		super(enumName, enumInt);
		this.mimeType = mime;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public String toString() {
		return "[" + this.getName() + "]";
	}

}
