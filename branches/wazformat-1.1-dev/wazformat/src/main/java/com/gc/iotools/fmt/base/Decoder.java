package com.gc.iotools.fmt.base;

import java.io.InputStream;

/**
 * Interface for extracting the content from an encoding format (es.Base64)
 * 
 * @author dvd.smnt
 */
public interface Decoder {

	InputStream decode(InputStream encodedStream);

	FormatEnum getFormat();
}
