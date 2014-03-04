package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.utils.LogUtils;

/**
 * Store that puts data in memory until threshold size is reach. At that point
 * data is written to the disk.
 * 
 * @author dvd.smnt
 * @since 1.2.0
 * @version $Id: ThresholdStore.java 463 2013-01-21 23:54:17Z
 *          dvd.smnt@gmail.com $
 */
public class ThresholdStore implements SeekableStore {
	private static final int BUF_SIZE = 8192;

	private static final Logger LOG = LoggerFactory
			.getLogger(ThresholdStore.class);

	private RandomAccessFile fileAccess;
	private File fileStorage;
	private final String instantiationPath;
	private final MemoryStore ms = new MemoryStore();
	private long position = 0;
	private long size = 0;

	private final int treshold;

	/**
	 * <p>
	 * Constructor for ThresholdStore.
	 * </p>
	 * 
	 * @param treshold
	 *            a int.
	 */
	public ThresholdStore(final int treshold) {
		this.treshold = treshold;
		this.instantiationPath = LogUtils.getCaller(ThresholdStore.class, 5);
	}

	/**
	 * <p>
	 * Constructor for ThresholdStore.
	 * </p>
	 * 
	 * @param treshold
	 *            a int.
	 * @param file
	 *            a {@link java.io.File} object.
	 */
	public ThresholdStore(final int treshold, final File file) {
		this.treshold = treshold;
		this.fileStorage = file;
		this.instantiationPath = LogUtils.getCaller(ThresholdStore.class, 5);
	}

	/** {@inheritDoc} */
	@Override
	public void cleanup() {
		this.size = 0;
		this.position = 0;
		this.ms.cleanup();
		if (this.fileAccess != null) {
			try {
				this.fileAccess.close();
			} catch (final IOException e) {
				ThresholdStore.LOG.warn(
						"Exception in closing the temporary "
								+ "stream associated to file ["
								+ this.fileStorage.getName() + "] it "
								+ "is possible to continue but some"
								+ " resources are not released.", e);
			}
			this.fileAccess = null;
		}
		if (this.fileStorage != null) {
			// FileCleaningTracker ftc=new FileCleaningTracker();
			// ftc.exitWhenFinished()

			final boolean deleted = this.fileStorage.delete();
			if (deleted) {
				this.fileStorage = null;
			} else {
				this.fileStorage.deleteOnExit();
				ThresholdStore.LOG.warn("Temporary file ["
						+ this.fileStorage.getName()
						+ "] was not deleted. It "
						+ "is possible to continue but some"
						+ " resources are not released. Instantiation path ["
						+ this.instantiationPath + "]");
			}
		}
	}

	/**
	 * {@inheritDoc} Clean up the temporary files eventually open.
	 */
	@Override
	protected void finalize() throws Throwable {
		cleanup();
	}

	/** {@inheritDoc} */
	@Override
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
		this.position += Math.max(result, 0);
		return result;
	}

	/**
	 * <p>
	 * Getter for the field <code>size</code>.
	 * </p>
	 * 
	 * @return a long.
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * <p>
	 * Getter for the field <code>treshold</code>.
	 * </p>
	 * 
	 * @return a int.
	 */
	public int getTreshold() {
		return this.treshold;
	}

	/** {@inheritDoc} */
	@Override
	public void put(final byte[] bytes, final int offset, final int length)
			throws IOException {
		if (length <= 0) {
			throw new IllegalArgumentException("lenght = [" + length + "]");
		}
		if (this.size + length < this.treshold) {
			this.ms.put(bytes, offset, length);
		} else {
			if (this.size < this.treshold) {
				// empty the memory buffer and init the file buffer
				if (this.fileStorage == null) {
					this.fileStorage = File.createTempFile("iotools-storage",
							".tmp");
				}
				this.fileAccess = new RandomAccessFile(this.fileStorage, "rw");
				final byte[] buffer = new byte[ThresholdStore.BUF_SIZE];
				this.ms.seek(0);
				int len;
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

	/** {@inheritDoc} */
	@Override
	public void seek(final long position) throws IOException {
		// if already in place do nothing.
		if (this.position != position) {
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
	}

	/**
	 * <p>
	 * Setter for the field <code>position</code>.
	 * </p>
	 * 
	 * @param position
	 *            a long.
	 */
	public void setPosition(final long position) {
		this.position = position;
	}

	/**
	 * {@inheritDoc} Provides a String representation of the state of the
	 * Store for debugging purposes.
	 */
	@Override
	public String toString() {
		String str = this.getClass().getSimpleName() + "[pos="
				+ this.position + ",size=" + this.size;
		if (this.fileStorage != null) {
			str += ",file=" + this.fileStorage;
		} else {
			str += ",ms=" + this.ms;
		}
		if (this.fileAccess != null) {
			try {
				str += ",fp=" + this.fileAccess.getFilePointer();
			} catch (final IOException e) {
				// do nothing... here for debugging.
			}
		}
		return str + "]";
	}
}
