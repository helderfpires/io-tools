/*
 * � The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4305
 * PRONOM 4
 *
 * $History: FileFormat.java $
 * 
 * *****************  Version 3  *****************
 * User: Walm         Date: 5/04/05    Time: 18:07
 * Updated in $/PRONOM4/FFIT_SOURCE/signatureFile
 * review headers
 *
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;
import java.util.List;

import uk.gov.nationalarchives.droid.MessageDisplay;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;


/**
 * holds details of a file format
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormat extends SimpleElement {
	int identifier;
	String name;
	String version;
	String PUID;
	List<Integer> internalSigIDs = new ArrayList<Integer>();
	List<String> extensions = new ArrayList<String>();
	List<Integer> hasPriorityOver = new ArrayList<Integer>();
	String mimeType;

	public String getExtension(final int theIndex) {
		return this.extensions.get(theIndex).toString();
	}

	public int getHasPriorityOver(final int theIndex) {
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
	 * Indicates whether the file extension given is valid for this file format
	 * 
	 * @param theExtension
	 *            file extension
	 */
	public boolean hasMatchingExtension(final String theExtension) {
		boolean matchingExtension = false;
		for (int iExtension = 0; iExtension < this.getNumExtensions(); iExtension++) {
			if (theExtension.equalsIgnoreCase(this.getExtension(iExtension))) {
				matchingExtension = true;
			}
		}// loop through Extensions
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
			MessageDisplay.unknownAttributeWarning(this.name, this
					.getElementName());
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
