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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * A <code>FileBufferedInputStream</code> adds functionality to another input
 * stream namely, the ability to buffer the input and to support the
 * <code>mark</code> and <code>reset</code> methods.
 * <p>
 * It is very similar to <code>java.io.BufferInputStream</code> but the buffer
 * is kept on disk instead of memory. This is for supporting <code>mark</code>
 * and <code>reset</code> over a large amount of data where a standard
 * <code>BufferInputStream</code> takes too much memory.
 * </p>
 * <p>
 * It also adds the functionality of marking an <code>InputStream</code> without
 * specifying a mark length, thus allowing a <code>reset</code> after an
 * indefinite length of bytes has been read. Check {@link mark } for details.
 * </p>
 * <p>
 * It's best used when a <i>large</i> sequential read of an
 * <code>InputStream</code> has to be performed and then the content of the
 * stream must be available again. Since it's disk based performances of
 * <code>{@linkplain reset}</code> method are low. When frequent
 * <code>{@linkplain reset}</code> are needed a cache in memory should be used
 * to speed up the process.
 * </p>
 * 
 * @see BufferedInputStream
 * @author dvd.smnt
 * @since 1.0.9
 */
public class FileBufferedInputStream extends InputStream {
	private static final Log LOG = LogFactory
			.getLog(FileBufferedInputStream.class);
	/*
	 * Position of cursor into the marked stream if 0 the cursor is at the
	 * beginning
	 */
	private long currentPosition = 0;
	private InputStream fileIs = null;
	private OutputStream fileOs;
	private int markLimit = 0;
	/*
	 * -1 no marking <br> 0 the mark is at the beginning of the marking stream
	 * <br> >0 need a skip because the mark was in the middle of the marking
	 * stream (multiple marks)<br>
	 */
	private long markPos = 0;
	private final InputStream original;
	private File tempFile = null;

	/**
	 * Creates a <code>FileBufferedInputStream</code> and saves its argument,
	 * the input stream <code>source</code> for later use.
	 * 
	 * @param source
	 *            the underlying input stream.
	 */
	public FileBufferedInputStream(final InputStream source) {
		this.original = source;
	}

	@Override
	public int available() throws IOException {
		int result;
		if (this.fileIs != null) {
			result = this.fileIs.available() + this.original.available();
		} else {
			result = this.original.available();
		}
		return result;
	}

	/**
	 * Closes the underlying <code>InputStream</code> and cleans up temporary
	 * files eventually created.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 * 
	 * @see InputStream#close()
	 */
	@Override
	public void close() throws IOException {
		try {
			if (this.fileIs != null) {
				this.fileIs.close();
			}
		} catch (final IOException e) {
			FileBufferedInputStream.LOG.debug(
					"Problem closing internal inputStream", e);
		}
		cleanup();
		this.original.close();
	}

	/**
	 * <p>
	 * Marks the current position in this input stream. A subsequent call to the
	 * <code>reset</code> method repositions this stream at the last marked
	 * position so that subsequent reads re-read the same bytes.
	 *</p>
	 * <p>
	 * This method extends the original behavior of the class
	 * <code>InputStream</code> allowing to use <i>indefinite</i> marking.
	 * <ul>
	 * <li><code>readLimit > 0</code> The <code>readLimit</code> arguments tells
	 * this input stream to allow that many bytes to be read before the mark
	 * position gets invalidated.</li>
	 * <li><code>readLimit == 0</code> Invalidate the all the current marks and
	 * clean up the temporary files.</li>
	 * <li><code>readLimit < 0 </code> Set up an indefinite mark: reset can be
	 * invoked regardless on how many bytes have been read.</li>
	 * <ul>
	 * </p>
	 * 
	 * @param markLimit
	 *            the maximum limit of bytes that can be read before the mark
	 *            position becomes invalid. If negative allows <i>indefinite</i>
	 *            marking (the mark never becomes invalid).
	 * 
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public synchronized void mark(final int markLimit) {
		final boolean mark = (markLimit != 0);
		if (mark) {
			try {
				if (this.tempFile == null) {
					this.tempFile = File.createTempFile("iotools-filebufferis",
							".tmp");
					this.tempFile.deleteOnExit();
					this.currentPosition = 0;
				}
				this.fileOs = new FileOutputStream(this.tempFile, true);
			} catch (final FileNotFoundException e) {
				throw new IllegalStateException("File [" + this.tempFile
						+ "] not found. maybe "
						+ "temporary directory was cleaned.", e);
			} catch (final IOException e) {
				throw new IllegalStateException("Problem creating temporary "
						+ "file", e);
			}
			this.markPos = this.currentPosition;
		} else {
			// if there is a mark active and we're not reading from inner stream
			// do cleanup
			if ((this.fileIs == null) && (this.markLimit != 0)) {
				cleanup();
			}
		}
		this.markLimit = markLimit;
	}

	/**
	 * Tests if this input stream supports the <code>mark</code> and
	 * <code>reset</code> methods.
	 * 
	 * @see java.io.InputStream#markSupported()
	 * @return Always return <code>true</code> for this class.
	 **/
	@Override
	public boolean markSupported() {
		return true;
	}

