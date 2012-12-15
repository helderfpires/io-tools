package com.gc.iotools.fmt;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;
import com.gc.iotools.fmt.decoders.CompositeDecoder;
import com.gc.iotools.fmt.detect.droid.DroidDetectorImpl;

final class DetectionStrategy {

	private class IdentificationResult {
		final FormatId[] formats;
		final ResettableInputStream resettableIs;

		IdentificationResult(final ResettableInputStream resettableIs,
				final FormatId[] formats) {
			this.resettableIs = resettableIs;
			this.formats = formats;
		}

	}

	private static final Logger LOG = LoggerFactory
			.getLogger(DroidDetectorImpl.class);

	private static FormatId detectFormatStream(
			final ResettableInputStream stream,
			final DetectionLibrary[] detectors,
			final FormatEnum[] enabledFormats) throws IOException {

		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		final Collection<FormatEnum> toDetect = new ArrayList<FormatEnum>(
				Arrays.asList(enabledFormats));
		if (detectors != null) {
			for (int i = 0; (i < detectors.length)
					&& FormatEnum.UNKNOWN.equals(detected.format)
					&& (toDetect.size() > 0); i++) {
				final DetectionLibrary detectionLibrary = detectors[i];
				try {
					if (isDetectorNeeded(detectionLibrary, toDetect)) {
						detected = detectionLibrary.detect(
								toDetect.toArray(new FormatEnum[0]), stream);
						toDetect.removeAll(Arrays.asList(detectionLibrary
								.getDetectedFormats()));
					}
				} catch (final Exception e) {
					LOG.debug("detector [" + detectionLibrary
							+ "] threw exception", e);
				}
				stream.resetToBeginning();
			}
		}
		return detected;
	}

	private static Map<FormatEnum, Decoder> getDecodersMap(
			final Decoder[] decoders) {
		final Map<FormatEnum, Decoder> formatsMap = new HashMap<FormatEnum, Decoder>();
		if (decoders != null) {
			for (final Decoder decoder : decoders) {
				formatsMap.put(decoder.getFormat(), decoder);
			}
		}
		return formatsMap;
	}

	private static boolean isDetectorNeeded(final DetectionLibrary detect,
			final Collection<FormatEnum> toDetect) {
		final FormatEnum[] formats = detect.getDetectedFormats();
		boolean result = false;
		for (int i = 0; (i < formats.length) && (!result); i++) {
			result |= toDetect.contains(formats[i]);
		}
		return result;
	}

	private final Decoder[] decoders;
	private final DetectionLibrary[] detectionLibraries;
	private FormatEnum[] enabledFormats;
	private final ResettableStreamRASAdapter internalStream;
	// recursion disabled by default
	private int maxRecursion = 0;
	private IdentificationResult result;

	public DetectionStrategy(final DetectionLibrary[] detectors,
			final Decoder[] decoders, final FormatEnum[] enabledFormats,
			final ResettableStreamRASAdapter istream) {
		this.internalStream = istream;
		this.detectionLibraries = detectors;
		this.decoders = decoders;
		this.enabledFormats = enabledFormats;
	}

	private void checkInitialized() throws IOException {
		if (this.result == null) {
			this.result = identify();
		}
	}

	private Decoder getDecoder(final List<FormatId> formats,
			final Map<FormatEnum, Decoder> decMap) {
		final Decoder decoder;
		if (formats.size() > 1) {
			final Collection<Decoder> decoderColl = new ArrayList<Decoder>();
			for (final FormatId formatId1 : formats) {
				final FormatEnum format = formatId1.format;
				final Decoder decoder1 = decMap.get(format);
				decoderColl.add(decoder1);
			}
			decoder = new CompositeDecoder(
					decoderColl.toArray(new Decoder[0]));
		} else {
			final FormatEnum format = formats.get(0).format;
			decoder = decMap.get(format);
		}
		return decoder;
	}

	public FormatId[] getFormats() throws IOException {
		checkInitialized();
		return this.result.formats;
	}

	public ResettableInputStream getStream() throws IOException {
		checkInitialized();
		return this.result.resettableIs;
	}

	private IdentificationResult identify() throws IOException {
		final List<FormatId> formats = new ArrayList<FormatId>();
		final Map<FormatEnum, Decoder> decMap = getDecodersMap(this.decoders);
		FormatId curFormat;
		this.internalStream.enable(true);
		ResettableInputStream currentStream = this.internalStream;
		int recursionLevel = 0;
		do {
			curFormat = detectFormatStream(currentStream,
					this.detectionLibraries, this.enabledFormats);
			if ((recursionLevel == 0)
					|| !FormatEnum.UNKNOWN.equals(curFormat)) {
				formats.add(curFormat);
			}
			if (!FormatEnum.UNKNOWN.equals(curFormat.format)
					&& decMap.containsKey(curFormat.format)) {
				final Decoder decoder = getDecoder(formats, decMap);
				currentStream = new ResettableStreamWrapper(
						this.internalStream, decoder);
			}
			recursionLevel++;
		} while (decMap.containsKey(curFormat.format)
				&& (recursionLevel <= this.maxRecursion));
		this.internalStream.enable(false);
		return new IdentificationResult(currentStream,
				formats.toArray(new FormatId[formats.size()]));
	}

	public void setEnabledFormats(final FormatEnum[] enabledFormats) {
		this.enabledFormats = enabledFormats;
		this.result = null;
	}

	public void setMaxRecursion(final int maxRecursion) {
		if (this.maxRecursion != maxRecursion) {
			this.result = null;
		}
		this.maxRecursion = maxRecursion;
	}
}
