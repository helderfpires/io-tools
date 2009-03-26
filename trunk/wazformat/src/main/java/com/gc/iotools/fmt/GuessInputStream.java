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
			final String streamConfigFile) throws IOException {
		if ((droidSignatureFile == null) && (streamConfigFile == null)) {
			throw new IllegalArgumentException(
					"both configuration files are null.");
		}
		final Collection<DetectionLibrary> detectionLibraries = new HashSet<DetectionLibrary>();
		if (streamConfigFile != null) {
			final DetectionLibrary stream = new StreamDetectorImpl(
					streamConfigFile, clazz);
			detectionLibraries.add(stream);
		}
		if (droidSignatureFile != null) {
			final DetectionLibrary stream = new DroidDetectorImpl(clazz,
					droidSignatureFile, null);
			detectionLibraries.add(stream);
		}
		return getInstance(istream, null, detectionLibraries
				.toArray(new DetectionLibrary[0]), DEFAULT_DECODERS
				.toArray(new Decoder[0]));
	}

	// private static final Loggerger LOGGER = Loggerger
	// .getLoggerger(GuessFormatInputStream.class);
	// Should become a collection to support multiple detectors per format
	// private final Set<StreamDetector> definiteLength = new
	// HashSet<StreamDetector>();

	public static GuessInputStream getInstance(final InputStream source,
			final FormatEnum[] enabledFormats) {
		return getInstance(source, enabledFormats);
	}

	public static GuessInputStream getInstance(final InputStream stream,
			final FormatEnum[] enabledFormats,
			final DetectionLibrary[] detectors, final Decoder[] decoders) {
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
		final DetectionStrategy ds = new DetectionStrategy(detectors,
				decoders, enabledFormats, ris);
		result = new GuessInputStream(enabledFormats, ris, ds);
		return result;
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
			final FormatEnum[] enabledFormats, final int recursionLevel)
			throws IOException {

		final Collection<DetectionLibrary> detectionLibraries = new ArrayList<DetectionLibrary>();
		detectionLibraries.add(new StreamDetectorImpl());
		detectionLibraries.add(new DroidDetectorImpl());
		return getInstance(source, enabledFormats, detectionLibraries
				.toArray(new DetectionLibrary[0]), DEFAULT_DECODERS
				.toArray(new Decoder[0]));
	}

	private final ResettableStreamRASAdapter baseStream;

	private final DetectionStrategy detectionStrategy;

	private final Collection<FormatEnum> enabledFormats;

	private InputStreamStatusEnum status = InputStreamStatusEnum.NOT_INITIALIZED;

	private boolean decode = false;

	protected GuessInputStream(final FormatEnum[] enabledFormats,
			final ResettableStreamRASAdapter baseStream,
			final DetectionStrategy decodedStream) {
		this.enabledFormats = Collections.unmodifiableCollection(Arrays
				.asList(enabledFormats));
		this.baseStream = baseStream;
		this.detectionStrategy = decodedStream;
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
		for (int i = 0; (i < formatEnums.length) && result; i++) {
			final FormatEnum formatEnum = formatEnums[i];
			result &= this.enabledFormats.contains(formatEnum);
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		getStream().close();
	}

	public FormatId[] getDetectedFormatsId() throws IOException {
		return this.detectionStrategy.getFormats();
	}

	public final FormatEnum getFormat() throws IOException {
		return getFormatId().format;
	}

	public final FormatId getFormatId() throws IOException {
		return getDetectedFormatsId()[0];
	}

	public final void setRecursion(int level) {
		if (InputStreamStatusEnum.READING_DATA.equals(this.status)) {
			throw new IllegalStateException(
					"The number of recursion can "
							+ "be set only before any read() operation has been called.");
		}
		this.detectionStrategy.setMaxRecursion(level);
	}

	public final FormatEnum[] getFormats() throws IOException {
		final FormatId[] formats = getDetectedFormatsId();
		final Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (final FormatId formatId : formats) {
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
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b, off, len);
	}

	public void decode(final boolean decode) {
		if (this.status.equals(InputStreamStatusEnum.READING_DATA)
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
	public long skip(final long n) throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().skip(n);
	}

	private InputStream getStream() throws IOException {
		return (this.decode ? this.detectionStrategy.getStream()
				: this.baseStream);
	}
}
