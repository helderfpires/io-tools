package com.gc.iotools.fmt.detect.droid;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FormatEnumDroidGenerator {

	private static class DataHolder {
		final String[] extensions;
		final String description;
		final int id;
		final String version;

		DataHolder(final String[] extensions, final String description,
				final int id, final String version) {
			this.extensions = extensions;
			this.description = description;
			this.id = id;
			this.version = version;
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		final Document dom = parseXml(FormatEnumDroidGenerator.class
				.getResourceAsStream("DROID_SignatureFile_V13.xml"));

		final NodeList formatsList = dom.getElementsByTagName("FileFormat");
		final Map<String, DataHolder> mappa = new TreeMap<String, DataHolder>();
		final MultiValueMap versionMap = new MultiValueMap();
		final MultiValueMap idMap = new MultiValueMap();
		for (int i = 0; i < formatsList.getLength(); i++) {
			final Node node = formatsList.item(i);
			String[] extensions = getExtensions(node);
			getNode(node, "MimeType");
			String internalId = getNode(node, "InternalSignatureID");
			final String id = getAttribute(node, "ID");
			final String description = getAttribute(node, "Name").trim();
			final String version = getAttribute(node, "Version");
			if (extensions.length == 0) {
				extensions = new String[] { "unknown" };
			}
			if (StringUtils.isNotBlank(internalId)) {
			final DataHolder dh = new DataHolder(extensions, description,
					Integer.parseInt(id), version);
			if (StringUtils.isNotBlank(dh.version)) {
				versionMap.put(dh.description, dh.version);
			}
			mappa.put(description, dh);
			idMap.put(description, id);
			}
		}
		final Collection<String> conflictingExtensions = getConflictingExtensions(mappa);
		final Map<String, String> enumDescrMap = getDescrEnumMap(mappa);
		final StringWriter sb1 = getFormatEnumHeader(mappa,
				conflictingExtensions, versionMap);
		System.out.println(sb1.toString());
		final StringWriter sb = getFormatEnumStr(enumDescrMap, versionMap,
				mappa);
		System.out.println(sb.toString());
		// final StringWriter sb2 = getAllocationMap(enumDescrMap, idMap);
		// System.out.println(sb2.toString());

		// StringWriter sb1 = getMimeTypes(mappa, conflictingExtensions,
		// mmtypeMap);
		// System.out.println(sb1.toString());
	}

	private static StringWriter getAllocationMap(
			final Map<String, String> mappa, final MultiMap mm) {
		final StringWriter sb = new StringWriter();
		final PrintWriter pw = new PrintWriter(sb);

		for (final String enumVal : mappa.keySet()) {
			final String description = mappa.get(enumVal);
			final Collection<String> ids = (Collection) mm.get(description);
			String str = "";
			for (final String id : ids) {
				if (StringUtils.isNotBlank(str)) {
					str += ",";
				}
				str += id;
			}
			pw.println(enumVal + "=" + str);
		}
		return sb;
	}

	// private static StringWriter getMimeTypes(Map<String, DataHolder> mappa,
	// Collection<String> conflictingExtensions,
	// Map<String, String> mtype) {
	// StringWriter sb = new StringWriter();
	// PrintWriter pw = new PrintWriter(sb);
	//
	// for (String description : mappa.keySet()) {
	// DataHolder dh = mappa.get(description);
	// boolean hasConflict = false;
	// for (String ext : dh.extensions) {
	// hasConflict |= conflictingExtensions.contains(ext);
	// }
	// final String nomeUC = dh.extensions[0].toUpperCase();
	// String nome = (hasConflict ? normalizeName(description) : nomeUC);
	// if (StringUtils.isNotBlank(mtype.get(dh.description))) {
	// pw.println(nome + "=" + mtype.get(dh.description));
	// }
	// }
	// pw.close();
	// return sb;
	// }

	private static String getAttribute(final Node node, final String name) {
		final NamedNodeMap nnm = node.getAttributes();
		final Node attnode = nnm.getNamedItem(name);
		String nodeValue = null;
		if (attnode != null) {
			nodeValue = attnode.getNodeValue();
		}
		return nodeValue;
	}

	private static Collection<String> getConflictingExtensions(
			final Map<String, DataHolder> mappa) {
		final Collection<String> confl = new HashSet<String>();
		final Collection<String> used = new ArrayList<String>();
		for (final DataHolder dh : mappa.values()) {
			final String[] extensions = dh.extensions;
			for (final String ext : extensions) {
				if (used.contains(ext)) {
					confl.add(ext);
				}
			}
			used.addAll(Arrays.asList(dh.extensions));
		}
		return confl;
	}

	private static Map<String, String> getDescrEnumMap(
			final Map<String, DataHolder> mappa) {
		final Collection<String> conflictingExtensions = getConflictingExtensions(mappa);
		final Map<String, String> result = new TreeMap<String, String>();
		for (final String description : mappa.keySet()) {
			final DataHolder dh = mappa.get(description);
			boolean hasConflict = false;
			for (final String ext : dh.extensions) {
				hasConflict |= conflictingExtensions.contains(ext);
			}
			String nomeUC = dh.extensions[0].toUpperCase();
			if (nomeUC.startsWith(".")) {
				nomeUC = nomeUC.substring(1);
			}
			if (nomeUC.matches("[0-9].*")) {
				nomeUC = normalizeName(description);
			}
			
			final String name = (hasConflict ? normalizeName(description)
					: nomeUC);
			result.put(name, description);
		}
		return result;
	}

	private static String[] getExtensions(final Node node) {
		final NodeList nl = node.getChildNodes();
		final Collection<String> cl = new ArrayList<String>();
		for (int i = 0; i < nl.getLength(); i++) {
			final Node child = nl.item(i);
			final String localName = child.getNodeName();
			if ("Extension".equals(localName)) {
				final String textContent = child.getTextContent();
				cl.add(textContent);
			}

		}
		return cl.toArray(new String[0]);
	}

	private static StringWriter getFormatEnumHeader(
			final Map<String, DataHolder> mappa,
			final Collection<String> conflictingExtensions,
			final MultiMap versionMap) {
		final StringWriter sb = new StringWriter();
		final PrintWriter pw = new PrintWriter(sb);
		pw.println("* <table>");
		pw.println("* <thead>");
		pw.println("* <tr><td>Enum name</td><td>Description</td>"
				+ "<td>Supported versions</td></tr>");
		pw.println("* </thead> <tbody>");
		for (final String description : mappa.keySet()) {
			final DataHolder dh = mappa.get(description);
			boolean hasConflict = false;
			for (final String ext : dh.extensions) {
				hasConflict |= conflictingExtensions.contains(ext);
			}
			String nomeUC = dh.extensions[0].toUpperCase();
			if (nomeUC.startsWith(".")) {
				nomeUC = nomeUC.substring(1);
			}
			if (nomeUC.matches("[0-9].*")) {
				nomeUC = normalizeName(description);
			}
			final String nome = (hasConflict ? normalizeName(description)
					: nomeUC);
			pw.println("* <tr>");
			String versions = getVersions(versionMap, description);
			pw.println("* <td>" + nome + "</td> <td>" + description
					+ "</td> <td>" + versions + "</td>");
			pw.println("* </tr>");
		}
		pw.println("* </tbody> </table>");
		return sb;
	}

	private static String getVersions(final MultiMap versionMap,
			final String description) {
		final Collection<String> descriptions = (Collection) versionMap
				.get(description);
		String versions = "";
		if (descriptions != null) {
			for (final String string : descriptions) {
				if (StringUtils.isNotBlank(versions)) {
					versions += ", ";
				}
				versions += string;
			}
		}
		return versions;
	}

	private static StringWriter getFormatEnumStr(
			final Map<String, String> mappa, final MultiValueMap versionMap,
			Map<String, DataHolder> mm) {
		final StringWriter sb = new StringWriter();
		final PrintWriter pw = new PrintWriter(sb);

		for (final String enumKey : mappa.keySet()) {
			final String description = mappa.get(enumKey);
			DataHolder dh = mm.get(description);
			pw.println("/**");
			pw.println(" * Constant integer for enum : " + enumKey + " .");
			pw.println(" */");
			pw.println(" public static final int " + enumKey + "_INT = "
					+ dh.id
					+ ";");
			pw.println("/**");
			pw.println(" * Enum : " + enumKey
					+ " : this enum describes format " + description + ".");
			String versions = getVersions(versionMap, description);
			if (StringUtils.isNotBlank(versions)) {
				pw.println(" * Supported versions :" + versions);
			}
			pw.println(" */");
			pw.println(" public static final FormatEnum " + enumKey
					+ " = new FormatEnum(\"" + enumKey + "\", " + enumKey
					+ "_INT);");
			pw.println("");
		}
		return sb;
	}

	private static String getNode(final Node node, final String string) {
		final NodeList nl = node.getChildNodes();
		String cl = null;
		for (int i = 0; i < nl.getLength(); i++) {
			final Node child = nl.item(i);
			final String localName = child.getNodeName();
			if (string.equals(localName)) {
				cl = child.getTextContent();
			}

		}
		return cl;
	}

	private static String normalizeName(final String name) {
		String normalized = name.toUpperCase().replaceAll("[^\\p{Alnum}]",
				"_").replaceAll("__", "_");
		if (normalized.endsWith("_")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}
		if (normalized.matches("[\\p{Punct}].*")) {
			normalized = normalized.substring(1);
		}
		if (normalized.matches("[0-9].*")) {
			normalized = "N" + normalized;
		}
		return normalized;
	}

	private static Document parseXml(final InputStream istream)
			throws Exception {
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		final DocumentBuilder docBuilder = docBuilderFactory
				.newDocumentBuilder();
		final Document doc = docBuilder.parse(istream);
		return doc;
	}
}
