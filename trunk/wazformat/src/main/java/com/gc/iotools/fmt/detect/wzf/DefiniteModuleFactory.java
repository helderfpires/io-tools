package com.gc.iotools.fmt.detect.wzf;

/*
 * Copyright (c) 2008, Davide Simonetti.  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;

public class DefiniteModuleFactory {
	private static final String DEF_CONF = "deflen.properties";
	private final Class<?> enumClazz;
	private final DefiniteLengthModule[] modules;

	DefiniteModuleFactory() {
		this(DEF_CONF, FormatEnum.class);
	}

	DefiniteModuleFactory(final String confFile, final Class<?> enumClass) {
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
		int lineNumber = 0;
		try {
			while ((curLine = lnread.readLine()) != null) {
				if (!curLine.startsWith("#")
						&& StringUtils.isNotBlank(curLine)
						&& curLine.contains("=")) {
					lineNumber = lnread.getLineNumber();
					final DefiniteLengthModule dm = getInstance(curLine,
							lineNumber);
					if (dm != null) {
						modulesColl.add(dm);
					}
				}
			}
			istream.close();
		} catch (final IOException e) {
			throw new IllegalStateException(
					"Problem reading configuration file[" + confFile
							+ "] line[" + lineNumber + "]", e);
		}
		this.modules = modulesColl.toArray(new DefiniteLengthModule[0]);
	}

	private FormatId getFormatId(final String enumName) {
		String version;
		FormatEnum fenum;
		if (enumName.contains(":")) {
			final String[] parts = enumName.split(":");
			fenum = FormatEnum.getEnum(this.enumClazz, parts[0]);
			version = parts[1];
		} else {
			fenum = FormatEnum.getEnum(this.enumClazz, enumName);
			version = null;
		}
		final FormatId result = (fenum == null ? new FormatId(
				FormatEnum.UNLISTED, enumName) : new FormatId(fenum, version));

		return result;
	}

	private DefiniteLengthModule getInstance(final String curLine,
			final int lineNo) {
		final String enumName = curLine.split("=")[0];
		final FormatId fenum = getFormatId(enumName);
		final String paramLine = curLine.substring(enumName.length() + 1,
				curLine.length());
		final String method = paramLine.substring(0, paramLine.indexOf(':'));
		final DetectMode selectedMode = DetectMode.valueOf(method
				.toUpperCase());
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
		case STRINGNC:
			result = new StringncDetectorModule();
			break;
		case CLASS:
			result = instantiateClass(lineNo, params);
			break;
		default:
			throw new UnsupportedOperationException("mode [" + selectedMode
					+ "] not supported");
		}
		result.init(fenum, params);
		return result;
	}

	private DefiniteLengthModule instantiateClass(final int lineNo,
			final String params) {
		DefiniteLengthModule result;

		try {
			final Class<?> module = Class.forName(params);
			result = (DefiniteLengthModule) module.newInstance();
		} catch (final Exception e) {
			throw new IllegalStateException("Problem instantiating class ["
					+ params + "] line[" + lineNo + "]", e);
		}
		return result;
	}

	DefiniteLengthModule[] getConfiguredModules() {
		return this.modules;
	}

}
