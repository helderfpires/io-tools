package com.gc.iotools.fmt.detectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;

import com.gc.iotools.fmt.base.Detector;

public final class DetectorFactory {

	public static Detector[] getConfiguredDetectors() {
		final DefaultMutableTreeNode[] nodes = parse(null);
		final Collection detectors = new ArrayList();
		for (int i = 0; i < nodes.length; i++) {
			final DefaultMutableTreeNode defaultMutableTreeNode = nodes[i];
			final Detector detect = getDetector(defaultMutableTreeNode);
			if (detect != null) {
				detectors.add(detect);
			}
		}
		return (Detector[]) detectors.toArray(new Detector[detectors.size()]);
	}

	// TODO: Use commons configuration
	public static void setConfigurationFile(final String string) {

	}

	private static DefaultMutableTreeNode[] parse(final Reader r)
			throws IOException {
		final BufferedReader br = new BufferedReader(r);
		String line;
		final Collection rules = new HashSet();
		DefaultMutableTreeNode currentNode = null;
		while ((line = br.readLine()) != null) {
			if (StringUtils.isNotBlank(line) && line.charAt(0) != '#') {
				line = line.trim();
				if (line.startsWith(">")) {
					if (currentNode == null) {
						throw new IllegalStateException(
								"Line ["
										+ line
										+ "] starts with a > but no previous line for that format was defined");
					} else {
						final DefaultMutableTreeNode child = new DefaultMutableTreeNode(
								line);
						currentNode.add(child);
					}
				} else {
					currentNode = new DefaultMutableTreeNode(line);
					rules.add(currentNode);
				}

			}
		}
		return (DefaultMutableTreeNode[]) rules
				.toArray(new DefaultMutableTreeNode[rules.size()]);
	}

	static Detector getDetector(final DefaultMutableTreeNode node) {
		return null;
	}

}
