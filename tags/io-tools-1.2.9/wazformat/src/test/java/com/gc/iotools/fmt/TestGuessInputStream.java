package com.gc.iotools.fmt;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;

public class TestGuessInputStream extends JUnit4Mockery {

	@org.junit.Test
	public void testRecursion() throws IOException {
		final InputStream istream = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final byte[] reference = IOUtils.toByteArray(istream);
		
		final InputStream istream2 = TestGuessInputStream.class
				.getResourceAsStream("/testFiles/test_pdf.pdf.b64");
		final GuessInputStream gis = GuessInputStream.getInstance(istream2,
				new FormatEnum[] { FormatEnum.BASE64, FormatEnum.PDF });
		gis.setIdentificationDepth(3);
		assertEquals("Format detected", FormatEnum.BASE64, gis.getFormat());
		assertTrue("Bytes read are same", Arrays.equals(reference, IOUtils
				.toByteArray(gis)));
		assertEquals("Detected formats", 2, gis.getFormats().length);
		assertArrayEquals("Formats", new FormatEnum[] { FormatEnum.BASE64,
				FormatEnum.PDF }, gis.getFormats());
		gis.close();
	}

	@org.junit.Test
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
		// assertTrue("inputstream is wrapped",
		// gis2 instanceof GuessInputStreamWrapper);
		gis2.close();
	}

	@org.junit.Test
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
		gis.close();
	}

	@org.junit.Test
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
		gis.close();
	}

	@org.junit.Test
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
		gis.close();
	}

	@org.junit.Test
	public void testDecode() throws IOException {
		final byte[] reference = "<xml>this is xml</xml>".getBytes();
		final InputStream istream = new Base64InputStream(
				new ByteArrayInputStream(reference), true);
		//System.out.println(IOUtils.toString(istream));
		final GuessInputStream gis = GuessInputStream.getInstance(istream,
				new FormatEnum[] { FormatEnum.BASE64 });
		gis.decode(true);
		assertEquals("Format detected", FormatEnum.BASE64, gis.getFormat());
		assertArrayEquals("Bytes read are same", reference, IOUtils
				.toByteArray(gis));
		try {
			gis.decode(false);
			fail("Illegal State exception must be thrown here");
		} catch (IllegalStateException e) {
			//EVERYTHING OK
		}
		gis.close();
	}

	@org.junit.Test
	public void testMaxRecursion() throws Exception {
		final byte[] reference = "<xml>this is xml</xml>".getBytes();
		final InputStream istream = new Base64InputStream(
				new Base64InputStream(new ByteArrayInputStream(reference),
						true), true);
		GuessInputStream gis = GuessInputStream.getInstance(istream,
				new FormatEnum[] { FormatEnum.BASE64, FormatEnum.XML });
		gis.setIdentificationDepth(1);
		gis.decode(true);
		assertArrayEquals("Recursion level 1",
				new FormatEnum[] { FormatEnum.BASE64 }, gis.getFormats());
		gis.setIdentificationDepth(2);
		assertArrayEquals("Recursion level 2", new FormatEnum[] {
				FormatEnum.BASE64, FormatEnum.BASE64 }, gis.getFormats());
		gis.setIdentificationDepth(3);
		assertArrayEquals("Recursion level 3", new FormatEnum[] {
				FormatEnum.BASE64, FormatEnum.BASE64, FormatEnum.XML }, gis
				.getFormats());
		gis.setIdentificationDepth(4);
		assertArrayEquals("Recursion level 4", new FormatEnum[] {
				FormatEnum.BASE64, FormatEnum.BASE64, FormatEnum.XML }, gis
				.getFormats());
		byte[] bytes = IOUtils.toByteArray(gis);
		assertArrayEquals("Content ", reference, bytes);
		gis.close();
	}

	@org.junit.Test
	public void testRecursiveDecode() {
		//TODO
	}

	@org.junit.Test
	public void testSequenceReduction() throws IOException {
		final DetectionLibrary detect1 = mock(DetectionLibrary.class,
				"detect1");
		final DetectionLibrary detect2 = mock(DetectionLibrary.class,
				"detect2");
		checking(new Expectations() {
			{
				// first detector can detect two formats
				allowing(detect1).getDetectedFormats();
				will(returnValue(new FormatEnum[] { FormatEnum.AVI,
						FormatEnum.BASE64 }));
				allowing(detect1).detect(with(any(FormatEnum[].class)),
						with(any(ResettableInputStream.class)));
				will(returnValue(new FormatId(FormatEnum.UNKNOWN, null)));

				/*
				 * second detector detects a format already detected by the
				 * first one
				 */
				allowing(detect2).getDetectedFormats();
				will(returnValue(new FormatEnum[] { FormatEnum.AVI }));

				never(detect2).detect(with(any(FormatEnum[].class)),
						with(any(ResettableInputStream.class)));
			}
		});
		ByteArrayInputStream bais = new ByteArrayInputStream("test"
				.getBytes());
		GuessInputStream gis = GuessInputStream.getInstance(bais,
				new FormatEnum[] { FormatEnum.AVI, FormatEnum.BASE64 },
				new DetectionLibrary[] { detect1, detect2 }, null);
		assertEquals("Format ", FormatEnum.UNKNOWN, gis.getFormat());
		gis.close();
	}

	@org.junit.Test
	public void testNotNeedNotCalled() throws IOException {
		final DetectionLibrary detect1 = mock(DetectionLibrary.class);

		checking(new Expectations() {
			{
				// detects a format who is not needed by the detection process
				allowing(detect1).getDetectedFormats();
				will(returnValue(new FormatEnum[] { FormatEnum.AVI }));
				// assert it is not called.
				never(detect1).detect(with(any(FormatEnum[].class)),
						with(any(ResettableInputStream.class)));
			}
		});
		ByteArrayInputStream bais = new ByteArrayInputStream("test"
				.getBytes());
		GuessInputStream gis = GuessInputStream.getInstance(bais,
				new FormatEnum[] { FormatEnum.BASE64 },
				new DetectionLibrary[] { detect1 }, null);
		assertEquals("Format ", FormatEnum.UNKNOWN, gis.getFormat());
		gis.close();
	}
}
