package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

/**
 * TODO: more efficient memory usage.
 * 
 * @author dvd.smnt
 * @since 1.2
 */
public class MemoryStore implements SeekableStore {
	private long position = 0;

	public long getPosition() {
		return position;
	}

	private byte[] buffer = new byte[0];

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
		} else if (effectiveLength == 0) {
			result = 0;
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
