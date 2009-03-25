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
import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;
import com.gc.iotools.fmt.decoders.Base64Decoder;
import com.gc.iotools.fmt.decoders.GzipDecoder;
import com.gc.iotools.fmt.decoders.Pkcs7Decoder;
import com.gc.iotools.fmt.detect.droid.DroidDetectorImpl;
import com.gc.iotools.fmt.detect.wzf.StreamDetectorImpl;
import com.gc.iotools.stream.is.RandomAccessInputStream;

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
public class GuessInputStream extends InputStream {
	public static final Collection<Decoder> DEFAULT_DECODERS = new HashSet<Decoder>();

	static {
		DEFAULT_DECODERS.add(new Base64Decoder());
		DEFAULT_DECODERS.add(new GzipDecoder());
		DEFAULT_DECODERS.add(new Pkcs7Decoder());
	}

	public static void addDefaultDecoder(final Decoder decoder) {
		if (decoder == null) {
			throw new IllegalArgumentException("decoder is null");
		}
		DEFAULT_DECODERS.add(decoder);
	}

	public static void addDefaultDecoders(final Decoder[] decoders) {
		if (decoders == null) {
			throw new IllegalArgumentException("decoders array is null");
		}
		DEFAULT_DECODERS.addAll(Arrays.asList(decoders));
	}

	public static GuessInputStream getInstance(final InputStream istream)
			throws IOException {
		return getInstance(istream, FormatEnum.values(), 0);
	}

	public static GuessInputStream getInstance(final InputStream istream,
			final Class clazz, final String droidSignatureFile,
			String streamConfigFile) throws IOException {
		if (droidSignatureFile == null && streamConfigFile == null) {
			throw new IllegalArgumentException(
					"both configuration files are null.");
		}
		Collection<DetectionLibrary> detectionLibraries = new HashSet<DetectionLibrary>();
		if (streamConfigFile != null) {
			DetectionLibrary stream = new StreamDetectorImpl(streamConfigFile, clazz);
			detectionLibraries.add(stream);
		}
		if (droidSignatureFile != null) {
			DetectionLibrary stream = new DroidDetectorImpl();
			detectionLibraries.add(stream);
		}
		return getInstance(istream, null, detectionLibraries.toArray(new DetectionLibrary[0]),
				DEFAULT_DECODERS.toArray(new Decoder[0]), 0);
	}

	// private static final Loggerger LOGGER = Loggerger
	// .getLoggerger(GuessFormatInputStream.class);
	// Should become a collection to support multiple detectors per format
	// private final Set<StreamDetector> definiteLength = new
	// HashSet<StreamDetector>();

	public static GuessInputStream getInstance(final InputStream source,
			final FormatEnum[] enabledFormats) throws IOException {
		return getInstance(source, enabledFormats, 0);
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
			final FormatEnum[] enabledFormats, int recursionLevel)
			throws IOException {

		Collection<DetectionLibrary> detectionLibraries = new ArrayList<DetectionLibrary>();
		detectionLibraries.add(new StreamDetectorImpl());
		detectionLibraries.add(new DroidDetectorImpl());
		return getInstance(source, enabledFormats, detectionLibraries
				.toArray(new DetectionLibrary[0]), DEFAULT_DECODERS
				.toArray(new Decoder[0]), recursionLevel);
	}

	public static GuessInputStream getInstance(final InputStream stream,
			final FormatEnum[] enabledFormats, final DetectionLibrary[] detectors,
			final Decoder[] decoders, int recursionLevel) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("Parameter stream==null");
		}
		GuessInputStream result;
		ResettableStreamRASAdapter ris;
		if (stream instanceof GuessInputStream) {
			final GuessInputStream gis = (GuessInputStream) stream;
			ris = gis.baseStream;
		} else {
			ris = new ResettableStreamRASAdapter(new RandomAccessInputStream(
					stream));
		}
		ris.enable(true);
		DetectionStrategy ds = new DetectionStrategy(detectors, decoders,
				enabledFormats, ris, recursionLevel);
		result = new GuessInputStream(enabledFormats, ds.getFormats(), ris,
				ds.getStream());
		ris.enable(false);
		return result;
	}

	private final ResettableStreamRASAdapter baseStream;

	private final ResettableInputStream decodedStream;

	private final FormatId[] detectedFormats;

	private final Collection<FormatEnum> enabledFormats;

	private InputStreamStatusEnum status = InputStreamStatusEnum.NOT_INITIALIZED;

	private boolean decode = false;

	protected GuessInputStream(final FormatEnum[] enabledFormats,
			FormatId[] detected, ResettableStreamRASAdapter baseStream,
			ResettableInputStream decodedStream) {
		this.enabledFormats = Collections.unmodifiableCollection(Arrays
				.asList(enabledFormats));
		this.baseStream = baseStream;
		this.decodedStream = decodedStream;
		this.detectedFormats = detected;
	}

	public void setDecode(boolean decode) {
		if (status.equals(InputStreamStatusEnum.READING_DATA)
				&& (decode != this.decode)) {
			throw new IllegalStateException("Some byte has been "
					+ " read already from the underlying stream. "
					+ "It is not possible "
					+ "to change the decoding behaviour now. "
					+ "Decoding behaviour set [" + this.decode
					+ "] decoding wanted[" + decode + "]");
		}
		this.decode = decode;
	}

	@Override
	public int available() throws IOException {
		return getStream().available();
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

	@Override
	public void close() throws IOException {
		status = InputStreamStatusEnum.READING_DATA;
		getStream().close();
	}

	public FormatId[] getDetectedFormatsId() {
		return detectedFormats;
	}

	public final FormatEnum getFormat() {
		return getFormatId().format;
	}

	public final FormatId getFormatId() {
		return getDetectedFormatsId()[0];
	}

	public final FormatEnum[] getFormats() {
		FormatId[] formats = getDetectedFormatsId();
		Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (FormatId formatId : formats) {
			result.add(formatId.format);
		}
		return result.toArray(new FormatEnum[0]);
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read() throws IOException {
		status = InputStreamStatusEnum.READING_DATA;
		return getStream().read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		status = InputStreamStatusEnum.READING_DATA;
		return getStream().skip(n);
	}

	private InputStream getStream() {
		return (decode ? decodedStream : baseStream);
	}
}
