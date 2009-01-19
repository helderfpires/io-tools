package com.gc.iotools.fmt.base;

public final class FormatId {

	public final FormatEnum format;
	public final String version;

	public FormatId(final FormatEnum format, final String version) {
		this.format = format;
		this.version = version;
	}
}
