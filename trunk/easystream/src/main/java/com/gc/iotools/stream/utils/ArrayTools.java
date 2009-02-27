package com.gc.iotools.stream.utils;

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
 * Miscellaneous utilities for Arrays, i haven't found anywhere.
 * 
 * @author dvd.smnt
 * @since 1.0.9
 */
public final class ArrayTools {
	/**
	 * Find the index of the contained array in the src array.
	 * 
	 * @param src
	 * @param contained
	 * @return
	 */
	public static int indexOf(final byte[] src, final byte[] contained) {
		if (src == null) {
			throw new IllegalArgumentException("Source array can not be null");
		}
		int result = -1;
		if (src.length >= contained.length) {
			boolean found = false;
			int pos = 0;
			for (; (pos <= (src.length - contained.length)) && !found; pos++) {
				boolean equals = true;
				for (int j = 0; (j < contained.length) && equals; j++) {
					equals = (src[pos + j] == contained[j]);
					found = (j == (contained.length - 1)) && equals;
				}
			}
			result = (found ? (pos - 1) : -1);
		}
		return result;
	}

	/**
	 * utility class, shouldn't be instantiated.
	 */
	private ArrayTools() {

	}
}
