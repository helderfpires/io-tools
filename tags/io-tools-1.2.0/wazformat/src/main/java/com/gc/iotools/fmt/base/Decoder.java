package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for extracting the content from an encoding format (es.Base64)
 * 
 * @author dvd.smnt
 */
public interface Decoder {
	InputStream decode(InputStream inStream) throws IOException;

	FormatEnum getFormat();
	
	int getOffset();

	float getRatio();
}
