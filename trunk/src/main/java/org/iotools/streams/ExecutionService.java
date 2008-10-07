package org.iotools.streams;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible of instantiating the right executor given an
 * ExecutionModel
 * 
 * TODO: Should return an executorService for joins
 * @author Davide Simonetti
 * 
 */
public final class ExecutionService {

	private static class ThreadExecutor implements Executor {

		public void execute(final Runnable command) {
			final Thread thread = new Thread(command);
			thread.start();
		}

	}

	private static Executor EXECUTOR = new ThreadPoolExecutor(10, 20, 5,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));

	public static Executor getExecutor(final ExecutionModel tmodel) {
		final Executor result;
		switch (tmodel) {
		case THREAD_PER_INSTANCE:
			result = new ThreadExecutor();
			break;
		case STATIC_THREAD_POOL:
			result = ExecutionService.EXECUTOR;
			break;
		default:
			throw new UnsupportedOperationException("ExecutionModel [" + tmodel
					+ "] not supported");
		}
		return result;
	}

	public static void setDefaultExecutor(final Executor executor) {
		EXECUTOR = executor;
	}
}