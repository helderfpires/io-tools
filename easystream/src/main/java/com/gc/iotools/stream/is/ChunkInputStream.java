package com.gc.iotools.stream.is;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang.ArrayUtils;

import com.gc.iotools.stream.utils.ArrayTools;
import com.gc.iotools.stream.utils.StreamUtils;

/**
 * <p>
 * This class is useful when you have an <code>InputStream</code> and you want
 * to filter some parts of it basing on its content without reading it into
 * memory.
 * </p>
 * <p>
 * It strips the initial bytes of the stream until a sequence of bytes equal to
 * <code>startMarker</code> is found.
 * 
 * When a sequence of bytes equals to <code>endMarker</code> is found the stream
 * hide the bytes until a new <code>startMarker</code> is found.
 * </p>
 * 
 * The result is that only the bytes between <code>startMarker</code> and
 * <code>endMarker</code> are shown to the outer stream through the
 * <code>read()</code> methods.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * InputStream is = new ByteArrayInputStream(&quot;aa start bbb stopfff&quot;.getBytes());
 * ChunckInputStream chunkIs = new ChunkInputStream(&quot;rt&quot;.getBytes(), &quot;stop&quot;
 * 		.getBytes(), is);
 * byte[] bytes = IOUtils.toByteArray(chunkIs);
 * //here bytes contains &quot; bbb &quot;
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.0.7
 */
public final class ChunkInputStream extends InputStream {
	// private static Log LOGGER = LogFactory.getLog(ChunkInputStream.class);

	private final byte[] start;

	private final byte[] stop;

	private final InputStream wrappedIs;

	private final CircularFifoBuffer buffer;

	private boolean copyToOuter = false;

	public ChunkInputStream(final byte[] startMarker, final byte[] stopMarker,
			final InputStream wrappedIs) {
		if (wrappedIs == null) {
			throw new IllegalArgumentException(
					"Wrapped InputStrem can't be null");
		}
		this.start = (startMarker == null ? new byte[0] : startMarker);
		this.stop = (stopMarker == null ? new byte[0] : stopMarker);
		this.wrappedIs = new BufferedInputStream(wrappedIs);
		final int size = Math.max(this.start.length, this.stop.length);
		this.buffer = new CircularFifoBuffer(size);
	}

	@Override
	public int available() throws IOException {
		findStartMarker();
		return this.wrappedIs.available();
	}

	@Override
	public void close() throws IOException {
		this.wrappedIs.close();
	}

	@Override
	public synchronized void mark(final int readlimit) {
		this.wrappedIs.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return this.wrappedIs.markSupported();
	}

	@Override
	public int read() throws IOException {
		findStartMarker();
		int readed;
		if (this.copyToOuter) {
			readed = this.wrappedIs.read();
			if ((readed >= 0) && (this.stop.length > 0)) {
				final byte bstop = (byte) readed;
				if (bstop == this.stop[0]) {
					this.wrappedIs.mark(this.stop.length + 1);
					final byte[] stopBuffer = fillStopBuffer(new byte[] { bstop });
					final boolean foundEndMarker = Arrays.equals(this.stop,
							stopBuffer);
					this.copyToOuter = !foundEndMarker;
					if (!foundEndMarker) {
						// end marker not found. return normally.
						this.wrappedIs.reset();
					} else {
						readed = this.read();
					}
				}
			}
		} else {
			readed = -1;
		}
		return readed;
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		findStartMarker();
		int ret;
		if (this.copyToOuter) {
			if (this.stop.length > 0) {
				this.wrappedIs.mark(len + this.stop.length);
			}
			ret = this.wrappedIs.read(b, off, len);
			if ((ret != -1) && (this.stop.length > 0)) {
				final byte[] buffer = fillStopBuffer(ArrayUtils.subarray(b, 0,
						ret));
				final int position = ArrayTools.indexOf(buffer, this.stop);
				if (position == -1) {
					this.wrappedIs.reset();
					this.wrappedIs.skip(ret);
				} else {
					// FIXME: if position == 0 this method breaks the contract
					// "at least one byte is readed"
					ret = position;
					this.wrappedIs.reset();
					this.wrappedIs.skip(position + this.stop.length);
					Arrays.fill(b, position, b.length, (byte) 0);
					this.copyToOuter = false;
				}
			}
		} else {
			ret = -1;
		}
		return ret;
	}

	@Override
	public synchronized void reset() throws IOException {
		this.wrappedIs.reset();
	}

	@Override
	public long skip(final long n) throws IOException {
		findStartMarker();
		return super.skip(n);
	}

	/*
	 * Read extra bytes needed to check if a stopMarker has been found in the
	 * end of the reading.
	 */
	private byte[] fillStopBuffer(final byte[] readed) throws IOException {
		final byte[] stopBuffer = new byte[readed.length + this.stop.length - 1];
		if (this.stop.length > 0) {
			System.arraycopy(readed, 0, stopBuffer, 0, readed.length);
			if ((stopBuffer.length - readed.length) > 0) {
				StreamUtils.tryReadFully(this.wrappedIs, stopBuffer,
						readed.length, stopBuffer.length - readed.length);
			}
		}
		return stopBuffer;
	}

	private void findStartMarker() throws IOException {
		// TODO: skip faster, use array reading
		if (!this.copyToOuter) {
			// if no start marker set copy
			boolean found = (this.start.length == 0);
			int n;
			while (!found && ((n = this.wrappedIs.read()) >= 0)) {
				final Byte byt = new Byte((byte) n);
				this.buffer.add(byt);
				@SuppressWarnings("unchecked")
				final Byte[] bytes = (Byte[]) this.buffer
						.toArray(new Byte[this.buffer.size()]);
				final byte[] bytesP = org.apache.commons.lang.ArrayUtils
						.toPrimitive(bytes);
				found = Arrays.equals(this.start, bytesP);
			}
			this.copyToOuter = found;
		}
	}
}
