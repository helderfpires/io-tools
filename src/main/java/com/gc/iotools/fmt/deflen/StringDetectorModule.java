package com.gc.iotools.fmt.deflen;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.stream.utils.ArrayTools;

public class StringDetectorModule implements DefiniteLengthModule {
	private byte[] byteSequence = null;
	private int detectLength = -1;
	private FormatEnum detectedFormat;

	@Override
	public boolean detect(final byte[] readedBytes) {
		boolean result;
		if (this.detectLength == this.byteSequence.length) {
			result = Arrays.equals(readedBytes, this.byteSequence);
		} else {
			result = ArrayTools.indexOf(readedBytes, this.byteSequence) >= 0;
		}
		return result;
	}

	@Override
	public FormatEnum getDetectedFormat() {
		if (this.detectedFormat == null) {
			throw new IllegalStateException(
					"getDetectFormat called before init");
		}
		return this.detectedFormat;
	}

	@Override
	public int getDetectLenght() {
		if (this.byteSequence == null) {
			throw new IllegalStateException(
					"getDetectLength called before init");
		}
		return this.detectLength;
	}

	@Override
	public void init(final FormatEnum fenum, final String param) {
		final int sepPos = param.indexOf(':');
		this.byteSequence = param.substring(sepPos).getBytes();
		final String detectLString = param.substring(0, sepPos);
		if (StringUtils.isNotBlank(detectLString)) {
			this.detectLength = Integer.parseInt(detectLString);
		}
		if (this.detectLength == 0) {
			this.detectLength = this.byteSequence.length;
		}

		this.detectedFormat = fenum;
	}

}
