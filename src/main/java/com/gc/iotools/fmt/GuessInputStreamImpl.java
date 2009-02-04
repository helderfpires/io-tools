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
		File tmpFile = null;
		final Map<FormatEnum, Decoder> decMap = getDecodersMap(decoders);
		FormatId curFormat;
		InputStream currentStream = bufStream;
		final Collection<File> createdFiles = new ArrayList<File>();
		do {
			curFormat = detectFormatStream(currentStream, this.streamDetectors,
					enabledFormats);
			if (FormatEnum.UNKNOWN.equals(curFormat.format)
					&& this.fileDetectors != null) {
				// copy original stream to file.
				if (tmpFile == null) {
					tmpFile = copyToTempFile(tmpFile, currentStream);
				}
				File currentFile;
				if (bufStream == currentStream) {
					currentFile = tmpFile;
				} else {
					currentFile = copyToTempFile(tmpFile, currentStream);
					currentStream.close();
					// schedule for deletion
					createdFiles.add(currentFile);
				}
				for (final FileDetector fileDetect : this.fileDetectors) {
					curFormat = fileDetect.detect(enabledFormats, currentFile);
				}
				currentStream = new FileInputStream(currentFile);
			}
			if (decMap.containsKey(curFormat.format)) {
				final Decoder decoder = decMap.get(curFormat.format);
				currentStream = decoder.decode(currentStream);
			}
		} while (decMap.containsKey(curFormat.format));
		if (tmpFile == null) {
			this.bis = bufStream;
		} else {
			this.bis = new FileInputStream(tmpFile);
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
	public final FormatId getFormat() {
		return this.formats[0];
	}

	@Override
	public final FormatId[] getFormats() {
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
	
	private FileDetector[] getEnabledFileDetectors(FormatEnum[] enabledFormats,
			StreamDetector[] definiteLength, FileDetector fileDetector) {
		Set<FormatEnum> undetectedFormats = getUndetectedFormats(
				enabledFormats, definiteLength);

	}

	private Set<FormatEnum> getUndetectedFormats(FormatEnum[] enabledFormats,
			Detector[] detectors) {
		Set<FormatEnum> set = new HashSet<FormatEnum>(Arrays
				.asList(enabledFormats));
		if (detectors != null) {
			Collection<FormatEnum> detected = new HashSet<FormatEnum>();
			for (Detector detector : detectors) {
				detected.addAll(Arrays.asList(detector.getDetectedFormats()));
			}
			set.removeAll(detected);
			
		}
		return set;
	}
	
	private File copyToTempFile(final File tmpFile,
			final InputStream currentStream) throws IOException,
			FileNotFoundException {
		File currentFile;
		currentFile = File.createTempFile("iotoos-fmt", ".tmp");
		final FileOutputStream output = new FileOutputStream(tmpFile);
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

	private FileDetector[] getInDefiniteLenght(final Detector[] detectors) {
		final Collection<FileDetector> coll = new ArrayList<FileDetector>();
		for (final Detector detector : detectors) {
			if (detector instanceof StreamDetector) {
				coll.add((FileDetector) detector);
			}
		}

		return (coll.size() == 0 ? null : coll.toArray(new FileDetector[0]));
	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
		super.finalize();
	}
}
