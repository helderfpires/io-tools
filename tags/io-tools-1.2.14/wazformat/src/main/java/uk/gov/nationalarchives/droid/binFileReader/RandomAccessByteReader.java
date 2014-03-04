package uk.gov.nationalarchives.droid.binFileReader;

import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.fmt.base.ResettableInputStream;

/**
 * @author dvd.smt
 */
public final class RandomAccessByteReader extends AbstractByteReader {

	private static final int BUFFER_MAX_SIZE = 4096;

	private final byte[] buffer = new byte[BUFFER_MAX_SIZE];

	private long bufferStartOffset = 0;
	private long fileMarker = 0;

	private final long len;
	private long position = 0;
	private final ResettableInputStream ras;

	public RandomAccessByteReader(final IdentificationFile theIDFile,
			final ResettableInputStream stream) throws IOException {
		super(theIDFile);
		this.ras = stream;
		this.len = getSize(stream);
		this.ras.resetToBeginning();
		this.position = 0;
	}

	@Override
	public byte getByte(final long fileIndex) {
		if (fileIndex >= this.len) {
			throw new ArrayIndexOutOfBoundsException("Read position["
					+ fileIndex + "] is above EOF");
		}
		byte result;
		if (this.bufferStartOffset <= fileIndex
				&& fileIndex < (this.bufferStartOffset + this.buffer.length)) {
			result = this.buffer[(int) (fileIndex - this.bufferStartOffset)];
		} else {
			try {
				final long startIndex = Math.max(0, fileIndex
						- (BUFFER_MAX_SIZE / 2));
				if (this.position > startIndex
						&& this.position < (startIndex + BUFFER_MAX_SIZE)) {
					// some part of the buffer was already read.
					final int copyLen = (int) (this.position - startIndex);
					System.arraycopy(this.buffer,
							(int) (startIndex - this.bufferStartOffset),
							this.buffer, 0, copyLen);
					final int read = readLenBytes(copyLen);
					this.position += read;
				} else {
					seek(startIndex);
					final int read = readLenBytes(0);
					this.position = startIndex + read;
				}
				this.bufferStartOffset = startIndex;
				result = this.buffer[(int) (fileIndex - this.bufferStartOffset)];
			} catch (final IOException e) {
				throw new IllegalStateException("Read position[" + fileIndex
						+ "] had exception.", e);
			}
		}
		return result;
	}

	@Override
	public long getFileMarker() {
		return this.fileMarker;
	}

	@Override
	public long getNumBytes() {
		return this.len;
	}

	private long getSize(final InputStream stream) throws IOException {
		long curPos = 0;
		int readLen = 0;
		final byte[] buf = new byte[8192];
		while (readLen >= 0) {
			readLen = stream.read(buf, 0, buf.length);
			if (curPos < this.buffer.length && readLen >= 0) {
				System.arraycopy(buf, 0, this.buffer, (int) curPos,
						Math.min(readLen, this.buffer.length - (int) curPos));
			}
			if (readLen > 0) {
				curPos += readLen;
			}
		}
		return curPos;
	}

	public int readLenBytes(final int copyLen) throws IOException {
		int read = 0;
		final int len = (this.buffer.length - copyLen);
		while (len - read > 0) {
			final int n = this.ras.read(this.buffer, copyLen + read, len
					- read);
			if (n >= 0) {
				read += n;
			} else {
				break;
			}
		}
		return read;
	}

	private void seek(final long fileIndex) throws IOException {
		if (fileIndex > this.position) {
			final long skip = this.ras.skip(fileIndex - this.position);
			if (skip != fileIndex - this.position) {
				throw new ArrayIndexOutOfBoundsException();
			}
		} else if (fileIndex < this.position) {
			this.ras.resetToBeginning();
			if (fileIndex > 0) {
				final long l = this.ras.skip(fileIndex);
				if (l != fileIndex) {
					throw new IOException();
				}
			}
		}
	}

	@Override
	public void setFileMarker(final long markerPosition) {
		this.fileMarker = markerPosition;
	}
}
