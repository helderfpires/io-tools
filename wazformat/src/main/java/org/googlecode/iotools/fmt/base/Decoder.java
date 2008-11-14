package org.googlecode.iotools.fmt.base;


/**
 * Interface for extracting the content from an encoding format (es.Base64)
 * 
 * @author dvd.smnt
 */
public interface Decoder {
	byte[] decode(byte[] encodedBytes);

	int getEncodingOffset();

	FormatEnum getFormat();

	float getRatio();
}
