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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FileDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.StreamDetector;

final class GuessInputStreamImpl extends GuessInputStream {

	private static FormatId detectFormatStream(final InputStream stream,
			final StreamDetector[] detectors, final FormatEnum[] enabledFormats)
			throws IOException {
		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		if (detectors != null) {
			for (int i = 0; (i < detectors.length)
					&& FormatEnum.UNKNOWN.equals(detected.format); i++) {
				final StreamDetector detector = detectors[i];
				stream.mark(detector.getDetectLength(enabledFormats));
				detected = detector.detect(enabledFormats, stream);
				stream.reset();
			}
		}
		return detected;
	}

	private static Map<FormatEnum, Decoder> getDecodersMap(
			final Decoder[] decoders) {
		final Map<FormatEnum, Decoder> formatsMap = new HashMap<FormatEnum, Decoder>();
		for (final Decoder decoder : decoders) {
			formatsMap.put(decoder.getFormat(), decoder);
		}
		return formatsMap;
	}

	private final InputStream bis;

	private final StreamDetector[] streamDetectors;

	private final FormatId formats[];

	private final FileDetector[] fileDetectors;

	public GuessInputStreamImpl(final Detector[] detectors,
			final Decoder[] decoders, final FormatEnum[] enabledFormats,
			final InputStream istream) throws IOException {
		super(enabledFormats);
		this.streamDetectors = getDefiniteLenght(detectors);
		this.fileDetectors = getInDefiniteLenght(detectors);
		final Collection<FormatId> formats = new ArrayList<FormatId>();
		final BufferedInputStream bufStream = new BufferedInputStream(istream);
		File originalFile = null;
		final Map<FormatEnum, Decoder> decMap = getDecodersMap(decoders);
		FormatId curFormat;
		InputStream currentStream = bufStream;
		final Collection<File> createdFiles = new ArrayList<File>();
		do {
			curFormat = detectFormatStream(currentStream, this.streamDetectors,
					enabledFormats);
			if (FormatEnum.UNKNOWN.equals(curFormat.format)) {
				final Set<FileDetector> enableDetectors = getEnabledFileDetectors(
						enabledFormats, this.streamDetectors,
						this.fileDetectors);
				if (enableDetectors.size() > 0) {

					// copy original stream to file.
					if (originalFile == null) {
						originalFile = copyToTempFile(bufStream);
					}
					File currentFile;
					if (bufStream == currentStream) {
						currentFile = originalFile;
					} else {
						currentFile = copyToTempFile(currentStream);
						currentStream.close();
						// schedule for deletion
						createdFiles.add(currentFile);
					}
					for (final FileDetector fileDetect : enableDetectors) {
						curFormat = fileDetect.detect(enabledFormats,
								currentFile);
					}
					currentStream = new FileInputStream(currentFile);
				}
			}
			if (decMap.containsKey(curFormat.format)) {
				final Decoder decoder = decMap.get(curFormat.format);
				currentStream = decoder.decode(currentStream);
			}
			formats.add(curFormat);
		} while (decMap.containsKey(curFormat.format));
		if (originalFile == null) {
			this.bis = bufStream;
		} else {
			this.bis = new FileInputStream(originalFile);
		}
		for (final File file : createdFiles) {
			file.delete();
		}
		this.formats = formats.toArray(new FormatId[formats.size()]);
	}

	@Override
	public int available() throws IOException {
		return this.bis.available();
	}

	@Override
	public void close() throws IOException {
		cleanup();
		this.bis.close();

	}

	@Override
	public final FormatId[] identify() {
		return this.formats;
	}

	@Override
	public void mark(final int readlimit) {
		this.bis.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return this.bis.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.bis.read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return this.bis.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.bis.read(b, off, len);
	}

	@Override
	public void reset() throws IOException {
		this.bis.reset();
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.bis.skip(n);
	}

	private void cleanup() {

	}

	private File copyToTempFile(final InputStream currentStream)
			throws IOException, FileNotFoundException {
		final File currentFile = File.createTempFile("iotoos-fmt", ".tmp");
		final FileOutputStream output = new FileOutputStream(currentFile);
		IOUtils.copyLarge(currentStream, output);
		output.close();
		return currentFile;
	}

	private StreamDetector[] getDefiniteLenght(final Detector[] detectors) {
		final Collection<StreamDetector> coll = new ArrayList<StreamDetector>();
		for (final Detector detector : detectors) {
			if (detector instanceof StreamDetector) {
				coll.add((StreamDetector) detector);
			}
		}

		return (coll.size() == 0 ? null : coll.toArray(new StreamDetector[0]));
	}

	private Set<FileDetector> getEnabledFileDetectors(
			final FormatEnum[] enabledFormats,
			final StreamDetector[] definiteLength,
			final FileDetector[] fileDetectors) {
		final Set<FileDetector> result = new HashSet<FileDetector>();
		if (fileDetectors != null) {
			final Set<FormatEnum> undetectedFormats = getUndetectedFormats(
					enabledFormats, definiteLength);
			for (final FileDetector fileDetector : fileDetectors) {
				final FormatEnum[] formats = fileDetector.getDetectedFormats();
				boolean found = false;
				if (formats != null) {
					for (int i = 0; (i < formats.length) && !found; i++) {
						final FormatEnum formatEnum = formats[i];
						found = undetectedFormats.contains(formatEnum);
					}
				}
				if (found) {
					result.add(fileDetector);
					undetectedFormats.removeAll(Arrays.asList(formats));
				}
			}
		}
		return result;
	}

	private FileDetector[] getInDefiniteLenght(final Detector[] detectors) {
		final Collection<FileDetector> coll = new ArrayList<FileDetector>();
		for (final Detector detector : detectors) {
			if (detector instanceof FileDetector) {
				coll.add((FileDetector) detector);
			}
		}

		return (coll.size() == 0 ? null : coll.toArray(new FileDetector[0]));
	}

	private Set<FormatEnum> getUndetectedFormats(
			final FormatEnum[] enabledFormats, final Detector[] detectors) {
		final Set<FormatEnum> set = new HashSet<FormatEnum>(Arrays
				.asList(enabledFormats));
		if (detectors != null) {
			final Collection<FormatEnum> detected = new HashSet<FormatEnum>();
			for (final Detector detector : detectors) {
				detected.addAll(Arrays.asList(detector.getDetectedFormats()));
			}
			set.removeAll(detected);

		}
		return set;
	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
		super.finalize();
	}
}