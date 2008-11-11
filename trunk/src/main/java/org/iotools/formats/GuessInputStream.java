package org.iotools.formats;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iotools.formats.base.Decoder;
import org.iotools.formats.base.Detector;
import org.iotools.formats.base.FormatEnum;
import org.iotools.formats.decoders.Base64Decoder;
import org.iotools.formats.detectors.Base64Detector;
import org.iotools.formats.detectors.GifDetector;
import org.iotools.formats.detectors.M7MDetector;
import org.iotools.formats.detectors.PdfDetector;
import org.iotools.formats.detectors.PemDetector;
import org.iotools.formats.detectors.RTFDetectorModule;
import org.iotools.formats.detectors.XmlDetector;
import org.iotools.formats.detectors.ZipDetectorModule;
import org.iotools.formats.detectors.pksc7.PKCS7Detector;

/**
 * InputStream that wraps the original InputStream and guess the format.
 * 
 * To support a new format:
 * <ul>
 * <li>implement a new DetectorModule. The metod parse(bytes[]) should return
 * true when the format is recognized</li>
 * <li>Extend the enum FormatEnum to provide the new name for the format.</li>
 * <li>Either register it statically in GuessFormatInputStream with the method
 * addDetector or pass an instance in the constructor.</li>
 * </ul>
 * 
 */
public final class GuessInputStream extends BufferedInputStream {
	private static final Map<FormatEnum, Decoder> DECODERS = Collections
			.synchronizedMap(new HashMap<FormatEnum, Decoder>());

	// private static final Logger LOGGER = Logger
	// .getLogger(GuessFormatInputStream.class);

	private static final Map<FormatEnum, Detector> DETECTORS = Collections
			.synchronizedMap(new HashMap<FormatEnum, Detector>());
	private static final int MAX_LEVELS = 2;

	static {
		GuessInputStream.DETECTORS.put(FormatEnum.BASE64, new Base64Detector());
		GuessInputStream.DETECTORS.put(FormatEnum.GIF, new GifDetector());
		GuessInputStream.DETECTORS.put(FormatEnum.M7M, new M7MDetector());
		GuessInputStream.DETECTORS.put(FormatEnum.PDF, new PdfDetector());
		GuessInputStream.DETECTORS.put(FormatEnum.PEM, new PemDetector());
		GuessInputStream.DETECTORS.put(FormatEnum.PKCS7, new PKCS7Detector());
		GuessInputStream.DETECTORS.put(FormatEnum.RTF, new RTFDetectorModule());
		GuessInputStream.DETECTORS.put(FormatEnum.XML, new XmlDetector());
		GuessInputStream.DETECTORS.put(FormatEnum.ZIP, new ZipDetectorModule());

		GuessInputStream.DECODERS.put(FormatEnum.BASE64, new Base64Decoder());
	}

	public static void addDecoder(final Decoder decoder) {
		if (decoder == null) {
			throw new IllegalArgumentException("decoder is null");
		}
		GuessInputStream.DECODERS.put(decoder.getFormat(), decoder);
	}

	public static void addDecoders(final Decoder[] decoders) {
		if (decoders == null) {
			throw new IllegalArgumentException("decoders array is null");
		}
		for (final Decoder decoder : decoders) {
			if (decoder != null) {
				GuessInputStream.DECODERS.put(decoder.getFormat(), decoder);
			}
		}
	}

	public static void addDetector(final Detector detector) {
		if (detector == null) {
			throw new IllegalArgumentException("detector is null");
		}
		GuessInputStream.DETECTORS.put(detector.getDetectedFormat(), detector);
	}

	public static void addDetectors(final Detector[] detectors) {
		if (detectors == null) {
			throw new IllegalArgumentException("detectors are null");
		}
		for (final Detector detector : detectors) {
			if (detector != null) {
				GuessInputStream.DETECTORS.put(detector.getDetectedFormat(),
						detector);
			}
		}
	}

	public static Map<FormatEnum, Detector> getDetectorsMap() {
		return GuessInputStream.DETECTORS;
	}

	private static FormatEnum detectFormat(final byte[] bytes,
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors) {
		FormatEnum detected = FormatEnum.UNKNOWN;
		final Set<Detector> detectors = getDetectorModules(enabledFormats,
				extraDetectors);

		for (final Detector detectorModule : detectors) {
			final int bytesToCopy = Math.min(detectorModule.getDetectLenght(),
					bytes.length);
			final byte[] splittedBytes = new byte[bytesToCopy];
			System.arraycopy(bytes, 0, splittedBytes, 0, bytesToCopy);
			if (detectorModule.detect(splittedBytes)) {
				detected = detectorModule.getDetectedFormat();
				break;
			}
		}
		return detected;
	}

