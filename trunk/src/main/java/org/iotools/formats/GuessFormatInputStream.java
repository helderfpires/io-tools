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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public final class GuessFormatInputStream extends BufferedInputStream {
	private static final int READ_SIZE = 4096;

	// private static final Logger LOGGER = Logger
	// .getLogger(GuessFormatInputStream.class);

	private static final Map<FormatEnum, DetectorModule> DETECTORS = new HashMap<FormatEnum, DetectorModule>();

	static {
		DETECTORS.put(FormatEnum.PDF, new PdfDetectorModule());
		DETECTORS.put(FormatEnum.ZIP, new ZipDetectorModule());
		DETECTORS.put(FormatEnum.M7M, new M7MDetectorModule());
		DETECTORS.put(FormatEnum.PEM, new PEMDetectorModule());
		DETECTORS.put(FormatEnum.PKCS7, new PKCS7DetectorModule());
		// DETECTORS.put(FormatEnum.BASE64, new M7MDetectorModule());
	}

	public static void addDetector(final FormatEnum format,
			final DetectorModule detector) {
		DETECTORS.put(format, detector);
	}

	public static Map<FormatEnum, DetectorModule> getDetectorsMap() {
		return DETECTORS;
	}

	private static FormatEnum detectFormat(final byte[] bytes,
			final FormatEnum[] enabledFormats,
			final DetectorModule[] extraDetectors) {
		FormatEnum detected = FormatEnum.UNKNOWN;
		final Set<DetectorModule> detectors = getDetectorModules(
				enabledFormats, extraDetectors);

		for (final DetectorModule detectorModule : detectors) {
			final int bytesToCopy = Math.min(detectorModule.getDetectLenght(),
					bytes.length);
			final byte[] splittedBytes = Arrays.copyOf(bytes, bytesToCopy);
			if (detectorModule.detect(splittedBytes)) {
				detected = detectorModule.getDetectedFormat();
				break;
			}
		}
		return detected;
	}

	private static int getBufferSize(final FormatEnum[] enabledFormats,
			final DetectorModule[] extraDetectors) {
		int detectSize = 1;
		final Set<DetectorModule> detectors = getDetectorModules(
				enabledFormats, extraDetectors);
		for (final DetectorModule detectorModule : detectors) {
			detectSize = Math.max(detectSize, detectorModule.getDetectLenght());
		}
		return detectSize + 1;
	}

	private static Set<DetectorModule> getDetectorModules(
			final FormatEnum[] enabledFormats,
			final DetectorModule[] extraDetectors) {
		final Set<DetectorModule> modules = new HashSet<DetectorModule>();
		for (final FormatEnum formatEnum : enabledFormats) {
			final DetectorModule detectModule = DETECTORS.get(formatEnum);
			if (detectModule != null) {
				modules.add(detectModule);
			}
		}
		if (extraDetectors != null) {
			modules.addAll(Arrays.asList(extraDetectors));
		}
		return modules;
	}

	private static FormatEnum[] getEnabledFormats(
			final FormatEnum[] enabledFormats,
			final DetectorModule[] extraDetectors) {
		final Set<FormatEnum> enabledFormats1;
		if (enabledFormats != null) {
			enabledFormats1 = new HashSet<FormatEnum>(Arrays
					.asList(enabledFormats));
		} else {
			enabledFormats1 = new HashSet<FormatEnum>();
		}
		if (extraDetectors != null) {
			for (final DetectorModule detector : extraDetectors) {
				enabledFormats1.add(detector.getDetectedFormat());
			}
		}
		return enabledFormats1.toArray(new FormatEnum[enabledFormats1.size()]);
	}

	private static byte[] readBytesAndReset(final BufferedInputStream input,
			final int size) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		final byte[] buffer = new byte[READ_SIZE];
		long count = 0;
		int n = 0;
		while ((-1 != (n = input.read(buffer))) && (count < size)) {
			baos.write(buffer, 0, n);
			count += n;
		}
		input.reset();
		return baos.toByteArray();
	}

	private final FormatEnum[] enabledFormats;

	private final FormatEnum format;

	public GuessFormatInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats) throws IOException {
		this(istream, enabledFormats, null);
	}

	public GuessFormatInputStream(final InputStream istream,
			final FormatEnum[] enabledFormats,
			final DetectorModule[] extraDetectors) throws IOException {
		super(istream, getBufferSize(enabledFormats, extraDetectors));
		this.enabledFormats = getEnabledFormats(enabledFormats, extraDetectors);
		final byte[] bytes = readBytesAndReset(this, super.buf.length - 1);
		this.format = detectFormat(bytes, enabledFormats, extraDetectors);
	}

	public boolean canRecognize(final FormatEnum tenum) {
		return Arrays.asList(this.enabledFormats).contains(tenum);
	}

	public final FormatEnum getFormat() {
		return this.format;
	}
}
