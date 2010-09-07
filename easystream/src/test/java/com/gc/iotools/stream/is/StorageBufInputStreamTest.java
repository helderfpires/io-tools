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
import java.io.IOException;

import org.junit.Test;

public class StorageBufInputStreamTest {

	@Test
	public void testFullReadAndReset() throws IOException {
		// final BigDocumentReader bis = new BigDocumentReader(131072);
		// final byte[] reference = IOUtils.toByteArray(bis);
		// bis.resetToBeginning();
		// final StorageBufInputStream ris = new StorageBufInputStream(bis);
		// final byte[] test = IOUtils.toByteArray(ris);
		// assertArrayEquals("simple read", reference, test);
		// ris.resetToBeginning();
		// final byte[] test2 = IOUtils.toByteArray(ris);
		// assertArrayEquals("simple read", reference, test2);
	}

	@Test
	public void testMarkAndReset() throws IOException {
		// final BigDocumentReader bis = new BigDocumentReader(131072);
		// final byte[] reference = IOUtils.toByteArray(bis);
		// bis.resetToBeginning();
		//
		// final StorageBufInputStream ris = new StorageBufInputStream(bis);
		//
		// ris.read(new byte[5]);
		// ris.mark(150);
		// final byte[] b = new byte[100];
		// ris.read(b);
		// assertArrayEquals("correct position after mark", ArrayUtils.subarray(
		// reference, 5, 105), b);
		// ris.reset();
		// byte[] bytes = StreamUtils.read(ris, 200);
		// assertArrayEquals("correct position after reset", ArrayUtils
		// .subarray(reference, 5, 205), bytes);
		// ris.resetToBeginning();
		// final byte[] test2 = IOUtils.toByteArray(ris);
		// assertArrayEquals("full read after resetToBeginning", reference,
		// test2);
	}

	@Test
	public void testSkipAndReset() throws IOException {
		// final BigDocumentReader bis = new BigDocumentReader(131072);
		// final byte[] reference = IOUtils.toByteArray(bis);
		// bis.resetToBeginning();
		// final StorageBufInputStream ris = new StorageBufInputStream(bis);
		// final byte[] b = new byte[5];
		// ris.read(b);
		// ris.skip(32768);
		// ris.read(b);
		// assertArrayEquals("read correct position", ArrayUtils.subarray(
		// reference, 32768 + 5, 32768 + 10), b);
		// ris.resetToBeginning();
		// final byte[] test2 = IOUtils.toByteArray(ris);
		// assertArrayEquals("skip and reset read", reference, test2);
	}

}
