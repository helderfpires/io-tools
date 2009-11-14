package com.gc.iotools.stream.is;

import java.io.InputStream;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

/**
 * <p>
 * Gather some statistics of the <code>InputStream</code> passed in the
 * constructor.
 * </p>
 * <p>
 * It can be used to read:
 * <ul>
 * <li>The size of the internal stream.</li>
 * <li>The time spent reading the bytes.</li>
 * <li>The raw bandwidth of the underlying stream, calculated excluding the time
 * spent by the external process to elaborate the data.</li>
 * </ul>
 * </p>
 * <p>
 * Full statistics are available after the stream has been fully processed (by
 * other parts of the application), or after invoking the method
 * {@linkplain #close()} while partial statistics are available on the fly.
 * </p>
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * StatsInputStream srIstream = new StatsInputStream(originalStream);
 * //performs all the application operation on stream
 * performTasksOnStream(srIstream);
 * srIstream.close();
 * long size = srIstream.getSize();
 * </pre>
 * <p>
 * This class is deprecated and will be removed in 1.3 release. Please use
 * com.gc.iotools.stream.is.inspection.StatsInputStream instead
 * </p>
 * 
 * @deprecated
 * @see com.gc.iotools.stream.is.inspection.StatsInputStream
 * @author dvd.smnt
 * @since 1.2.1
 */
@Deprecated
public class StatsInputStream extends
		com.gc.iotools.stream.is.inspection.StatsInputStream {

	public StatsInputStream(InputStream istream, boolean fullReadOnClose) {
		super(istream, fullReadOnClose);
	}

	public StatsInputStream(InputStream source) {
		super(source);
	}
}
