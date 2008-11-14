package org.googlecode.iotools.fmt;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.googlecode.iotools.fmt.base.Decoder;
import org.googlecode.iotools.fmt.base.Detector;
import org.googlecode.iotools.fmt.base.FormatEnum;
import org.googlecode.iotools.fmt.decoders.Base64Decoder;
import org.googlecode.iotools.fmt.detectors.Base64Detector;
import org.googlecode.iotools.fmt.detectors.GifDetector;
import org.googlecode.iotools.fmt.detectors.M7MDetector;
import org.googlecode.iotools.fmt.detectors.PdfDetector;
import org.googlecode.iotools.fmt.detectors.PemDetector;
import org.googlecode.iotools.fmt.detectors.RTFDetectorModule;
import org.googlecode.iotools.fmt.detectors.XmlDetector;
import org.googlecode.iotools.fmt.detectors.ZipDetectorModule;
import org.googlecode.iotools.fmt.detectors.pksc7.PKCS7Detector;

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
	private static final Map DECODERS = Collections
			.synchronizedMap(new HashMap());

	// private static final Logger LOGGER = Logger
	// .getLogger(GuessFormatInputStream.class);

	private static final Map DETECTORS = Collections
			.synchronizedMap(new HashMap());
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
		for (int i = 0; i < decoders.length; i++) {
			final Decoder decoder = decoders[i];
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
		for (int i = 0; i < detectors.length; i++) {
			final Detector detector = detectors[i];
			if (detector != null) {
				GuessInputStream.DETECTORS.put(detector.getDetectedFormat(),
						detector);
			}
		}
	}

	public static Map getDetectorsMap() {
		return GuessInputStream.DETECTORS;
	}

	/**
	 * This method creates an instance of the GuessInputStream. It checks if the
	 * InputStream is already an instance of GuessInputStream and do
	 * optimizations if possible.
	 * 
	 * @param istream
	 * @return
	 */
	public static GuessInputStream getInstance(final InputStream istream,
			final FormatEnum[] enabledFormats) throws IOException {
		final GuessInputStream result = new GuessInputStream(istream,
				enabledFormats);
		return result;
	}

	private static FormatEnum detectFormat(final byte[] bytes,
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors) {
		FormatEnum detected = FormatEnum.UNKNOWN;
		final Set detectors = getDetectorModules(enabledFormats, extraDetectors);
		for (final Iterator iterator = detectors.iterator(); iterator.hasNext();) {
			final Detector detectorModule = (Detector) iterator.next();
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
		final Collection formats = new ArrayList();
		FormatEnum currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < GuessInputStream.MAX_LEVELS)
				&& ((currentFormat == null) || GuessInputStream.DECODERS
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, enabledFormats,
					extraDetectors);
			formats.add(currentFormat);
			if (GuessInputStream.DECODERS.containsKey(currentFormat)) {
				currentBytes = ((Decoder) GuessInputStream.DECODERS
						.get(currentFormat)).decode(currentBytes);
			}
		}
		return (FormatEnum[]) formats.toArray(new FormatEnum[formats.size()]);
	}

	private static int getBufferSize(final FormatEnum[] enabledFormats,
			final Detector[] extraDetectors) {
		int detectSize = 1;
		final Set detectors = getDetectorModules(enabledFormats, extraDetectors);
		for (final Iterator iterator = detectors.iterator(); iterator.hasNext();) {
			final Detector detectorModule = (Detector) iterator.next();
			detectSize = Math.max(detectSize, detectorModule.getDetectLenght());
		}

		final Set decoders = getDecoders(enabledFormats);
		int decodeOffset = 1;
		for (final Iterator iterator = decoders.iterator(); iterator.hasNext();) {
			final Decoder decoder = (Decoder) iterator.next();
			decodeOffset = Math.max(decodeOffset, decoder.getEncodingOffset());
		}

		float decodeRatio = 1;
		for (final Iterator iterator = decoders.iterator(); iterator.hasNext();) {
			final Decoder decoder = (Decoder) iterator.next();
			decodeRatio = Math.max(decodeRatio, decoder.getRatio());
		}

		return (int) (detectSize * decodeRatio) + decodeOffset + 1;
	}

	private static Set getDecoders(final FormatEnum[] enabledFormats) {
		final Set modules = new HashSet();
		for (int i = 0; i < enabledFormats.length; i++) {
			final FormatEnum formatEnum = enabledFormats[i];
			final Decoder decoder = (Decoder) GuessInputStream.DECODERS
					.get(formatEnum);
			if (decoder != null) {
				modules.add(decoder);
			}
		}
		return modules;
	}

	private static Set getDetectorModules(final FormatEnum[] enabledFormats,
			final Detector[] extraDetectors) {
		final Set modules = new HashSet();
		for (int i = 0; i < enabledFormats.length; i++) {
			final FormatEnum formatEnum = enabledFormats[i];
			final Detector detectModule = (Detector) GuessInputStream.DETECTORS
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
		final Set enabledFormats1;
		if (enabledFormats != null) {
			enabledFormats1 = new HashSet(Arrays.asList(enabledFormats));
		} else {
			enabledFormats1 = new HashSet();
		}
		if (extraDetectors != null) {
			for (int i = 0; i < extraDetectors.length; i++) {
				final Detector detector = extraDetectors[i];
				enabledFormats1.add(detector.getDetectedFormat());
			}
		}
		return (FormatEnum[]) enabledFormats1
				.toArray(new FormatEnum[enabledFormats1.size()]);
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
		this(istream, (FormatEnum[]) GuessInputStream.DETECTORS.keySet()
				.toArray(
						new FormatEnum[GuessInputStream.DETECTORS.keySet()
								.size()]));
	}

	public GuessInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats, final Detector[] extraDetectors)
			throws IOException {
		super(istream, getBufferSize(enabledFormats, extraDetectors));
		this.enabledFormats = getEnabledFormats(enabledFormats, extraDetectors);
		final byte[] bytes = readBytesAndReset(this, super.buf.length);
		this.format = detectFormats(bytes, enabledFormats, extraDetectors);
	}

	/*
	 * SEE getInstance()
	 */
	private GuessInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats) throws IOException {
		this(istream, enabledFormats, null);
	}

	public boolean canDetect(final FormatEnum tenum) {
		return Arrays.asList(this.enabledFormats).contains(tenum);
	}

	public boolean canDetectAll(final FormatEnum[] formatEnums) {
		if (formatEnums == null) {
			throw new IllegalArgumentException("Parameter formatEnum is null");
		}
		boolean result = true;
		final List enabledFormatList = Arrays.asList(this.enabledFormats);
		for (int i = 0; (i < formatEnums.length) && result; i++) {
			final FormatEnum formatEnum = formatEnums[i];
			result &= enabledFormatList.contains(formatEnum);
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
