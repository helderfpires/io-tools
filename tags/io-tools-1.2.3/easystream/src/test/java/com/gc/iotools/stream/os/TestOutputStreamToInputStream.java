package com.gc.iotools.stream.os;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.is.BigDocumentIstream;

public class TestOutputStreamToInputStream {

	/**
	 * Benchmarks
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		copy(1024, 1024);
		System.out.println("-----------");
		copy(1024 * 1024, 1024);
		copy(1024 * 1024 * 1024, 1024);
		copy(1024 * 1024, 32768);
		copy(1024 * 1024 * 1024, 32768);
	}

	private static void copy(long fileLength, final int bufSize)
			throws Exception {
		long startTime = System.currentTimeMillis();
		OutputStreamToInputStream.setDefaultPipeSize(bufSize);
		final OutputStreamToInputStream<Void> osisA = new OutputStreamToInputStream<Void>(
				true, ExecutionModel.THREAD_PER_INSTANCE) {
			@Override
			protected Void doRead(InputStream istream) throws Exception {
				copyLarge(istream, new NullOutputStream(), bufSize);
				return null;
			}
		};

		copyLarge(new BigDocumentIstream(fileLength), osisA, bufSize);
		osisA.close();

		System.out.println("bufSize:" + bufSize + " bytes: " + fileLength
				+ " time:" + (System.currentTimeMillis() - startTime));
	}

	private static long copyLarge(InputStream input, OutputStream output,
			int bsize) throws IOException {
		byte[] buffer = new byte[bsize * 2];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	@org.junit.Test
	public void testLong() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);
		final OutputStreamToInputStream<String> osisA = new OutputStreamToInputStream<String>(
				true, es) {
			@Override
			protected String doRead(final InputStream istream)
					throws Exception {
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
		} catch (final IOException e) {
			assertEquals("Wrapped exception", TimeoutException.class, e
					.getCause().getClass());
		}
		assertEquals("Active Threads", 0, es.getActiveCount());
	}

	@org.junit.Test
	public void testReadException() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);
		final OutputStreamToInputStream<Object> osisA = new OutputStreamToInputStream<Object>(
				true, es) {
			@Override
			protected Object doRead(final InputStream istream)
					throws Exception {
				Thread.sleep(500);
				throw new IllegalStateException("testException");
			}
		};
		byte[] test = new byte[32768];
		osisA.write(test);
		try {
			osisA.write(test);
			fail("Exception must be thrown");
		} catch (final IOException e) {
			assertTrue("Real exception has been wrapped",
					(e.getCause() instanceof IllegalStateException));
		}
		Thread.sleep(600);
		assertEquals("Thread count", 0, es.getActiveCount());
	}

	@org.junit.Test
	public void testIncompleteRead() throws Exception {
		final OutputStreamToInputStream<String> oStream2IStream = new OutputStreamToInputStream<String>(
				true, ExecutionModel.SINGLE_THREAD) {
			@Override
			protected String doRead(final InputStream istream) {
				return "end";
			}
		};

		final byte[] test = new byte[32768];
		try {
			oStream2IStream.write(test);
		} finally {
			// don't miss the close (or a thread would not terminate correctly).
			oStream2IStream.close();
		}
		assertEquals("Results are returned", "end", oStream2IStream
				.getResults());
	}

	@org.junit.Test
	public void testResult() throws Exception {
		final OutputStreamToInputStream<String> oStream2IStream = new OutputStreamToInputStream<String>(
				true, ExecutionModel.SINGLE_THREAD) {
			@Override
			protected String doRead(final InputStream istream)
					throws Exception {
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
