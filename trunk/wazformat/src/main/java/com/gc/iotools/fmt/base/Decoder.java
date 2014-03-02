package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
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

}
