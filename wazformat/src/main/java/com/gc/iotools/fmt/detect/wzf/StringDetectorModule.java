package com.gc.iotools.fmt.detect.wzf;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini.
 * This source code is released under the BSD License.
 */
import java.util.Arrays;

import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.stream.utils.ArrayTools;

public class StringDetectorModule implements DefiniteLengthModule {
	private byte[] byteSequence = null;

	private int detectLength = -1;
	private FormatId detectedFormat;

	public boolean detect(final byte[] readBytes) {
		boolean result;
		if (this.detectLength == this.byteSequence.length) {
			result = Arrays.equals(readBytes, this.byteSequence);
		} else {
			result = ArrayTools.indexOf(readBytes, this.byteSequence) >= 0;
		}
		return result;
	}

	public FormatId getDetectedFormat() {
		if (this.detectedFormat == null) {
			throw new IllegalStateException(
					"getDetectFormat called before init");
		}
		return this.detectedFormat;
	}

	public int getDetectLength() {
		if (this.byteSequence == null) {
			throw new IllegalStateException(
					"getDetectLength called before init");
		}
		return this.detectLength;
	}

	public void init(final FormatId fenum, final String param) {
		final int sepPos = param.indexOf(':');
		this.byteSequence = param.substring(sepPos + 1).getBytes();
		if (sepPos > 0) {
			final String detectLString = param.substring(0, sepPos);
			this.detectLength = Integer.parseInt(detectLString);
		}
		if (this.detectLength <= 0) {
			this.detectLength = this.byteSequence.length;
		}

		this.detectedFormat = fenum;
	}

	@Override
	public String toString() {
		return "StringModule [" + this.detectedFormat + "] len["
				+ this.detectLength + "] str ["
				+ new String(this.byteSequence) + "]";
	}

}
