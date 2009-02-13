package com.gc.iotools.stream.storage;

public interface Storage {

	void cleanup();

	int get(byte[] bytes, int offset, int length);

	void put(byte[] bytes, int offset, int length);

	void seek(long position);
}