/*
 * c The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 SimpleElement.java
 */
package uk.gov.nationalarchives.droid.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * holds the basic details of an element read from an XML file
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class SimpleElement {
	String myText = "";
	private static final Logger LOG = LoggerFactory
	.getLogger(SimpleElement.class);
	/**
	 * method to be overridden in cases where the element content needs to be
	 * specified only when the end of element tag is reached
	 */
	public void completeElementContent() {
	}

	public String getElementName() {
		String className = this.getClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		return className;
	}

	/* getters */
	public String getText() {
		return this.myText;
	}

	public void setAttributeValue(final String name, final String value) {
		final String theCMDMessage = "WARNING: Unknown XML attribute "
				+ name + " found for " + getElementName() + " ";
		LOG.warn(theCMDMessage);
	}

	/* setters */
	public void setText(final String theText) {
		this.myText += theText;
	}
}
