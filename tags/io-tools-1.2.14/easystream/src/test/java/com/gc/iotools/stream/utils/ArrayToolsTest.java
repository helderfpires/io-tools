package com.gc.iotools.stream.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrayToolsTest {

	@Test
	public void testIndexOf() {
		final byte[] reference = "0123456789".getBytes();
		assertEquals("Test not found", -1,
				ArrayTools.indexOf(reference, "12there".getBytes()));
		assertEquals("postion", 2,
				ArrayTools.indexOf(reference, "23".getBytes()));
		assertEquals("Test start", 0,
				ArrayTools.indexOf(reference, reference));
		assertEquals("test end", -1,
				ArrayTools.indexOf(reference, "91".getBytes()));

	}

}
