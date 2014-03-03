package com.gc.iotools.stream.writer.inspection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;

public class StatsWriterTest {

	@org.junit.Test
	public void testSize() throws IOException {
		final StringWriter baos = new StringWriter();
		final StatsWriter stOs = new StatsWriter(baos);
		final String reference = "test";
		stOs.write(reference);
		stOs.write(reference);
		assertEquals("size read", 8, stOs.getSize());
		stOs.close();
		assertEquals("data read",
				 baos.toString(),reference + reference);
	}
}
