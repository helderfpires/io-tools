package com.gc.iotools.fmt.base;

import java.io.File;

public interface IndefiniteLengthDetector {
	FormatEnum detect(FormatEnum[] enabledFormats, File theFile);
}
