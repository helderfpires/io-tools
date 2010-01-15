package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti. This source code is released
 * under the BSD License.
 */

import java.io.EOFException;
import java.io.IOException;

/**
 * <p>
 * Represents a <code>Store</code> that can be seeked to a random position.
 * </p>
 * <p>
 * All the data passed to this store must be remembered until
 * {@link #cleanup()} is invoked, in case a {@link #seek(long)} repositions on
 * pa previously read data.
 * </p>
 * 
 * @author dvd.smnt
 * @since 1.2.0
 * @see Store
 */
public interface SeekableStore extends Store {
	/**
	 * Reposition this <code>Store</code> on a previously read position.
	 * 
	 * @param position
	 *            position to read the data from.
	 * @throws IOException
	 *             If some error in the internal store happens.
	 * @throws EOFException
	 *             If a <code>position</code> is greater than the actual Store
	 *             size.
	 */
	void seek(long position) throws IOException;
}
