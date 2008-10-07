package org.iotools.streams;

/**
 * This class describes how Threads are used
 * 
 * @author Davide Simonetti
 * 
 */
public enum ExecutionModel {
	/**
	 * <p>
	 * Threads are taken from a static pool.
	 * </p>
	 * Some slow thread might lock up the pool and other processes might be
	 * slowed down
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
