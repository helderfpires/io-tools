package com.gc.iotools.fmt.base;

import java.io.IOException;
import java.io.InputStream;

public interface StreamDetector extends Detector {

	FormatId detect(FormatEnum[] enabledFormats, InputStream stream)
			throws IOException;

	int getDetectLength(FormatEnum[] formats);
}
