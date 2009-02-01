package com.gc.iotools.fmt.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.StreamDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;

public final class DefiniteLengthImpl implements StreamDetector {

	private static byte[] readBytesAndReset(final BufferedInputStream input,
			final int size) throws IOException {
		final int size1 = size - 1;
		final byte[] buffer = new byte[size1];
		input.mark(size);
		int pos = 0;
		int n = 0;
		while ((pos < (size1))
				&& (-1 != (n = input.read(buffer, pos, (size1 - pos))))) {
			pos += n;
		}
		input.reset();
		byte[] result;
		if (pos == size1) {
			result = buffer;
		} else {
			result = new byte[pos];
			System.arraycopy(buffer, 0, result, 0, pos);
		}
		return result;
	}

	private static FormatId detectFormat(final byte[] bytes,
			final StreamDetector[] detectors, final FormatEnum[] enabledFormats) {
		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		for (int i = 0; (i < detectors.length)
				&& FormatEnum.UNKNOWN.equals(detected.format); i++) {
			final StreamDetector detector = detectors[i];
			detected = detector.detect(enabledFormats, bytes);
		}
		return detected;
	}

	private static int getBufferSize(final StreamDetector[] detectors,
			final Decoder[] decoders, final FormatEnum[] enabledFormats) {
		int detectSize = 1;
		for (final StreamDetector detector : detectors) {
			detectSize = Math.max(detectSize, detector
					.getDetectLength(enabledFormats));
		}

		int decodeOffset = 1;
		for (final Decoder decoder : decoders) {
			decodeOffset = Math.max(decodeOffset, decoder.getEncodingOffset());
		}

		float decodeRatio = 1;
		for (final Decoder decoder : decoders) {
			decodeRatio = Math.max(decodeRatio, decoder.getRatio());
		}

		return (int) (detectSize * decodeRatio) + decodeOffset + 1;
	}

	private static FormatId detectFormat(final byte[] bytes,
			final DefiniteLengthModule[] detectors) {
		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		for (final DefiniteLengthModule detector : detectors) {
			final int bytesToCopy = Math.min(detector.getDetectLenght(),
					bytes.length);
			final byte[] splittedBytes = new byte[bytesToCopy];
			System.arraycopy(bytes, 0, splittedBytes, 0, bytesToCopy);
			if (detector.detect(splittedBytes)) {
				detected = detector.getDetectedFormat();
				break;
			}
		}
		return detected;
	}

	private final DefiniteLengthModule[] configuredModules;

	public DefiniteLengthImpl() {
		this(null, null);
	}

	public DefiniteLengthImpl(final String confFile, final Class<?> enumclass) {
		final DefiniteModuleFactory dfmf = new DefiniteModuleFactory(confFile,
				enumclass);
		this.configuredModules = dfmf.getConfiguredModules();
	}

	public FormatId detect(final FormatEnum[] enabledFormats, final byte[] bytes) {
		DefiniteLengthModule[] modules = getModulesForFormats(enabledFormats);
		return detectFormat(bytes, modules);
	}

	public FormatEnum[] getDetectedFormats() {
		Collection<FormatEnum> formats = new HashSet<FormatEnum>();
		for (DefiniteLengthModule module : this.configuredModules) {
			formats.add(module.getDetectedFormat().format);
		}
		return formats.toArray(new FormatEnum[formats.size()]);
	}

	public int getDetectLength(final FormatEnum[] enabledFormats) {
		DefiniteLengthModule[] modules = getModulesForFormats(enabledFormats);
		int detectLen = -1;
		for (DefiniteLengthModule module : modules) {
			detectLen = Math.max(detectLen, module.getDetectLenght());
		}
		return detectLen;
	}

	private DefiniteLengthModule[] getModulesForFormats(
			FormatEnum[] requestedFormats) {
		Collection<DefiniteLengthModule> modules = new ArrayList<DefiniteLengthModule>();
		List<FormatEnum> reqFormatList = Arrays.asList(requestedFormats);
		for (DefiniteLengthModule module : this.configuredModules) {
			if (reqFormatList.contains(module.getDetectedFormat())) {
				modules.add(module);
			}
		}
		return modules.toArray(new DefiniteLengthModule[modules.size()]);
	}
}
