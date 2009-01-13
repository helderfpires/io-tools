package com.gc.iotools.fmt.deflen;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.gc.iotools.fmt.base.FormatEnum;

public class DefiniteModuleFactory {
	private static final String DEF_CONF = "deflen.properties";
	private final Class enumClazz;
	private final DefiniteLengthModule[] modules;

	DefiniteModuleFactory() {
		this(DEF_CONF, FormatEnum.class);
	}

	DefiniteModuleFactory(final String confFile, final Class enumClass) {
		this.enumClazz = (enumClass == null ? FormatEnum.class : enumClass);
		final String confFile1 = (StringUtils.isBlank(confFile) ? DEF_CONF
				: confFile);
		final InputStream istream = DefiniteModuleFactory.class
				.getResourceAsStream(confFile1);
		if (istream == null) {
			throw new IllegalArgumentException("Configuration file ["
					+ confFile1 + "] not found");
		}
		final LineNumberReader lnread = new LineNumberReader(
				new InputStreamReader(istream));
		String curLine;
		final Collection<DefiniteLengthModule> modulesColl = new ArrayList<DefiniteLengthModule>();
		while ((curLine = lnread.readLine()) != null) {
			if (!curLine.startsWith("#") && StringUtils.isNotBlank(curLine)) {
				final DefiniteLengthModule dm = getInstance(curLine, lnread
						.getLineNumber());
				if (dm != null) {
					modulesColl.add(dm);
				}
			}
		}
		this.modules = modulesColl.toArray(new DefiniteLengthModule[modulesColl
				.size()]);
	}

	private DefiniteLengthModule getInstance(final String curLine,
			final int lineNo) {
		final String enumName = curLine.split("=")[0];
		final FormatEnum fenum = FormatEnum.getEnum(this.enumClazz, enumName);
		final String method = curLine.substring(enumName.length() + 1, enumName
				.indexOf(':'));
		final DetectMode selectedMode = DetectMode
				.valueOf(method.toUpperCase());
		final String params = curLine.substring(enumName.length()
				+ method.length() + 2);
		DefiniteLengthModule result;
		switch (selectedMode) {
		case REGEXP:
			result = new RegexpDetectorModule();
			break;
		case STRING:
			result = new StringDetectorModule();
			break;
		case CLASS:
			final Class module = Class.forName(params);
			result = (DefiniteLengthModule) module.newInstance();
			break;
		default:
			throw new UnsupportedOperationException("mode [" + selectedMode
					+ "] not supported");
		}
		result.init(fenum, params);
		return result;
	}

	DefiniteLengthModule[] getConfiguredModules() {
		return null;
	}

}
