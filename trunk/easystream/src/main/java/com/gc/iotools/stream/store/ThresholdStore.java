package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2009 Davide Simonetti
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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dvd.smnt
 * @since 1.2
 */
public class ThresholdStore implements SeekableStore {
	private static final Logger LOG = LoggerFactory.getLogger(ThresholdStore.class);

	private static final int BUF_SIZE = 8192;

	private final int treshold;
	private File fileStorage;
	private RandomAccessFile fileAccess;
	private long size = 0;
	private long position = 0;
	private final MemoryStore ms = new MemoryStore();

	public ThresholdStore(final int treshold) {
		this.treshold = treshold;
	}

	public void cleanup() {
		this.size = 0;
		this.position = 0;
		this.ms.cleanup();
		if (this.fileAccess != null) {
			try {
				this.fileAccess.close();
			} catch (final IOException e) {
				LOG.warn("Exception in closing the temporary "
						+ "stream associated to file ["
						+ this.fileStorage.getName() + "] it "
						+ "is possible to continue but some"
						+ " resources are not released.", e);
			}
			this.fileAccess = null;
		}
		if (this.fileStorage != null) {
			final boolean deleted = this.fileStorage.delete();
			if (!deleted) {
				this.fileStorage.deleteOnExit();
				LOG.warn("Exception in deleting the temporary " + "file ["
						+ this.fileStorage.getName() + "] it "
						+ "is possible to continue but some"
						+ " resources are not released.");
			}
			this.fileStorage = null;
		}
	}

	public int get(final byte[] bytes, final int offset, final int length)
			throws IOException {
		int result;
		if (this.size < this.treshold) {
			result = this.ms.get(bytes, offset, length);
		} else {
			if (this.position != this.fileAccess.getFilePointer()) {
				this.fileAccess.seek(this.position);
			}
			result = this.fileAccess.read(bytes, offset, length);
		}
		this.position += (result > 0 ? result : 0);
		return result;
	}

	public void put(final byte[] bytes, final int offset, final int length)
			throws IOException {
		if (this.size + length < this.treshold) {
			this.ms.put(bytes, offset, length);
		} else {
			if (this.size < this.treshold) {
				// empty the memory buffer and init the file buffer
				this.fileStorage = File.createTempFile("iotools-storage",
						"tmp");
				this.fileAccess = new RandomAccessFile(this.fileStorage, "rw");
				int len;
				final byte[] buffer = new byte[BUF_SIZE];
				while ((len = this.ms.get(buffer, 0, buffer.length)) > 0) {
					this.fileAccess.write(buffer, 0, len);
				}
				this.ms.cleanup();
			} else {
				final long fp = this.fileAccess.getFilePointer();
				if (fp != this.size) {
					this.fileAccess.seek(this.size);
				}
			}
			this.fileAccess.write(bytes, offset, length);
		}
		this.size += length;
	}

	public void seek(final long position) throws IOException {
		this.position = position;
		if (position <= this.size) {
			if (this.size < this.treshold) {
				this.ms.seek(position);
			} else {
				final long fp = this.fileAccess.getFilePointer();
				if (fp != position) {
					this.fileAccess.seek(position);
				}
			}
		} else {
			// seek outside the buffer
			throw new IOException("Seek at posiotion [" + position
					+ "]outside buffer size[" + this.size + "]");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		cleanup();
	}

}
