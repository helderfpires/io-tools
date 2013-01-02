package com.gc.iotools.stream.store;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;

/**
 * <p>OnOffStore class.</p>
 *
 * @author gcontini
 * @version $Id$
 */
public class OnOffStore implements SeekableStore {
	private boolean canEnable = true;
	private boolean enabled = true;
	private final SeekableStore store;

	/**
	 * <p>Constructor for OnOffStore.</p>
	 *
	 * @param store a {@link com.gc.iotools.stream.store.SeekableStore} object.
	 */
	public OnOffStore(final SeekableStore store) {
		this.store = store;
	}

	/** {@inheritDoc} */
	@Override
	public void cleanup() {
		this.store.cleanup();
	}

	/**
	 * <p>enable</p>
	 *
	 * @param enable a boolean.
	 */
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

	/** {@inheritDoc} */
	@Override
	public int get(final byte[] bytes, final int offset, final int length)
			throws IOException {

		final int num = this.store.get(bytes, offset, length);
		if (!this.enabled && num == 0) {
			cleanup();
		}
		return num;
	}

	/** {@inheritDoc} */
	@Override
	public void put(final byte[] bytes, final int offset, final int length)
			throws IOException {
		if (this.enabled) {
			this.store.put(bytes, offset, length);
		} else {
			this.canEnable = false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void seek(final long position) throws IOException {
		this.store.seek(position);
	}
}
