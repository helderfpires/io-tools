package org.iotools.formats.decoder;

import org.iotools.formats.FormatEnum;

/**
 * Interface for extracting the content from an encoding format (es.Base64)
 * 
 * @author dvd.smnt
 */
public interface Decoder {
	byte[] decode(byte[] encodedBytes);

	int getEncodingOffset();

	FormatEnum getFormat();

	int getRatio();
}
