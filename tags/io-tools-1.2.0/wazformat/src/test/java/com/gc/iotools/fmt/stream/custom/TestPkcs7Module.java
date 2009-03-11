package com.gc.iotools.fmt.stream.custom;

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import com.gc.iotools.fmt.base.TestUtils;
import com.gc.iotools.fmt.detect.wzf.custom.PKCS7Detector;

public class TestPkcs7Module extends TestCase {
	@org.junit.Test
	public void testDetect() throws Exception {
		PKCS7Detector p7 = new PKCS7Detector();
		final Map<String, byte[]> goodMap = TestUtils.getBytesForFiles(
				new String[] { "p7m" }, p7.getDetectLength(), true);
		for (String fname : goodMap.keySet()) {
			assertTrue("file [" + fname + "]is not detected", p7.detect(goodMap
					.get(fname)));
		}
	}

	@org.junit.Test
	public void testNotDetect() throws IOException {
		PKCS7Detector p7 = new PKCS7Detector();
		final Map<String, byte[]> badMap = TestUtils.getBytesForFiles(
				new String[] { "p7m" }, p7.getDetectLength(), false);
		for (String fname : badMap.keySet()) {
			assertFalse(
					"file [" + fname + "]is detected as p7while should not",
					p7.detect(badMap.get(fname)));
		}

	}
}
