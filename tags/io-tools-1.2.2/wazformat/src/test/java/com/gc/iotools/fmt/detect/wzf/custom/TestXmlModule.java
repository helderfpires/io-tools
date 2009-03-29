package com.gc.iotools.fmt.detect.wzf.custom;

import com.gc.iotools.fmt.detect.wzf.custom.XmlModule;

import junit.framework.TestCase;

public class TestXmlModule extends TestCase {

	public void testXmlDetector() {
		assertTrue("short xml detected", new XmlModule()
				.detect("<xml>this is xml</xml>".getBytes()));
	}
}
