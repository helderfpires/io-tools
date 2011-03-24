/*
 * c The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $Id: IdentificationFile.java,v 1.9 2006/03/13
 * 15:15:25 linb Exp $ $Logger: IdentificationFile.java,v $ Revision 1.9
 * 2006/03/13 15:15:25 linb Changed copyright holder from Crown Copyright to
 * The National Archives. Added reference to licence.txt Changed dates to
 * 2005-2006 Revision 1.8 2006/02/09 15:31:23 linb Updates to javadoc and code
 * following the code review Revision 1.7 2006/02/09 13:17:41 linb Changed
 * StreamByteReader to InputStreamByteReader Refactored common code from
 * UrlByteReader and InputStreamByteReader into new class StreamByteReader,
 * from which they both inherit Updated javadoc Revision 1.6 2006/02/09
 * 12:14:15 linb Changed some javadoc to allow it to be created cleanly
 * Revision 1.5 2006/02/08 11:45:48 linb - add support for streams Revision
 * 1.4 2006/02/08 08:56:35 linb - Added header comments$History:
 * IdentificationFile.java $ ***************** Version 10 *****************
 * User: Walm Date: 5/04/05 Time: 18:08 Updated in $/PRONOM4/FFIT_SOURCE
 * review headers ***************** Version 9 ***************** User: Walm
 * Date: 29/03/05 Time: 11:11 Updated in $/PRONOM4/FFIT_SOURCE Read in hit
 * results from a file collection file ***************** Version 8
 * ***************** User: Walm Date: 24/03/05 Time: 11:17 Updated in
 * $/PRONOM4/FFIT_SOURCE add Text versions of file classifications
 * ***************** Version 7 ***************** User: Walm Date: 15/03/05
 * Time: 15:31 Updated in $/PRONOM4/FFIT_SOURCE initialise identification
 * warning ***************** Version 6 ***************** User: Walm Date:
 * 15/03/05 Time: 14:40 Updated in $/PRONOM4/FFIT_SOURCE File classifications
 * are now integer constants defined in AnalysisController *****************
 * Version 5 ***************** User: Mals Date: 14/03/05 Time: 18:09 Updated
 * in $/PRONOM4/FFIT_SOURCE return null identification status
 * ***************** Version 4 ***************** User: Mals Date: 14/03/05
 * Time: 15:07 Updated in $/PRONOM4/FFIT_SOURCE Added positive ,tentitive and
 * unidentified status setters ***************** Version 3 *****************
 * User: Mals Date: 14/03/05 Time: 14:00 Updated in $/PRONOM4/FFIT_SOURCE
 * Changed location of FileFormatHit ***************** Version 2
 * ***************** User: Mals Date: 11/03/05 Time: 12:20 Updated in
 * $/PRONOM4/FFIT_SOURCE Added setters ***************** Version 1
 * ***************** User: Mals Date: 11/03/05 Time: 11:54 Created in
 * $/PRONOM4/FFIT_SOURCE Reprsents an analysed or to be analysed file
 */

package uk.gov.nationalarchives.droid.binFileReader;

import uk.gov.nationalarchives.droid.base.DroidConstants;
import uk.gov.nationalarchives.droid.base.FileFormatHit;
import uk.gov.nationalarchives.droid.base.MessageDisplay;
import uk.gov.nationalarchives.droid.base.SimpleElement;

/**
 * Reprsents an analysed or to be analysed file.
 * <p/>
 * <p>
 * Holds details on warnings, status and format hits for the file.
 * 
 * @author Shahzad Malik
 * @version V1.R0.M.0, 11-Mar-2005
 */
public class IdentificationFile extends SimpleElement implements Comparable {

	private final java.util.List<FileFormatHit> fileHits = new java.util.ArrayList<FileFormatHit>();
	private String filePath;
	private String identificationWarning = "";
	private int myIDStatus = DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED;

	public IdentificationFile() {
	}

	/**
	 * Creates a new instance of IdentificationFile.
	 * 
	 * @param filePath
	 *            Full file path
	 */
	public IdentificationFile(final String filePath) {
		setFilePath(filePath);
	}

	/**
	 * Add a file format hit for this file.
	 * <p/>
	 * <p>
	 * Used for reading XML
	 * 
	 * @param theHit
	 *            file format hit found
	 */
	public void addFileFormatHit(final FileFormatHit theHit) {
		this.fileHits.add(theHit);
	}

	/**
	 * Add a file format hit for this file.
	 * 
	 * @param hit
	 *            file format hit found
	 */
	protected void addHit(final FileFormatHit hit) {
		this.fileHits.add(hit);
	}

	@Override
	public int compareTo(final Object otherIDFile) {
		// This object always smaller if path not set
		if ((this.filePath == null) || (this.filePath.length() == 0)) {
			return -1;
		}

		return getFilePath().compareToIgnoreCase(
				((IdentificationFile) otherIDFile).getFilePath());
	}

	/**
	 * Base equality on filePath
	 */
	@Override
	public boolean equals(final Object otherIDFile) {
		if (otherIDFile instanceof IdentificationFile) {
			return this.filePath.toLowerCase()
					.equals(((IdentificationFile) otherIDFile).filePath
							.toLowerCase());
		} else {
			return false;
		}
	}

	/**
	 * Returns the file classification found by the identification The options
	 * are setup as constants under Analysis Controller:
	 * *FILE_CLASSIFICATION_POSITIVE *FILE_CLASSIFICATION_TENTATIVE
	 * *FILE_CLASSIFICATION_NOHIT *FILE_CLASSIFICATION_ERROR
	 * *FILE_CLASSIFICATION_NOTCLASSIFIED
	 */
	public int getClassification() {
		return this.myIDStatus;
	}

