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
import org.junit.Test;

import com.gc.iotools.stream.base.ExecutionModel;
import com.gc.iotools.stream.is.BigDocumentIstream;

public class TestOutputStreamToInputStream {

	private class MyOsIs extends OutputStreamToInputStream<String>{
		private String variableToInitialize="notInitialized";
		
		public MyOsIs(boolean startImmediately) {
			super(startImmediately);
			try {
				//some lengthly operation
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.variableToInitialize = "initialized";
		}

		@Override
		protected String doRead(InputStream istream) throws Exception {
			return variableToInitialize;
		}
		
	}
	/**
	 * Tests that the constructor completes before the produce is called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExplicitSubclassing() throws Exception {
		//standard case: produce is called after constructor end.
		OutputStreamToInputStream<String> isOs = new MyOsIs(false);
		isOs.close();
		assertEquals("method produce was called before "
				+ "the constructor end.", "initialized", isOs.getResult());
		//optimised case for anonymous subclassing: produce is called before constructor end.
		OutputStreamToInputStream<String> isOs2 = new MyOsIs(true);
		isOs2.close();
		assertEquals("method produce was called before "
				+ "the constructor end.", "notInitialized", isOs2.getResult());
	}

	private static void copy(final long fileLength, final int bufSize)
			throws Exception {
		final long startTime = System.currentTimeMillis();
		OutputStreamToInputStream.setDefaultPipeSize(bufSize);
		final OutputStreamToInputStream<Void> osisA = new OutputStreamToInputStream<Void>(
				true, ExecutionModel.THREAD_PER_INSTANCE) {
			@Override
			protected Void doRead(final InputStream istream) throws Exception {
				copyLarge(istream, new NullOutputStream(), bufSize);
				return null;
			}
		};

		copyLarge(new BigDocumentIstream(fileLength), osisA, bufSize);
		osisA.close();

		System.out.println("bufSize:" + bufSize + " bytes: " + fileLength
				+ " time:" + (System.currentTimeMillis() - startTime));
	}

	private static long copyLarge(final InputStream input,
			final OutputStream output, final int bsize) throws IOException {
		final byte[] buffer = new byte[bsize * 2];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Benchmarks
	 * 
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		copy(1024, 1024);
		System.out.println("-----------");
		copy(1024 * 1024, 1024);
		copy(1024 * 1024 * 1024, 1024);
		copy(1024 * 1024, 32768);
		copy(1024 * 1024 * 1024, 32768);
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
			// don't miss the close (or a thread would not terminate
			// correctly).
			oStream2IStream.close();
		}
		assertEquals("Results are returned", "end",
				oStream2IStream.getResult());
	}

	/**
	 * Tests the timeout of the close method
	 * 
	 * @throws Exception
	 */
	@org.junit.Test
	public void testCloseTimeout() throws Exception {
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
			protected Object doRead(final InputStream istream)
					throws Exception {
				Thread.sleep(500);
				throw new IllegalStateException("testException");
			}
		};
		final byte[] test = new byte[32768];
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
			// don't miss the close (or a thread would not terminate
			// correctly).
			oStream2IStream.close();
		}
		assertEquals("Results are returned", "test was processed.",
				oStream2IStream.getResult());
	}

}
