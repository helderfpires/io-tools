package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD License.
 */

public final class FormatId {

	public final FormatEnum format;

	public final String version;

	public FormatId(final FormatEnum format, final String version) {
		if (format == null) {
			throw new IllegalArgumentException("Format can't be null");
		}
		this.format = format;
		this.version = version;
	}

	@Override
	public String toString() {
		final String str = "Format:" + this.format.getName();
		return str
				+ (this.version == null ? "" : (",version:" + this.version));
	}

}
