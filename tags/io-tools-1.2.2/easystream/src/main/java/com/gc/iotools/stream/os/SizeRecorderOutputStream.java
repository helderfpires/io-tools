package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.OutputStream;

/**
 * <p>
 * This class counts the number of bytes written to the
 * <code>OutputStream</code> passed in the constructor.
 * </p>
 * 
 * Will be removed in 1.3 version.
 * 
 * TODO: junits
 * 
 * @deprecated
 * @see StatsOutputStream
 * @author dvd.smnt
 * @since 1.0.6
 */
@Deprecated
public class SizeRecorderOutputStream extends StatsOutputStream {

	public SizeRecorderOutputStream(final OutputStream destination) {
		super(destination);
	}
}
