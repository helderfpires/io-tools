package com.gc.iotools.fmt;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
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
 * <p>
 * InputStream that wraps the original InputStream and guess the format. If you
 * want to use the wazformat library
 * </p>
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

	/**
	 * Constructs a new GuessInputStream given a source InputStream.
	 * 
	 * @param source
	 *            Stream to be identified.
	 * @return
	 */
	public static GuessInputStream getInstance(final InputStream source) {
		return getInstance(source, FormatEnum.values());
	}

	public static GuessInputStream getInstance(final InputStream istream,
			final Class clazz, final String droidSignatureFile,
			final String streamConfigFile) {
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
			final FormatEnum[] enabledFormats) {

		final Collection<DetectionLibrary> detectionLibraries = new ArrayList<DetectionLibrary>();
		detectionLibraries.add(new StreamDetectorImpl());
		detectionLibraries.add(new DroidDetectorImpl());
		return getInstance(source, enabledFormats, detectionLibraries
				.toArray(new DetectionLibrary[0]), DEFAULT_DECODERS
				.toArray(new Decoder[0]));
	}

	public static GuessInputStream getInstance(final InputStream stream,
			final FormatEnum[] enabledFormats,
			final DetectionLibrary[] detectors, final Decoder[] decoders) {
		if (stream == null) {
			throw new IllegalArgumentException("Parameter stream==null");
		}
		FormatEnum[] effectiveFormats = getEffectiveFormats(enabledFormats,
				detectors);
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
				decoders, effectiveFormats, ris);
		result = new GuessInputStream(effectiveFormats, ris, ds);
		return result;
	}

	private static FormatEnum[] getEffectiveFormats(
			final FormatEnum[] enabledFormats,
			final DetectionLibrary[] detectors) {
		Collection<FormatEnum> formats = new ArrayList<FormatEnum>();
		for (DetectionLibrary detectionLibrary : detectors) {
			formats.addAll(Arrays.asList(detectionLibrary
					.getDetectedFormats()));
		}
		final FormatEnum[] allFormats = formats.toArray(new FormatEnum[0]);
		FormatEnum[] effectiveFormats = (enabledFormats == null ? allFormats
				: enabledFormats);
		return effectiveFormats;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int available() throws IOException {
		return getStream().available();
	}

	/**
	 * Return <code>true</code> if this stream can detect the format passed as
	 * argument.
	 * 
	 * @param formatEnum
	 *            the format to check if it can be detected.
	 * @return <code>true</code> if this stream can detect the format passed as
	 *         argument.
	 */
	public final boolean canDetect(final FormatEnum formatEnum) {
		if (formatEnum == null) {
			throw new IllegalArgumentException("Parameter formatEnum is null");
		}
		return this.enabledFormats.contains(formatEnum);
	}

	/**
	 * Return <code>true</code> if this stream can detect the all the formats
	 * passed as argument.
	 * 
	 * @param formatsEnum
	 *            the formats to check if it can be detected.
	 * @return <code>true</code> if this stream can detect all the formats
	 *         passed as argument.
	 */
	public final boolean canDetectAll(final FormatEnum[] formatsEnum) {
		if (formatsEnum == null) {
			throw new IllegalArgumentException(
					"Parameter formatEnums is null");
		}
		boolean result = true;
		for (int i = 0; (i < formatsEnum.length) && result; i++) {
			final FormatEnum formatEnum = formatsEnum[i];
			result &= this.enabledFormats.contains(formatEnum);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		this.baseStream.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		// double check the base stream is closed to delete any temporary file
		// eventually left
		this.baseStream.close();
	}

	/**
	 * Define if the content of the internal stream must be decoded or left
	 * unchanged. Default: <b>false</b>.
	 * <ul>
	 * <li><b>true</b>: if a decoder is found for the internal data the data
	 * read from the external <code>InputStream</code> is filtered through this
	 * decoder. This also applies for recursive decoding.</li>
	 * <li><b>false</b>: the data read from <code>GuessInputStream</code> is the
	 * copy of the original <code>InputStream</code></li>
	 * </ul>
	 * 
	 * @param decode
	 *            whether to decode or not the content of the original stream.
	 */
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

	/**
	 * Get the result of the detection as a {@link FormatId} array. At place 0
	 * is the format identified for the external stream, at place 1 is the
	 * format identified after the decoder for FormatId[0] was applied.
	 * 
	 * @return the array of eventually identified formats or
	 *         {@linkplain FormatEnum#UNKNOWN} if no format recognized.
	 * @throws IOException
	 *             threw if some error happens reading from the internal stream.
	 */
	public FormatId[] getDetectedFormatsId() throws IOException {
		return this.detectionStrategy.getFormats();
	}

	/**
	 * Get the result of the detection as a {@link FormatEnum}. It is a shortcut
	 * for <code>getDetectedFormatsId()[0].format</code>.
	 * 
	 * @see #getDetectedFormatsId()
	 * @return the eventually identified format or
	 *         {@linkplain FormatEnum#UNKNOWN} if no format recognized.
	 * @throws IOException
	 *             threw if some error happens reading from the internal stream.
	 */
	public final FormatEnum getFormat() throws IOException {
		return getFormatId().format;
	}

	public final FormatId getFormatId() throws IOException {
		return getDetectedFormatsId()[0];
	}

	public final FormatEnum[] getFormats() throws IOException {
		final FormatId[] formats = getDetectedFormatsId();
		final Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (final FormatId formatId : formats) {
			result.add(formatId.format);
		}
		return result.toArray(new FormatEnum[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b) throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		this.status = InputStreamStatusEnum.READING_DATA;
		return getStream().read(b, off, len);
	}

	/**
	 * <p>
	 * Set the maximum number of recursive identification allowed. 1 for no
	 * recursion (single level detection). It also represent the maximum size of
	 * the array returned by {@link #getDetectedFormatsId()}.
	 * </p>
	 * <p>
	 * It can be set multiple times if no read() is invoked between the
	 * invocations.
	 * </p>
	 * 
	 * @param level
	 *            Integer >= 1 indicating the number of recursive identification
	 *            steps.
	 */
	public final void setIdentificationDepth(final int level) {
		if (InputStreamStatusEnum.READING_DATA.equals(this.status)) {
			throw new IllegalStateException("The number of recursion can "
					+ "be set only before any read() "
					+ "operation has been called.");
		}
		if (level < 1) {
			throw new IllegalArgumentException(
					"Identification depth must be >=1 but was ][" + level
							+ "]");
		}
		this.detectionStrategy.setMaxRecursion(level - 1);
	}

	/**
	 * {@inheritDoc}
	 */
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
