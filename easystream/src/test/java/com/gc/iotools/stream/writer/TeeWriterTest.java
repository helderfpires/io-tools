package com.gc.iotools.stream.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class TeeWriterTest {

	/*
	 * Test the enableCopy(boolean) method disabling the copy for some data.
	 */
	@Test
	public void testEnableCopy() throws Exception {
		final StringWriter destination1 = new StringWriter();
		final StringWriter destination2 = new StringWriter();

		final TeeWriter tee = new TeeWriter(destination1, destination2);
		tee.write("test ");
		// disable copy for all streams
		tee.enableCopy(false);
		tee.write("notwritten");
		tee.enableCopy(true);
		tee.write("writtenAgain");

		final String reference = "test writtenAgain";

		assertEquals("the two arrays are equals", reference,
				destination1.toString());
		assertEquals("reference stream 2", reference, destination2.toString());
		assertTrue("byte count", reference.length() < tee.getSize());
	}

	/*
	 * Test the enableCopy(boolean[]) method disabling the copy of some data
	 * on a specific stream.
	 */
	@Test
	public void testEnableCopyArr() throws Exception {
		final StringWriter destination1 = new StringWriter();
		final StringWriter destination2 = new StringWriter();

		final TeeWriter tee = new TeeWriter(destination1, destination2);
		tee.write("test ");
		// disable copy for first wrapped stream
		tee.enableCopy(new boolean[] { false, true });
		tee.write("|disabled data |");
		// enable copy again on all.
		tee.enableCopy(true);
		tee.write("written Again");
		tee.close();

		// first stream string does not contain the disabled data
		final String reference = "test written Again";
		assertEquals("first stream string "
				+ "does not contain the disabled data", reference,
				destination1.toString());

		// second stream contains full data
		final String reference2 = "test |disabled data |written Again";
		assertEquals("second stream contains full data", reference2,
				destination2.toString());
		// all the bytes written are counted
		assertEquals("byte count", reference2.length(), tee.getSize());
	}

	/*
	 * Tests the TeeWriter standard usage.
	 */
	@Test
	public void testStandardTee() throws Exception {
		final String reference = "test";
		final Reader source = new StringReader(reference);
		final StringWriter destination1 = new StringWriter();
		final StringWriter destination2 = new StringWriter();

		final TeeWriter tee = new TeeWriter(destination1, destination2);
		org.apache.commons.io.IOUtils.copy(source, tee);
		tee.close();

		assertEquals("the two string are equals", reference,
				destination1.toString());
		assertEquals("the two string are equals", reference,
				destination2.toString());
		assertEquals("byte count", reference.length(), tee.getSize());
	}

	/*
	 * Tests the TeeWriter write metods.
	 */
	@Test
	public void testVariousWriteMethods() throws Exception {
		final String reference = "test";
		final StringWriter destination1 = new StringWriter();

		final TeeWriter tee = new TeeWriter(destination1);
		// "t"
		tee.write(reference.charAt(0));
		// "est"
		tee.write(reference, 1, 3);
		assertEquals("the two string are equals", reference,
				destination1.toString());
		assertEquals("byte count", reference.length(), tee.getSize());
	}
}
