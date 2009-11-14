package com.gc.iotools.stream.is;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ChunkInputStreamTest {
	@Test
	public void testBufferNotModified() throws IOException {
		final ChunkInputStream chis = getStream("0123456789", "0", "2",
				false, true);
		final byte[] bytes = new byte[5];
		Arrays.fill(bytes, (byte) 1);
		chis.read(bytes, 1, bytes.length - 1);
		final byte[] reference = new byte[] { 1, "1".getBytes()[0], 1, 1, 1 };
		assertArrayEquals("read buffer", reference, bytes);
	}

	@Test
	public void testIncompleteMarkerInEnd() throws IOException {
		doTest("0123456789", "0", "890", "123456789");
	}

	@Test
	public void testNoAutomaticFetch() throws IOException {
		final ChunkInputStream chis = getStream("012st3en45st67en   st89",
				"st", "en", false, false);
		final byte[][] reference = new byte[][] { "3".getBytes(),
				"67".getBytes(), "89".getBytes() };
		int i = 0;
		final int n1 = chis.read(new byte[5], 0, 5);
		assertEquals("stream not initialized", -1, n1);
		while (chis.fetchNextChunk()) {
			final byte[] read = IOUtils.toByteArray(chis);
			assertArrayEquals("bytes read", reference[i++], read);
		}
		final ChunkInputStream chis1 = getStream("012st3en45st67en   st89",
				"st", "en", false, false);
		final int rd = chis1.read();
		assertEquals("stream not initialized", -1, rd);

		i = 0;
		while (chis1.fetchNextChunk()) {
			int pos = 0;
			int n = 0;
			while ((n = chis1.read()) > 0) {
				assertEquals("read byte pos[" + pos + "] chunk [" + i + "]",
						reference[i][pos], (byte) n);
				pos++;
			}
			i++;
		}
	}

	// Not implemented yet
	// @Test
	// public void testIncludeMarkers() throws IOException {
	// doTest("0123456789", null, "67", "012345");
	// }

	@Test
	public void testNoEndMarker() throws IOException {
		doTest("012345678", "67", null, "8");
		doTest("012345678", "0", null, "12345678");
	}

	@Test
	public void testNoStartMarker() throws IOException {
		final ChunkInputStream chis = getStream("0123en4567en89", null, "en",
				false, false);
		final byte[][] reference = new byte[][] { "0123".getBytes(),
				"4567".getBytes(), "89".getBytes() };
		int i = 0;
		final int n1 = chis.read(new byte[5], 0, 5);
		assertEquals("stream not initialized", -1, n1);
		while (chis.fetchNextChunk()) {
			final byte[] read = IOUtils.toByteArray(chis);
			assertArrayEquals("bytes read", reference[i++], read);
		}
	}

	@Test
	public void testNoZeroLenghtRead() throws IOException {
		final ChunkInputStream chis = getStream("0123456789", "7", "8",
				false, true);
		final byte[] bytes = new byte[5];
		int ret = chis.read(bytes);
		assertEquals("No zero lenght read", -1, ret);
		final ChunkInputStream chis2 = getStream("01234567879", "7", "8",
				false, true);
		ret = chis2.read(bytes);
		assertEquals("No zero lenght read", 1, ret);
	}

	@Test
	public void testReadMultiple() throws IOException {
		doTest("01st23en45st67en st89", "st", "en", "236789");
	}

	@Test
	public void testStandard() throws IOException {
		final ChunkInputStream chunkIs = new ChunkInputStream(
				ChunkInputStreamTest.class.getResourceAsStream("test.xml"),
				"<simpledocument>".getBytes(),
				"</simpledocument>".getBytes(), false, true);
		final String decoded = IOUtils.toString(chunkIs).trim();
		assertTrue("doc [" + decoded + "] starts with <document>", decoded
				.startsWith("<document>"));
		assertTrue("doc [" + decoded + "] ends with </document>", decoded
				.endsWith("</document>"));
	}

	@Test
	public void testStartMarkerNotFound() throws IOException {
		doTest("01st23en45st67enst89", "notFound", "en", "");
	}

	private void doTest(final String base, final String start,
			final String end, final String expected) throws IOException {
		ChunkInputStream chIs = getStream(base, start, end, false, true);
		String str = IOUtils.toString(chIs);
		assertEquals("Method read(buf,int,int)", expected, str);
		chIs = getStream(base, start, end, false, true);
		str = new String(readWithSingleByte(chIs));
		assertEquals("Method read()", expected, str);
	}

	private ChunkInputStream getStream(final String referencestr,
			final String start, final String end, final boolean showMarkers,
			final boolean automaticFetch) {
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

		final ChunkInputStream chIs = new ChunkInputStream(reference, startb,
				endb, showMarkers, automaticFetch);
		return chIs;
	}

	private byte[] readWithSingleByte(final InputStream is)
			throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int n = 0;
		while ((n = is.read()) >= 0) {
			baos.write(n);
		}
		return baos.toByteArray();
	}

}
