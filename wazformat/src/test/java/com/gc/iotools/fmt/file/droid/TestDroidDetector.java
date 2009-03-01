package com.gc.iotools.fmt.file.droid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.gc.iotools.fmt.base.FileDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.TestUtils;

public class TestDroidDetector {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		// enabledFormats.put(FormatEnum.M7M, ".m7m");
		//enabledFormats.put(FormatEnum.PDF_A, "pdf");
		//enabledFormats.put(FormatEnum.PKCS7, "p7m");
		//enabledFormats.put(FormatEnum.XML, "xml");
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
	public void testRTF() throws IOException{
		checkFormat(FormatEnum.RTF, "rtf");
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
			FileDetector stDetect = new DroidDetectorImpl();
			final FormatEnum[] detectedFormats = stDetect
					.getDetectedFormats();
			FormatId formats = stDetect.detect(detectedFormats, file);
			assertEquals("Formato file [" + file.getName() + "]", expected,
					formats.format);
		}
		String[] badFiles = TestUtils
				.listFilesExcludingExtension( extensions);
		for (String fname : badFiles) {
			File file = new File(fname);
			FileDetector stDetect = new DroidDetectorImpl();
			FormatEnum[] enabledFormats = new FormatEnum[] { expected };
			FormatId formats = stDetect.detect(enabledFormats, file);
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
