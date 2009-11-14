package com.gc.iotools.stream.os;

import java.io.OutputStream;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

/**
 * <p>
 * Gather some statistics on the <code>OutputStream</code> passed in the
 * constructor.
 * </p>
 * <p>
 * It can be used to read:
 * <ul>
 * <li>The size of the data written to the underlying stream.</li>
 * <li>The time spent writing the bytes.</li>
 * <li>The bandwidth of the underlying stream.</li>
 * </ul>
 * </p>
 * <p>
 * Full statistics are available after the stream has been fully processed (by
 * other parts of the application), or after invoking the method
 * {@linkplain #close()} while partial statistics are available on the fly.
 * </p>
 * 
 * @deprecated
 * @author dvd.smnt
 * @since 1.2.1
 */
@Deprecated
public class StatsOutputStream extends
		com.gc.iotools.stream.os.inspection.StatsOutputStream {

	public StatsOutputStream(OutputStream destination) {
		super(destination);
		// TODO Auto-generated constructor stub
	}

}
