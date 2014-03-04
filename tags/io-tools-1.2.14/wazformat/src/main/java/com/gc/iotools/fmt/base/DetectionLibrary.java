package com.gc.iotools.fmt.base;

import java.io.IOException;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */

public interface DetectionLibrary {

	FormatId detect(FormatEnum[] enabledFormats, ResettableInputStream stream)
			throws IOException;

	FormatEnum[] getDetectedFormats();
}
