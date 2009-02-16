package com.gc.iotools.stream.store;

public class MemoryStore implements SeekableStore {
	private long position = 0;

	private byte[] buffer = new byte[0];

	public void cleanup() {
		buffer = new byte[0];
		position = 0;
	}

	public int get(final byte[] bytes, final int offset, final int length) {
		final int effectiveLength = (int) Math.min(length, this.buffer.length
				- this.position);
		int result;
		if (effectiveLength > 0) {
			System.arraycopy(this.buffer, (int) this.position, bytes, offset,
					effectiveLength);
			result = effectiveLength;
			position += effectiveLength;
		} else {
			result = -1;
		}
		return result;
	}

	public void put(final byte[] bytes, final int offset, final int length) {
		final byte[] tmpBuffer = new byte[length + this.buffer.length];
		System.arraycopy(this.buffer, 0, tmpBuffer, 0, this.buffer.length);
		System
				.arraycopy(bytes, offset, tmpBuffer, this.buffer.length,
						length);
		this.buffer = tmpBuffer;
	}

	public void seek(final long position) {
		this.position = position;
	}

}
