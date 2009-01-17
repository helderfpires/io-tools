package com.gc.iotools.fmt;

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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;

final class GuessInputStreamImpl extends GuessInputStream {
	private static final int MAX_LEVELS = 2;

	private static FormatEnum detectFormat(final byte[] bytes,
			final Detector[] detectors) {
		FormatEnum detected = FormatEnum.UNKNOWN;
		for (int i = 0; i < detectors.length; i++) {
			final Detector detector = detectors[i];
			final int bytesToCopy = Math.min(detector.getDetectLenght(),
					bytes.length);
			final byte[] splittedBytes = new byte[bytesToCopy];
			System.arraycopy(bytes, 0, splittedBytes, 0, bytesToCopy);
			if (detector.detect(splittedBytes)) {
				detected = detector.getDetectedFormat();
				break;
			}
		}
		return detected;
	}

	private static FormatEnum[] detectFormats(final byte[] bytes,
			final Detector[] detectors, final Decoder[] decoders) {
		final Map decodersMap = getDecodersMap(decoders);
		final List formats = new ArrayList();
		FormatEnum currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < MAX_LEVELS)
				&& ((currentFormat == null) || decodersMap
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, detectors);
			formats.add(currentFormat);
			if (decodersMap.containsKey(currentFormat)) {
				currentBytes = ((Decoder) decodersMap.get(currentFormat))
						.decode(currentBytes);
			}
		}
		return (FormatEnum[]) formats.toArray(new FormatEnum[formats.size()]);
	}

	private static int getBufferSize(final Detector[] detectors,
			final Decoder[] decoders) {
		int detectSize = 1;
		for (int i = 0; i < detectors.length; i++) {
			final Detector detector = detectors[i];
			detectSize = Math.max(detectSize, detector.getDetectLenght());
		}

		int decodeOffset = 1;
		for (int i = 0; i < decoders.length; i++) {
			final Decoder decoder = decoders[i];
			decodeOffset = Math.max(decodeOffset, decoder.getEncodingOffset());
		}

		float decodeRatio = 1;
		for (int i = 0; i < decoders.length; i++) {
			final Decoder decoder = decoders[i];
			decodeRatio = Math.max(decodeRatio, decoder.getRatio());
		}

		return (int) (detectSize * decodeRatio) + decodeOffset + 1;
	}

	private static Map getDecodersMap(final Decoder[] decoders) {
		final Map formatsMap = new HashMap();
		for (int i = 0; i < decoders.length; i++) {
			final Decoder decoder = decoders[i];
			formatsMap.put(decoder.getFormat(), decoder);
		}
		return formatsMap;
	}

	private static FormatEnum[] getEnabledFormats(final Detector[] detectors) {
		final Collection formatsColl = new ArrayList();
		for (int i = 0; i < detectors.length; i++) {
			final Detector detector = detectors[i];
			formatsColl.add(detector.getDetectedFormat());
		}
		return (FormatEnum[]) formatsColl.toArray(new FormatEnum[formatsColl
				.size()]);
	}

	private static byte[] readBytesAndReset(final BufferedInputStream input,
			final int size) throws IOException {
		final int size1 = size - 1;
		final byte[] buffer = new byte[size1];
		input.mark(size);
		int pos = 0;
		int n = 0;
		while ((pos < (size1))
				&& (-1 != (n = input.read(buffer, pos, (size1 - pos))))) {
			pos += n;
		}
		input.reset();
		byte[] result;
		if (pos == size1) {
			result = buffer;
		} else {
			result = new byte[pos];
			System.arraycopy(buffer, 0, result, 0, pos);
		}
		return result;
	}

	private final FormatEnum formats[];

	private final BufferedInputStream bis;

	public GuessInputStreamImpl(final Detector[] detectors,
			final Decoder[] decoders, final InputStream istream)
			throws IOException {
		super(getEnabledFormats(detectors));
		final int bufferSize = getBufferSize(detectors, decoders);
		this.bis = new BufferedInputStream(istream, bufferSize);
		final byte[] bytes = readBytesAndReset(this.bis, bufferSize);
		this.formats = detectFormats(bytes, detectors, decoders);
	}

	public int available() throws IOException {
		return this.bis.available();
	}

	public void close() throws IOException {
		this.bis.close();
	}

	public final FormatEnum getFormat() {
		return this.formats[0];
	}

	public final FormatEnum[] getFormats() {
		return this.formats;
	}

	public void mark(final int readlimit) {
		this.bis.mark(readlimit);
	}

	public boolean markSupported() {
		return this.bis.markSupported();
	}

	public int read() throws IOException {
		return this.bis.read();
	}

	public int read(final byte[] b) throws IOException {
		return this.bis.read(b);
	}

	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.bis.read(b, off, len);
	}

	public void reset() throws IOException {
		this.bis.reset();
	}

	public long skip(final long n) throws IOException {
		return this.bis.skip(n);
	}
}
