package com.gc.iotools.stream.utils;

/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */

/**
 * Miscellaneous utilities for Arrays, i haven't found anywhere.
 *
 * @author dvd.smnt
 * @since 1.0.9
 * @version $Id: $
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

	/*
	 * utility class, shouldn't be instantiated.
	 */
	private ArrayTools() {
		// utility class, shouldn't be instantiated.
	}
}
