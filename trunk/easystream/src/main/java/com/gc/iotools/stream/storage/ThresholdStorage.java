package com.gc.iotools.stream.storage;

import java.io.File;
import java.io.RandomAccessFile;

public class ThresholdStorage implements SeekableStorage {
	private final int treshold;
	private File fileStorage;
	private RandomAccessFile fileAccess;
	private long size = 0;
	private long position = 0;

	public ThresholdStorage(int treshold) {
		this.treshold = treshold;
	}

	private MemoryStorage ms = new MemoryStorage();

	public void cleanup() {

	}

	public int get(final byte[] bytes, final int offset, final int length) {
		final int effectiveLength = (int) Math.min(length, this.buffer.length
				- this.position);
		int result;
		if (effectiveLength > 0) {
		System.arraycopy(this.buffer, (int) this.position, bytes, offset,
				effectiveLength);
		result = effectiveLength;
		} else {
			result = -1;
		}
		return result;
	}

	public void put(final byte[] bytes, final int offset, final int length) {
		if(size+length<treshold){
			ms.put(bytes, offset, length);
			size +=length;
		} else {
			if(size<treshold){
				//empty the memory buffer and init the file buffer
				ms.cleanup();
			}else{
				//check if the fileAccess is well positioned.
				this.fileAccess.write(bytes, offset, length);
			}
		}
	}

	public void seek(long position) {
		this.position = position;
	}


}
