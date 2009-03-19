package com.gc.iotools.fmt.detect.droid;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableFileInputStream;
import com.gc.iotools.fmt.base.TestUtils;

public class TestDroidDetector {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		// enabledFormats.put(FormatEnum.M7M, "m7m");
		// enabledFormats.put(FormatEnum.PDF, "pdf");
		// enabledFormats.put(FormatEnum.PKCS7, "p7m");
		// enabledFormats.put(FormatEnum.XML, "xml");
		enabledFormats.put(FormatEnum.BMP, "bmp");
		// enabledFormats.put(FormatEnum.GIF, "gif");
		enabledFormats.put(FormatEnum.EPSF, "eps");
		enabledFormats.put(FormatEnum.JPEG, "jpg");
		enabledFormats.put(FormatEnum.PCX, "pcx");
		enabledFormats.put(FormatEnum.PNG, "png");
		enabledFormats.put(FormatEnum.RTF, "rtf");
		enabledFormats.put(FormatEnum.ZIP, "zip");
	}

	// @org.junit.Test
	// public void testHugeFile() throws IOException {
	// File tmpFile=File.createTempFile("iotools-test", ".tmp");
	// tmpFile.deleteOnExit();
	// FileOutputStream fos=new FileOutputStream(tmpFile);
	// // 1Gb file
	// IOUtils.copyLarge(new BigDocumentIstream(1024 * 1024 * 200), fos);
	// fos.close();
	// try {
	// FileDetector stDetect = new DroidDetectorImpl();
	// final FormatEnum[] detectedFormats = stDetect
	// .getDetectedFormats();
	// stDetect.detect(detectedFormats, tmpFile);
	// } catch (OutOfMemoryError oe) {
	// fail("OutOfMemory");
	// }
	// tmpFile.delete();
	// }

	
	@org.junit.Test
	public void testPDF() throws IOException {
		checkFormat(FormatEnum.PDF, "pdf");
	}

	@org.junit.Test
	public void testGIF() throws IOException {
		checkFormat(FormatEnum.GIF, "gif");
	}
	
	private void checkFormat(FormatEnum expected, String extension) throws IOException{
		checkFormat(new String[]{extension}, expected);
	}
	
	private void checkFormat(String[] extensions, FormatEnum expected)
			throws IOException {
		URL url=TestDroidDetector.class.getResource("/testFiles");
		final String path = url.getPath();
		@SuppressWarnings("unchecked")
		Iterator<File> fiter = FileUtils.iterateFiles(new File(path),
				extensions, false);
		assertTrue("at least one file",fiter.hasNext());
		while (fiter.hasNext()) {
			File file = fiter.next();
			assertTrue("File to be checked ["+file+"] exists",file.exists());
			assertTrue("File to be checked ["+file+"] can be read",file.canRead());
			Detector stDetect = new DroidDetectorImpl();
			final FormatEnum[] detectedFormats = stDetect
					.getDetectedFormats();
			FormatId formats = stDetect.detect(detectedFormats,
					new ResettableFileInputStream(file));
			assertEquals("Formato file [" + file.getName() + "]", expected,
					formats.format);
		}
		String[] badFiles = TestUtils
				.listFilesExcludingExtension( extensions);
		for (String fname : badFiles) {
			File file = new File(fname);
			Detector stDetect = new DroidDetectorImpl();
			FormatEnum[] enabledFormats = new FormatEnum[] { expected };
			FormatId formats = stDetect.detect(enabledFormats,
					new ResettableFileInputStream(file));
			assertEquals("Formato file [" + fname + "]", FormatEnum.UNKNOWN,
					formats.format);
		}
	}

	@org.junit.Test
	public void testFileModule() throws IOException {
		for (FormatEnum formatEnum : enabledFormats.keySet()) {
			checkFormat(formatEnum, enabledFormats.get(formatEnum));
		}
	}
}
