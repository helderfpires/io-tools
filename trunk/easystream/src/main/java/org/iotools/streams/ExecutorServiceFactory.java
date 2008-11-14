package org.iotools.streams;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class is responsible of instantiating the right executor given an
 * ExecutionModel
 * 
 * TODO: Should return an executorService for joins
 * 
 * @author Davide Simonetti
 * 
 */
public final class ExecutorServiceFactory {

	private static class OneShotThreadExecutor implements ExecutorService {
		private final ExecutorService exec = Executors
				.newSingleThreadExecutor();

		public boolean awaitTermination(final long timeout, final TimeUnit unit)
				throws InterruptedException {
			return this.exec.awaitTermination(timeout, unit);
		}

		public void execute(final Runnable command) {
			this.exec.execute(command);
			shutdown();
		}

		public <T> List<Future<T>> invokeAll(final Collection<Callable<T>> tasks)
				throws InterruptedException {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> List<Future<T>> invokeAll(
				final Collection<Callable<T>> tasks, final long timeout,
				final TimeUnit unit) throws InterruptedException {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> T invokeAny(final Collection<Callable<T>> tasks)
				throws InterruptedException, ExecutionException {

			return null;
		}

		public <T> T invokeAny(final Collection<Callable<T>> tasks,
				final long timeout, final TimeUnit unit)
				throws InterruptedException, ExecutionException,
				TimeoutException {
			// TODO Auto-generated method stub
			return null;
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

		public <T> Future<T> submit(final Callable<T> task) {
			final Future<T> result = this.exec.submit(task);
			shutdown();
			return result;
		}

		public Future<?> submit(final Runnable task) {
			return this.exec.submit(task);
		}

		public <T> Future<T> submit(final Runnable task, final T result) {
			return this.exec.submit(task, result);
		}

	}

	private static ExecutorService EXECUTOR = new ThreadPoolExecutor(10, 20, 5,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));

	private static ExecutorService SINGLE_EXECUTOR = Executors
			.newSingleThreadExecutor();

	public static ExecutorService getExecutor(final ExecutionModel tmodel) {
		final ExecutorService result;
		switch (tmodel) {
		case THREAD_PER_INSTANCE:
			result = new OneShotThreadExecutor();
			break;
		case STATIC_THREAD_POOL:
			result = ExecutorServiceFactory.EXECUTOR;
			break;
		case SINGLE_THREAD:
			result = ExecutorServiceFactory.SINGLE_EXECUTOR;
			break;

		default:
			throw new UnsupportedOperationException("ExecutionModel [" + tmodel
					+ "] not supported");
		}
		return result;
	}

	public static void setDefaultExecutor(final ExecutorService executor) {
		ExecutorServiceFactory.EXECUTOR = executor;
	}
}