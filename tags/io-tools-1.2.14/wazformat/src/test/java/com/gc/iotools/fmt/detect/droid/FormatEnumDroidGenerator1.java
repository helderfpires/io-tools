package com.gc.iotools.fmt.detect.droid;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FormatEnumDroidGenerator1 {

	private static class DataHolder {
		final String[] extensions;
		final String description;
		final int id;

		DataHolder(String[] extensions, String description, int id) {
			this.extensions = extensions;
			this.description = description;
			this.id = id;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Document dom = parseXml(FormatEnumDroidGenerator.class
				.getResourceAsStream("DROID_SignatureFile_V18.xml"));

		NodeList formatsList = dom.getElementsByTagName("FileFormat");
		Map<String, DataHolder> mappa = new HashMap<String, DataHolder>();
		Map<String, String> mmtypeMap = new HashMap<String, String>();
		for (int i = 0; i < formatsList.getLength(); i++) {
			Node node = formatsList.item(i);
			String[] extensions = getExtensions(node);
			String mimeType = getMimeType(node);
			String id = getAttribute(node, "ID");
			String description = getAttribute(node, "Name");
			if (extensions.length == 0) {
				extensions = new String[] { "unknown" };
			}
			DataHolder dh = new DataHolder(extensions, description, Integer
					.parseInt(id));
			mappa.put(description, dh);
			if (StringUtils.isNotBlank(mimeType)) {
				mmtypeMap.put(description, mimeType);
			}
		}
		Collection<String> conflictingExtensions = getConflictingExtensions(mappa);

		StringWriter sb = getFormatEnumStr(mappa, conflictingExtensions);
		System.out.println(sb.toString());
		StringWriter sb1 = getMimeTypes(mappa, conflictingExtensions,
				mmtypeMap);
		System.out.println(sb1.toString());
	}

	private static StringWriter getMimeTypes(Map<String, DataHolder> mappa,
			Collection<String> conflictingExtensions,
			Map<String, String> mtype) {
		StringWriter sb = new StringWriter();
		PrintWriter pw = new PrintWriter(sb);

		for (String description : mappa.keySet()) {
			DataHolder dh = mappa.get(description);
			boolean hasConflict = false;
			for (String ext : dh.extensions) {
				hasConflict |= conflictingExtensions.contains(ext);
			}
			final String nomeUC = dh.extensions[0].toUpperCase();
			String nome = (hasConflict ? normalizeName(description) : nomeUC);
			if (StringUtils.isNotBlank(mtype.get(dh.description))) {
				pw.println(nome + "=" + mtype.get(dh.description));
			}
		}
		pw.close();
		return sb;
	}

	private static StringWriter getFormatEnumStr(
			Map<String, DataHolder> mappa,
			Collection<String> conflictingExtensions) {
		StringWriter sb = new StringWriter();
		PrintWriter pw = new PrintWriter(sb);

		for (String description : mappa.keySet()) {
			DataHolder dh = mappa.get(description);
			boolean hasConflict = false;
			for (String ext : dh.extensions) {
				hasConflict |= conflictingExtensions.contains(ext);
			}
			final String nomeUC = dh.extensions[0].toUpperCase();
			String nome = (hasConflict ? normalizeName(description) : nomeUC);
			pw.println("/**");
			pw.println(" * EnumName : " + nome);
			pw.println(" * " + description);
			pw.println(" */");
			pw
					.println(" public static final int " + nome + "_INT = "
							+ dh.id);
			pw.println(" public static final FormatEnum " + nome
					+ " = new FormatEnum(\"" + nome + "\", " + nome
					+ "_INT);");
			pw.println("");
		}
		return sb;
	}

	private static String[] getExtensions(Node node) {
		NodeList nl = node.getChildNodes();
		Collection<String> cl = new ArrayList<String>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			final String localName = child.getNodeName();
			if ("Extension".equals(localName)) {
				final String textContent = child.getTextContent();
				cl.add(textContent);
			}

		}
		return cl.toArray(new String[0]);
	}

	private static String getMimeType(Node node) {
		NodeList nl = node.getChildNodes();
		String cl = null;
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			final String localName = child.getNodeName();
			if ("MimeType".equals(localName)) {
				cl = child.getTextContent();
			}

		}
		return cl;
	}

	private static Collection<String> getConflictingExtensions(
			Map<String, DataHolder> mappa) {
		Collection<String> confl = new HashSet<String>();
		Collection<String> used = new ArrayList<String>();
		for (DataHolder dh : mappa.values()) {
			String[] extensions = dh.extensions;
			for (int i = 0; i < extensions.length; i++) {
				String ext = extensions[i];
				if (used.contains(ext)) {
					confl.add(ext);
				}
			}
			used.addAll(Arrays.asList(dh.extensions));
		}
		return confl;
	}

	private static String getAttribute(Node node, String name) {
		NamedNodeMap nnm = node.getAttributes();
		Node attnode = nnm.getNamedItem(name);
		return attnode.getNodeValue();
	}

	private static String normalizeName(String name) {
		String normalized = name.toUpperCase().replaceAll("[^\\p{Alnum}]",
				"_");
		return normalized;
	}

	private static Document parseXml(InputStream istream) throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(istream);
		return doc;
	}
}
