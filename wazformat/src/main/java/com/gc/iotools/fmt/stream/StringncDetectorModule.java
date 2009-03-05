package com.gc.iotools.fmt.stream;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import com.gc.iotools.fmt.base.FormatId;

public class StringncDetectorModule implements DefiniteLengthModule {
	private String byteSequence = null;
	private int detectLength = -1;
	private FormatId detectedFormat;

	public boolean detect(final byte[] readedBytes) {
		boolean result;
		String readString = new String(readedBytes);
		String ucaseStr = readString.toUpperCase();
		if (this.detectLength == this.byteSequence.length()) {
			result = byteSequence.equals(ucaseStr);
		} else {
			result = ucaseStr.contains(byteSequence);
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
		this.byteSequence = param.substring(sepPos + 1).toUpperCase();
		if (sepPos > 0) {
			final String detectLString = param.substring(0, sepPos);
			this.detectLength = Integer.parseInt(detectLString);
		}
		if (this.detectLength <= 0) {
			this.detectLength = this.byteSequence.length();
		}

		this.detectedFormat = fenum;
	}
	@Override
	public String toString() {
		return "StringNCModule [" + detectedFormat + "] len[" + detectLength
				+ "] strIgnoreCase [" + byteSequence + "]";
	}
}
