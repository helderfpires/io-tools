package com.gc.iotools.stream.os;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

public class TeeOutputStreamTest {

	/*
	 * Test the enableCopy(boolean) method disabling the copy for some data.
	 */
	@Test
	public void testEnableCopy() throws Exception {
		final ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream destination2 = new ByteArrayOutputStream();

		final TeeOutputStream tee = new TeeOutputStream(destination1,
				destination2);
		tee.write("test ".getBytes());
		// disable copy for all streams
		tee.enableCopy(false);
		tee.write("notwritten".getBytes());
		tee.enableCopy(true);
		tee.write("writtenAgain".getBytes());

		final byte[] reference = "test writtenAgain".getBytes();

		assertArrayEquals("the two arrays are equals", reference,
				destination1.toByteArray());
		assertArrayEquals("reference stream 2", reference,
				destination2.toByteArray());
		assertTrue("byte count", reference.length < tee.getSize());
	}

	/*
	 * Test the enableCopy(boolean[]) method disabling the copy of some data
	 * on a specific stream.
	 */
	@Test
	public void testEnableCopyArr() throws Exception {
		final ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream destination2 = new ByteArrayOutputStream();

		final TeeOutputStream tee = new TeeOutputStream(destination1,
				destination2);
		tee.write("test ".getBytes());
		// disable copy for first wrapped stream
		tee.enableCopy(new boolean[] { false, true });
		tee.write("|disabled data |".getBytes());
		// enable copy again on all.
		tee.enableCopy(true);
		tee.write("written Again".getBytes());
		tee.close();

		// first stream string does not contain the disabled data
		final byte[] reference = "test written Again".getBytes();
		assertArrayEquals("first stream string "
				+ "does not contain the disabled data", reference,
				destination1.toByteArray());

		// second stream contains full data
		final byte[] reference2 = "test |disabled data |written Again"
				.getBytes();
		assertArrayEquals("second stream contains full data", reference2,
				destination2.toByteArray());
		// all the bytes written are counted
		assertEquals("byte count", reference2.length, tee.getSize());
	}

	/*
	 * Tests the TeeOutputStream standard usage.
	 */
	@Test
	public void testStandardTee() throws Exception {
		final byte[] test = "test".getBytes();
		final InputStream source = new ByteArrayInputStream(test);
		final ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream destination2 = new ByteArrayOutputStream();

		final TeeOutputStream tee = new TeeOutputStream(destination1,
				destination2);
		org.apache.commons.io.IOUtils.copy(source, tee);
		tee.close();

		assertArrayEquals("the two arrays are equals", test,
				destination1.toByteArray());
		assertArrayEquals("the two arrays are equals", test,
				destination2.toByteArray());
		assertEquals("byte count", test.length, tee.getSize());
	}
}
