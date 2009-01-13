package com.gc.iotools.fmt.deflen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gc.iotools.fmt.base.Decoder;
import com.gc.iotools.fmt.base.DefiniteLengthDetector;
import com.gc.iotools.fmt.base.FormatEnum;

public final class DefiniteLengthImpl implements DefiniteLengthDetector {
	private static final int MAX_LEVELS = 2;

	private static FormatEnum detectFormat(final byte[] bytes,
			final DefiniteLengthModule[] detectors) {
		FormatEnum detected = FormatEnum.UNKNOWN;
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

	private static FormatEnum[] detectFormats(final byte[] bytes,
			final DefiniteLengthModule[] detectors, final Decoder[] decoders) {
		final Map decodersMap = getDecodersMap(decoders);
		final List formats = new ArrayList();
		FormatEnum currentFormat = null;
		byte[] currentBytes = bytes;
		for (int i = 0; (i < MAX_LEVELS)
				&& ((currentFormat == null) || decodersMap
						.containsKey(currentFormat)); i++) {
			currentFormat = detectFormat(currentBytes, detectors);
			formats.add(currentFormat);
			if (decodersMap.containsKey(currentFormat)) {
				currentBytes = ((Decoder) decodersMap.get(currentFormat))
						.decode(currentBytes);
			}
		}
		return (FormatEnum[]) formats.toArray(new FormatEnum[formats.size()]);
	}

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

	private final DefiniteLengthModule[] configuredModules;

	public DefiniteLengthImpl() {
		this(null, null);
	}

	public DefiniteLengthImpl(final String confFile, final Class enumclass) {
		final DefiniteModuleFactory dfmf = new DefiniteModuleFactory(confFile,
				enumclass);
		this.configuredModules = dfmf.getConfiguredModules();
	}

	@Override
	public FormatEnum detect(final FormatEnum[] enabledFormats,
			final InputStream stream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormatEnum[] getDetectedFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDetectLength(final FormatEnum[] enabledFormats) {
		// TODO Auto-generated method stub
		return 0;
	}

}
