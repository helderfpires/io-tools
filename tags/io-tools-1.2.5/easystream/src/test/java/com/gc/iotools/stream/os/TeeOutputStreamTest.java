package com.gc.iotools.stream.os;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

public class TeeOutputStreamTest {
	@Test
	public void testStandardTee() throws Exception{
		byte[] test = "test".getBytes();
		InputStream source = new ByteArrayInputStream(test);
		ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		ByteArrayOutputStream destination2 = new ByteArrayOutputStream();

		TeeOutputStream tee = new TeeOutputStream(destination1, destination2);
		org.apache.commons.io.IOUtils.copy(source, tee);
		tee.close();

		assertArrayEquals("the two arrays are equals", test, destination1
				.toByteArray());
		assertArrayEquals("the two arrays are equals", test, destination2
				.toByteArray());
		assertEquals("byte count", test.length, tee.getSize());
	}
}
