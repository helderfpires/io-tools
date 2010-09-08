package sandbox;

import java.io.IOException;
import java.io.InputStream;

/**
 * Multiply a given InputStream numOut times.
 * 
 * @author dvd.smnt
 * 
 */
public final class MultiplyInputStream {

	private static class Buffer {
		private final long streamHead = 0;
		private final long[] streamTails;

		public Buffer(int ntails) {
			this.streamTails = new long[ntails];
		}

		byte[] read(int numStream) {
			return null;
		}

	}

	private static class MultInputStream extends InputStream {
		private final int whoami;
		private final Buffer buffer;

		MultInputStream(int whoami, Buffer buffer) {
			this.whoami = whoami;
			this.buffer = buffer;
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public int read() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	public static InputStream[] multiply(InputStream input, int numOut) {
		final Buffer buffer = new Buffer(numOut);
		InputStream[] result = new MultInputStream[numOut];
		for (int i = 0; i < numOut; i++) {
			result[i] = new MultInputStream(i, buffer);
		}
		return result;
	}
}
