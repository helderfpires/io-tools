package com.gc.iotools.fmt.base;
/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.File;


/**
 * Describes a Format Identification Library that relies on files.
 * 
 * @since 1.2.0
 */
public interface FileDetector extends Detector {
	/**
	 * 
	 * @param enabledFormats
	 *            Arrays of formats. If null all the supported formats will be
	 *            identified.
	 * @param theFile
	 * @return
	 */
	FormatId detect(FormatEnum[] enabledFormats, File theFile);
}
