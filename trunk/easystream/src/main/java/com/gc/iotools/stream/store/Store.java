package com.gc.iotools.stream.store;

import java.io.IOException;

public interface Store {

	void cleanup();

	int get(byte[] bytes, int offset, int length) throws IOException;

	void put(byte[] bytes, int offset, int length) throws IOException;

}