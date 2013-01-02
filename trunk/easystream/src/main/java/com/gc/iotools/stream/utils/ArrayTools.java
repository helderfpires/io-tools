package com.gc.iotools.stream.utils;

import java.lang.reflect.Array;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */

/**
 * Miscellaneous utilities for Arrays, i haven't found anywhere.
 * 
 * @author dvd.smnt
 * @since 1.0.9
 * @version $Id$
 */
public final class ArrayTools {
	/**
	 * Find the index of the contained array in the src array.
	 * 
	 * @param src
	 *            Source array.
	 * @param contained
	 *            Array to search for.
	 * @return position of the contained array or -1 if not found.
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

	public static byte[] subarray(final byte[] array,
			int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return new byte[0];
		}

		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/*
	 * utility class, shouldn't be instantiated.
	 */
	private ArrayTools() {
		// utility class, shouldn't be instantiated.
	}
}
