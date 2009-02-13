package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import com.gc.iotools.stream.utils.StreamUtils;

public class FileBufferedInputStreamTest {

	static int getTmpFileNum() {
		final File tmpDir1 = new File(System.getProperty("java.io.tmpdir"));
		int result = 0;
		final File[] files = tmpDir1.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-filebufferis.*tmp")) {
				result++;
			}
		}
		return result;
	}

	@Before
	public void setUp() throws Exception {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		final File[] files = tmpDir.listFiles();
		for (final File file : files) {
			final String name = file.getName();
			if (name.matches("iotools-filebufferis.*tmp")) {
				final boolean canDelete = file.delete();
				if (!canDelete) {
					throw new IOException("Can't delete file [" + name + "]");
				}
			}
		}
	}

	@Test
	public void testMark0() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.read(new byte[5]);
		testStream.mark(140);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 100);
		assertEquals("read bytes", 100, readBeforeReset.length);
		assertArrayEquals("read before reset", ArrayUtils.subarray(reference,
				0, 100), readBeforeReset);
		assertEquals("TempFile Number", 1, getTmpFileNum());
		testStream.mark(0);
		assertEquals("TempFile Number", 0, getTmpFileNum());
		final byte[] read = StreamUtils.read(testStream, 5);
		assertArrayEquals("read after mark 0", ArrayUtils.subarray(reference,
				100, 105), read);
		try {
			testStream.reset();
			fail("exception should be thrown");
		} catch (final IOException e) {
			// everything all right
		}
		testStream.close();
	}

	@Test
	public void testMarkOverLimit() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.read(new byte[5]);
		testStream.mark(50);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 100);
		assertEquals("read bytes", 100, readBeforeReset.length);
		assertArrayEquals("read before reset", ArrayUtils.subarray(reference,
				0, 100), readBeforeReset);
		assertEquals("TempFile Number", 0, getTmpFileNum());
		try {
			testStream.reset();
			fail("Exception must be thrown");
		} catch (final IOException e) {
			// everything ok here
		}
	}

	@Test
	public void testMultipleMarkOnDisk() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.read(new byte[5]);
		testStream.mark(120);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 100);
		assertEquals("read bytes", 100, readBeforeReset.length);
		assertArrayEquals("read before reset", ArrayUtils.subarray(reference,
				0, 100), readBeforeReset);
		assertEquals("TempFile Number", 1, getTmpFileNum());
		testStream.reset();
		final byte[] readAfterReset = StreamUtils.read(testStream, 10);
		assertArrayEquals("After first Reset", ArrayUtils.subarray(reference,
				0, 10), readAfterReset);
		testStream.mark(101);
		final byte[] readAfter2Mark = StreamUtils.read(testStream, 100);
		assertEquals("TempFile Number", 1, getTmpFileNum());
		assertArrayEquals("After first Reset", ArrayUtils.subarray(reference,
				10, 110), readAfter2Mark);
		testStream.reset();
		final byte[] content = IOUtils.toByteArray(testStream);
		assertArrayEquals("read after reset", ArrayUtils.subarray(reference,
				10, reference.length), content);
		testStream.close();
		assertEquals("Temporary files ", 0, getTmpFileNum());
	}

	@Test
	public void testRead() throws IOException {

	}

	@Test
	public void testSimpleRead() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(32768);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final byte[] test = IOUtils
				.toByteArray(new FileBufferedInputStream(bis));
		assertArrayEquals("simple read", reference, test);
	}

	@Test
	public void testSkipInsideMark() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.skip(5);
		testStream.mark(-1);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 1000);
		assertArrayEquals("read before reset", reference, readBeforeReset);
		assertEquals("TempFile Number", 1, getTmpFileNum());
		testStream.reset();
		testStream.skip(5);
		final byte[] readAfterReset = StreamUtils.read(testStream, 10);
		assertArrayEquals("read after reset", ArrayUtils.subarray(reference, 5,
				15), readAfterReset);
		testStream.mark(20);
		testStream.skip(5);
		byte[] read = StreamUtils.read(testStream, 15);
		assertArrayEquals("read after reset", ArrayUtils.subarray(reference,
				20, 35), read);
		testStream.reset();
		read = StreamUtils.read(testStream, 15);
		assertArrayEquals("read after reset", ArrayUtils.subarray(reference,
				15, 30), read);
		testStream.close();
		assertEquals("TempFile Number", 0, getTmpFileNum());
	}

	@Test
	public void testSkipOutsideMarkLimit() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.mark(20);
		testStream.skip(50);
		assertEquals("TempFile Number", 0, getTmpFileNum());
		try {
			testStream.reset();
			fail("exception should be thrown");
		} catch (final IOException e) {

		}
		testStream.close();
	}

	@Test
	public void testUnlimitedMark() throws IOException {
		final BigDocumentIstream bis = new BigDocumentIstream(1000);
		bis.read(new byte[5]);
		final byte[] reference = IOUtils.toByteArray(bis);
		bis.resetToBeginning();
		final InputStream testStream = new FileBufferedInputStream(bis);
		testStream.read(new byte[5]);
		testStream.mark(-1);
		final byte[] readBeforeReset = StreamUtils.read(testStream, 1000);
		assertArrayEquals("read before reset", reference, readBeforeReset);
		assertEquals("TempFile Number", 1, getTmpFileNum());
		testStream.reset();
		final byte[] readAfterReset = StreamUtils.read(testStream, 1000);
		assertArrayEquals("read after reset", reference, readAfterReset);
		assertEquals("TempFile Number", 0, getTmpFileNum());
		testStream.close();
	}
}
