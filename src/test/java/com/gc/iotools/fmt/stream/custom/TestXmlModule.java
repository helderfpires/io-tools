package com.gc.iotools.fmt.stream.custom;

import junit.framework.TestCase;

public class TestXmlModule extends TestCase {

	public void testXmlDetector() {
		assertTrue("short xml detected", new XmlModule()
				.detect("<xml>this is xml</xml>".getBytes()));
	}
}
