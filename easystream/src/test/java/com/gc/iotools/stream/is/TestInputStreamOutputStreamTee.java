package com.gc.iotools.stream.is;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

public class TestInputStreamOutputStreamTee extends TestCase {
	@org.junit.Test
	public void testReadAtSpecificPosition() throws Exception {
		final byte[] referenceBytes = "123".getBytes();
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				referenceBytes);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final InputStream teeStream = new TeeInputStreamOutputStream(bais, baos);
		final byte[] readBytes = new byte[512];
		Arrays.fill(readBytes, ((byte) 0));
		teeStream.read(readBytes, 1, 256);
		assertTrue("Array uguali", Arrays.equals(referenceBytes, baos
				.toByteArray()));
	}

	@org.junit.Test
	public void testReadWrite() throws Exception {
		final byte[] testBytes = "testString".getBytes();
		final InputStream istream = new ByteArrayInputStream(testBytes);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final InputStream teeStream = new TeeInputStreamOutputStream(istream,
				bos);
		IOUtils.copy(teeStream, new NullOutputStream());
		teeStream.close();
		bos.toByteArray();
	}

	@org.junit.Test
	public void testSuddenClose() throws Exception {
		final byte[] testBytes = "testString".getBytes();
		final InputStream istream = new ByteArrayInputStream(testBytes);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final InputStream teeStream = new TeeInputStreamOutputStream(istream,
				bos);
		teeStream.close();
		final byte[] result = bos.toByteArray();
		assertTrue("Arrays equal", Arrays.equals(testBytes, result));
	}
}
