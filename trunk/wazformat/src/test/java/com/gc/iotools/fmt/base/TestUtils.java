package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.stream.is.SizeLimitInputStream;

public final class TestUtils {

	public static Map<String, byte[]> getBytesForFiles(
			final String[] included, final int nbytes, final boolean includes)
			throws IOException {
		final String[] goodFiles = (includes ? TestUtils
				.listFilesIncludingExtension(included) : TestUtils
				.listFilesExcludingExtension(included));
		final Map<String, byte[]> result = new HashMap<String, byte[]>();
		for (final String fileName : goodFiles) {
			final InputStream is = new FileInputStream(fileName);
			final byte[] bytes = IOUtils
					.toByteArray(new SizeLimitInputStream(is, nbytes));
			result.put(fileName, bytes);
		}
		return result;
	}

	public static String[] listFilesExcludingExtension(
			final String[] forbidden) throws IOException {
		final URL fileURL = TestUtils.class.getResource("/testFiles");
		String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection<String> goodFiles = new Vector<String>();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (final String file : files) {
			boolean insert = true;
			for (final String extForbidden : forbidden) {
				insert &= !(file.endsWith(extForbidden));
			}
			if (insert) {
				goodFiles.add(filePath + file);
			}
		}
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

	/**
	 * @deprecated
	 * @see FileUtils#iterate();
	 * @param allowed
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static String[] listFilesIncludingExtension(final String[] allowed)
			throws IOException {
		final URL fileURL = TestUtils.class.getResource("/testFiles");
		String filePath = URLDecoder.decode(fileURL.getPath(), "UTF-8");
		final File dir = new File(filePath);
		final String[] files = dir.list();
		final Collection<String> goodFiles = new Vector<String>();
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		for (final String file : files) {
			for (final String element : allowed) {
				if (file.endsWith(element)) {
					goodFiles.add(filePath + file);
				}
			}
		}
		return goodFiles.toArray(new String[goodFiles.size()]);
	}

}
