package com.gc.iotools.stream.storage;

public class ThresholdStorage implements Storage {
	private final int treshold;
	
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
			
		}
	}

	public void seek(long position) {
		this.position = position;
	}


}
