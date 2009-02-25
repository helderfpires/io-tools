package com.gc.iotools.fmt.file.droid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.fmt.base.FileDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.TestUtils;
import com.gc.iotools.fmt.stream.BigDocumentIstream;


public class TestDroidDetector {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		// enabledFormats.put(FormatEnum.PEM, "pem");
		// enabledFormats.put(FormatEnum.M7M, ".m7m");
		enabledFormats.put(FormatEnum.PDF_A, ".pdf");
		// enabledFormats.put(FormatEnum.PKCS7, ".p7m");
		// enabledFormats.put(FormatEnum.RTF, ".rtf");
		// enabledFormats.put(FormatEnum.XML, ".xml");
		// enabledFormats.put(FormatEnum.ZIP, ".zip");

	}
	@org.junit.Test
	public void testHugeFile() throws IOException {
		File tmpFile=File.createTempFile("iotools-test", ".tmp");
		tmpFile.deleteOnExit();
		FileOutputStream fos=new FileOutputStream(tmpFile);
		// 1Gb file
		IOUtils.copyLarge(new BigDocumentIstream(1024 * 1024 * 200), fos);
		fos.close();
		try {
			FileDetector stDetect = new DroidDetectorImpl();
			final FormatEnum[] detectedFormats = stDetect
					.getDetectedFormats();
			stDetect.detect(detectedFormats, tmpFile);
		} catch (OutOfMemoryError oe) {
			fail("OutOfMemory");
		}
		tmpFile.delete();
	}

	@org.junit.Test
	public void testFileModule() throws IOException {
		for (FormatEnum formatEnum : enabledFormats.keySet()) {
			String[] goodFiles = TestUtils
					.listFilesIncludingExtension(new String[] { enabledFormats
							.get(formatEnum) });
			for (String fname : goodFiles) {
				File file = new File(fname);
				FileDetector stDetect = new DroidDetectorImpl();
				final FormatEnum[] detectedFormats = stDetect
						.getDetectedFormats();
				FormatId formats = stDetect.detect(detectedFormats, file);
				assertEquals("Formato file [" + fname + "]", formatEnum,
						formats.format);
			}
			String[] badFiles = TestUtils
					.listFilesExcludingExtension(new String[] { enabledFormats
							.get(formatEnum) });
			for (String fname : badFiles) {
				File file = new File(fname);
				FileDetector stDetect = new DroidDetectorImpl();
				FormatEnum[] enabledFormats = new FormatEnum[] { formatEnum };
				FormatId formats = stDetect.detect(enabledFormats, file);
				assertEquals("Formato file [" + fname + "]",
						FormatEnum.UNKNOWN,
						formats.format);
			}
		}
	}
}
