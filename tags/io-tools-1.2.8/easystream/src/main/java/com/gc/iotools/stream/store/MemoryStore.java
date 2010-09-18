package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti. This source code is released
 * under the BSD License.
 */

/**
 * TODO: more efficient memory usage.
 * 
 * @author dvd.smnt
 * @since 1.2.0
 */
public class MemoryStore implements SeekableStore {
	private byte[] buffer = new byte[0];

	private long position = 0;

	public void cleanup() {
		this.buffer = new byte[0];
		this.position = 0;
	}

	public int get(final byte[] bytes, final int offset, final int length) {
		final int effectiveLength = (int) Math.min(length, this.buffer.length
				- this.position);
		int result;
		if (effectiveLength > 0) {
			System.arraycopy(this.buffer, (int) this.position, bytes, offset,
					effectiveLength);
			result = effectiveLength;
			this.position += effectiveLength;
		}
		// else if (effectiveLength == 0) {
		// result = 0;
		// }
		else {
			result = -1;
		}
		return result;
	}

	public long getPosition() {
		return this.position;
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

	/**
	 * Provides a String representation of the state of the Store for
	 * debugging purposes.
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[pos=" + this.position
				+ ",size=" + this.buffer.length + "]";
	}
}
