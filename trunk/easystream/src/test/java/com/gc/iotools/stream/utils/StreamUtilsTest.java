package com.gc.iotools.stream.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class StreamUtilsTest {

	@Test
	public void testGetBandWidthString() {
		assertEquals("rate", "0.03 Byte/sec",
				StreamUtils.getRateString(3, 100000));
		assertEquals("rate", "30 Byte/sec",
				StreamUtils.getRateString(30, 1000));
		assertEquals("rate", "3.5 KB/sec",
				StreamUtils.getRateString((int) (1024 * 3.5), 1000));
		assertEquals("rate", "35 KB/sec",
				StreamUtils.getRateString((1024 * 35), 1000));
		assertEquals("rate", "3.5 MB/sec",
				StreamUtils.getRateString((int) (1024 * 1024 * 3.5), 1000));
		assertEquals("rate", "3500 GB/sec",
				StreamUtils.getRateString(1024L * 1024 * 1024 * 3500, 1000));
	}

}
