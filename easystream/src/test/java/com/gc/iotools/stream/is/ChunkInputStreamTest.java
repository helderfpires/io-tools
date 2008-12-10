package com.gc.iotools.stream.is;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ChunkInputStreamTest {
	@Test
	public void testIncompleteMarkerInEnd() throws IOException {
		doTest("0123456789", "0", "890", "123456789");
	}

	@Test
	public void testNoEndMarker() throws IOException {
		doTest("012345678", "67", null, "8");
		doTest("012345678", "0", null, "12345678");
	}

	@Test
	public void testNoStartMarker() throws IOException {
		doTest("0123456789", null, "67", "012345");
	}

	@Test
	public void testOneByteEndMarker() {

	}

	@Test
	public void testOneByteStartMarker() {

	}

	@Test
	public void testReadMultiple() throws IOException {
		doTest("01st23en45st67en st89", "st", "en", "236789");
	}

	@Test
	public void testStartMarkerNotFound() throws IOException {
		doTest("01st23en45st67enst89", "notFound", "en", "");
	}

	private void doTest(final String base, final String start,
			final String end, final String expected) throws IOException {
		ChunkInputStream chIs = getStream(base, start, end);
		String str = IOUtils.toString(chIs);
		assertEquals("Method read(buf,int,int)", expected, str);
		chIs = getStream(base, start, end);
		str = new String(readWithSingleByte(chIs));
		assertEquals("Method read()", expected, str);
	}

	private ChunkInputStream getStream(final String referencestr,
			final String start, final String end) {
		final InputStream reference = new ByteArrayInputStream(referencestr
				.getBytes());

		byte[] startb = null;
		if (start != null) {
			startb = start.getBytes();
		}

		byte[] endb = null;
		if (end != null) {
			endb = end.getBytes();
		}

		final ChunkInputStream chIs = new ChunkInputStream(startb, endb,
				reference);
		return chIs;
	}

	private byte[] readWithSingleByte(final InputStream is) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int n = 0;
		while ((n = is.read()) >= 0) {
			baos.write(n);
		}
		return baos.toByteArray();
	}

}
