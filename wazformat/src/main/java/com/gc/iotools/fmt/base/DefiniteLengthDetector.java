package com.gc.iotools.fmt.base;

import java.io.InputStream;

public interface DefiniteLengthDetector extends Detector {

	FormatEnum detect(FormatEnum[] enabledFormats, InputStream stream);

	int getDetectLength(FormatEnum[] enabledFormats);
}
