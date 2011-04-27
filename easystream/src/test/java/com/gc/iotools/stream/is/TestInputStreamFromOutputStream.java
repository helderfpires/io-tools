package com.gc.iotools.stream.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestInputStreamFromOutputStream {
	@Test
	public void testExceptionRead() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final InputStreamFromOutputStream<Void> isos = new InputStreamFromOutputStream<Void>(
				es) {
			@Override
			public Void produce(final OutputStream ostream) throws Exception {
				ostream.write("test".getBytes());
				throw new Exception("Test Exception");
			}
		};
		try {
			IOUtils.toByteArray(isos);
			fail("Exception must be thrown");
		} catch (final IOException e) {
			Thread.sleep(600);
			assertEquals("Active Trheads", 0, es.getActiveCount());
		}

	}

	@Test
	public void testHugeDocument() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final InputStreamFromOutputStream<Void> isos = new InputStreamFromOutputStream<Void>(
				es) {
			@Override
			public Void produce(final OutputStream ostream) throws Exception {
				final byte[] buffer = new byte[65536];
				for (int i = 0; i < 255; i++) {
					Arrays.fill(buffer, (byte) i);
					ostream.write(buffer);
				}
				return null;
			}
		};

		int i = 0;

		while ((isos.read()) >= 0) {
			i++;
		}
		isos.close();
		assertEquals("Bytes read", 65536 * 255, i);
		Thread.sleep(100);
		assertEquals("Active Trheads", 0, es.getActiveCount());
	}

	@Test
	public void testNotClosed() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		InputStreamFromOutputStream<Void> isos = new InputStreamFromOutputStream<Void>(
				es) {
			@Override
			public Void produce(final OutputStream ostream) throws Exception {
				while (true) {
					ostream.write("test".getBytes());
				}
			}
		};
		final byte[] b = new byte[255];
		isos.read(b);
		assertEquals("Active threads", 1, es.getActiveCount());
		// cleanup threads
		isos.close();
		isos = null;
		Thread.sleep(2000);
		assertEquals("Active threads", 0, es.getActiveCount());
	}

	@Test
	public void testProduce() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final InputStreamFromOutputStream<String> isos = new InputStreamFromOutputStream<String>(
				es) {
			@Override
			protected String produce(final OutputStream ostream)
					throws Exception {
				ostream.write("test".getBytes());
				return "return";
			}
		};
		final byte[] b = new byte[255];
		final int n = isos.read(b);
		assertEquals("byte letti", 4, n);
		assertEquals("string read", "test", new String(b).substring(0, n));
		isos.close();
		assertEquals("Return value", "return", isos.getResult());
		Thread.sleep(1000);
		assertEquals("Active threads ", 0, es.getActiveCount());
	}

	private class MyISOS extends InputStreamFromOutputStream<String> {
		private String variableToInitialize = "notInitialized";

		public MyISOS() {
			super();
			try {
				//some lengthly operation
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.variableToInitialize = "initialized";
		}

		@Override
		protected String produce(OutputStream sink) throws Exception {
			return this.variableToInitialize;
		}

	}

	/**
	 * Tests that the constructor completes before the produce is called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExplicitSubclassing() throws Exception {
		InputStreamFromOutputStream<String> isos = new MyISOS();
		isos.close();
		assertEquals("method produce was called before "
				+ "the constructor end.", "initialized", isos.getResult());
	}

	@Test
	public void testSlowProducer() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final InputStreamFromOutputStream<Void> isos = new InputStreamFromOutputStream<Void>(
				es) {
			@Override
			public Void produce(final OutputStream ostream) throws Exception {
				final byte[] buffer = new byte[256];
				for (int i = 0; i < 10; i++) {
					Arrays.fill(buffer, (byte) i);
					Thread.sleep(100);
					ostream.write(buffer);
				}
				return null;
			}
		};

		int i = 0;

		while ((isos.read()) >= 0) {
			i++;
		}
		isos.close();
		assertEquals("Bytes read", 10 * 256, i);
		Thread.sleep(1000);
		assertEquals("Active Threads", 0, es.getActiveCount());
	}
}
