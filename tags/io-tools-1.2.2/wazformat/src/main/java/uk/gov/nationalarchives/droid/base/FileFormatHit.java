/*
 * The National Archives 2005-2006.  All rights reserved.
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
 * $Id: FileFormatHit.java,v 1.4 2006/03/13 15:15:25 linb Exp $
 * 
 * $Logger: FileFormatHit.java,v $
 * Revision 1.4  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 *
 * *$History: FileFormatHit.java $
 *
 * *****************  Version 4  *****************
 * User: Walm         Date: 5/04/05    Time: 18:08
 * Updated in $/PRONOM4/FFIT_SOURCE
 * review headers
 *
 */

package uk.gov.nationalarchives.droid.base;

import uk.gov.nationalarchives.droid.signatureFile.FileFormat;

/**
 * holds the description of a hit (format identification) on a file
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormatHit extends SimpleElement {
	String myHitWarning = "";
	int myHitType;
	FileFormat myHitFileFormat;

	public FileFormatHit() {
	}

	/**
	 * Creates a new blank instance of fileFormatHit
	 * 
	 * @param theFileFormat
	 *            The file format which has been identified
	 * @param theType
	 *            The type of hit i.e. Positive/tentative
	 * @param theSpecificity
	 *            Flag is set to true for Positive specific hits
	 * @param theWarning
	 *            A warning associated with the hit
	 */
	public FileFormatHit(final FileFormat theFileFormat, final int theType,
			final boolean theSpecificity, final String theWarning) {
		this.myHitFileFormat = theFileFormat;
		if (theType == DroidConstants.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
			if (theSpecificity) {
				this.myHitType = DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC;
			} else {
				this.myHitType = DroidConstants.HIT_TYPE_POSITIVE_GENERIC;
			}
		} else {
			this.myHitType = theType;
		}
		setIdentificationWarning(theWarning);
	}

	/**
	 * get the fileFormat for the hit
	 */
	public FileFormat getFileFormat() {
		return this.myHitFileFormat;
	}

	/**
	 * get the name of the fileFormat of this hit
	 */
	public String getFileFormatName() {
		return this.myHitFileFormat.getName();
	}

	/**
	 * get the PUID of the fileFormat of this hit
	 */
	public String getFileFormatPUID() {
		return this.myHitFileFormat.getPUID();
	}

	/**
	 * get the version of the fileFormat of this hit
	 */
	public String getFileFormatVersion() {
		return this.myHitFileFormat.getVersion();
	}

	/**
	 * get the code of the hit type
	 */
	public int getHitType() {
		return this.myHitType;
	}

	/**
	 * get the name of the hit type
	 */
	public String getHitTypeVerbose() {
		String theHitType = "";
		if (this.myHitType == DroidConstants.HIT_TYPE_POSITIVE_GENERIC) {
			theHitType = DroidConstants.HIT_TYPE_POSITIVE_GENERIC_TEXT;
		} else if (this.myHitType == DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC) {
			theHitType = DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC_TEXT;
		} else if (this.myHitType == DroidConstants.HIT_TYPE_TENTATIVE) {
			theHitType = DroidConstants.HIT_TYPE_TENTATIVE_TEXT;
		} else if (this.myHitType == DroidConstants.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
			theHitType = DroidConstants.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT;
		}
		return theHitType;
	}

	/**
	 * get any warning associated with the hit
	 */
	public String getHitWarning() {
		return this.myHitWarning;
	}

	/**
	 * Get the mime type
	 * 
	 * @return
	 */
	public String getMimeType() {
		return this.myHitFileFormat.getMimeType();
	}

	/**
	 * For positive hits, this returns true if hit is Specific or returns false
	 * if hit is Generic. Meaningless for Tentative hits. (though returns false)
	 */
	public boolean isSpecific() {
		if (this.myHitType == DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Populates the details of the IdentificationFile when it is read in from
	 * XML file
	 * 
	 * @param theName
	 *            Name of the attribute read in
	 * @param theValue
	 *            Value of the attribute read in
	 */
	@Override
	public void setAttributeValue(final String theName, final String theValue) {
		if (theName.equals("HitStatus")) {
			setStatus(theValue);
		} else if (theName.equals("FormatName")) {
			setName(theValue);
		} else if (theName.equals("FormatVersion")) {
			setVersion(theValue);
		} else if (theName.equals("FormatPUID")) {
			setPUID(theValue);
		} else if (theName.equals("HitWarning")) {
			setIdentificationWarning(theValue);
		} else {
			MessageDisplay.unknownAttributeWarning(theName, getElementName());
		}
	}

	/**
	 * Updates the warning message for a hit
	 * <p/>
	 * Used by XML reader for
	 * IdentificationFile/FileFormatHit/IdentificationWarning element
	 * 
	 * @param theWarning
	 *            A warning associated with the hit
	 */
	public void setIdentificationWarning(final String theWarning) {
		this.myHitWarning = theWarning;
	}

	/**
	 * Set hit format MIME type. Used by XML reader for
	 * IdentificationFile/FileFormatHit/PUID element
	 */
	public void setMimeType(final String value) {
		if (this.myHitFileFormat == null) {
			this.myHitFileFormat = new FileFormat();
		}
		this.myHitFileFormat.setAttributeValue("MIMEType", value);
	}

	/**
	 * Set hit format name. Used by XML reader for
	 * IdentificationFile/FileFormatHit/Name element
	 */
	public void setName(final String value) {
		// if necessary, this creates a new dummy File format
		if (this.myHitFileFormat == null) {
			this.myHitFileFormat = new FileFormat();
		}
		this.myHitFileFormat.setAttributeValue("Name", value);
	}

	/**
	 * Set hit format PUID. Used by XML reader for
	 * IdentificationFile/FileFormatHit/PUID element
	 */
	public void setPUID(final String value) {
		if (this.myHitFileFormat == null) {
			this.myHitFileFormat = new FileFormat();
		}
		this.myHitFileFormat.setAttributeValue("PUID", value);
	}

	/**
	 * Set hit status. Used by XML reader for
	 * IdentificationFile/FileFormatHit/Status element
	 */
	public void setStatus(final String value) {
		// String value = element.getText();
		if (value.equals(DroidConstants.HIT_TYPE_POSITIVE_GENERIC_TEXT)) {
			this.myHitType = DroidConstants.HIT_TYPE_POSITIVE_GENERIC;
		} else if (value
				.equals(DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC_TEXT)) {
			this.myHitType = DroidConstants.HIT_TYPE_POSITIVE_SPECIFIC;
		} else if (value.equals(DroidConstants.HIT_TYPE_TENTATIVE_TEXT)) {
			this.myHitType = DroidConstants.HIT_TYPE_TENTATIVE;
		} else if (value
				.equals(DroidConstants.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT)) {
			this.myHitType = DroidConstants.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC;
		} else {
			MessageDisplay.generalWarning("Unknown hit status listed: "
					+ value);
		}
	}

	/**
	 * Set hit format version. Used by XML reader for
	 * IdentificationFile/FileFormatHit/Version element
	 */
	public void setVersion(final String value) {
		if (this.myHitFileFormat == null) {
			this.myHitFileFormat = new FileFormat();
		}
		this.myHitFileFormat.setAttributeValue("Version", value);
	}

}
