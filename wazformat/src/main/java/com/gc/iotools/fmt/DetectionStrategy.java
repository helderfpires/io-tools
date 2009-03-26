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

		IdentificationResult(ResettableInputStream resettableIs,
				FormatId[] formats) {
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
	private final FormatEnum[] enabledFormats;
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

	private IdentificationResult identify() throws IOException {
		final Collection<FormatId> formats = new ArrayList<FormatId>();
		final Map<FormatEnum, Decoder> decMap = getDecodersMap(decoders);
		FormatId curFormat;
		this.internalStream.enable(true);
		ResettableInputStream currentStream = this.internalStream;
		int recursionLevel = 0;
		do {
			curFormat = detectFormatStream(currentStream,
					this.detectionLibraries, enabledFormats);
			if (!FormatEnum.UNKNOWN.equals(curFormat.format)
					&& decMap.containsKey(curFormat.format)) {
				final Decoder decoder = decMap.get(curFormat.format);
				currentStream = new ResettableStreamWrapper(currentStream,
						decoder);
			}
			formats.add(curFormat);
			recursionLevel++;
		} while (decMap.containsKey(curFormat.format)
				&& (recursionLevel <= maxRecursion));
		this.internalStream.enable(false);
		return new IdentificationResult(currentStream, formats
				.toArray(new FormatId[formats.size()]));
	}

	public FormatId[] getFormats() throws IOException {
		checkInitialized();
		return this.result.formats;
	}

	public ResettableInputStream getStream() throws IOException {
		checkInitialized();
		return this.result.resettableIs;
	}

	private void checkInitialized() throws IOException {
		if (this.result == null) {
			this.result = identify();
		}
	}

	public void setMaxRecursion(int maxRecursion) {
		if (this.maxRecursion != maxRecursion)
			this.result = null;
		this.maxRecursion = maxRecursion;
	}
}
