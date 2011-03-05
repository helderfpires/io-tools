package com.gc.iotools.fmt.detect.wzf.custom;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

public class TestXmlModule extends TestCase {
	@org.junit.Test
	public void testXmlDetector() {
		assertTrue("short xml detected", new XmlModule()
				.detect("<xml>this is xml</xml>".getBytes()));
	}

	@org.junit.Test
	public void testUserFile() throws Exception {
		InputStream is = TestXmlModule.class
				.getResourceAsStream("/testFiles/userFile.xml");
		byte[] bytes = IOUtils.toByteArray(is);
		int len = Math.min(bytes.length, new XmlModule().getDetectLength());
		byte[] dest = new byte[len];
		System.arraycopy(bytes, 0, dest, 0, len);
		assertTrue("user format detected", new XmlModule().detect(bytes));
	}
}
