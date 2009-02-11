package com.gc.iotools.fmt.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.gc.iotools.fmt.TestUtils;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.stream.is.SizeReaderInputStream;

public class TestStreamModule {
	private static final Map<FormatEnum, String> enabledFormats = new HashMap<FormatEnum, String>();

	static {
		// enabledFormats.put(FormatEnum.PEM, "pem");
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
			String[] goodFiles = TestUtils
					.listFilesIncludingExtension(new String[] { enabledFormats
							.get(formatEnum) });
			for (String fname : goodFiles) {
				InputStream istream = new FileInputStream(fname);
				SizeReaderInputStream srIs = new SizeReaderInputStream(istream);
				StreamDetectorImpl stDetect = new StreamDetectorImpl();
				final FormatEnum[] detectedFormats = stDetect
						.getDetectedFormats();
				int declared = stDetect.getDetectLength(detectedFormats);
				FormatId formats = stDetect.detect(detectedFormats, srIs);
				assertEquals("Formato file [" + fname + "]", formatEnum,
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
