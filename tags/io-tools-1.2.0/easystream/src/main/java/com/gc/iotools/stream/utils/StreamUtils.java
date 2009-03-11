package com.gc.iotools.stream.utils;

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
import java.io.InputStream;

/**
 * General utilities for handling streams.
 * 
 * @author dvd.smnt
 * @since 1.0.9
 */
public final class StreamUtils {
	/**
	 * <p>
	 * Read a specified amount of bytes from the <i>source</i> InputStream and
	 * place them into the returned byte array.
	 * </p>
	 * <p>
	 * This utility ensures that either <code>size</code> bytes are read or the
	 * end of the stream has been reached.
	 * 
	 * @param source
	 *            Stream from which the data is read.
	 * @param size
	 *            The maximum length of the data read.
	 * @return byte[] containing the data read from the stream.
	 *         <code>null</code> if the End Of File has been reached.
	 * @exception IOException
	 *                If the first byte cannot be read for any reason other than
	 *                end of file, or if the input stream has been closed, or if
	 *                some other I/O error occurs.
	 * @since 1.0.9
	 */
	public static byte[] read(final InputStream source, final int size)
			throws IOException {
		final byte[] test = new byte[size];
		final int n = tryReadFully(source, test, 0, size);
		byte[] result = test;
		if (n < size) {
			if (n <= 0) {
				result = null;
			} else {
				result = new byte[n];
				System.arraycopy(test, 0, result, 0, n);
			}
		}
		return result;
	}

	/**
	 * <p>
	 * Read bytes from the <i>source</i> InputStream into the
	 * <code>buffer</code>.
	 * </p>
	 * <p>
	 * This utility ensures that either <code>len</code> bytes are read or the
	 * end of the stream has been reached.
	 * </p>
	 * 
	 * @see InputStream.read(byte[] buf,int off, int len)
	 * 
	 * @param source
	 *            Stream from which the data is read.
	 * @param buffer
	 *            the buffer into which the data is read.
	 * @param offset
	 *            the start offset in array <code>buffer</code> at which the
	 *            data is written.
	 * @param len
	 *            maximum length of the bytes read.
	 * 
	 * @return the total number of bytes read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of the
	 *         stream has been reached.
	 * 
	 * @throws IOException
	 *             If the first byte cannot be read for any reason other than
	 *             end of file, or if the input stream has been closed, or if
	 *             some other I/O error occurs.
	 * @exception NullPointerException
	 *                If <code>b</code> is <code>null</code>.
	 * @exception IndexOutOfBoundsException
	 *                If <code>off</code> is negative, <code>len</code> is
	 *                negative, or <code>len</code> is greater than
	 *                <code>b.length - off</code>
	 * 
	 * @since 1.0.8
	 */
	public static int tryReadFully(final InputStream source,
			final byte[] buffer, final int offset, final int len)
			throws IOException {
		if (len < 0) {
			throw new IndexOutOfBoundsException("len [" + len + "] < 0");
		}
		int n = 0;
		while (n < len) {
			final int count = source.read(buffer, offset + n, len - n);
			if (count < 0) {
				if (n == 0) {
					n = count;
				}
				break;
			}
			n += count;
		}
		return n;
	}

	/**
	 * Utility class: shouldn't be instantiated.
	 */
	private StreamUtils() {

	}
}
