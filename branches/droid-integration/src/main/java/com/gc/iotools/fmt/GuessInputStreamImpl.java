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
import com.gc.iotools.fmt.base.DefiniteLengthDetector;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.IndefiniteLengthDetector;

final class GuessInputStreamImpl extends GuessInputStream {
	private static final int MAX_LEVELS = 2;

	private static FormatId detectFormat(final byte[] bytes,
			final DefiniteLengthDetector[] detectors,
			final FormatEnum[] enabledFormats) {
		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		for (int i = 0; (i < detectors.length)
				&& FormatEnum.UNKNOWN.equals(detected.format); i++) {
			final DefiniteLengthDetector detector = detectors[i];
			detected = detector.detect(enabledFormats, bytes);
		}
		return detected;
	}

	private static FormatId[] detectFormats(final byte[] bytes,
			final DefiniteLengthDetector[] detectors, final Decoder[] decoders,
			final FormatEnum[] enformats) {
		final Map<FormatEnum, Decoder> decodersMap = getDecodersMap(decoders);
		final List<FormatId> formats = new ArrayList<FormatId>();
		FormatId currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < MAX_LEVELS)
				&& ((currentFormat == null) || decodersMap
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, detectors, enformats);
			formats.add(currentFormat);
			if (decodersMap.containsKey(currentFormat.format)) {
				currentBytes = (decodersMap.get(currentFormat))
						.decode(currentBytes);
			}
		}
		return formats.toArray(new FormatId[0]);
	}

	private static int getBufferSize(final DefiniteLengthDetector[] detectors,
			final Decoder[] decoders, final FormatEnum[] enabledFormats) {
		int detectSize = 1;
		for (final DefiniteLengthDetector detector : detectors) {
			detectSize = Math.max(detectSize, detector
					.getDetectLength(enabledFormats));
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
		for (final Decoder decoder : decoders) {
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

	private final FormatId formats[];

	private final IndefiniteLengthDetector[] inDefLen;

	public GuessInputStreamImpl(final Detector[] detectors,
			final Decoder[] decoders, final FormatEnum[] enabledFormats,
			final InputStream istream) throws IOException {
		super(enabledFormats);
		this.defLen = getDefiniteLenght(detectors);
		this.inDefLen = getInDefiniteLenght(detectors);

		// definite lenght dection
		final int bufferSize = getBufferSize(this.defLen, decoders,
				enabledFormats);
		final BufferedInputStream tempStream = new BufferedInputStream(istream);
		tempStream.mark(bufferSize);
		final byte[] bytes = readBytesAndReset(tempStream, bufferSize);
		final FormatId[] formats = detectFormats(bytes, this.defLen, decoders,
				enabledFormats);
		if (formats.length > 0) {
			this.formats = formats;
			this.bis = tempStream;
		} else {

			// if no result copy to a file
			// indefinite lenght detection

			// internal stream is
			this.formats = null;
			this.bis = null;
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
	public final FormatId getFormat() {
		return this.formats[0];
	}

	@Override
	public final FormatId[] getFormats() {
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

	private void cleanup() {

	}

	private DefiniteLengthDetector[] getDefiniteLenght(
			final Detector[] detectors) {
		Collection<DefiniteLengthDetector> coll = new ArrayList<DefiniteLengthDetector>();
		for (Detector detector : detectors) {
			if (detector instanceof DefiniteLengthDetector) {
				coll.add((DefiniteLengthDetector) detector);
			}
		}
		
		return (coll.size() == 0 ? null : coll
				.toArray(new DefiniteLengthDetector[0]));
	}

	private IndefiniteLengthDetector[] getInDefiniteLenght(
			final Detector[] detectors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
		super.finalize();
	}
}
