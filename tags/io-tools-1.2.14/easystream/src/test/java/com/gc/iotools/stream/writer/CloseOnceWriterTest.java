package com.gc.iotools.stream.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class CloseOnceWriterTest {

	@Test
	public void testCloseOnceWriter() throws IOException {
		StringWriter baos = new StringWriter();
		final CloseOnceWriter<StringWriter> diagnosticStream = new CloseOnceWriter<StringWriter>(
				baos);
		CloseOnceWriter<CloseOnceWriter<StringWriter>> test = new CloseOnceWriter<CloseOnceWriter<StringWriter>>(
				diagnosticStream);
		test.write("test");
		test.close();
		test.close();
		assertEquals("Close called twice", 2, test.getCloseCount());
		assertEquals("Close called once on diagnostic", 1,
				diagnosticStream.getCloseCount());
		assertEquals("bytes were written","test", baos.toString());
	}

}
