/*
 * The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $History: Shift.java $ ***************** Version
 * 6 ***************** User: Walm Date: 17/05/05 Time: 12:48 Updated in
 * $/PRONOM4/FFIT_SOURCE/signatureFile wait for end of element tag before
 * setting its content via the completeElementContent method *****************
 * Version 5 ***************** User: Walm Date: 5/04/05 Time: 18:07 Updated in
 * $/PRONOM4/FFIT_SOURCE/signatureFile review headers
 */
package uk.gov.nationalarchives.droid.signatureFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.nationalarchives.droid.base.SimpleElement;

/**
 * holds a shift function value
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class Shift extends SimpleElement {
	private static final Logger LOG = LoggerFactory.getLogger(Shift.class);
	private int myShiftByte = 999;
	private long myShiftValue;

	/**
	 * Set the shift distance when the end of element tag is reached. This
	 * will have been stored in the text attribute by the setText method
	 * defined in SimpleElement
	 */
	@Override
	public void completeElementContent() {
		final String theElementValue = getText(); // ((SimpleElement)this).getText();
		try {
			this.myShiftValue = Long.parseLong(theElementValue);
		} catch (final Exception e) {
			LOG.warn("The following non-numerical shift distance was found in the signature file: "
					+ theElementValue);
			this.myShiftValue = 1;
		}

	}

	/* getters */
	public int getShiftByte() {
		return this.myShiftByte;
	}

	public long getShiftValue() {
		return this.myShiftValue;
	}

	/**
	 * Respond to an XML attribute
	 * 
	 * @param theName
	 *            attribute name
	 * @param theValue
	 *            attribute value
	 */
	@Override
	public void setAttributeValue(final String theName, final String theValue) {
		if (theName.equals("Byte")) {
			try {
				this.myShiftByte = Integer.parseInt(theValue, 16);
			} catch (final Exception e) {
			}
		} else {
			final String theCMDMessage = "WARNING: Unknown XML attribute "
					+ theName + " found for " + getElementName() + " ";
			LOG.warn(theCMDMessage);
		}
	}
}
