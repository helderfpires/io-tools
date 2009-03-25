package com.gc.iotools.stream.is;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

public class TestInputStreamOutputStreamTee {
	@org.junit.Test
	public void testMarkAndReset() throws Exception {
		final BigDocumentIstream bis = new BigDocumentIstream(131072);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final InputStream teeStream = new TeeInputStreamOutputStream(bis, bos);
		teeStream.read(new byte[5]);
		teeStream.mark(50);
		teeStream.read(new byte[25]);
		teeStream.reset();
		teeStream.close();
		final byte[] result = bos.toByteArray();
		assertArrayEquals("Arrays equal", reference, result);
	}

	@org.junit.Test
	public void testReadAtSpecificPosition() throws Exception {
		final byte[] referenceBytes = "123".getBytes();
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				referenceBytes);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final InputStream teeStream = new TeeInputStreamOutputStream(bais,
				baos);
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
		assertArrayEquals("Arrays equal", testBytes, result);
	}
}
