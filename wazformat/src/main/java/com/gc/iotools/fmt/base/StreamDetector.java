package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

public interface StreamDetector extends Detector {

	FormatId detect(FormatEnum[] enabledFormats, InputStream stream)
			throws IOException;

	int getDetectLength(FormatEnum[] formats);
}
