package com.gc.iotools.stream.store;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

public class ThresholdStorageTest {

	static int getTmpFileNum() {
		final File tmpDir1 = new File(System.getProperty("java.io.tmpdir"));
		int result = 0;
		final File[] files = tmpDir1.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-storage.*tmp")) {
				result++;
			}
		}
		return result;
	}

	@Test
	public void seek() throws IOException {
		final ThresholdStore tss = new ThresholdStore(20);
		final byte[] ref1 = new byte[140];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		tss.seek(20);
		final byte[] read = new byte[20];
		tss.get(read, 0, read.length);
		assertArrayEquals("lettura corretta", ArrayUtils.subarray(ref1, 20,
				40), read);
		tss.seek(0);
		tss.get(read, 0, read.length);
		assertArrayEquals("lettura corretta", ArrayUtils
				.subarray(ref1, 0, 20), read);
		tss.cleanup();
	}

	@Before
	public void setUp() throws Exception {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		final File[] files = tmpDir.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-storage.*tmp")) {
				final boolean canDelete = file.delete();
				if (!canDelete) {
					throw new IOException("Can't delete file [" + name + "]");
				}
			}
		}
	}

	@Test
	public void testCleanup() throws IOException {
		final ThresholdStore tss = new ThresholdStore(20);
		final byte[] ref1 = new byte[140];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		tss.cleanup();
		final byte[] read = new byte[20];
		int n = tss.get(ref1, 0, read.length);
		assertEquals("read " + tss, -1, n);
		assertEquals("temporary files after cleanup", 0, getTmpFileNum());
		// still works after cleanup
		tss.put(ref1, 0, ref1.length);
		final byte[] read1 = new byte[280];
		int pos = 0;
		while ((n = tss.get(read1, pos, (read1.length - pos))) >= 0) {
			pos += n;
		}
		assertEquals("read", ref1.length, pos);
		assertArrayEquals(ref1, ArrayUtils.subarray(read, 0, ref1.length));
	}

	@Test
	public void testGetOverTreshold() throws IOException {
		final ThresholdStore tss = new ThresholdStore(150);
		final byte[] ref1 = new byte[140];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		assertEquals("temporary files", 0, getTmpFileNum());
		tss.put(ref1, 0, ref1.length);
		int n;
		int pos = 0;
		final byte[] read = new byte[280];
		while ((n = tss.get(read, pos, (read.length - pos))) > 0) {
			pos += n;
		}
		assertArrayEquals("letti = scritti", ArrayUtils.addAll(ref1, ref1),
				read);
		assertEquals("temporary files", 1, getTmpFileNum());
		tss.cleanup();
		assertEquals("temporary files after cleanup", 0, getTmpFileNum());
	}

	@Test
	public void testPutMoreThanTreshold() throws IOException {
		final ThresholdStore tss = new ThresholdStore(50);
		byte[] ref1 = new byte[130];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		seekEqualsReference(tss, ref1);
		assertEquals("get over eof", -1, tss.get(new byte[10], 0, 10));
		seekEqualsReference(tss, ref1);
	}

	@Test
	public void testGetPositionAcrossThreshold() throws IOException {
		final ThresholdStore tss = new ThresholdStore(150);
		byte[] ref1 = new byte[130];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		tss.get(new byte[5], 0, 5);
		// goes over threshold
		tss.put(ref1, 0, ref1.length);
		for (int i = 0; i < (ref1.length - 5); i++) {
			byte b = ref1[i + 5];
			byte[] read = new byte[1];
			tss.get(read, 0, 1);
			assertEquals("position [" + i + "]", b, read[0]);
		}
	}

	@Test
	public void testPutTwiceLessThanTreshold() throws IOException {
		final ThresholdStore tss = new ThresholdStore(50);
		byte[] ref1 = new byte[25];
		final Random r = new Random();
		r.nextBytes(ref1);
		tss.put(ref1, 0, ref1.length);
		tss.put(ref1, 0, ref1.length);
		seekEqualsReference(tss, ArrayUtils.addAll(ref1, ref1));
	}

	private void seekEqualsReference(ThresholdStore t, byte[] reference)
			throws IOException {
		for (int i = 0; i < reference.length; i++) {
			byte b = reference[i];
			byte[] read = new byte[1];
			t.seek(i);
			t.get(read, 0, 1);
			assertEquals("position [" + i + "]", b, read[0]);
		}
	}
}
