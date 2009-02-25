package com.gc.iotools.fmt.base;

import java.io.File;

public interface FileDetector extends Detector {
	FormatId detect(FormatEnum[] enabledFormats, File theFile);
}
