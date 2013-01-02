package com.gc.iotools.stream.os.inspection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;

public class StatsOutputStreamTest {

	@Before
	public void setUp() throws Exception {
		// nothing to do
	}

	@org.junit.Test
	public void testSize() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final StatsOutputStream stOs = new StatsOutputStream(baos);
		final byte[] reference = "test".getBytes();
		stOs.write(reference);
		stOs.write(reference);
		assertEquals("size read", reference.length * 2, stOs.getSize());
		stOs.close();
		assertArrayEquals("data read",
				ArrayUtils.addAll(reference, reference), baos.toByteArray());
	}
}
