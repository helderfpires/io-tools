package com.gc.iotools.stream.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * General utilities for handling streams.
 * 
 * @author dvd.smnt
 * @since 1.0.7
 */
public final class StreamUtils {

	/**
	 * <p>
	 * Read bytes from the <i>source</i> InputStream into the
	 * <code>buffer</code>.
	 * </p>
	 * <p>
	 * This utility ensures that either <code>len</code> bytes are readed or the
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
	 * @return the total number of bytes read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of the
	 *         stream has been reached.
	 * 
	 * @exception IOException
	 *                If the first byte cannot be read for any reason other than
	 *                end of file, or if the input stream has been closed, or if
	 *                some other I/O error occurs.
	 * @exception NullPointerException
	 *                If <code>b</code> is <code>null</code>.
	 * @exception IndexOutOfBoundsException
	 *                If <code>off</code> is negative, <code>len</code> is
	 *                negative, or <code>len</code> is greater than
	 *                <code>b.length - off</code>
	 * 
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
				break;
			}
			n += count;
		}
		return n;
	}
}
