package com.gc.iotools.stream.base;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */

/**
 * <p>
 * This class enumerates the policies for instantiating <code>Threads</code>
 * in classes of EasyStream library that needs of them.
 * </p>
 *
 * @author dvd.smnt
 * @since 1.0
 * @version $Id: 1$
 */
public enum ExecutionModel {
	/**
	 * <p>
	 * Only one thread is shared by all instances (slow).
	 * </p>
	 */
	SINGLE_THREAD,
	/**
	 * <p>
	 * Threads are taken from a static pool.
	 * </p>
	 * <p>
	 * Some slow thread might lock up the pool and other processes might be
	 * slowed down.
	 * </p>
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor
	 */

	STATIC_THREAD_POOL,
	/**
	 * <p>
	 * One thread per instance of class. Slow but each instance can work in
	 * isolation. Also if some thread is not correctly closed there might be
	 * threads leaks.
	 * </p>
	 */
	THREAD_PER_INSTANCE
}