	private static FormatEnum[] detectFormats(final byte[] bytes,
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors) {
		final Collection<FormatEnum> formats = new ArrayList<FormatEnum>();
		FormatEnum currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < GuessInputStream.MAX_LEVELS)
				&& ((currentFormat == null) || GuessInputStream.DECODERS
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, enabledFormats,
					extraDetectors);
			formats.add(currentFormat);
			if (GuessInputStream.DECODERS.containsKey(currentFormat)) {
				currentBytes = GuessInputStream.DECODERS.get(currentFormat)
						.decode(currentBytes);
			}
		}
		return formats.toArray(new FormatEnum[formats.size()]);
	}

	private static int getBufferSize(final FormatEnum[] enabledFormats,
			final Detector[] extraDetectors) {
		int detectSize = 1;
		final Set<Detector> detectors = getDetectorModules(enabledFormats,
				extraDetectors);
		for (final Detector detectorModule : detectors) {
			detectSize = Math.max(detectSize, detectorModule.getDetectLenght());
		}

		final Set<Decoder> decoders = getDecoders(enabledFormats);
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

	private static Set<Decoder> getDecoders(final FormatEnum[] enabledFormats) {
		final Set<Decoder> modules = new HashSet<Decoder>();
		for (final FormatEnum formatEnum : enabledFormats) {
			final Decoder decoder = GuessInputStream.DECODERS.get(formatEnum);
			if (decoder != null) {
				modules.add(decoder);
			}
		}
		return modules;
	}

	private static Set<Detector> getDetectorModules(
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors) {
		final Set<Detector> modules = new HashSet<Detector>();
		for (final FormatEnum formatEnum : enabledFormats) {
			final Detector detectModule = GuessInputStream.DETECTORS
					.get(formatEnum);
			if (detectModule != null) {
				modules.add(detectModule);
			} else {
				throw new IllegalArgumentException("Detector for ["
						+ formatEnum + "] not registred");
			}
		}
		if (extraDetectors != null) {
			modules.addAll(Arrays.asList(extraDetectors));
		}
		return modules;
	}

	private static FormatEnum[] getEnabledFormats(
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors) {
		final Set<FormatEnum> enabledFormats1;
		if (enabledFormats != null) {
			enabledFormats1 = new HashSet<FormatEnum>(Arrays
					.asList(enabledFormats));
		} else {
			enabledFormats1 = new HashSet<FormatEnum>();
		}
		if (extraDetectors != null) {
			for (final Detector detector : extraDetectors) {
				enabledFormats1.add(detector.getDetectedFormat());
			}
		}
		return enabledFormats1.toArray(new FormatEnum[enabledFormats1.size()]);
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

	private final FormatEnum[] enabledFormats;

	private final FormatEnum[] format;

	public GuessInputStream(final InputStream istream) throws IOException {
		this(istream, GuessInputStream.DETECTORS.keySet().toArray(
				new FormatEnum[GuessInputStream.DETECTORS.keySet().size()]));
	}

	public GuessInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats) throws IOException {
		this(istream, enabledFormats, null);
	}

	public GuessInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors)
			throws IOException {
		super(istream, getBufferSize(enabledFormats, extraDetectors));
		this.enabledFormats = getEnabledFormats(enabledFormats, extraDetectors);
		final byte[] bytes = readBytesAndReset(this, super.buf.length);
		this.format = detectFormats(bytes, enabledFormats, extraDetectors);
	}

	public boolean canDetect(final FormatEnum tenum) {
		return Arrays.asList(this.enabledFormats).contains(tenum);
	}

	public boolean canDetectAll(final FormatEnum[] formatEnum) {
		if (formatEnum == null) {
			throw new IllegalArgumentException("Parameter formatEnum is null");
		}
		boolean result = true;
		final List<FormatEnum> enabledFormatList = Arrays
				.asList(this.enabledFormats);
		for (final FormatEnum formatEnum2 : formatEnum) {
			result &= enabledFormatList.contains(formatEnum2);
		}
		return result;
	}

	public final FormatEnum getFormat() {
		return this.format[0];
	}

	public final FormatEnum[] getFormats() {
		return this.format;
	}
}
