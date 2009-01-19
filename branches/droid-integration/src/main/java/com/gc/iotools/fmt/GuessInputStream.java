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
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.DefiniteLengthDetector;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.decoders.Base64Decoder;

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
 * TODO: read formats from a property file.
 * 
 */
public abstract class GuessInputStream extends InputStream {
	private final Map DECODERS = Collections.synchronizedMap(new HashMap());

	// private static final Logger LOGGER = Logger
	// .getLogger(GuessFormatInputStream.class);
	// Should become a collection to support multiple detectors per format
	private final Set<DefiniteLengthDetector> definiteLength = new HashSet<DefiniteLengthDetector>();

	{
		DECODERS.put(FormatEnum.BASE64, new Base64Decoder());
	}

	public void addDecoder(final Decoder decoder) {
		if (decoder == null) {
			throw new IllegalArgumentException("decoder is null");
		}
		DECODERS.put(decoder.getFormat(), decoder);
	}

	public void addDecoders(final Decoder[] decoders) {
		if (decoders == null) {
			throw new IllegalArgumentException("decoders array is null");
		}
		for (int i = 0; i < decoders.length; i++) {
			final Decoder decoder = decoders[i];
			if (decoder != null) {
				DECODERS.put(decoder.getFormat(), decoder);
			}
		}
	}

	// public static void addDetector(final Detector detector) {
	// if (detector == null) {
	// throw new IllegalArgumentException("detector is null");
	// }
	// GuessInputStream.DETECTORS.put(detector.getDetectedFormat(), detector);
	// }
	//
	// public static void addDetectors(final Detector[] detectors) {
	// if (detectors == null) {
	// throw new IllegalArgumentException("detectors are null");
	// }
	// for (int i = 0; i < detectors.length; i++) {
	// final Detector detector = detectors[i];
	// if (detector != null) {
	// GuessInputStream.DETECTORS.put(detector.getDetectedFormat(),
	// detector);
	// }
	// }
	// }
	//
	// public static Map getDetectorsMap() {
	// return GuessInputStream.DETECTORS;
	// }

	public static GuessInputStream getInstance(final InputStream istream)
			throws IOException {

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
		final GuessInputStream result;

		if (istream instanceof GuessInputStream) {
			final GuessInputStream gis = (GuessInputStream) istream;
			if (gis.canDetectAll(enabledFormats)) {
				// TODO: if formats are same return the same inputStream, don't
				// wrap.
				result = new GuessInputStreamWrapper(gis, enabledFormats);
			} else {
				result = new GuessInputStreamImpl(detectors, decoders, istream);
			}
		} else {
			result = new GuessInputStreamImpl(detectors, decoders, istream);
		}
		return result;
	}

	public static GuessInputStream getInstance(final InputStream istream,
			Class clazz, String droidSignatureFile,
			String definiteLenghtFile) throws IOException {

	}

	public static GuessInputStream getInstance(final InputStream istream,
			Class clazz, Decoder[] decoders,
			Detector[] detector) throws IOException {

	}

	private final Collection enabledFormats;

	protected GuessInputStream(final FormatEnum[] enabledFormats) {
		this.enabledFormats = Collections.unmodifiableCollection(Arrays
				.asList(enabledFormats));
	}

	public final boolean canDetect(final FormatEnum tenum) {

	}

	public final boolean canDetectAll(final FormatEnum[] formatEnums) {

	}

	public abstract FormatEnum getFormat();

	public abstract FormatEnum[] getFormats();
}