	/**
	 * See the general contract of the <code>read</code> method of
	 * <code>InputStream</code>.
	 * 
	 * @return the next byte of data, or <code>-1</code> if the end of the
	 *         stream is reached.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[1];
		final int n = this.read(buf);
		final int result = (n > 0 ? buf[0] : n);
		return result;
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException("b.length[" + b.length
					+ "] offset[" + off + "] length[" + len + "");
		} else if (len == 0) {
			return 0;
		}
		final boolean markActive = (this.markLimit != 0);

		int n;
		if (this.fileIs != null) {
			n = this.fileIs.read(b, off, len);
			if (n < 0) {
				// switch to original stream reads.
				this.fileIs.close();
				this.fileIs = null;
				if (!markActive) {
					// if no mark active cleanup
					cleanup();
				}
				n = readAndWriteToOs(b, off, len, markActive);
			} else {
				this.currentPosition += n;
			}
		} else {
			n = readAndWriteToOs(b, off, len, markActive);
		}
		return n;
	}

	@Override
	public synchronized void reset() throws IOException {
		if (this.tempFile == null) {
			throw new IOException("Restet to an invalid mark");
		}
		this.fileOs.close();
		this.fileOs = null;
		this.fileIs = new FileInputStream(this.tempFile);
		if (this.markPos > 0) {
			this.fileIs.skip(this.markPos);
		}
		this.currentPosition = this.markPos;
		this.markLimit = 0;
	}

	@Override
	public long skip(final long n) throws IOException {
		long result = 0;
		if (this.fileIs != null) {
			final long skipped = this.fileIs.skip(n);
			this.currentPosition += skipped;
			if (((this.currentPosition - this.markPos) > this.markLimit)
					&& (this.markLimit > 0)) {
				// current mark limit has passed: discard existing marks and
				// cleanup
				this.fileIs.close();
				this.fileIs = null;
				cleanup();
			}
			if (skipped < n) {
				skipAndCopy(n - skipped);
			}
		} else {
			if (this.markLimit != 0) {
				skipAndCopy(n);
			} else {
				// fileIs == null && ! markActive: simply skip
				result = this.original.skip(n);
			}
		}
		return result;
	}

	private void cleanup() {
		if (this.fileOs != null) {
			try {
				this.fileOs.close();
			} catch (final IOException e) {
				FileBufferedInputStream.LOG.debug("Exception closing the "
						+ "fileOutputStream", e);
			}
			this.fileOs = null;
		}

		if (this.tempFile != null) {
			final boolean del = this.tempFile.delete();
			if (!del) {
				FileBufferedInputStream.LOG.warn("can not delete ["
						+ this.tempFile.getName() + "]");
			}
			this.tempFile = null;
		}
	}

	private int readAndWriteToOs(final byte[] b, final int off, final int len,
			final boolean markActive) throws IOException {
		final int n = this.original.read(b, off, len);
		if ((n > 0) && markActive) {
			this.currentPosition += n;
			if ((this.markLimit < 0)
					|| ((this.markLimit > 0) && ((this.currentPosition - this.markPos) < this.markLimit))) {
				this.fileOs.write(b, off, n);
			} else {
				// if markLimit exceeded invalidate the mark.
				this.markLimit = 0;
				cleanup();
			}
		}
		return n;
	}

	private void skipAndCopy(final long n) throws IOException {
		final byte[] buf = new byte[8192];

		for (long i = 0, readed = 0; (i < n) && (readed >= 0); i += readed) {
			final int maxLen = (int) Math.min(buf.length, n - i);
			readed = this.read(buf, 0, maxLen);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.fileIs != null) {
			this.fileIs.close();
			this.fileIs = null;
		}
		cleanup();
	}

}
