package com.gc.iotools.stream.is;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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
 * Basically it strips the initial bytes of the stream until a sequence of bytes
 * equal to <code>startMarker</code> is found.
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
 * <p>
 * The class has three operational modes. They can be selected invoking the
 * constructor with four parameters. These two modes affect how this class
 * handles multiple chunks in a file.
 * </p>
 * <ul>
 * <li><code>automaticFetch=true</code> <i>(default)</i>. After an
 * <code>endMarker</code> is found the stream automatically moves to the next
 * <code>startMarker</code> if found. Usage pattern is shown in the example
 * above.</li>
 * <li><code>automaticFetch=false</code> Each chunk need to be fetched invoking
 * explicitly a <code>{@link #fetchNextChunk()}</code> methods. The stream is
 * initially in an EOF state and <code>{@link #fetchNextChunk()}</code> must be
 * called at first. At this point all the bytes of the stream are shown until an
 * <code>endMarker</code> is found. At this point the ChunkInputStream is in an
 * EOF state until a <code>{@link #fetchNextChunk()}</code> is invoked.</li>
 * <li><code>automaticFetch=false</code> and <code>startMarker=null</code> It is
 * similar to the previous case. It can be used to the src stream on the
 * <code>endMarker</code>s</li>
 * </ul>
 * <p>
 * Example of <code>automaticFetch=false</code> mode:
 * </p>
 * 
 * <pre>
 * InputStream is = new ByteArrayInputStream(&quot;aa start bbb stopfff&quot;.getBytes());
 * ChunckInputStream chunkIs = new ChunkInputStream(is, &quot;rt&quot;.getBytes(), &quot;stop&quot;
 * 		.getBytes(), false, false);
 * while (chunkIs.moveToNextChunk()) {
 * 	byte[] bytes = IOUtils.toByteArray(chunkIs);
 * 	//here bytes contains &quot; bbb &quot;
 * }
 * </pre>
 * 
 * @author dvd.smnt
 * @since 1.0.8
 */
public final class ChunkInputStream extends InputStream {

	private static final int SKIP_BUFFER_SIZE = 2048;

	private final boolean automaticFetch;

	private boolean copyToOuter = false;

	private final boolean showMarkers;

	private final byte[] start;

	private final byte[] stop;

	private final InputStream wrappedIs;

	/**
	 * Constructs a <code>ChunkInputStream</code>.
	 * 
	 * @param src
	 *            Source stream. Must not be <code>null</code>.
	 * @param startMarker
	 *            When this sequence of bytes is found in the <code>src</code>
	 *            stream the bytes of the inner stream are shown until an
	 *            <code>endMarker</code> is found. If this parameter is set to
	 *            <code>null</code> the stream is initially in an EOF state
	 *            until a <code>fetchNextChunk()</code> is performed.
	 * 
	 * @param stopMarker
	 *            when this sequence of bytes is found in the <code>src</code>
	 *            stream the bytes of the inner stream are hidden until a
	 *            <code>startMarker</code> is found. If this parameter is set to
	 *            <code>null</code> the stream is made available until the inner
	 *            stream reaches an EOF.
	 */
	public ChunkInputStream(final InputStream src, final byte[] startMarker,
			final byte[] stopMarker) {
		this(src, startMarker, stopMarker, false,
				((startMarker != null) && (startMarker.length > 0)));
	}

	/**
	 * Gets an instance of the ChunkInputStream. If
	 * <code>startMarker!=null</code> the operating mode is set to
	 * <code>automaticFetch=true</code>
	 * 
	 * @param src
	 *            Source stream. Must not be <code>null</code>.
	 * @param startMarker
	 *            When this sequence of bytes is found in the <code>src</code>
	 *            stream the bytes of the inner stream are shown until an
	 *            <code>endMarker</code> is found. If this parameter is set to
	 *            <code>null</code> the stream is initially in an EOF state
	 *            until a <code>fetchNextChunk()</code> is performed.
	 * 
	 * @param stopMarker
	 *            when this sequence of bytes is found in the <code>src</code>
	 *            stream the bytes of the inner stream are hidden until a
	 *            <code>startMarker</code> is found. If this parameter is set to
	 *            <code>null</code> the stream is made available until the inner
	 *            stream reaches an EOF.
	 * @param showMarkers
	 *            if set to <code>true</code> start and end markers are shown in
	 *            the outer stream.
	 * @param automaticFetch
	 *            enables automatic fetching of <code>startMarker</code>s. If
	 *            <code>false</code> <code>startMarker</code>s must be fetched
	 *            manually invoking <code>{@link #fetchNextChunk()}</code>
	 * 
	 * @see fetchNextChunk()
	 */
	public ChunkInputStream(final InputStream src, final byte[] startMarker,
			final byte[] stopMarker, final boolean showMarkers,
			final boolean automaticFetch) {
		if (src == null) {
			throw new IllegalArgumentException(
					"Wrapped InputStrem can't be null");
		}
		this.start = (startMarker == null ? new byte[0] : startMarker);
		this.stop = (stopMarker == null ? new byte[0] : stopMarker);
		this.wrappedIs = new BufferedInputStream(src);
		this.automaticFetch = automaticFetch;
		if ((this.start.length == 0) && automaticFetch) {
			throw new IllegalArgumentException(
					"It's not possible to specify " + "a startMarker"
							+ (startMarker == null ? "=null" : ".size=0")
							+ " and" + " automaticFetch=[" + automaticFetch
							+ "]");
		}
		this.showMarkers = showMarkers;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int available() throws IOException {
		findStartMarker();
		return this.wrappedIs.available();
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void close() throws IOException {
		this.wrappedIs.close();
	}

	/**
	 * This method must be called if <code>automaticFetch=false</code> before
	 * the stream can be used and each time an endMarker has been found to
	 * proceed to next startMarker.
	 * 
	 * @return <code>true</code> if another chunk is available,
	 *         <code>false</code> otherwise.
	 * @throws IOException
	 *             exception thrown if it is impossible to read from the inner
	 *             stream for some unknown reason.
	 */
	public boolean fetchNextChunk() throws IOException {
		if (this.automaticFetch) {
			throw new IllegalStateException(
					"this method shouldn't be called when automaticFetch ["
							+ this.automaticFetch + "]");
		}
		this.copyToOuter = moveToNextStartMarker();
		return this.copyToOuter;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public synchronized void mark(final int readlimit) {
		this.wrappedIs.mark(readlimit);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean markSupported() {
		return this.wrappedIs.markSupported();
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int read() throws IOException {
		final byte[] buf = new byte[1];
		final int rd = read(buf);
		int result = buf[0];
		if (rd < 0) {
			result = rd;
		}
		return result;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int read(final byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException("b.length[" + b.length
					+ "] offset[" + off + "] length[" + len + "");
		} else if (len == 0) {
			return 0;
		}

		findStartMarker();
		int ret;
		if (this.copyToOuter) {
			if (this.stop.length > 0) {
				final int readSize = len - off + this.stop.length;
				final byte[] tmpBuffer = new byte[readSize];
				this.wrappedIs.mark(readSize);
				ret = StreamUtils.tryReadFully(this.wrappedIs, tmpBuffer, 0,
						readSize);
				this.wrappedIs.reset();
				if (ret != -1) {
					final int position = ArrayTools.indexOf(tmpBuffer,
							this.stop);
					if (position == -1) {
						// stop marker not found
						ret = Math.min(ret, len - off);
						this.wrappedIs.skip(ret);
						System.arraycopy(tmpBuffer, 0, b, off, ret);
					} else if (position == 0) {
						this.wrappedIs.skip(this.stop.length);
						this.copyToOuter = false;
						ret = this.read(b, off, len);
					} else {
						// position >0
						ret = position;
						final int bytesToSkip = position + this.stop.length;
						this.wrappedIs.skip(bytesToSkip);
						System.arraycopy(tmpBuffer, 0, b, off, position);
						this.copyToOuter = false;
					}
				}
			} else {
				ret = this.wrappedIs.read(b, off, len);
			}
		} else {
			ret = -1;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public synchronized void reset() throws IOException {
		this.wrappedIs.reset();
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public long skip(final long n) throws IOException {
		findStartMarker();
		return super.skip(n);
	}

	private void findStartMarker() throws IOException {
		if (!this.copyToOuter && this.automaticFetch) {
			// if no start marker set copy
			this.copyToOuter = moveToNextStartMarker();
		}
	}

	private boolean moveToNextStartMarker() throws IOException {
		boolean found;
		if (this.start.length == 0) {
			this.wrappedIs.mark(2);
			// if EOF stop.
			found = (this.wrappedIs.read() >= 0);
			this.wrappedIs.reset();
		} else {
			int n;
			found = false;
			final byte[] buffer = new byte[ChunkInputStream.SKIP_BUFFER_SIZE
					+ this.start.length];
			do {
				this.wrappedIs.mark(ChunkInputStream.SKIP_BUFFER_SIZE
						+ this.start.length);
				n = StreamUtils.tryReadFully(this.wrappedIs, buffer, 0,
						SKIP_BUFFER_SIZE + this.start.length);
				if (n > 0) {
					final int pos = ArrayTools.indexOf(ArrayUtils.subarray(
							buffer, 0, n), this.start);
					if (pos >= 0) {
						// found
						found = true;
						this.wrappedIs.reset();
						final int skip = pos
								+ (this.showMarkers ? 0 : this.start.length);
						this.wrappedIs.skip(skip);
					} else {
						// not found
						if (n - this.start.length > 0) {
							this.wrappedIs.reset();
							this.wrappedIs.skip(n - this.start.length);
						}
					}
				}
			} while (!found && (n >= 0));
		}
		return found;
	}
}
