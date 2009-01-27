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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.DefiniteLengthDetector;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.IndefiniteLengthDetector;

final class GuessInputStreamImpl extends GuessInputStream {
	@Override
	protected void finalize() throws Throwable {
		cleanup();
		super.finalize();
	}

	private static final int MAX_LEVELS = 2;



	private static FormatEnum detectFormat(final byte[] bytes,
			final DefiniteLengthDetector[] detectors,
			FormatEnum[] enabledFormats) {
		FormatEnum detected = FormatEnum.UNKNOWN;
		for (int i = 0; (i < detectors.length)
				&& FormatEnum.UNKNOWN.equals(detected); i++) {
			final DefiniteLengthDetector detector = detectors[i];
			detected = detector.detect(enabledFormats, bytes);
		}
		return detected;
	}

	private static FormatEnum[] detectFormats(final byte[] bytes,
			final DefiniteLengthDetector[] detectors, final Decoder[] decoders,
			FormatEnum[] enformats) {
		final Map decodersMap = getDecodersMap(decoders);
		final List formats = new ArrayList();
		FormatEnum currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < MAX_LEVELS)
				&& ((currentFormat == null) || decodersMap
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, detectors, enformats);
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
		for (final Decoder decoder : decoders) {
			decodeOffset = Math.max(decodeOffset, decoder.getEncodingOffset());
		}

		float decodeRatio = 1;
		for (final Decoder decoder : decoders) {
			decodeRatio = Math.max(decodeRatio, decoder.getRatio());
		}

		return (int) (detectSize * decodeRatio) + decodeOffset + 1;
	}

	private static Map getDecodersMap(final Decoder[] decoders) {
		final Map formatsMap = new HashMap();
		for (final final Decoder decoder : decoders) {
			formatsMap.put(decoder.getFormat(), decoder);
		}
		return formatsMap;
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

	private final InputStream bis;

	private final DefiniteLengthDetector[] defLen;

	private final FormatEnum formats[];

	private final IndefiniteLengthDetector[] inDefLen;

	public GuessInputStreamImpl(final Detector[] detectors,
			final Decoder[] decoders, final InputStream istream)
			throws IOException {
		super(getEnabledFormats(detectors));
		defLen = getDefiniteLenght(detectors);
		inDefLen = getInDefiniteLenght(detectors);
		FormatEnum[] enabledFormats;
		// definite lenght dection
		final int bufferSize = getBufferSize(defLen, decoders);
		BufferedInputStream tempStream = new BufferedInputStream(istream,
				bufferSize);
		tempStream.mark(bufferSize);
		byte[] bytes = readBytesAndReset(tempStream, bufferSize);
		FormatEnum[] formats = detectFormats(bytes, defLen, decoders,
				enabledFormats);
		if (formats.length > 0) {
			this.formats = formats;
			this.bis = tempStream;
		} else {
			
			// if no result copy to a file
			// indefinite lenght detection

			// internal stream is
		}
	}

	@Override
	public int available() throws IOException {
		return this.bis.available();
	}

	@Override
	public void close() throws IOException {
		cleanup();
		this.bis.close();
		
	}

	@Override
	public final FormatEnum getFormat() {
		return this.formats[0];
	}

	@Override
	public final FormatEnum[] getFormats() {
		return this.formats;
	}

	@Override
	public void mark(final int readlimit) {
		this.bis.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return this.bis.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.bis.read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return this.bis.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.bis.read(b, off, len);
	}

	@Override
	public void reset() throws IOException {
		this.bis.reset();
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.bis.skip(n);
	}
}
