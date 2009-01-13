package com.gc.iotools.fmt.deflen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gc.iotools.fmt.base.FormatEnum;

final class Base64Detector extends AbstractFormatDetector {
	private static final int DETECT_LENGTH = 200;
	private static final Pattern PATTERN = Pattern.compile(
			"[\\p{Alnum}/+\\n\\r]*[=]{0,3}", Pattern.MULTILINE);

	public Base64Detector() {
		super(DETECT_LENGTH, FormatEnum.BASE64);
	}

	public boolean detect(final byte[] bytes) {
		final Matcher match = PATTERN.matcher(new String(bytes));
		return match.matches();
	}

}
