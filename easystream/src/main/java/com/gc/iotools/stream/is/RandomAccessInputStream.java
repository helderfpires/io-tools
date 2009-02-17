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
import com.gc.iotools.stream.store.MemoryStore;
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
 * When the <code>RandomAccessInputStream</code> is created, an internal
 * {@linkplain Store} is created. As bytes from the stream are read or skipped,
 * the internal store is refilled as necessary from the source input stream. The
 * implementation can be changed to fit the application needs (cache on disk
 * rather than in memory). </p>
 * <p>
 * It also adds the functionality of marking an <code>InputStream</code> without
 * specifying a mark length, thus allowing a <code>reset</code> after an
 * indefinite length of bytes has been read. Check {@link #mark() } for details.
 * </p>
 * 
 * @since 1.2
 */
public class RandomAccessInputStream extends AbstractInputStreamWrapper {
	private static final int DEFAULT_TRHESHOLD = 32768;
	/**
	 * Position of reading in the source stream.
	 */
	private long sourcePosition = 0;
	/**
	 * Position of read cursor in the RandomAccessInputStream.
	 */
	private long randomAccessIsPosition = 0;
	private long markPosition = 0;
	private long markLimit = -1;
	private final SeekableStore store;

	public RandomAccessInputStream(final InputStream source) {
		this(source, DEFAULT_TRHESHOLD);
	}

	public RandomAccessInputStream(final InputStream source,
			final int threshold) {
		super(source);
		this.store = new ThresholdStore(threshold);
	}

	public RandomAccessInputStream(final InputStream source,
			final SeekableStore store) {
		super(source);
		this.store = store;
	}

	@Override
	public int available() throws IOException {
		return (int)Math.min(sourcePosition
				- randomAccessIsPosition
				+ this.source.available(), Integer.MAX_VALUE);
	}

	@Override
	protected void closeOnce() throws IOException {
		this.store.cleanup();
		this.source.close();
	}

	@Override
	public int innerRead(final byte[] b, final int off, final int len)
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
			 * randomAccessIsPosition > sourcePosition. A reset() was called on
			 * the StorageBufInputStream. just read from source don't buffer.
			 */
			final int efflen = (int) Math.min(len,
					this.randomAccessIsPosition - this.sourcePosition);
			n = this.source.read(b, off, efflen);
			this.sourcePosition += Math.max(n, 0);
		}
		return n;
	}

	@Override
	public synchronized void mark(final int readlimit) {
		this.markLimit = readlimit;
		this.markPosition = this.randomAccessIsPosition;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void reset() throws IOException {
		this.randomAccessIsPosition = this.markPosition;
		store.seek(markPosition);
	}

	public void seek(long position) throws IOException {
		final long len = position - randomAccessIsPosition;
		if (len > 0) {
			long n = skip(len);
			if (n < len) {
				throw new IOException("Requested seek to [" + position
						+ "] but the stream is only ["
						+ (n + randomAccessIsPosition) + "] bytes long.");
			}
		} else {
			this.randomAccessIsPosition = position;
			this.store.seek(position);
		}
	}
}
