package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti.
 * This source code is released under the BSD License.
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

	/**
	 * gets <code>length</code> bytes from the store.
	 * 
	 * @param bytes
	 *            array where to put the data in.
	 * @param offset
	 *            offset in the array to start put the data.
	 * @param length
	 *            length of the bytes got from the store.
	 * @return number of bytes effectively put in the array or -1 if the Store
	 *         was empty.
	 * @throws IOException
	 */
	int get(byte[] bytes, int offset, int length) throws IOException;

	void put(byte[] bytes, int offset, int length) throws IOException;

}