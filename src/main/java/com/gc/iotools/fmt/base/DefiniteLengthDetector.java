package com.gc.iotools.fmt.base;

public interface DefiniteLengthDetector extends Detector {

	FormatId detect(FormatEnum[] enabledFormats, byte[] bytes);

	int getDetectLength(FormatEnum[] enabledFormats);
}
