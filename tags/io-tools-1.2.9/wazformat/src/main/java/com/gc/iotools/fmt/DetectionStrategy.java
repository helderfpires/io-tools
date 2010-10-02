package com.gc.iotools.fmt;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;
import com.gc.iotools.fmt.detect.droid.DroidDetectorImpl;

final class DetectionStrategy {

	private class IdentificationResult {
		final ResettableInputStream resettableIs;
		final FormatId[] formats;

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
						detected = detectionLibrary.detect(toDetect
								.toArray(new FormatEnum[0]), stream);
						toDetect.removeAll(Arrays.asList(detectionLibrary
								.getDetectedFormats()));
					}
				} catch (final Exception e) {
					LOG.warn("deterctor [" + detectionLibrary
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
	private FormatEnum[] enabledFormats;
	private final ResettableStreamRASAdapter internalStream;
	private final DetectionLibrary[] detectionLibraries;
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

	public FormatId[] getFormats() throws IOException {
		checkInitialized();
		return this.result.formats;
	}

	public ResettableInputStream getStream() throws IOException {
		checkInitialized();
		return this.result.resettableIs;
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

	private void checkInitialized() throws IOException {
		if (this.result == null) {
			this.result = identify();
		}
	}

	private IdentificationResult identify() throws IOException {
		final Collection<FormatId> formats = new ArrayList<FormatId>();
		final Map<FormatEnum, Decoder> decMap = getDecodersMap(this.decoders);
		FormatId curFormat;
		this.internalStream.enable(true);
		ResettableInputStream currentStream = this.internalStream;
		int recursionLevel = 0;
		do {
			curFormat = detectFormatStream(currentStream,
					this.detectionLibraries, this.enabledFormats);
			if (!FormatEnum.UNKNOWN.equals(curFormat.format)
					&& decMap.containsKey(curFormat.format)) {
				final Decoder decoder = decMap.get(curFormat.format);
				currentStream = new ResettableStreamWrapper(currentStream,
						decoder);
			}
			if ((recursionLevel == 0)
					|| !FormatEnum.UNKNOWN.equals(curFormat)) {
				formats.add(curFormat);
			}
			recursionLevel++;
		} while (decMap.containsKey(curFormat.format)
				&& (recursionLevel <= this.maxRecursion));
		this.internalStream.enable(false);
		return new IdentificationResult(currentStream, formats
				.toArray(new FormatId[formats.size()]));
	}
}
