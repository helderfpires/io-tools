package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

public abstract class AbstractFormatDetector implements DetectionLibrary {
	private final int detectLenght;
	private final FormatEnum detectedFormat;

	protected AbstractFormatDetector(final int detectLenght,
			final FormatEnum detectedFormat) {
		this.detectLenght = detectLenght;
		this.detectedFormat = detectedFormat;
	}

	public final FormatEnum getDetectedFormat() {
		return this.detectedFormat;
	}

	public final int getDetectLenght() {
		return this.detectLenght;
	}

}
