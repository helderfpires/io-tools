package org.iotools.formats.detectors;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

import org.iotools.formats.base.Detector;
import org.iotools.formats.detectors.pksc7.PKCS7Detector;

public class TestDetectors extends TestCase {

	private static void checkDetector(final Detector detector,
			final String[] extensions) throws IOException {
		final String[] goodFiles = listFilesIncludingExtension(extensions);
		for (final String fileName : goodFiles) {
			final InputStream is = TestDetectors.class
					.getResourceAsStream(fileName);
			final byte[] data = readBytesAndReset(is, detector
					.getDetectLenght());
			assertTrue("file [" + fileName + "] was not recognized as ["
					+ detector.getDetectedFormat() + "] by [" + detector + "]",
					detector.detect(data));
		}
		final String[] badFiles = listFilesIncludingExtension(extensions);
		for (final String fileName : badFiles) {
			final InputStream is = TestDetectors.class
					.getResourceAsStream(fileName);
			final byte[] data = readBytesAndReset(is, detector
					.getDetectLenght());
			assertTrue("file [" + fileName
					+ "] WAS UNCORRECTLY recognized as ["
					+ detector.getDetectedFormat() + "] by [" + detector + "]",
					detector.detect(data));
		}
	}

	private static byte[] readBytesAndReset(final InputStream input,
			final int size) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		final byte[] buffer = new byte[size];
		long count = 0;
		int n = 0;
		while ((-1 != (n = input.read(buffer))) && (count <= size)) {
			baos.write(buffer, 0, (int) Math.min(n, size - count));
			count += n;
		}
		return baos.toByteArray();
	}

	static String[] listFilesExcludingExtension(final String[] forbidden)
			throws Exception {
		final URL fileURL = TestDetectors.class.getResource(".");
		final String filePath = fileURL.toString();
		final File dir = new File(new URI(filePath));
		final String[] files = dir.list();
		final Collection<String> goodFiles = new Vector<String>();
		for (final String file : files) {
			boolean insert = true;
			for (final String extForbidden : forbidden) {
				insert &= !(file.endsWith(extForbidden));
			}
			if (insert) {
				goodFiles.add(file);
			}
		}
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

	static String[] listFilesIncludingExtension(final String[] allowed)
			throws IOException {
		final URL fileURL = TestDetectors.class.getResource(".");
		final String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection<String> goodFiles = new Vector<String>();
		for (final String file : files) {
			for (final String element : allowed) {
				if (file.endsWith(element)) {
					goodFiles.add(file);
				}
			}
		}
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

	@org.junit.Test
	public void testBase64Detector() throws Exception {
		checkDetector(new Base64Detector(), new String[] { ".b64" });
	}

	@org.junit.Test
	public void testM7MDetectorModule() throws Exception {
		checkDetector(new M7MDetector(), new String[] { ".m7m" });
	}

	@org.junit.Test
	public void testPdfDetector() throws Exception {
		checkDetector(new PdfDetector(), new String[] { ".pdf" });
	}

	@org.junit.Test
	public void testPKCS7DetectorModule() throws Exception {
		checkDetector(new PKCS7Detector(), new String[] { ".p7m" });
	}

	@org.junit.Test
	public void testRTFDetectorModule() throws Exception {
		checkDetector(new RTFDetectorModule(), new String[] { ".rtf" });
	}

	@org.junit.Test
	public void testXmlDetector() throws Exception {
		checkDetector(new XmlDetector(), new String[] { ".xml" });
	}

	@org.junit.Test
	public void testZipDetectorModule() throws Exception {
		checkDetector(new ZipDetectorModule(), new String[] { ".zip" });
	}
}
