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
 * $Id: ByteSequence.java,v 1.7 2006/03/13 15:15:29 linb Exp $
 *
 * $Logger: ByteSequence.java,v $
 * Revision 1.7  2006/03/13 15:15:29  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.6  2006/02/09 15:04:37  gaur
 * Corrected formatting
 *
 * Revision 1.5  2006/02/08 16:14:01  gaur
 * Corrected error in merge
 *
 * Revision 1.4  2006/02/08 16:06:29  gaur
 * Moved endianness from internal signatures to byte sequences
 *
 * Revision 1.3  2006/02/07 17:16:22  linb
 * - Change fileReader to ByteReader in formal parameters of methods
 * - use new static constructors
 * - Add detection of if a filePath is a URL or not
 *
 * Revision 1.2  2006/02/07 11:30:04  gaur
 * Added support for endianness of signature
 *
 *
 * $History: ByteSequence.java $
 *
 * *****************  Version 5  *****************
 * User: Walm         Date: 5/04/05    Time: 18:07
 * Updated in $/PRONOM4/FFIT_SOURCE/signatureFile
 * review headers
 *
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;
import java.util.List;

import uk.gov.nationalarchives.droid.base.MessageDisplay;
import uk.gov.nationalarchives.droid.base.SimpleElement;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;

/**
 * holds details of a byte sequence
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class ByteSequence extends SimpleElement {

	int parentSignature;
	List<SubSequence> subSequences = new ArrayList<SubSequence>();
	String reference = "";
	boolean bigEndian = true; // Assume a signature is big-endian unless we are
	// told to the contrary.
	int indirectOffsetLength = 0;
	int indirectOffsetLocation = 0;

	// int MaxOffset = 0;

	/* setters */

	public void addSubSequence(final SubSequence sseq) {
		this.subSequences.add(sseq);
	}

	public int getIndirectOffsetLength() {
		return this.indirectOffsetLength;
	}

	public int getIndirectOffsetLocation() {
		return this.indirectOffsetLocation;
	}

	public int getNumSubSequences() {
		return this.subSequences.size();
	}

	public int getParentSignature() {
		return this.parentSignature;
	}

	public String getReference() {
		return this.reference;
	}

	// public int getMaxOffset() { return MaxOffset; }

	public SubSequence getSubSequence(final int theIndex) {
		return this.subSequences.get(theIndex);
	}

	/* getters */
	public List getSubSequences() {
		return this.subSequences;
	}

	/**
	 * is this byte sequence anchored to either BOF or EOF
	 * 
	 * @return
	 */
	public boolean isAnchored() {
		return getReference().endsWith("EOFoffset")
				|| getReference().endsWith("BOFoffset");
	}

	public boolean isBigEndian() {
		return this.bigEndian;
	}

	/**
	 * checks whether the binary file specified by targetFile is compliant with
	 * this byte sequence
	 * 
	 * @param targetFile
	 *            The binary file to be identified
	 */
	public boolean isFileCompliant(final ByteReader targetFile) {
		// System.out.println("Looking at new byte sequence with reference "+Reference);
		// initialise variables and start with the file marker at the beginning
		// of the file
		boolean isCompliant = true;
		final boolean reverseOrder = (getReference()
				.equalsIgnoreCase("EOFoffset")) ? true : false;
		final int ssLoopStart = reverseOrder ? getNumSubSequences() - 1 : 0;
		final int ssLoopEnd = reverseOrder ? -1 : getNumSubSequences();
		final int searchDirection = reverseOrder ? -1 : 1;
		if (reverseOrder) {
			targetFile.setFileMarker(targetFile.getNumBytes() - 1L);
		} else {
			targetFile.setFileMarker(0L);
		}

		// check whether each subsequence in turn is compliant
		for (int iSS = ssLoopStart; (searchDirection * iSS < searchDirection
				* ssLoopEnd)
				& isCompliant; iSS += searchDirection) {
			final boolean isFixedStart = getReference().endsWith("EOFoffset")
					|| getReference().endsWith("BOFoffset");
			if ((iSS == ssLoopStart) && (isFixedStart)) {
				isCompliant = getSubSequence(iSS).isFoundAtStartOfFile(
						targetFile, reverseOrder, this.bigEndian); // ,
				// MaxOffset);
			} else {
				isCompliant = getSubSequence(iSS).isFoundAfterFileMarker(
						targetFile, reverseOrder, this.bigEndian);
			}

		}
		return isCompliant;

	}

	// public void setMaxOffset( String theMaxOffset ) { this.MaxOffset =
	// Integer.parseInt(theMaxOffset); }
	@Override
	public void setAttributeValue(final String name, final String value) {
		if (name.equals("Reference")) {
			setReference(value);
		} else if (name.equals("Endianness")) {
			setEndianness(value);
		} else if (name.equals("IndirectOffsetLength")) {
			setIndirectOffsetLength(value);
		} else if (name.equals("IndirectOffsetLocation")) {
			setIndirectOffsetLocation(value);
		} else {
			MessageDisplay.unknownAttributeWarning(name, getElementName());
		}
	}

	public void setEndianness(final String endianness) {
		this.bigEndian = !endianness.equals("Little-endian");
	}

	public void setIndirectOffsetLength(final String indirectOffsetLength) {
		this.indirectOffsetLength = Integer.parseInt(indirectOffsetLength);
	}

	public void setIndirectOffsetLocation(final String indirectOffsetLocation) {
		this.indirectOffsetLocation = Integer
				.parseInt(indirectOffsetLocation);
	}

	public void setParentSignature(final int parentSignature) {
		this.parentSignature = parentSignature;
	}

	public void setReference(final String theRef) {
		this.reference = theRef;
	}

	public void setSubSequences(final List<SubSequence> SubSequences) {
		this.subSequences = SubSequences;
	}

	@Override
	public String toString() {
		return this.reference + "{" + this.subSequences + "}";
	}
}
