package com.gc.iotools.stream.os;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CloseOnceOutputStreamTest {

	@Test
	public void testCloseOnceOutputStream() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final CloseOnceOutputStream<ByteArrayOutputStream> diagnosticStream = new CloseOnceOutputStream<ByteArrayOutputStream>(
				baos);
		CloseOnceOutputStream<CloseOnceOutputStream<ByteArrayOutputStream>> test = new CloseOnceOutputStream<CloseOnceOutputStream<ByteArrayOutputStream>>(
				diagnosticStream);
		test.write("test".getBytes());
		test.close();
		test.close();
		assertEquals("Close called twice", 2, test.getCloseCount());
		assertEquals("Close called once on diagnostic", 1,
				diagnosticStream.getCloseCount());
		assertArrayEquals("bytes were written","test".getBytes(), baos.toByteArray());
	}

}
