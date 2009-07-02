package com.gc.iotools.stream.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible of instantiating the right executor given an
 * ExecutionModel.
 * 
 * 
 * @author Davide Simonetti
 * @since 1.0.2
 */
public final class ExecutorServiceFactory {

	/**
	 * Executor that stops after a single execution.
	 * 
	 */
	private static class OneShotThreadExecutor extends
			AbstractExecutorService {
		private final ExecutorService exec = Executors
				.newSingleThreadExecutor();

		public boolean awaitTermination(final long timeout,
				final TimeUnit unit) throws InterruptedException {
			return this.exec.awaitTermination(timeout, unit);
		}

		public void execute(final Runnable command) {
			this.exec.execute(command);
			shutdown();
		}

		public boolean isShutdown() {
			return this.exec.isShutdown();
		}

		public boolean isTerminated() {
			return this.exec.isTerminated();
		}

		public void shutdown() {
			this.exec.shutdown();
		}

		public List<Runnable> shutdownNow() {
			return this.exec.shutdownNow();
		}

		@Override
		public <T> Future<T> submit(final Callable<T> task) {
			final Future<T> result = this.exec.submit(task);
			shutdown();
			return result;
		}

	}

	/*
	 * Default should be 0 otherwise there are problems stopping application
	 * servers.
	 */
	private static ExecutorService executor = new ThreadPoolExecutor(0, 20,
			5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));

	private static final ExecutorService SINGLE_EXECUTOR = Executors
			.newSingleThreadExecutor();

	public static ExecutorService getExecutor(final ExecutionModel tmodel) {
		final ExecutorService result;
		switch (tmodel) {
		case THREAD_PER_INSTANCE:
			result = new OneShotThreadExecutor();
			break;
		case STATIC_THREAD_POOL:
			result = ExecutorServiceFactory.executor;
			break;
		case SINGLE_THREAD:
			result = ExecutorServiceFactory.SINGLE_EXECUTOR;
			break;

		default:
			throw new UnsupportedOperationException("ExecutionModel ["
					+ tmodel + "] not supported");
		}
		return result;
	}

	/**
	 * <p>
	 * Sets the default ExecutorService returned when this class is invoked with
	 * {@link ExecutionModel#STATIC_THREAD_POOL}.
	 * </p>
	 * <p>
	 * It can also be used to initialize the class (for instance for use into a
	 * web application).
	 * </p>
	 * 
	 * @param executor
	 *            ExecutorService for the STATIC_THREAD_POOL model.
	 */
	public static void setDefaultThreadPoolExecutor(
			final ExecutorService executor) {
		ExecutorServiceFactory.executor = executor;
	}

	/**
	 * Users should not instantiate this class directly.
	 */
	private ExecutorServiceFactory() {

	}

	/**<p>
	 * Call this method to initialize the <code>ExecutorService</code> that is used in
	 * <code>STATIC_THREAD_POOL</code> execution mode.</p>
	 * 
	 * @see ExecutionModel#STATIC_THREAD_POOL
	 * @see #setDefaultThreadPoolExecutor(ExecutorService)
	 */
	public static void init() {
		setDefaultThreadPoolExecutor(new ThreadPoolExecutor(0, 20, 5,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500)));
	}

	/**
	 * <p>
	 * Call this method to finalize the execution queue.
	 * </p>
	 * <p>
	 * It is mandatory when you use this library in a container, otherwise the
	 * container doesn't terminate gracefully (for instance you can call it in a
	 * {@link ContextListener#shutdown()}).
	 * </p>
	 */
	public static void shutDown() {
		executor.shutdown();
	}
}
