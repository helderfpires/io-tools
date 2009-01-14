package com.gc.iotools.fmt;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FormatEnumGenerator {

	public static void main(final String[] args) throws Exception {
		final InputStream stream = FormatEnumGenerator.class
				.getResourceAsStream("DROID_SignatureFile_V20.xml");
		final File outFile = new File("FormatEnum.txt");

		new FileWriter(outFile);
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		final DocumentBuilder db = dbf.newDocumentBuilder();
		final Document dom = db.parse(stream);
		final NodeList list = dom.getElementsByTagName("FileFormat");
		for (int i = 0; i < list.getLength(); i++) {
			list.item(i);
		}
	}
}
