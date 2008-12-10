package com.gc.iotools.stream.base;

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

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;

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

	private static Executor QUEUED_EXECUTOR = new QueuedExecutor();

	private static Executor POOLED_EXECUTOR = new PooledExecutor(20);

	private static Executor THREAD_PER_INSTANCE = new ThreadedExecutor();

	public static Executor getExecutor(final ExecutionModel tmodel) {
		final Executor result;
		switch (tmodel.getValue()) {
		case ExecutionModel.THREAD_PER_INSTANCE_INT:
			result = ExecutorServiceFactory.THREAD_PER_INSTANCE;
			break;
		case ExecutionModel.STATIC_THREAD_POOL_INT:
			result = ExecutorServiceFactory.POOLED_EXECUTOR;
			break;
		case ExecutionModel.SINGLE_THREAD_INT:
			result = ExecutorServiceFactory.QUEUED_EXECUTOR;
			break;

		default:
			throw new UnsupportedOperationException("ExecutionModel [" + tmodel
					+ "] not supported");
		}
		return result;
	}

	public static void setDefaultExecutor(final Executor executor) {
		POOLED_EXECUTOR = executor;
	}
}