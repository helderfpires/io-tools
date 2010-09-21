package com.gc.iotools.stream.os;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

public class TeeOutputStreamTest {
	@Test
	public void testDisable() throws Exception {

		final ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream destination2 = new ByteArrayOutputStream();

		final TeeOutputStream tee = new TeeOutputStream(destination1,
				destination2);
		tee.write("test ".getBytes());
		tee.enableCopy(false);
		tee.write("notwritten".getBytes());
		tee.enableCopy(true);
		tee.write("writtenAgain".getBytes());

		final byte[] reference = "test writtenAgain".getBytes();
		assertArrayEquals("the two arrays are equals", reference,
				destination1.toByteArray());
		assertArrayEquals("the two arrays are equals", reference,
				destination2.toByteArray());
		assertEquals("byte count", reference.length, tee.getSize());
	}

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
