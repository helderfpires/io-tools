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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.decoders.Base64Decoder;
import com.gc.iotools.fmt.decoders.GzipDecoder;
import com.gc.iotools.fmt.decoders.Pkcs7Decoder;
import com.gc.iotools.fmt.detect.droid.DroidDetectorImpl;
import com.gc.iotools.fmt.detect.wzf.StreamDetectorImpl;

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
public abstract class GuessInputStream extends InputStream {
	public static final Collection<Decoder> DEFAULT_DECODERS = new HashSet();

	public static void addDefaultDecoder(final Decoder decoder) {
		if (decoder == null) {
			throw new IllegalArgumentException("decoder is null");
		}
		DEFAULT_DECODERS.add(decoder);
	}

	public static GuessInputStream getInstance(final InputStream istream)
			throws IOException {
		return getInstance(istream, FormatEnum.values());
	}

	public static GuessInputStream getInstance(final InputStream istream,
			final Class clazz, final String droidSignatureFile,
			String streamConfigFile) throws IOException {
		if (droidSignatureFile == null && streamConfigFile == null) {
			throw new IllegalArgumentException(
					"both configuration files are null.");
		}
		Collection<Detector> detectors = new HashSet<Detector>();
		if (streamConfigFile != null) {
			Detector stream = new StreamDetectorImpl(streamConfigFile, clazz);
			detectors.add(stream);
		}
		if (droidSignatureFile != null) {
			Detector stream = new DroidDetectorImpl();
			detectors.add(stream);
		}
		return getInstance(istream, null, detectors.toArray(new Detector[0]),
				DEFAULT_DECODERS.toArray(new Decoder[0]));
	}

	/**
	 * This method creates an instance of the GuessInputStream. It checks if the
	 * InputStream is already an instance of GuessInputStream and do
	 * optimizations if possible.
	 * 
	 * @param source
	 *            Source stream to be wrapped.
	 * @return Instance of the newly created GuessInputStream
	 */
	public static GuessInputStream getInstance(final InputStream source,
			final FormatEnum[] enabledFormats) throws IOException {

		Collection<Detector> detectors = new HashSet<Detector>();
		Detector stream = new StreamDetectorImpl();
		detectors.add(stream);
		Detector stream1 = new DroidDetectorImpl();
		detectors.add(stream1);
		return getInstance(source, enabledFormats, detectors
				.toArray(new Detector[0]), DEFAULT_DECODERS
				.toArray(new Decoder[0]));
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

	public static GuessInputStream getInstance(final InputStream stream,
			final FormatEnum[] enabledFormats, final Detector[] detectors,
			final Decoder[] decoders) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("Parameter stream==null");
		}
		GuessInputStream result;
		if (stream instanceof GuessInputStream) {
			final GuessInputStream gis = (GuessInputStream) stream;
			if (gis.canDetectAll(enabledFormats)) {
				// TODO: if formats are same return the same inputStream, don't
				// wrap.
				result = new GuessInputStreamWrapper(gis, enabledFormats);
			} else {
				result = new GuessInputStreamImpl(detectors, decoders,
						enabledFormats, stream, false);
			}
		} else {
			result = new GuessInputStreamImpl(detectors, decoders,
					enabledFormats, stream, false);
		}
		return result;
	}

	// private static final Loggerger LOGGER = Loggerger
	// .getLoggerger(GuessFormatInputStream.class);
	// Should become a collection to support multiple detectors per format
	// private final Set<StreamDetector> definiteLength = new
	// HashSet<StreamDetector>();

	private final Collection<FormatEnum> enabledFormats;

	static {
		DEFAULT_DECODERS.add(new Base64Decoder());
		DEFAULT_DECODERS.add(new GzipDecoder());
		DEFAULT_DECODERS.add(new Pkcs7Decoder());
	}

	protected GuessInputStream(final FormatEnum[] enabledFormats) {
		this.enabledFormats = Collections.unmodifiableCollection(Arrays
				.asList(enabledFormats));
	}

	public static void addDefaultDecoders(final Decoder[] decoders) {
		if (decoders == null) {
			throw new IllegalArgumentException("decoders array is null");
		}
		DEFAULT_DECODERS.addAll(Arrays.asList(decoders));
	}

	public final boolean canDetect(final FormatEnum formatEnum) {
		if (formatEnum == null) {
			throw new IllegalArgumentException("Parameter formatEnum is null");
		}
		return this.enabledFormats.contains(formatEnum);
	}

	public final boolean canDetectAll(final FormatEnum[] formatEnums) {
		if (formatEnums == null) {
			throw new IllegalArgumentException(
					"Parameter formatEnums is null");
		}
		boolean result = true;
		for (int i = 0; i < formatEnums.length && result; i++) {
			FormatEnum formatEnum = formatEnums[i];
			result &= this.enabledFormats.contains(formatEnum);
		}
		return result;
	}

	public final FormatEnum getFormat() {
		return getFormatId().format;
	}

	public final FormatId getFormatId() {
		return identify()[0];
	}

	public final FormatEnum[] getFormats() {
		FormatId[] formats = identify();
		Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (FormatId formatId : formats) {
			result.add(formatId.format);
		}
		return result.toArray(new FormatEnum[0]);
	}

	public abstract FormatId[] identify();
}
