package com.gc.iotools.stream.os;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class OutputStreamDumperTest {

	@Test
	public void testStandardDump() throws Exception {
		byte[] test = "test".getBytes();
		ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		OutputStreamDumper dump = new OutputStreamDumper(destination1);
		dump.write(test);
		assertArrayEquals("the two arrays are equals", test,
				destination1.toByteArray());
		assertArrayEquals("Dumped data od", test, dump.getData());
	}

	@Test
	public void testDisable() throws Exception {
		ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
		OutputStreamDumper dumper = new OutputStreamDumper(destination1);
		dumper.write("test ".getBytes());
		dumper.enableDump(false);
		dumper.write("1 ".getBytes());
		dumper.enableDump(true);
		dumper.write("2".getBytes());
		assertEquals("Dumped Data", "test 2", new String(dumper.getData()));
		assertEquals("Copied data", "test 1 2",
				new String(destination1.toByteArray()));
	}

	@Test
	public void testMaxSize() throws Exception {
		byte[] buffer = new byte[1024];
		Random random = new Random();
		random.nextBytes(buffer);
		ByteArrayOutputStream destination = new ByteArrayOutputStream();
		OutputStreamDumper dumper = new OutputStreamDumper(destination, 512);
		dumper.write(buffer);
		dumper.enableDump(true);
		dumper.write(buffer);
		assertArrayEquals("All data was copied", ArrayUtils.addAll(buffer, buffer),destination.toByteArray());

		assertArrayEquals("only first 512 dumped", ArrayUtils.subarray(buffer, 0, 512),
				dumper.getData());
		
	}
}
