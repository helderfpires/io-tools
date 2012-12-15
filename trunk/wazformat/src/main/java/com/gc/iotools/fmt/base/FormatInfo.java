package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Gabriele Contini. This source code is released
 * under the BSD License.
 */

public final class FormatInfo {

	private final String description;

	private final FormatId formatId;

	private final String[] mimeType;

	public FormatInfo(final FormatId formatId, final String[] mimeType,
			final String[] extensions, final String description) {
		this.formatId = formatId;
		this.mimeType = mimeType;
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public FormatEnum getFormat() {
		return this.formatId.format;
	}

	public FormatId getFormatId() {
		return this.formatId;
	}

	public String[] getMimeType() {
		return this.mimeType;
	}

	public String getVersion() {
		return this.formatId.version;
	}

}
