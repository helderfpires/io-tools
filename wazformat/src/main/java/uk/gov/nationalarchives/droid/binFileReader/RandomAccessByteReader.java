package uk.gov.nationalarchives.droid.binFileReader;

import java.io.IOException;
import java.io.InputStream;

import com.gc.iotools.stream.is.RandomAccessInputStream;

public final class RandomAccessByteReader extends AbstractByteReader {

	private final RandomAccessInputStream ras;
	private long len = -1;
	private long fileMarker = 0;
	private final FileByteReader fbr;
	public RandomAccessByteReader(final IdentificationFile theIDFile,
			final InputStream stream, FileByteReader fbr) {
		super(theIDFile);
		this.ras = new RandomAccessInputStream(stream);
		this.fbr = fbr;
	}

	public byte[] getbuffer() {
		return null;
	}

	public byte getByte(final long fileIndex) {
		boolean exc = false;
		byte expR = 0;
		try {
			expR = fbr.getByte(fileIndex);
		} catch (Exception e) {
			exc = true;
		}
		if (fileIndex > getNumBytes()) {
			throw new ArrayIndexOutOfBoundsException("Read position["
					+ fileIndex + "] is above EOF");
		}
		byte result;
		try {
			if (this.len - 1 == fileIndex) {
				System.out.println("stop here");
			}
			this.ras.seek(fileIndex);
			final int res = this.ras.read();
			if (res < 0) {
				if (!exc) {
					System.out.println("Exception1 [" + fileIndex
							+ "] result[" + expR + "] read[" + res
							+ "] fsize [" + this.len + "]");
				}
				throw new ArrayIndexOutOfBoundsException("Read position["
						+ fileIndex + "] is above EOF");
			} else {
				result = (byte) res;
				if (result != expR) {
					System.out.println("idx [" + fileIndex + "]fbr [" + expR
							+ "] lett[" + result + "]");
				}
			}
		} catch (final IOException e) {
			throw new IllegalStateException("Read position[" + fileIndex
					+ "] had exception.", e);

		}
		return result;
	}

	public long getFileMarker() {
		fbr.getFileMarker();
		return this.fileMarker;
	}

	public long getNumBytes() {
		if (this.len <= 0) {
			try {
				this.ras.seek(0);
				this.len = this.ras.skip(Long.MAX_VALUE);
			} catch (final IOException e) {
				throw new IllegalStateException(
						"Problem reading number of bytes", e);
			}
		}
		if (this.len != fbr.getNumBytes()) {
			System.out.println("Different len [" + len + "] fbr["
					+ fbr.getNumBytes() + "]");
		}
		return this.len;
	}

	public void setFileMarker(final long markerPosition) {
		this.fileMarker = markerPosition;
		fbr.setFileMarker(markerPosition);
	}

}
