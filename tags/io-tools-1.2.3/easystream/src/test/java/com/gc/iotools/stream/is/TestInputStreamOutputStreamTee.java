package com.gc.iotools.stream.is;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
	public void testMultipleStreams() throws Exception {
		final BigDocumentIstream bis = new BigDocumentIstream(131072);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final ByteArrayOutputStream bos[] = { new ByteArrayOutputStream(),
				new ByteArrayOutputStream(), new ByteArrayOutputStream() };
		final  TeeInputStreamOutputStream teeStream = new TeeInputStreamOutputStream(bis, true, bos);
		teeStream.close();
		for (ByteArrayOutputStream byteArrayOutputStream : bos) {
			final byte[] result = byteArrayOutputStream.toByteArray();
			assertArrayEquals("Arrays equal", reference, result);			
		}
		long[] wtime=teeStream.getWriteTime();
		assertEquals("array length",3,wtime.length);
//		for (long l : wtime) {
//			assertTrue("Time ["+l+"] >0",l>0);
//		}
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
