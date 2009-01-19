package com.gc.iotools.fmt.base;

import java.io.File;

public interface IndefiniteLengthDetector extends Detector {
	FormatId detect(FormatEnum[] enabledFormats, File theFile);
}
