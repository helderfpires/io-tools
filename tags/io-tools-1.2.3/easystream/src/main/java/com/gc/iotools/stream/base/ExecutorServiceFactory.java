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

	private static ExecutorService executor = new ThreadPoolExecutor(10, 20,
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
	 * Sets the default ExecutorService returned when this class is invoked with
	 * {@link ExecutionModel#STATIC_THREAD_POOL}.
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
}
