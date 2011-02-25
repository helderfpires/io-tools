package com.gc.iotools.fmt.detect.wzf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableFileInputStream;
import com.gc.iotools.fmt.base.TestUtils;
import com.gc.iotools.fmt.detect.droid.TestDroidDetector;

public class TestStreamDetector {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		enabledFormats.put(FormatEnum.M7M, "m7m");
		enabledFormats.put(FormatEnum.PDF, "pdf");
		enabledFormats.put(FormatEnum.BZIP2, "bz2");
		enabledFormats.put(FormatEnum.PKCS7, "p7m");
		enabledFormats.put(FormatEnum.RTF, "rtf");
		enabledFormats.put(FormatEnum.XML, "xml");
		enabledFormats.put(FormatEnum.ZIP, "zip");
	}

	@org.junit.Test
	public void testStreamModule() throws IOException {
		for (FormatEnum formatEnum : enabledFormats.keySet()) {
			URL url = TestDroidDetector.class.getResource("/testFiles");
			final String path = url.getPath();
			final String ext = enabledFormats.get(formatEnum);
			@SuppressWarnings("unchecked")
			Iterator<File> fiter = FileUtils.iterateFiles(new File(path),
					new String[] { ext }, false);
			assertTrue("at least one file [" + ext + "]", fiter.hasNext());
			while (fiter.hasNext()) {
				File file = fiter.next();
				DetectionLibrary stDetect = new StreamDetectorImpl();
				final FormatEnum[] detectedFormats = stDetect
						.getDetectedFormats();

				FormatId formats = stDetect.detect(detectedFormats,
						new ResettableFileInputStream(file));
				assertEquals("Formato file [" + file.getName() + "]",
						formatEnum, formats.format);

			}
			String[] badFiles = TestUtils
					.listFilesExcludingExtension(new String[] { ext });
			for (String fname : badFiles) {
				StreamDetectorImpl stDetect = new StreamDetectorImpl();
				FormatEnum[] enabledFormats = new FormatEnum[] { formatEnum };
				// int declared = stDetect.getDetectLength(enabledFormats);
				FormatId formats = stDetect.detect(enabledFormats,
						new ResettableFileInputStream(new File(fname)));
				assertEquals("Formato file [" + fname + "]",
						FormatEnum.UNKNOWN, formats.format);
			}
		}
	}
}
