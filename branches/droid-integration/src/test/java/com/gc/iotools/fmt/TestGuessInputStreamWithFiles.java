package com.gc.iotools.fmt;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.TestUtils;

public class TestGuessInputStreamWithFiles extends TestCase {

	private static void checkDetector(final FormatEnum expectedFormat,
			final String[] extensions) throws Exception {
		final String[] goodFiles = TestUtils
				.listFilesIncludingExtension(extensions);
		for (int i = 0; i < goodFiles.length; i++) {
			final String fileName = goodFiles[i];
			final InputStream is = new FileInputStream(fileName);
			final GuessInputStream gis = GuessInputStream.getInstance(is);
			assertEquals("file format [" + fileName + "]", expectedFormat, gis
					.getFormat());
			final byte[] reference = IOUtils.toByteArray(new FileInputStream(
					fileName));
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
		}
	}

	
	public void testBase64Detector() throws Exception {
		checkDetector(FormatEnum.BASE64, new String[] { ".b64" });
	}

	public void testGifDetectorModule() throws Exception {
		checkDetector(FormatEnum.GIF, new String[] { ".gif" });
	}

	public void testM7MDetectorModule() throws Exception {
		checkDetector(FormatEnum.M7M, new String[] { ".m7m" });
	}

	public void testPdfDetector() throws Exception {
		checkDetector(FormatEnum.PDF, new String[] { ".pdf" });
	}

	public void testPKCS7DetectorModule() throws Exception {
		checkDetector(FormatEnum.PKCS7, new String[] { ".p7m" });
	}

	public void testRTFDetectorModule() throws Exception {
		checkDetector(FormatEnum.RTF, new String[] { ".rtf" });
	}

	public void testXmlDetector() throws Exception {
		checkDetector(FormatEnum.XML, new String[] { ".xml" });
	}

	public void testZipDetectorModule() throws Exception {
		checkDetector(FormatEnum.ZIP, new String[] { ".zip" });
	}

	public void testZeroLengthModule() throws Exception {
		checkDetector(FormatEnum.UNKNOWN,
				new String[] { ".zln", ".txt",
				".png", ".jpg", ".bmp" });
	}
}
