/*
 * The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $Id: InternalSignature.java,v 1.5 2006/03/13
 * 15:15:29 linb Exp $ $Logger: InternalSignature.java,v $ Revision 1.5
 * 2006/03/13 15:15:29 linb Changed copyright holder from Crown Copyright to
 * The National Archives. Added reference to licence.txt Changed dates to
 * 2005-2006 Revision 1.4 2006/02/08 16:06:35 gaur Moved endianness from
 * internal signatures to byte sequences Revision 1.3 2006/02/07 17:16:22 linb
 * - Change fileReader to ByteReader in formal parameters of methods - use new
 * static constructors - Add detection of if a filePath is a URL or not
 * Revision 1.2 2006/02/07 11:30:04 gaur Added support for endianness of
 * signature $History: InternalSignature.java $ ***************** Version 3
 * ***************** User: Walm Date: 5/04/05 Time: 18:07 Updated in
 * $/PRONOM4/FFIT_SOURCE/signatureFile review headers
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.nationalarchives.droid.base.SimpleElement;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;

/**
 * holds the details of an internal signature
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class InternalSignature extends SimpleElement {
	private static final Logger LOG = LoggerFactory
			.getLogger(InternalSignature.class);
	private List<ByteSequence> byteSequences = new ArrayList<ByteSequence>();
	List<FileFormat> fileFormatList = new ArrayList<FileFormat>();
	int intSigID;
	String specificity;

	/* setters */
	public void addByteSequence(final ByteSequence byteSequence) {
		byteSequence.setParentSignature(this.intSigID);
		for (int i = 0; i < byteSequence.getNumSubSequences(); i++) {
			final SubSequence subSequence = byteSequence.getSubSequence(i);
			subSequence.setParentSignature(this.intSigID);
			subSequence.setReference(byteSequence.getReference());
			subSequence.setBigEndian(byteSequence.isBigEndian());
			subSequence.setByteSequence(byteSequence);
		}
		this.byteSequences.add(byteSequence);
	}

	protected void addFileFormat(final FileFormat theFileFormat) {
		this.fileFormatList.add(theFileFormat);
	}

	protected ByteSequence getByteSequence(final int theByteSeq) {
		return (ByteSequence) getByteSequences().get(theByteSeq);
	}

	/* getters */
	public List getByteSequences() {
		return this.byteSequences;
	}

	protected FileFormat getFileFormat(final int theIndex) {
		return this.fileFormatList.get(theIndex);
	}

	public int getID() {
		return this.intSigID;
	}

	public int getNumByteSequences() {
		return this.byteSequences.size();
	}

	public int getNumFileFormats() {
		return this.fileFormatList.size();
	}

	public String getSpecificity() {
		return this.specificity;
	}

	/**
	 * Indicates whether the file is compliant with this internal signature
	 * 
	 * @param targetFile
	 *            the binary file to be identified
	 */
	protected boolean isFileCompliant(final ByteReader targetFile) {
		// initialise variable
		boolean isCompliant = true;
		// check each byte sequence in turn - stop as soon as one is found to
		// be
		// non-compliant
		for (int i = 0; (i < this.byteSequences.size()) && isCompliant; i++) {
			isCompliant = getByteSequence(i).isFileCompliant(targetFile);
		}
		return isCompliant;
	}

	public boolean isSpecific() {
		return this.specificity.equalsIgnoreCase("specific");
	}

	/**
	 * Reset the bytesequences after reordering (to ensure BOF and EOF
	 * sequences are checked first
	 * 
	 * @param byteSequences
	 */
	protected void resetByteSequences(final List<ByteSequence> byteSequences) {
		this.byteSequences = byteSequences;
	}

	@Override
	public void setAttributeValue(final String name, final String value) {
		if (name.equals("ID")) {
			setID(value);
		} else if (name.equals("Specificity")) {
			setSpecificity(value);
		} else {
			final String theCMDMessage = "WARNING: Unknown XML attribute "
					+ name + " found for " + getElementName() + " ";
			LOG.warn(theCMDMessage);
		}
	}

	public void setID(final String theIntSigID) {
		this.intSigID = Integer.parseInt(theIntSigID);
	}

	public void setSpecificity(final String Specificity) {
		this.specificity = Specificity;
	}

	@Override
	public String toString() {
		return this.intSigID + "(" + this.specificity + ")"
				+ this.byteSequences;
	}
}
