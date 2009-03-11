package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */


import java.io.IOException;

/**
 * Represent a place where bytes are memorized. Used in streams that need to
 * remember the data who was read.
 * 
 * @author dvd.smnt
 * @since 1.2.0
 * @see com.gc.iotools.stream.is.RandomAccessInputStream
 */
public interface Store {
	/**
	 * Cleans up the Store. Forget all the data previously stored.
	 */
	void cleanup();

	int get(byte[] bytes, int offset, int length) throws IOException;

	void put(byte[] bytes, int offset, int length) throws IOException;

}