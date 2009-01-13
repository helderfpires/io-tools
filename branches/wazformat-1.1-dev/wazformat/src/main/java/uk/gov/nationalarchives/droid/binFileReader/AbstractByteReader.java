/*
 *  The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4826
 * PRONOM 5a
 *
 * $Id: AbstractByteReader.java,v 1.9 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: AbstractByteReader.java,v $
 * Revision 1.9  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.8  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.7  2006/02/09 13:17:41  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.6  2006/02/09 12:14:15  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.5  2006/02/08 12:51:52  linb
 * Added javadoc comments for file.
 *
 * Revision 1.4  2006/02/08 12:03:37  linb
 * - add more comments
 *
 * Revision 1.3  2006/02/08 11:45:48  linb
 * - add support for streams
 *
 * Revision 1.2  2006/02/08 08:58:09  linb
 * - Added header comments
 *
 *
 */

package uk.gov.nationalarchives.droid.binFileReader;

import uk.gov.nationalarchives.droid.FileFormatHit;
import uk.gov.nationalarchives.droid.IdentificationFile;

/**
 * Abstract base class for the ByteReader interface.
 * <p/>
 * This implements the methods that are passed on to the
 * <code>IdentificationFile</code> object.
 * 
 * @author linb
 */
public abstract class AbstractByteReader implements ByteReader {

	/**
	 * Creates a ByteReader object, and depending on the readFile setting, it
	 * may or may not read in the binary file specified
	 * 
	 * @param theIDFile
	 *            The file to be read in
	 */
	public static ByteReader newByteReader(final IdentificationFile theIDFile) {
		return newByteReader(theIDFile, true);
	}

	/**
	 * Static constructor for a ByteReader object, and depending on the readFile
	 * setting, it may or may not read in the binary file specified.
	 * <p/>
	 * This may create a FileByteReader, UrlByteReader or InputStreamByteReader,
	 * depending on the the nature of the IdentificationFile passed in.
	 * 
	 * @param theIDFile
	 *            The file to be read in
	 * @param readFile
	 *            Flag specifying whether file should be read in or not
	 */
	public static ByteReader newByteReader(final IdentificationFile theIDFile,
			final boolean readFile) {
		if (InputStreamByteReader.isInputStream(theIDFile.getFilePath())) {
			return InputStreamByteReader.newInputStreamByteReader(theIDFile,
					readFile);
		} else if (UrlByteReader.isURL(theIDFile.getFilePath())) {
			return UrlByteReader.newUrlByteReader(theIDFile, readFile);
		} else {
			return new FileByteReader(theIDFile, readFile);
		}

	}

	/**
	 * The file represented by this object
	 */
	protected IdentificationFile myIDFile;

	/**
	 * Creates a ByteReader object, and depending on the readFile setting, it
	 * may or may not read in the binary file specified
	 * 
	 * @param theIDFile
	 *            The file to be read in
	 */
	protected AbstractByteReader(final IdentificationFile theIDFile) {
		this.myIDFile = theIDFile;
	}

	/**
	 * Add another hit to the list of hits for this file.
	 * 
	 * @param theHit
	 *            The <code>FileFormatHit</code> to be added
	 */
	public void addHit(final FileFormatHit theHit) {
		this.myIDFile.addHit(theHit);
	}

	/**
	 * Get classification of the file
	 */
	public int getClassification() {
		return this.myIDFile.getClassification();
	}

	/**
	 * Get file name of the associated file
	 */
	public String getFileName() {
		return this.myIDFile.getFileName();
	}

	/**
	 * Get file path of the associated file
	 */
	public String getFilePath() {
		return this.myIDFile.getFilePath();
	}

	/**
	 * Get a file format hit
	 * 
	 * @param theIndex
	 *            index of the <code>FileFormatHit</code> to get
	 * @return the hit associated with <code>theIndex</code>
	 */
	public FileFormatHit getHit(final int theIndex) {
		return this.myIDFile.getHit(theIndex);
	}

	/**
	 * Get any warning message created when identifying this file
	 */
	public String getIdentificationWarning() {
		return this.myIDFile.getWarning();
	}

	/**
	 * Get number of file format hits
	 */
	public int getNumHits() {
		return this.myIDFile.getNumHits();
	}

	/**
	 * Checks whether the file has yet been classified
	 */
	public boolean isClassified() {
		return this.myIDFile.isClassified();
	}

	/**
	 * Remove a hit from the list of hits for this file.
	 * 
	 * @param theIndex
	 *            Index of the hit to be removed
	 */
	public void removeHit(final int theIndex) {
		this.myIDFile.removeHit(theIndex);
	}

	/**
	 * Set identification status to Error
	 */
	public void setErrorIdent() {
		this.myIDFile.setErrorIdent();
	}

	/**
	 * Set identification warning
	 * 
	 * @param theWarning
	 *            the warning message to use
	 */
	public void setIdentificationWarning(final String theWarning) {
		this.myIDFile.setWarning(theWarning);
	}

	/**
	 * Set identification status to No identification
	 */
	public void setNoIdent() {
		this.myIDFile.setNoIdent();
	}

	/* Setters for identification status */
	/**
	 * Set identification status to Positive
	 */
	public void setPositiveIdent() {
		this.myIDFile.setPositiveIdent();
	}

	/**
	 * Set identification status to Tentative
	 */
	public void setTentativeIdent() {
		this.myIDFile.setTentativeIdent();
	}

}