	/**
	 * Returns the text description of the file classification found by the
	 * identification The options are setup as constants under Analysis
	 * Controller: *FILE_CLASSIFICATION_POSITIVE_TEXT
	 * *FILE_CLASSIFICATION_TENTATIVE_TEXT *FILE_CLASSIFICATION_NOHIT_TEXT
	 * *FILE_CLASSIFICATION_ERROR_TEXT *FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT
	 */
	public String getClassificationText() {
		String theClassificationText = "";
		if (this.myIDStatus == DroidConstants.FILE_CLASSIFICATION_POSITIVE) {
			theClassificationText = DroidConstants.FILE_CLASSIFICATION_POSITIVE_TEXT;
		} else if (this.myIDStatus == DroidConstants.FILE_CLASSIFICATION_TENTATIVE) {
			theClassificationText = DroidConstants.FILE_CLASSIFICATION_TENTATIVE_TEXT;
		} else if (this.myIDStatus == DroidConstants.FILE_CLASSIFICATION_NOHIT) {
			theClassificationText = DroidConstants.FILE_CLASSIFICATION_NOHIT_TEXT;
		} else if (this.myIDStatus == DroidConstants.FILE_CLASSIFICATION_ERROR) {
			theClassificationText = DroidConstants.FILE_CLASSIFICATION_ERROR_TEXT;
		} else if (this.myIDStatus == DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED) {
			theClassificationText = DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT;
		}
		return theClassificationText;
	}

	/**
	 * Returns the file name (without the full path)
	 */
	public String getFileName() {
		return (new java.io.File(this.filePath)).getName();
	}

	/**
	 * Returns the full file path
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Returns a hit object associated with the file that has been run through
	 * the identification process
	 */
	protected FileFormatHit getHit(final int theIndex) {
		return this.fileHits.get(theIndex);
	}

	/**
	 * Returns number of hits found for this file
	 */
	public int getNumHits() {
		return this.fileHits.size();
	}

	/**
	 * Returns any warning associated with the file
	 */
	public String getWarning() {
		return this.identificationWarning;
	}

	/**
	 * Use hashCode of filePath if set, as we wish to enforce this to be
	 * unique
	 */
	@Override
	public int hashCode() {
		if ((this.filePath == null) || (this.filePath.length() == 0)) {
			return super.hashCode();
		} else {
			return this.filePath.toLowerCase().hashCode();
		}
	}

	/**
	 * Checks whether the file has been classified yet (i.e return YES for the
	 * following classification values: FILE_CLASSIFICATION_POSITIVE ,
	 * FILE_CLASSIFICATION_TENTITIVE FILE_CLASSIFICATION_NOHIT ,
	 * FILE_CLASSIFICATION_ERROR
	 */
	public boolean isClassified() {
		return (this.myIDStatus != DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED);
	}

	/**
	 * Remove a file format hit for the file.
	 * 
	 * @param index
	 *            position in hit list of file
	 */
	protected void removeHit(final int index) {
		this.fileHits.remove(index);
	}

	/**
	 * Populate the details of the IdentificationFile object when read in from
	 * XML file.
	 * 
	 * @param theName
	 *            Name of the attribute read in
	 * @param theValue
	 *            Value of the attribute read in
	 */
	@Override
	public void setAttributeValue(final String theName, final String theValue) {
		if (theName.equals("Name")) {
			setFilePath(theValue);
		} else if (theName.equals("IdentQuality")) {
			// The IdentQuality attribute value should match one of those
			// specified in code -
			// otherwise show a warning
			if (theValue
					.equals(DroidConstants.FILE_CLASSIFICATION_POSITIVE_TEXT)) {
				this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_POSITIVE;
			} else if (theValue
					.equals(DroidConstants.FILE_CLASSIFICATION_TENTATIVE_TEXT)) {
				this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_TENTATIVE;
			} else if (theValue
					.equals(DroidConstants.FILE_CLASSIFICATION_NOHIT_TEXT)) {
				this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_NOHIT;
			} else if (theValue
					.equals(DroidConstants.FILE_CLASSIFICATION_ERROR_TEXT)) {
				this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_ERROR;
			} else if (theValue
					.equals(DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT)) {
				this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_NOTCLASSIFIED;
			} else {
				MessageDisplay.generalWarning("Unknown file status listed: <"
						+ theValue + "> is not the same as <"
						+ DroidConstants.FILE_CLASSIFICATION_POSITIVE_TEXT
						+ ">");
			}
		} else if (theName.equals("Warning")) {
			setWarning(theValue);
		} else {
			MessageDisplay.unknownAttributeWarning(theName, getElementName());
		}
	}

	/**
	 * Sets the status to error during identification
	 */
	protected void setErrorIdent() {
		this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_ERROR;
	}

	/**
	 * Set the full file Path.
	 * 
	 * @param filePath
	 *            full file path
	 */
	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Set the file identification status.
	 * 
	 * @param theStatus
	 *            file identification status
	 */
	public void setIDStatus(final int theStatus) {
		this.myIDStatus = theStatus;
	}

	/**
	 * Sets the status to not identified
	 */
	protected void setNoIdent() {
		this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_NOHIT;
	}

	/**
	 * Sets the file status to Postive
	 */
	protected void setPositiveIdent() {
		this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_POSITIVE;
	}

	/**
	 * Sets the file status to tentitive
	 */
	protected void setTentativeIdent() {
		this.myIDStatus = DroidConstants.FILE_CLASSIFICATION_TENTATIVE;
	}

	/**
	 * Set the file identification warning.
	 * 
	 * @param warning
	 *            file identification warning
	 */
	public void setWarning(final String warning) {
		this.identificationWarning = warning;
	}

}
