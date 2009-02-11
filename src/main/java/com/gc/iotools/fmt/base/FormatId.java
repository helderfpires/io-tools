package com.gc.iotools.fmt.base;

public final class FormatId {

	@Override
	public String toString() {
		final String str = "Format:" + format.getName();
		return str + (version == null ? "" : (",version:" + version));
	}

	public final FormatEnum format;
	public final String version;

	public FormatId(final FormatEnum format, final String version) {
		if (format == null) {
			throw new IllegalArgumentException("Format can't be null");
		}
		this.format = format;
		this.version = version;
	}

}
