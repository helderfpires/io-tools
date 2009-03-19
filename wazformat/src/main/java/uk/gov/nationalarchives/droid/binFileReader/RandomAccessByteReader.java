package uk.gov.nationalarchives.droid.binFileReader;

import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.fmt.base.ResettableInputStream;

/**
 * 
 * TODO a 2K buffer at beginning and end of the file will improve performances.
 * 
 * @author dvd.smt
 * 
 */
public final class RandomAccessByteReader extends AbstractByteReader {

	private static long getSize(final InputStream stream) throws IOException {
		long curPos = 0;
		int readLen = 0;
		final byte[] buf = new byte[8192];
		while (readLen >= 0) {
			readLen = stream.read(buf, 0, buf.length);
			if (readLen > 0) {
				curPos += readLen;
			}
		}
		return curPos;
	}

	private final ResettableInputStream ras;
	private long position = 0;
	private final long len;

	private long fileMarker = 0;

	public RandomAccessByteReader(final IdentificationFile theIDFile,
			final ResettableInputStream stream) throws IOException {
		super(theIDFile);
		this.ras = stream;
		this.len = getSize(stream);
		this.ras.resetToBeginning();
	}

	public void close() throws IOException {
		this.ras.close();
	}

	public byte[] getbuffer() {
		return null;
	}

	public byte getByte(final long fileIndex) {
		if (fileIndex > this.len) {
			throw new ArrayIndexOutOfBoundsException("Read position["
					+ fileIndex + "] is above EOF");
		}
		byte result;
		try {
			seek(fileIndex);
			final int res = this.ras.read();
			if (res < 0) {
				throw new ArrayIndexOutOfBoundsException("Read position["
						+ fileIndex + "] is above EOF");
			} else {
				result = (byte) res;
				this.position++;
			}
		} catch (final IOException e) {
			throw new IllegalStateException("Read position[" + fileIndex
					+ "] had exception.", e);

		}
		return result;
	}

	public long getFileMarker() {
		return this.fileMarker;
	}

	public long getNumBytes() {
		return this.len;
	}

	public void setFileMarker(final long markerPosition) {
		this.fileMarker = markerPosition;
	}

	private void seek(final long fileIndex) throws IOException {
		if (fileIndex > this.position) {
			this.ras.skip(fileIndex - this.position);
		} else if (fileIndex < this.position) {
			this.ras.resetToBeginning();
			if (fileIndex > 0) {
				this.ras.skip(fileIndex);
			}
		}
		this.position = fileIndex;
	}
}
