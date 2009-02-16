package com.gc.iotools.stream.store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThresholdStore implements SeekableStore {
	private static final Log LOG = LogFactory.getLog(ThresholdStore.class);

	@Override
	protected void finalize() throws Throwable {
		cleanup();
	}

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
			} catch (IOException e) {
				LOG.warn("Exception in closing the temporary "
						+ "stream associated to file ["
						+ this.fileStorage.getName() + "] it "
						+ "is possible to continue but some"
						+ " resources are not released.", e);
			}
			this.fileAccess = null;
		}
		if (this.fileStorage != null) {
			boolean deleted = this.fileStorage.delete();
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
				this.fileAccess.seek(position);
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
				byte[] buffer = new byte[BUF_SIZE];
				while ((len = ms.get(buffer, 0, buffer.length)) > 0) {
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

}
