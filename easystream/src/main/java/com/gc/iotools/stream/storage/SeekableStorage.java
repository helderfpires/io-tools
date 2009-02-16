package com.gc.iotools.stream.storage;

public interface SeekableStorage extends Storage {
	void seek(long position);
}
