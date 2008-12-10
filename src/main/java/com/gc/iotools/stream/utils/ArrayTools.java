package com.gc.iotools.stream.utils;

public final class ArrayTools {

	public static int indexOf(final byte[] src, final byte[] contained) {
		if (src == null) {
			throw new IllegalArgumentException("Source array can not be null");
		}
		int result = -1;
		if (src.length >= contained.length) {
			boolean found = false;
			int pos = 0;
			for (; pos <= (src.length - contained.length) && !found; pos++) {
				boolean equals = true;
				for (int j = 0; j < contained.length && equals; j++) {
					equals = (src[pos + j] == contained[j]);
					found = (j == (contained.length - 1)) && equals;
				}
			}
			result = (found ? (pos - 1) : -1);
		}
		return result;
	}
}
