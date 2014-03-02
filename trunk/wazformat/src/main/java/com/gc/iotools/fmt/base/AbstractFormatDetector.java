package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */

public abstract class AbstractFormatDetector implements DetectionLibrary {
	private final FormatEnum detectedFormat;
	private final int detectLenght;

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
