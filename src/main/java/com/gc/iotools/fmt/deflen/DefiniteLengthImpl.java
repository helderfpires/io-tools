package com.gc.iotools.fmt.deflen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.gc.iotools.fmt.base.DefiniteLengthDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;

public final class DefiniteLengthImpl implements DefiniteLengthDetector {

	private static FormatId detectFormat(final byte[] bytes,
			final DefiniteLengthModule[] detectors) {
		FormatId detected = FormatEnum.UNKNOWN;
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

	@Override
	public FormatId detect(final FormatEnum[] enabledFormats,
			final byte[] bytes) {
		DefiniteLengthModule[] modules = getModulesForFormats(enabledFormats);
		return detectFormat(bytes, modules);
	}

	@Override
	public FormatEnum[] getDetectedFormat() {
		Collection<FormatEnum> formats = new HashSet<FormatEnum>();
		for (DefiniteLengthModule module : this.configuredModules) {
			formats.add(module.getDetectedFormat());
		}
		return formats.toArray(new FormatEnum[formats.size()]);
	}

	@Override
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
