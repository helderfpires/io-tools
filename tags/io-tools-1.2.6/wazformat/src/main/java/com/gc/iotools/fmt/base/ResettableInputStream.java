package com.gc.iotools.fmt.base;

import java.io.IOException;
import java.io.InputStream;

public abstract class ResettableInputStream extends InputStream {

	public abstract void resetToBeginning() throws IOException;
}
