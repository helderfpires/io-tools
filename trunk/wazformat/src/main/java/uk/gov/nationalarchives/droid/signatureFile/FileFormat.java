/*
 * The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $History: FileFormat.java $ *****************
 * Version 3 ***************** User: Walm Date: 5/04/05 Time: 18:07 Updated in
 * $/PRONOM4/FFIT_SOURCE/signatureFile review headers
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.nationalarchives.droid.base.SimpleElement;

/**
 * holds details of a file format
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormat extends SimpleElement {
	private static final Logger LOG = LoggerFactory
	.getLogger(FileFormat.class);
	private final List<String> extensions = new ArrayList<String>();
	private List<Integer> hasPriorityOver = new ArrayList<Integer>();
	private int identifier;
	private List<Integer> internalSigIDs = new ArrayList<Integer>();
	private String mimeType;
	private String name;
	private String PUID;
	private String version;

	// TODO from UCDetector: Change visibility of Method
	// "FileFormat.getExtension(int)" to private
	public String getExtension(final int theIndex) { // NO_UCD
		return this.extensions.get(theIndex);
	}

	protected int getHasPriorityOver(final int theIndex) {
		return this.hasPriorityOver.get(theIndex);
	}

	public int getID() {
		return this.identifier;
	}

	public int getInternalSignatureID(final int theIndex) {
		return this.internalSigIDs.get(theIndex);
	}

	public String getMimeType() {
		return (this.mimeType == null ? "" : this.mimeType);
	}

	public String getName() {
		return this.name;
	}

	public int getNumExtensions() {
		return this.extensions.size();
	}

	public int getNumHasPriorityOver() {
		return this.hasPriorityOver.size();
	}

	/* getters */
	public int getNumInternalSignatures() {
		return this.internalSigIDs.size();
	}

	public String getPUID() {
		return this.PUID;
	}

	public String getVersion() {
		return this.version;
	}

	/**
	 * Indicates whether the file extension given is valid for this file
	 * format
	 * 
	 * @param theExtension
	 *            file extension
	 */
	public boolean hasMatchingExtension(final String theExtension) {
		boolean matchingExtension = false;
		// loop through Extensions
		for (int iExtension = 0; iExtension < getNumExtensions(); iExtension++) {
			if (theExtension.equalsIgnoreCase(getExtension(iExtension))) {
				matchingExtension = true;
			}
		}
		return matchingExtension;
	}

	@Override
	public void setAttributeValue(final String theName, final String theValue) {
		if (theName.equals("ID")) {
			this.identifier = Integer.parseInt(theValue);
		} else if (theName.equals("Name")) {
			this.name = theValue;
		} else if (theName.equals("Version")) {
			this.version = theValue;
		} else if (theName.equals("PUID")) {
			this.PUID = theValue;
		} else if (theName.equals("MIMEType")) {
			this.mimeType = theValue;
		} else {
			final String theCMDMessage = "WARNING: Unknown XML attribute "
					+ this.name + " found for " + getElementName() + " ";
			LOG.warn(theCMDMessage);
		}
	}

	public void setExtension(final String theExtension) {
		this.extensions.add(theExtension);
	}

	public void setHasPriorityOverFileFormatID(final String theID) {
		this.hasPriorityOver.add(Integer.parseInt(theID));
	}

	/* setters */
	public void setInternalSignatureID(final String theID) {
		this.internalSigIDs.add(Integer.parseInt(theID));
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

}
