package com.gc.iotools.fmt.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.StreamDetector;

public final class StreamDetectorImpl implements StreamDetector {

	private static byte[] readBytesAndReset(final InputStream input,
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
		byte[] result;
		if (pos == size1) {
			result = buffer;
		} else {
			result = new byte[pos];
			System.arraycopy(buffer, 0, result, 0, pos);
		}
		return result;
	}

	private FormatId detectFormat(byte[] bytes,
			final DefiniteLengthModule[] modules) {
		FormatId detected = new FormatId(FormatEnum.UNKNOWN, null);
		if (bytes.length > 0) {
			for (int i = 0; (i < modules.length)
					&& FormatEnum.UNKNOWN.equals(detected.format); i++) {
				final DefiniteLengthModule module = modules[i];
				final int detectLenght = module.getDetectLenght();
				if (detectLenght <= 0) {
					throw new IllegalStateException("Module ["
							+ module.getDetectedFormat()
							+ "] request a detect size of [" + detectLenght
							+ "]");
				}
				final int bytesToCopy = Math.min(detectLenght, bytes.length);
				final byte[] splittedBytes = new byte[bytesToCopy];
				System.arraycopy(bytes, 0, splittedBytes, 0, bytesToCopy);
				boolean success = module.detect(splittedBytes);
				detected = (success ? module.getDetectedFormat() : detected);
			}
		}
		return detected;
	}

	private final DefiniteLengthModule[] configuredModules;

	public StreamDetectorImpl() {
		this("deflen.properties", FormatEnum.class);
	}

	public StreamDetectorImpl(final String confFile, final Class<?> enumclass) {
		final DefiniteModuleFactory dfmf = new DefiniteModuleFactory(confFile,
				enumclass);
		this.configuredModules = dfmf.getConfiguredModules();
	}

	public FormatId detect(final FormatEnum[] enabledFormats,
			final InputStream stream) throws IOException {
		DefiniteLengthModule[] modules = getModulesForFormats(enabledFormats);
		int len = getDetectLength(enabledFormats);
		byte[] bytes = readBytesAndReset(stream, len);
		return detectFormat(bytes, modules);
	}

	public FormatEnum[] getDetectedFormats() {
		Collection<FormatEnum> formats = new HashSet<FormatEnum>();
		for (DefiniteLengthModule module : this.configuredModules) {
			final FormatId detectedFormat = module.getDetectedFormat();
			formats.add(detectedFormat.format);
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
			if (reqFormatList.contains(module.getDetectedFormat().format)) {
				modules.add(module);
			}
		}
		return modules.toArray(new DefiniteLengthModule[modules.size()]);
	}
}
