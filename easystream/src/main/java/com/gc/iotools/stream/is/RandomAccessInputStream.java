package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2009 Davide Simonetti
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

import com.gc.iotools.stream.base.AbstractInputStreamWrapper;
import com.gc.iotools.stream.store.SeekableStore;
import com.gc.iotools.stream.store.Store;
import com.gc.iotools.stream.store.ThresholdStore;

/**
 * <p>
 * A <code>RandomAccessInputStream</code> adds functionality to another input
 * stream-namely, the ability to buffer the input, allowing it to be read
 * multiple times, and to support the <code>mark</code> and <code>reset</code>
 * methods.
 * </p>
 * <p>
 * When the <code>RandomAccessInputStream</code> is created, an internal
 * {@linkplain Store} is created. As bytes from the stream are read or skipped,
 * the internal <code>store</code> is refilled as necessary from the source
 * input stream. The implementation of <code>store</code> can be changed to fit
 * the application needs: cache on disk rather than in memory. The default
 * <code>store</code> implementation caches 64K in memory and then write the
 * content on disk.
 * </p>
 * <p>
 * It also adds the functionality of marking an <code>InputStream</code> without
 * specifying a mark length, thus allowing a <code>reset</code> after an
 * indefinite length of bytes has been read. Check the {@link #mark()} javadoc
 * for details.
 * </p>
 * 
 * @author dvd.smnt
 * @see com.gc.iotools.stream.store.Store
 * @since 1.2
 */
public class RandomAccessInputStream extends AbstractInputStreamWrapper {
	private static final int DEFAULT_TRHESHOLD = 32768 * 2;
	/**
	 * Position of reading in the source stream.
	 */
	protected long sourcePosition = 0;
	/**
	 * Position of read cursor in the RandomAccessInputStream.
	 */
	protected long randomAccessIsPosition = 0;
	protected long markPosition = 0;
	protected long markLimit = 0;
	protected final SeekableStore store;

	/**
	 * 
	 * @param source
	 *            The underlying input stream.
	 */
	public RandomAccessInputStream(final InputStream source) {
		this(source, DEFAULT_TRHESHOLD);
	}

	/**
	 * <p>
	 * Creates a <code>RandomAccessInputStream</code> with the specified
	 * treshold, and saves its argument, the input stream <code>source</code>,
	 * for later use.
	 * </p>
	 * <p>
	 * When data read under threshold size <code>treshold</code> is kept into
	 * memory. Over this size it is placed on disk.
	 * </p>
	 * 
	 * @see ThresholdStore
	 * @param source
	 *            The underlying input stream.
	 * @param threshold
	 *            Maximum number of bytes to keep into memory.
	 */
	public RandomAccessInputStream(final InputStream source,
			final int threshold) {
		super(source);
		this.store = new ThresholdStore(threshold);
	}

	/**
	 * 
	 * 
	 * @param source
	 *            The underlying input stream.
	 * @param store
	 */
	public RandomAccessInputStream(final InputStream source,
			final SeekableStore store) {
		super(source);
		if (store == null) {
			throw new IllegalArgumentException("store can't be null.");
		}
		this.store = store;
	}

	@Override
	public int available() throws IOException {
		return (int) Math.min(this.sourcePosition
				- this.randomAccessIsPosition + this.source.available(),
				Integer.MAX_VALUE);
	}

	/**
	 * Return the underlying store where the cache of data is kept.
	 * 
	 * @return The underlying store that caches data.
	 */
	public Store getStore() {
		return this.store;
	}

