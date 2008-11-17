package org.googlecode.iotools.streams;

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
import org.googlecode.iotools.streams.ExecutionModel;
import org.googlecode.iotools.streams.OutputStreamToInputStream;

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

	@org.junit.Test
	public void testReadException() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);
		final OutputStreamToInputStream<Object> osisA = new OutputStreamToInputStream<Object>(
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

	@org.junit.Test
	public void testResult() throws Exception {
		final OutputStreamToInputStream<String> oStream2IStream = new OutputStreamToInputStream<String>(
				true, ExecutionModel.SINGLE_THREAD) {
			@Override
			protected String doRead(final InputStream istream) throws Exception {
				// read from InputStream into a string
				final String result = IOUtils.toString(istream);
				return result + " was processed.";
			}
		};

		final String testString = "test";
		try {
			oStream2IStream.write(testString.getBytes());
		} finally {
			// don't miss the close (or a thread would not terminate correctly).
			oStream2IStream.close();
		}
		assertEquals("Results are returned", "test was processed.",
				oStream2IStream.getResults());
	}

}
