package com.gc.iotools.stream.utils;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.gc.iotools.stream.base.EasyStreamConstants;

/**
 * General utilities for handling streams.
 * 
 * @author dvd.smnt
 * @since 1.0.9
 * @version $Id: StreamUtils.java 463 2013-01-21 23:54:17Z dvd.smnt@gmail.com
 *          $
 */
public final class StreamUtils {
	private static final double ONE_THOUSAND = 1000D;

	/**
	 * Returns a string representing the transfer rate. The unit is chosen
	 * automatically to keep the size of the string small.
	 * 
	 * @param bytes
	 *            bytes transferred
	 * @param milliseconds
	 *            time in milliseconds
	 * @return a string containing the bit rate in a convenient unit.
	 * @since 1.2.2
	 */
	public static String getRateString(final long bytes,
			final long milliseconds) {
		return getRateString(bytes, milliseconds, Locale.getDefault());
	}

	/**
	 * Returns a string representing the transfer rate. The unit is chosen
	 * automatically to keep the size of the string small.
	 * 
	 * @param bytes
	 *            bytes transferred
	 * @param milliseconds
	 *            time in milliseconds
	 * @param locale
	 *            the current locale to get the right decimal separators. If
	 *            null default localroe will be used.
	 * @return a string containing the bit rate in a convenient unit.
	 * @since 1.2.13
	 */
	public static String getRateString(final long bytes,
			final long milliseconds, final Locale locale) {
		final String[] units = new String[] { "Byte", "KB", "MB", "GB" };
		final double bytesSec = (bytes * StreamUtils.ONE_THOUSAND)
				/ milliseconds;
		// log1024(bytesSec)
		final double idx = Math.log(bytesSec)
				/ Math.log(EasyStreamConstants.ONE_KILOBYTE);
		final int intIdx = Math.max(0,
				Math.min((int) Math.floor(idx), units.length - 1));
		final double reducedRate = bytesSec
				/ Math.pow(EasyStreamConstants.ONE_KILOBYTE, intIdx);
		final DecimalFormat df = new DecimalFormat();
		final int ndigit = (int) Math.floor(Math.max(Math.log10(reducedRate),
				0));
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);
		if (locale != null) {
			df.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
		}
		df.setMaximumFractionDigits(Math.max(0, 2 - ndigit));
		return df.format(reducedRate) + " " + units[intIdx] + "/sec";
	}

	/**
	 * <p>
	 * Read a specified amount of bytes from the <i>source</i> InputStream and
	 * place them into the returned byte array.
	 * </p>
	 * <p>
	 * This utility ensures that either <code>size</code> bytes are read or
	 * the end of the stream has been reached. If the end of stream has been
	 * reached the returned array length is the length of the data read.
	 * </p>
	 * 
	 * @param source
	 *            Stream from which the data is read.
	 * @param size
	 *            The maximum length of the data read.
	 * @return byte[] containing the data read from the stream.
	 *         <code>null</code> if the stream was 0 length, or a byte[] whose
	 *         length is the length of the stream if the EOS has been reached.
	 * @exception IOException
	 *                If the first byte cannot be read for any reason other
	 *                than end of file, or if the input stream has been
	 *                closed, or if some other I/O error occurs.
	 * @since 1.0.9
	 * @throws java.io.IOException
	 *             if any.
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
	 * @see InputStream#read(byte[] buf,int off, int len)
	 * @param source
	 *            Stream from which the data is read.
	 * @param buffer
	 *            the buffer into which the data is read.
	 * @param offset
	 *            the start offset in array <code>buffer</code> at which the
	 *            data is written.
	 * @param len
	 *            maximum length of the bytes read.
	 * @return the total number of bytes read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of the
	 *         stream has been reached.
	 * @throws java.io.IOException
	 *             If the first byte cannot be read for any reason other than
	 *             end of file, or if the input stream has been closed, or if
	 *             some other I/O error occurs.
	 * @throws java.lang.NullPointerException
	 *             If <code>b</code> is <code>null</code>.
	 * @throws java.lang.IndexOutOfBoundsException
	 *             If <code>off</code> is negative, <code>len</code> is
	 *             negative, or <code>len</code> is greater than
	 *             <code>b.length - off</code>
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

	private StreamUtils() {
		/*
		 * Utility class: shouldn't be instantiated.
		 */
	}
}