	/**
	 * <p>
	 * Marks the current position in this input stream. A subsequent call to the
	 * {@linkplain #reset()} method repositions this stream at the last marked
	 * position so that subsequent reads re-read the same bytes.
	 *</p>
	 * <p>
	 * This method extends the original behavior of the class
	 * <code>InputStream</code> allowing to use <i>indefinite</i> marking.
	 * <ul>
	 * <li><code>readLimit&gt; 0</code> The <code>readLimit</code> arguments
	 * tells this input stream to allow that many bytes to be read before the
	 * mark position gets invalidated.</li>
	 * <li><code>readLimit == 0</code> Invalidate the all the current marks and
	 * clean up the temporary files.</li>
	 * <li><code>readLimit &lt; 0 </code> Set up an indefinite mark: reset can
	 * be invoked regardless on how many bytes have been read.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param readLimit
	 *            the maximum limit of bytes that can be read before the mark
	 *            position becomes invalid. If negative allows <i>indefinite</i>
	 *            marking (the mark never becomes invalid).
	 * 
	 * @see RandomAccessInputStream#reset()
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public synchronized void mark(final int readLimit) {
		this.markLimit = readLimit;
		this.markPosition = this.randomAccessIsPosition;
	}

	/**
	 * Overrides the <code>markSupported()</code> method of the
	 * <code>InputStream</code> class.
	 * 
	 * @see InputStream#markSupported();
	 * @return Always returns <code>true</code> for this kind of stream.
	 */
	@Override
	public boolean markSupported() {
		return true;
	}

	/**
	 * <p>
	 * Repositions this stream to the position at the time the <code>mark</code>
	 * method was last called on this input stream.
	 * </p>
	 * <p>
	 * After invoking <code>mark</code> it can be invoked multiple times and it
	 * always reset the stream at the previously marked position.
	 * </p>
	 * 
	 * @exception IOException
	 *                if this stream has not been marked or if the mark has been
	 *                invalidated.
	 * @see RandomAccessInputStream#mark(int)
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public synchronized void reset() throws IOException {
		if ((this.markLimit < 0)
				|| (this.randomAccessIsPosition - this.markPosition <= this.markLimit)) {
			this.randomAccessIsPosition = this.markPosition;
			this.store.seek(this.markPosition);
		} else {
			throw new IOException("Reset to an invalid mark.");
		}

	}

	/**
	 * {@inheritDoc}.
	 */
	public void seek(final long position) throws IOException {
		if (position < 0) {
			throw new IllegalArgumentException("Seek to negative position ["
					+ position + "]");
		}
		final long len = position - this.randomAccessIsPosition;
		if (len > 0) {
			final long n = skip(len);
			if (n < len) {
				throw new IOException("Requested seek to [" + position
						+ "] but the stream is only ["
						+ (n + this.randomAccessIsPosition) + "] bytes long.");
			}
		} else if (len < 0) {
			// if len==0 already at the right place. Do Nothing.
			this.randomAccessIsPosition = position;
			this.store.seek(position);
		}
	}

	/**
	 * Provides a String representation of the state of the stream for debugging
	 * purposes.
	 * 
	 * @return String that represent the state.
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[randomAccPos="
				+ this.randomAccessIsPosition + ",srcPos="
				+ this.sourcePosition + ", store=" + this.store + "]";
	}

	@Override
	protected void closeOnce() throws IOException {
		this.store.cleanup();
		this.source.close();
	}

	@Override
	protected int innerRead(final byte[] b, final int off, final int len)
			throws IOException {
		int n;
		if (this.sourcePosition == this.randomAccessIsPosition) {
			// source and external same position so read from source.
			n = super.source.read(b, off, len);
			if (n > 0) {
				this.sourcePosition += n;
				this.randomAccessIsPosition += n;
				this.store.put(b, off, n);
			}
		} else if (this.randomAccessIsPosition < this.sourcePosition) {
			// resetIS has been called. Read from buffer;n
			final int efflen = (int) Math.min(len, this.sourcePosition
					- this.randomAccessIsPosition);
			n = this.store.get(b, off, efflen);
			if (n <= 0) {
				throw new IllegalStateException(
						"Problem reading from buffer. Expecting bytes ["
								+ efflen + "] but buffer is empty.");
			}
			this.randomAccessIsPosition += n;
		} else {
			/*
			 * shouldn't be here. refactor throw exception
			 * randomAccessIsPosition > sourcePosition. A reset() was called on
			 * the StorageBufInputStream. just read from source don't buffer.
			 */
			// final int efflen = (int) Math.min(len,
			// this.randomAccessIsPosition - this.sourcePosition);
			// n = this.source.read(b, off, efflen);
			// this.sourcePosition += Math.max(n, 0);
			throw new IllegalStateException("randomAccessIsPosition["
					+ this.randomAccessIsPosition + "] > sourcePosition["
					+ this.sourcePosition + "]");
		}
		return n;
	}

}
