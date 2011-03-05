package com.gc.iotools.fmt.base;

import java.io.IOException;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

public interface DetectionLibrary {

	FormatId detect(FormatEnum[] enabledFormats, ResettableInputStream stream)
			throws IOException;

	FormatEnum[] getDetectedFormats();
}
