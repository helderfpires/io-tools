package com.gc.iotools.stream.reader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestReaderFromWriter {
	@Test
	public void testExceptionRead() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final ReaderFromWriter<Void> isos = new ReaderFromWriter<Void>(es) {
			@Override
			public Void produce(final Writer ostream) throws Exception {
				ostream.write("test");
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

		final ReaderFromWriter<Void> isos = new ReaderFromWriter<Void>(es) {
			@Override
			public Void produce(final Writer ostream) throws Exception {
				final char[] buffer = new char[65536];
				for (int i = 0; i < 255; i++) {
					Arrays.fill(buffer, 'i');
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

		ReaderFromWriter<Void> isos = new ReaderFromWriter<Void>(es) {
			@Override
			public Void produce(final Writer ostream) throws Exception {
				while (true) {
					ostream.write("test");
				}
			}
		};
		final char[] b = new char[255];
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

		final ReaderFromWriter<String> isos = new ReaderFromWriter<String>(es) {
			@Override
			protected String produce(final Writer ostream) throws Exception {
				ostream.write("test");
				return "return";
			}
		};
		final char[] b = new char[255];
		final int n = isos.read(b);
		assertEquals("byte letti", 4, n);
		assertEquals("string read", "test", new String(b).substring(0, n));
		isos.close();
		assertEquals("Return value", "return", isos.getResult());
		Thread.sleep(1000);
		assertEquals("Active threads ", 0, es.getActiveCount());
	}

	@Test
	public void testSlowProducer() throws Exception {
		final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(1);

		final ReaderFromWriter<Void> isos = new ReaderFromWriter<Void>(es) {
			@Override
			public Void produce(final Writer ostream) throws Exception {
				final char[] buffer = new char[256];
				for (int i = 0; i < 10; i++) {
					Arrays.fill(buffer, 'i');
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
