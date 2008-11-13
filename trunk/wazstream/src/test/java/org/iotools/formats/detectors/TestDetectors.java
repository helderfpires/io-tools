package org.iotools.formats.detectors;

import junit.framework.TestCase;

public class TestDetectors extends TestCase {

	public void testXmlDetector() {
		assertTrue("short xml detected", new XmlDetector()
				.detect("<xml>this is xml</xml>".getBytes()));
	}
}
