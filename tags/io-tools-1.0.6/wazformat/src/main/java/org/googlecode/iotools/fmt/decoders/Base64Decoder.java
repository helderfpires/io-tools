package org.googlecode.iotools.fmt.decoders;

import org.bouncycastle.util.encoders.Base64;
import org.googlecode.iotools.fmt.base.Decoder;
import org.googlecode.iotools.fmt.base.FormatEnum;

public class Base64Decoder implements Decoder {

	public byte[] decode(final byte[] encodedBytes) {
		final String string = new String(encodedBytes);
		String s1 = string.replaceAll("[\\p{Space}=]", "");
		final int fixlength = 4 - s1.length() % 4;
		if (fixlength < 4) {
			for (int i = 0; i < fixlength; i++) {
				s1 += "=";
			}
		}
		return Base64.decode(s1.getBytes());
	}

	public int getEncodingOffset() {
		return 0;
	}

	public FormatEnum getFormat() {
		return FormatEnum.BASE64;
	}

	public float getRatio() {
		return 4 / 3;
	}

}
