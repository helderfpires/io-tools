package org.iotools.streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;

public class TestOutputStream {

	@org.junit.Test
	public void testLong() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);
		final OutputStreamToInputStream<String> osisA = new OutputStreamToInputStream<String>(
				true, es) {
			@Override
			protected String doRead(final InputStream istream) throws Exception {
				Thread.sleep(1000);
				final String result = IOUtils.toString(istream);
				return result;
			}
		};

		final String testString = "test test test";
		osisA.write(testString.getBytes());
		try {
			osisA.close(500, TimeUnit.MILLISECONDS);
			fail("Must be a timeoutException");
		} catch (final TimeoutException e) {
			assertEquals("Active Threads", 1, es.getActiveCount());
		}
		Thread.sleep(600);
		assertEquals("Active Threads", 0, es.getActiveCount());
	}

	// public void testNotClosed() throws Exception {
	// while (inUse) {
	// wait(10000);
	// }
	// inUse = true;
	//
	// final OutputStreamToInputStream osisA = new OutputStreamToInputStream() {
	// @Override
	// protected Object doRead(final InputStream istream) throws Exception {
	// final String result = IOUtils.toString(istream);
	// return result;
	// }
	// };
	//
	// final String testString = "test test test";
	// osisA.write(testString.getBytes());
	// Thread.sleep(1000);
	// assertEquals("un cadavere", 1, OutputStreamToInputStream
	// .getActiveThreads());
	// final String threadName = OutputStreamToInputStream
	// .getActiveThreadNames()[0];
	// assertTrue("e' possibile risalire al creatore del thread non morto",
	// threadName.indexOf("testNotClosed") > 0);
	//
	// // cleanup
	// osisA.close();
	// inUse = false;
	// notify();
	// }
	//
	// public synchronized void testRead() throws Exception {
	// while (inUse) {
	// wait(10000);
	// }
	// inUse = true;
	// final OutputStreamToInputStream osisA = new OutputStreamToInputStream() {
	// @Override
	// protected Object doRead(final InputStream istream) throws Exception {
	// assertEquals("thread attivi (possibile esecuzione parallela)",
	// 1, OutputStreamToInputStream.getActiveThreads());
	// final String result = IOUtils.toString(istream);
	// return result;
	// }
	// };
	//
	// final String testString = "test test test";
	// osisA.write(testString.getBytes());
	// final Object result = osisA.getResults();
	// assertEquals("Risultato ", testString, result);
	// assertEquals("nessun cadavere", 0, OutputStreamToInputStream
	// .getActiveThreads());
	// inUse = false;
	// notify();
	// }
	@org.junit.Test
	public void testReadException() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);
		final OutputStreamToInputStream osisA = new OutputStreamToInputStream(
				true, es) {
			@Override
			protected Object doRead(final InputStream istream) throws Exception {
				throw new IllegalStateException("testException");
			}
		};
		osisA.write("test test test".getBytes());

		try {
			osisA.close();
			fail("Exception must be thrown");
		} catch (final IOException e) {
			assertTrue("Real exception has been wrapped",
					(e.getCause() instanceof IllegalStateException));
		}
		assertEquals("Thread count", 0, es.getActiveCount());
	}

}
