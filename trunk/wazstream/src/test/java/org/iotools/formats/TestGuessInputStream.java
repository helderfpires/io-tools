package org.iotools.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.iotools.formats.base.FormatEnum;
import org.junit.Test;

public class TestGuessInputStream {
	@Test
	public void testDecoder() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final GuessInputStream gis = new GuessInputStream(istream2,
				new FormatEnum[] { FormatEnum.BASE64, FormatEnum.PDF });
		assertEquals("Format detected", FormatEnum.BASE64, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 2, gis.getFormats().length);
		assertEquals("Format [0]", FormatEnum.BASE64, gis.getFormats()[0]);
		assertEquals("Format [1]", FormatEnum.PDF, gis.getFormats()[1]);
	}

	@Test
	public void testEnabledFormat() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final GuessInputStream gis = new GuessInputStream(istream2,
				new FormatEnum[] { FormatEnum.PDF });
		assertEquals("Format detected", FormatEnum.PDF, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 1, gis.getFormats().length);

	}

	@Test
	public void testNotEnabledFormat() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final GuessInputStream gis = new GuessInputStream(istream2,
				new FormatEnum[] { FormatEnum.XML });
		assertEquals("Format detected", FormatEnum.UNKNOWN, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 1, gis.getFormats().length);
	}

	@Test
	public void testShortFile() throws IOException {
		final InputStream istream = new ByteArrayInputStream(
				"<xml>this is xml</xml>".getBytes());
		final byte[] reference = IOUtils.toByteArray(istream);
		istream.reset();
		final GuessInputStream gis = new GuessInputStream(istream,
				new FormatEnum[] { FormatEnum.XML });
		assertEquals("Format detected", FormatEnum.XML, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
	}
}
