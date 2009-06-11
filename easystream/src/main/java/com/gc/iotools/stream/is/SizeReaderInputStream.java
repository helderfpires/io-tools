package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

import java.io.InputStream;

/**
 * <p>
 * Counts the bytes of the <code>InputStream</code> passed in the constructor.
 * It can be used to determine the size of a document passed as a stream. This
 * is possible only after the stream has been fully processed (by other parts of
 * the application).
 * </p>
 * <b>Since version 1.2.1 this class is deprecated and will be removed in 1.3
 * versions. Use StatsInputStream instead</b>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * SizeReaderInputStream srIstream = new SizeReaderInputStream(originalStream);
 * //performs all the application operation on stream
 * performTasksOnStream(srIstream);
 * srIstream.close();
 * long size = srIstream.getSize();
 * </pre>
 * 
 * @deprecated
 * @see StatsInputStream
 * @author dvd.smnt
 * @since 1.0.6
 */

@Deprecated
public class SizeReaderInputStream extends StatsInputStream {

	public SizeReaderInputStream(final InputStream source) {
		super(source);
	}

	public SizeReaderInputStream(final InputStream istream,
			final boolean fullReadOnClose) {
		super(istream, fullReadOnClose);
	}

}
