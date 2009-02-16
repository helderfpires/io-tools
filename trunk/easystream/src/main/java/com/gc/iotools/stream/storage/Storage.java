package com.gc.iotools.stream.storage;

import java.io.IOException;

public interface Storage {

	void cleanup();

	int get(byte[] bytes, int offset, int length) throws IOException;

	void put(byte[] bytes, int offset, int length) throws IOException;

}