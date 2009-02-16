package com.gc.iotools.stream.store;

import java.io.IOException;

public interface SeekableStore extends Store {
	void seek(long position) throws IOException;
}
