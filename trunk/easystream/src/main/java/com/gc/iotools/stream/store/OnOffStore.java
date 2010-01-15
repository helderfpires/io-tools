package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008,2010 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.IOException;

public class OnOffStore implements SeekableStore {
	private boolean canEnable = true;
	private boolean enabled = true;
	private final SeekableStore store;

	public OnOffStore(final SeekableStore store) {
		this.store = store;
	}

	public void cleanup() {
		this.store.cleanup();
	}

	public void enable(final boolean enable) {
		if (enable != this.enabled) {
			if (enable) {
				if (!this.canEnable) {
					throw new IllegalStateException(
							"Enable was called but some "
									+ "data was already put on the buffer. "
									+ "Can't reenable.");
				}
			}
			this.enabled = enable;
		}
	}

	public int get(final byte[] bytes, final int offset, final int length)
			throws IOException {

		int num = this.store.get(bytes, offset, length);
		if (!this.enabled && num == 0) {
			cleanup();
		}
		return num;
	}

	public void put(final byte[] bytes, final int offset, final int length)
			throws IOException {
		if (this.enabled) {
			this.store.put(bytes, offset, length);
		} else {
			this.canEnable = false;
		}
	}

	public void seek(final long position) throws IOException {
		this.store.seek(position);
	}
}
