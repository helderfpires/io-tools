package com.gc.iotools.fmt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.FormatEnum;

public class TestGuessInputStream extends TestCase {

	public void testDecoder() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final GuessInputStream gis = GuessInputStream.getInstance(istream2,
				new FormatEnum[] { FormatEnum.BASE64, FormatEnum.PDF });
		assertEquals("Format detected", FormatEnum.BASE64, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 2, gis.getFormats().length);
		assertEquals("Format [0]", FormatEnum.BASE64, gis.getFormats()[0]);
		assertEquals("Format [1]", FormatEnum.PDF, gis.getFormats()[1]);
	}

	public void testDoubleWrap() throws IOException {
		final InputStream istream = new ByteArrayInputStream(
				"<xml>this is xml</xml>".getBytes());
		final byte[] reference = IOUtils.toByteArray(istream);
		istream.reset();
		final GuessInputStream gis1 = GuessInputStream.getInstance(istream);
		assertEquals("Format detected", FormatEnum.XML, gis1.getFormat());

		final GuessInputStream gis2 = GuessInputStream.getInstance(gis1,
				new FormatEnum[] { FormatEnum.PDF });

		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis2)));
		assertTrue("inputstream is wrapped",
				gis2 instanceof GuessInputStreamWrapper);
	}

	public void testEnabledFormat() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final GuessInputStream gis = GuessInputStream.getInstance(istream2,
				new FormatEnum[] { FormatEnum.PDF });
		assertEquals("Format detected", FormatEnum.PDF, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 1, gis.getFormats().length);

	}

	public void testNotEnabledFormat() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final byte[] reference = IOUtils.toByteArray(istream);

		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf");
		final GuessInputStream gis = GuessInputStream.getInstance(istream2,
				new FormatEnum[] { FormatEnum.XML });
		assertEquals("Format detected", FormatEnum.UNKNOWN, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 1, gis.getFormats().length);
	}

	public void testShortFile() throws IOException {
		final InputStream istream = new ByteArrayInputStream(
				"<xml>this is xml</xml>".getBytes());
		final byte[] reference = IOUtils.toByteArray(istream);
		istream.reset();
		final GuessInputStream gis = GuessInputStream.getInstance(istream,
				new FormatEnum[] { FormatEnum.XML });
		assertEquals("Format detected", FormatEnum.XML, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
	}
}
