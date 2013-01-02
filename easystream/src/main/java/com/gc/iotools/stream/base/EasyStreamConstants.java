package com.gc.iotools.stream.base;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
/**
 * EasyStream constant values.
 *
 * @author gcontini
 * @version $Id$
 */
public final class EasyStreamConstants {
	/**
	 * Default size for pipe buffer.
	 */
	public static final int DEFAULT_PIPE_SIZE = 4096;

	/** Constant <code>ONE_KILOBYTE=1024F</code> */
	public static final float ONE_KILOBYTE = 1024F;

	/**
	 * Default skip size.
	 */
	public static final int SKIP_BUFFER_SIZE = 8192;

	private EasyStreamConstants() {
		// utility class: don't instantiate
	}
}
