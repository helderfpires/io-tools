package com.gc.iotools.stream.is;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import com.gc.iotools.stream.utils.StreamUtils;

public class DiskBufferedInputStreamTest {

	private File tmpDir;

	@Before
	public void setUp() throws Exception {
		this.tmpDir = new File(System.getProperty("java.io.tmpdir"));
		final File[] files = this.tmpDir.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-diskbufferis.*tmp")) {
				final boolean canDelete = file.delete();
				if (!canDelete) {
					throw new IOException("Can't delete file [" + name + "]");
				}
			}
		}
	}

	@Test
	public void testMarkAndResetUnderThreshold() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.reset();
		final InputStream testStream = new DiskBufferedInputStream(bis, 8192);
		testStream.read(new byte[5]);
		testStream.mark(80);
		final byte[] read = StreamUtils.read(testStream, 70);
		assertEquals("read bytes", 70, read.length);
		testStream.reset();
		final byte[] content = IOUtils.toByteArray(testStream);
		assertArrayEquals("simple read", reference, content);
	}

	@Test
	public void testMarkOverLimit() throws IOException {

	}

	@Test
	public void testMarkOverTreshold() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.reset();
		final InputStream testStream = new DiskBufferedInputStream(bis, 80);
		testStream.read(new byte[5]);
		testStream.mark(120);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 100);
		assertEquals("read bytes", 100, readBeforeReset.length);
		assertArrayEquals("read before reset", ArrayUtils.subarray(reference,
				0, 100), readBeforeReset);
		final String tmpFileName = getTmpFileName();
		assertNotNull("Temporary file created", tmpFileName);
		testStream.reset();
		final byte[] content = IOUtils.toByteArray(testStream);
		assertArrayEquals("read after reset", reference, content);
		testStream.close();
		assertNull("Temporary file [" + tmpFileName + "]deleted",
				getTmpFileName());
	}

	@Test
	public void testMultipleMark1Disk2Memory3Disk() throws IOException {

	}

	@Test
	public void testMultipleMark1Memory2Disk() throws IOException {

	}

	@Test
	public void testMultipleMarkOnDisk() throws IOException {

	}

	@Test
	public void testSimpleRead() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(32768);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.reset();
		final byte[] test = IOUtils.toByteArray(new DiskBufferedInputStream(
				bis, 8192));
		assertArrayEquals("simple read", reference, test);

	}

	@Test
	public void testUnlimitedMark() throws IOException {

	}

	private String getTmpFileName() {
		String result = null;
		final File[] files = this.tmpDir.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-diskbufferis.*tmp")) {
				result = name;
			}
		}
		return result;
	}

}
