package org.iotools.formats;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.iotools.formats.base.FormatEnum;

public class TestGuessInputStreamWithFiles {

	private static void checkDetector(final FormatEnum expectedFormat,
			final String[] extensions) throws Exception {
		final String[] goodFiles = listFilesIncludingExtension(extensions);
		for (final String fileName : goodFiles) {
			final InputStream is = new FileInputStream(fileName);
			final GuessInputStream gis = new GuessInputStream(is);
			assertEquals("file format [" + fileName + "]", expectedFormat, gis
					.getFormat());
			final byte[] reference = IOUtils.toByteArray(new FileInputStream(
					fileName));
			assertArrayEquals("Read equals reference [" + fileName + "]",
					reference, IOUtils.toByteArray(gis));
			gis.close();
		}
		final String[] badFiles = listFilesExcludingExtension(extensions);
		for (final String fileName : badFiles) {
			final GuessInputStream gis = new GuessInputStream(
					new FileInputStream(fileName));
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
		final Collection<String> goodFiles = new Vector<String>();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (final String file : files) {
			boolean insert = true;
			for (final String extForbidden : forbidden) {
				insert &= !(file.endsWith(extForbidden));
			}
			if (insert) {
				goodFiles.add(filePath + file);
			}
		}
		assertTrue("No files detected", goodFiles.size() > 0);
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

	static String[] listFilesIncludingExtension(final String[] allowed)
			throws IOException {
		final URL fileURL = TestGuessInputStreamWithFiles.class
				.getResource("/testFiles");
		String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection<String> goodFiles = new Vector<String>();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (final String file : files) {
			for (final String element : allowed) {
				if (file.endsWith(element)) {
					goodFiles.add(filePath + file);
				}
			}
		}
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

	@org.junit.Test
	public void testBase64Detector() throws Exception {
		checkDetector(FormatEnum.BASE64, new String[] { ".b64" });
	}

	@org.junit.Test
	public void testGifDetectorModule() throws Exception {
		checkDetector(FormatEnum.GIF, new String[] { ".gif" });
	}

	@org.junit.Test
	public void testM7MDetectorModule() throws Exception {
		checkDetector(FormatEnum.M7M, new String[] { ".m7m" });
	}

	@org.junit.Test
	public void testPdfDetector() throws Exception {
		checkDetector(FormatEnum.PDF, new String[] { ".pdf" });
	}

	@org.junit.Test
	public void testPKCS7DetectorModule() throws Exception {
		checkDetector(FormatEnum.PKCS7, new String[] { ".p7m" });
	}

	@org.junit.Test
	public void testRTFDetectorModule() throws Exception {
		checkDetector(FormatEnum.RTF, new String[] { ".rtf" });
	}

	@org.junit.Test
	public void testXmlDetector() throws Exception {
		checkDetector(FormatEnum.XML, new String[] { ".xml" });
	}

	@org.junit.Test
	public void testZipDetectorModule() throws Exception {
		checkDetector(FormatEnum.ZIP, new String[] { ".zip" });
	}
}
