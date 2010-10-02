package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

public final class FormatInfo {

	private final FormatId formatId;

	private final String[] mimeType;

	private final String[] extensions;

	private final String description;

	public FormatInfo(final FormatId formatId, final String[] mimeType,
			final String[] extensions, final String description) {
		this.formatId = formatId;
		this.mimeType = mimeType;
		this.description = description;
		this.extensions = extensions;
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
