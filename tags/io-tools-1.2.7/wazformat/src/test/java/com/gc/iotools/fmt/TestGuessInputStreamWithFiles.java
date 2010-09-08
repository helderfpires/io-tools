package com.gc.iotools.fmt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.TestUtils;
import com.gc.iotools.fmt.decoders.Base64Decoder;
import com.gc.iotools.fmt.detect.droid.TestDroidDetector;

public class TestGuessInputStreamWithFiles {

	private static void checkDetector(final FormatEnum expectedFormat,
			final String[] extensions) throws Exception {
		URL url = TestDroidDetector.class.getResource("/testFiles");
		final String path = url.getPath();
		@SuppressWarnings("unchecked")
		Iterator<File> fiter = FileUtils.iterateFiles(new File(path),
				extensions, false);
		assertTrue("at least one file", fiter.hasNext());
		while (fiter.hasNext()) {
			File file = fiter.next();
			final InputStream is = new FileInputStream(file);
			String fileName = file.getName();
			final GuessInputStream gis = GuessInputStream.getInstance(is);
			assertEquals("file format [" + fileName + "]", expectedFormat,
					gis.getFormat());
			final byte[] reference = IOUtils.toByteArray(new FileInputStream(
					file));
			assertTrue("Read equals reference [" + fileName + "]", Arrays
					.equals(reference, IOUtils.toByteArray(gis)));
			gis.close();
		}
		final String[] badFiles = TestUtils
				.listFilesExcludingExtension(extensions);
		for (int i = 0; i < badFiles.length; i++) {
			final String fileName = badFiles[i];
			final GuessInputStream gis = GuessInputStream
					.getInstance(new FileInputStream(fileName));
			assertTrue("file [" + fileName
					+ "] WAS UNCORRECTLY recognized as [" + expectedFormat
					+ "]", !expectedFormat.equals(gis.getFormat()));
			gis.close();
		}
	}

	@org.junit.Test
	public void testBase64Detector() throws Exception {
		checkDetector(FormatEnum.BASE64, new String[] { "b64" });
	}

	@org.junit.Test
	public void testDocDetector() throws Exception {
		checkDetector(FormatEnum.DOC, new String[] { "doc" });
	}

	@org.junit.Test
	public void testGifDetectorModule() throws Exception {
		checkDetector(FormatEnum.GIF, new String[] { "gif" });
	}

	@org.junit.Test
	public void testM7MDetectorModule() throws Exception {
		checkDetector(FormatEnum.M7M, new String[] { "m7m" });
	}

	@org.junit.Test
	public void testPdfDetector() throws Exception {
		checkDetector(FormatEnum.PDF, new String[] { "pdf" });
	}

	@org.junit.Test
	public void testPKCS7DetectorModule() throws Exception {
		String fileName = "/testFiles/head.zip.p7m";
		InputStream istream = TestDroidDetector.class
				.getResourceAsStream(fileName);
		GuessInputStream gis = GuessInputStream.getInstance(istream);
		assertEquals("file format [" + fileName + "]", FormatEnum.PKCS7, gis
				.getFormat());
		fileName = "/testFiles/ietf.p7m";
		istream = TestDroidDetector.class.getResourceAsStream(fileName);
		gis = GuessInputStream.getInstance(istream);
		assertEquals("file format [" + fileName + "]", FormatEnum.PKCS7, gis
				.getFormat());

	}

	@org.junit.Test
	public void testRTFDetectorModule() throws Exception {
		checkDetector(FormatEnum.RTF, new String[] { "rtf" });
	}

	@org.junit.Test
	public void testXmlDetector() throws Exception {
		checkDetector(FormatEnum.XML, new String[] { "xml" });
	}

	@org.junit.Test
	public void testZipDetectorModule() throws Exception {
		checkDetector(FormatEnum.ZIP, new String[] { "zip" });
	}

	@org.junit.Test
	public void testZeroLengthFile() throws Exception {
		final InputStream is = TestDroidDetector.class
				.getResourceAsStream("/testFiles/test.zln");
		final GuessInputStream gis = GuessInputStream.getInstance(is);
		assertEquals("file format ", FormatEnum.UNKNOWN, gis.getFormat());
		assertEquals("Read empty", -1, gis.read());
	}

	@org.junit.Test
	public void testBase64Doc() throws Exception {
//		final InputStream is1 = TestDroidDetector.class
//				.getResourceAsStream("/testFiles/canto_8parte.doc.b64");
//		Base64Decoder decoder=new Base64Decoder();
//		final GuessInputStream gis1 = GuessInputStream.getInstance(decoder.decode(is1));
//		gis1.setIdentificationDepth(4);
//		assertEquals("Formats detected [" + Arrays.toString(gis1.getFormats())
//				+ "]", 1, gis1.getFormats().length);
//		assertEquals("file format 1", FormatEnum.DOC, gis1.getFormats()[0]);
		//Test arrives here...
		final InputStream is = TestDroidDetector.class
				.getResourceAsStream("/testFiles/canto_8parte.doc.b64");
		final GuessInputStream gis = GuessInputStream.getInstance(is);
		gis.setIdentificationDepth(4);
		assertEquals("Formats detected [" + Arrays.toString(gis.getFormats())
				+ "]", 2, gis.getFormats().length);
		assertEquals("file format ", FormatEnum.BASE64, gis.getFormats()[0]);
		assertEquals("file format 2", FormatEnum.DOC, gis.getFormats()[1]);
		gis.close();
	}
}
