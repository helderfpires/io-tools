package uk.gov.nationalarchives.droid.binFileReader;

import java.io.IOException;

import com.gc.iotools.fmt.base.ResettableInputStream;

/**
 * 
 * TODO a 2K buffer at beginning and end of the file will improve performances.
 * 
 * @author dvd.smt
 * 
 */
public final class RandomAccessByteReader extends AbstractByteReader {

	private final ResettableInputStream ras;
	private long position = 0;
	private final long len;
	private long fileMarker = 0;

	public RandomAccessByteReader(final IdentificationFile theIDFile,
			final ResettableInputStream stream) throws IOException {
		super(theIDFile);
		this.ras = stream;
		this.len = this.ras.skip(Long.MAX_VALUE);
		this.ras.resetToBeginning();
	}

	public byte[] getbuffer() {
		return null;
	}

	public byte getByte(final long fileIndex) {
		if (fileIndex > len) {
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
				position++;
			}
		} catch (final IOException e) {
			throw new IllegalStateException("Read position[" + fileIndex
					+ "] had exception.", e);

		}
		return result;
	}

	private void seek(final long fileIndex) throws IOException {
		if (fileIndex > position) {
			this.ras.skip(fileIndex - position);
		} else if (fileIndex < position) {
			this.ras.resetToBeginning();
			this.ras.skip(fileIndex);
		}
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

	public void close() throws IOException {
		this.ras.close();
	}
}
