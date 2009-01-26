package com.gc.iotools.fmt.deflen;

import com.gc.iotools.fmt.base.FormatId;

public interface DefiniteLengthModule {

	boolean detect(final byte[] readedBytes);

	FormatId getDetectedFormat();

	int getDetectLenght();

	void init(FormatId fenum, String param);
}