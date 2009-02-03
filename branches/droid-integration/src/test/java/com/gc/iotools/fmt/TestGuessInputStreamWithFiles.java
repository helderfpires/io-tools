package com.gc.iotools.fmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.FormatEnum;

public class TestGuessInputStreamWithFiles extends TestCase {

	private static void checkDetector(final FormatEnum expectedFormat,
			final String[] extensions) throws Exception {
		final String[] goodFiles = listFilesIncludingExtension(extensions);
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
		final String[] badFiles = listFilesExcludingExtension(extensions);
		for (int i = 0; i < badFiles.length; i++) {
			final String fileName = badFiles[i];
			final GuessInputStream gis = GuessInputStream
					.getInstance(new FileInputStream(fileName));
			assertTrue("file [" + fileName
					+ "] WAS UNCORRECTLY recognized as [" + expectedFormat
					+ "]", !expectedFormat.equals(gis.getFormat()));
		}
	}

	static String[] listFilesExcludingExtension(final String[] forbidden)
			throws Exception {
		final URL fileURL = TestGuessInputStreamWithFiles.class
				.getResource("/testFiles");
		String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection goodFiles = new Vector();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (int i = 0; i < files.length; i++) {
			final String file = files[i];
			boolean insert = true;
			for (int j = 0; j < forbidden.length; j++) {
				final String extForbidden = forbidden[j];
				insert &= !(file.endsWith(extForbidden));
			}
			if (insert) {
				goodFiles.add(filePath + file);
			}
		}
		assertTrue("No files detected", goodFiles.size() > 0);
		return (String[]) goodFiles.toArray(new String[goodFiles.size()]);
	}

	static String[] listFilesIncludingExtension(final String[] allowed)
			throws IOException {
		final URL fileURL = TestGuessInputStreamWithFiles.class
				.getResource("/testFiles");
		String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection goodFiles = new Vector();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (int i = 0; i < files.length; i++) {
			final String file = files[i];
			for (int j = 0; j < allowed.length; j++) {
				final String element = allowed[j];
				if (file.endsWith(element)) {
					goodFiles.add(filePath + file);
				}
			}
		}
		return (String[]) goodFiles.toArray(new String[goodFiles.size()]);
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
		checkDetector(FormatEnum.UNKNOWN, new String[] { ".zln" });
	}
}
