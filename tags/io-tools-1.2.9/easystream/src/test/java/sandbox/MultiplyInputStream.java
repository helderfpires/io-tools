package sandbox;

import java.io.IOException;
import java.io.InputStream;

/**
 * Multiply a given InputStream numOut times.
 * 
 * @author dvd.smnt
 */
public final class MultiplyInputStream {

	private static class Buffer {
		public Buffer(final int ntails) {
		}

	}

	private static class MultInputStream extends InputStream {
		MultInputStream(final int whoami, final Buffer buffer) {
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

	public static InputStream[] multiply(final InputStream input,
			final int numOut) {
		final Buffer buffer = new Buffer(numOut);
		final InputStream[] result = new MultInputStream[numOut];
		for (int i = 0; i < numOut; i++) {
			result[i] = new MultInputStream(i, buffer);
		}
		return result;
	}
}
