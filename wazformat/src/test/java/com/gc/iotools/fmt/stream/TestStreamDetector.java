package com.gc.iotools.fmt.stream;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.TestUtils;
import com.gc.iotools.fmt.file.droid.TestDroidDetector;
import com.gc.iotools.stream.is.SizeReaderInputStream;

public class TestStreamDetector {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		enabledFormats.put(FormatEnum.PEM, "pem");
		enabledFormats.put(FormatEnum.M7M, "m7m");
		enabledFormats.put(FormatEnum.PDF, "pdf");
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
			@SuppressWarnings("unchecked")
			Iterator<File> fiter = FileUtils.iterateFiles(new File(path),
					new String[] { enabledFormats.get(formatEnum) }, false);
			assertTrue("at least one file", fiter.hasNext());
			while (fiter.hasNext()) {
				File file = fiter.next();
				InputStream istream = new FileInputStream(file);
				SizeReaderInputStream srIs = new SizeReaderInputStream(istream);
				StreamDetectorImpl stDetect = new StreamDetectorImpl();
				final FormatEnum[] detectedFormats = stDetect
						.getDetectedFormats();
				int declared = stDetect.getDetectLength(detectedFormats);
				FormatId formats = stDetect.detect(detectedFormats, srIs);
				assertEquals("Formato file [" + file.getName() + "]",
						formatEnum,
						formats.format);
				final long used = srIs.getSize();
				assertTrue("La dimensione letta[" + used
						+ "] non eccede quella prevista[" + declared + "]",
						declared >= used);
			}
			String[] badFiles = TestUtils
					.listFilesExcludingExtension(new String[] { enabledFormats
							.get(formatEnum) });
			for (String fname : badFiles) {
				InputStream istream = new FileInputStream(fname);
				SizeReaderInputStream srIs = new SizeReaderInputStream(istream);
				StreamDetectorImpl stDetect = new StreamDetectorImpl();
				FormatEnum[] enabledFormats = new FormatEnum[] { formatEnum };
				int declared = stDetect.getDetectLength(enabledFormats);
				FormatId formats = stDetect.detect(enabledFormats, srIs);
				assertEquals("Formato file [" + fname + "]",
						FormatEnum.UNKNOWN,
						formats.format);
				final long used = srIs.getSize();
				assertTrue("La dimensione letta[" + used
						+ "] non eccede quella prevista[" + declared + "]",
						declared >= used);
			}
		}
	}
}
