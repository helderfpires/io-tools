package com.gc.iotools.fmt.deflen;

import com.gc.iotools.fmt.base.FormatEnum;

public interface DefiniteLengthModule {

	boolean detect(final byte[] readedBytes);

	FormatEnum getDetectedFormat();

	int getDetectLenght();

	void init(FormatEnum fenum, String param);
}