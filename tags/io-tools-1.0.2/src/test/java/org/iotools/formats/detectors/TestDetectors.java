package org.iotools.formats.detectors;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestDetectors {
	@Test
	public void testXmlDetector() {
		assertTrue("short xml detected", new XmlDetector()
				.detect("<xml>this is xml</xml>".getBytes()));
	}
}
