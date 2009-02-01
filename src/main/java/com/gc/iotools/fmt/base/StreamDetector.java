package com.gc.iotools.fmt.base;

import java.io.InputStream;

public interface StreamDetector extends Detector {

	FormatId detect(FormatEnum[] enabledFormats, InputStream stream);

	int getDetectLength(FormatEnum[] formats);
}
