package org.iotools.formats;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

public class TestDetectors extends TestCase {

	private static void checkDetector(final DetectorModule detector,
			final String[] extensions) throws IOException {
		final String[] goodFiles = listFilesIncludingExtension(extensions);
		for (final String fileName : goodFiles) {
			final InputStream is = TestDetectors.class
					.getResourceAsStream(fileName);
			final DataInputStream dis = new DataInputStream(is);
			final byte[] data = new byte[detector.getDetectLenght()];
			try {
				dis.readFully(data);
			} catch (final EOFException e) {
				// no problems
			}
			assertTrue("file [" + fileName + "] was not recognized as ["
					+ detector.getDetectedFormat() + "] by [" + detector + "]",
					detector.detect(data));
		}
		final String[] badFiles = listFilesIncludingExtension(extensions);
		for (final String fileName : badFiles) {
			final InputStream is = TestDetectors.class
					.getResourceAsStream(fileName);
			final DataInputStream dis = new DataInputStream(is);
			final byte[] data = new byte[detector.getDetectLenght()];
			try {
				dis.readFully(data);
			} catch (final EOFException e) {
				// no problems
			}
			assertTrue("file [" + fileName
					+ "] WAS UNCORRECTLY recognized as ["
					+ detector.getDetectedFormat() + "] by [" + detector + "]",
					detector.detect(data));
		}
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
	public void testPdfDetectorModule() throws Exception {
		checkDetector(new PdfDetectorModule(), new String[] { ".pdf" });
	}

	@org.junit.Test
	public void testRTFDetectorModule() throws Exception {
		checkDetector(new RTFDetectorModule(), new String[] { ".rtf" });
	}

	@org.junit.Test
	public void testZipDetectorModule() throws Exception {
		checkDetector(new ZipDetectorModule(), new String[] { ".zip" });
	}
}
