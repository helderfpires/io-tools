/*
 * The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $Id: sideFragment.java,v 1.6 2006/03/13 15:15:29
 * linb Exp $ $Logger: sideFragment.java,v $ Revision 1.6 2006/03/13 15:15:29
 * linb Changed copyright holder from Crown Copyright to The National
 * Archives. Added reference to licence.txt Changed dates to 2005-2006
 * Revision 1.5 2006/02/09 15:04:37 gaur Corrected formatting Revision 1.4
 * 2006/02/07 11:30:04 gaur Added support for endianness of signature Revision
 * 1.3 2006/02/03 16:54:42 gaur We now allow general wildcards of arbitrary
 * endianness: e.g., [!~A1B1:C1D1] Revision 1.2 2006/02/02 17:15:47 gaur
 * Started migration to being able to handle byte specifier wildcards. This
 * version should have the same functionality as the old one (but making use
 * of the new ByteSeqSpecifier class). $History: sideFragment.java $
 * ***************** Version 4 ***************** User: Walm Date: 17/05/05
 * Time: 12:48 Updated in $/PRONOM4/FFIT_SOURCE/signatureFile wait for end of
 * element tag before setting its content via the completeElementContent
 * method ***************** Version 3 ***************** User: Walm Date:
 * 5/04/05 Time: 18:07 Updated in $/PRONOM4/FFIT_SOURCE/signatureFile review
 * headers
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.nationalarchives.droid.base.SimpleElement;

/**
 * holds the details of a left or right fragment associated with a subsequence
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class SideFragment extends SimpleElement {
	private static final Logger LOG = LoggerFactory
			.getLogger(SideFragment.class);
	ArrayList<ByteSeqSpecifier> myByteSpecifierSequence;
	private int myMaxOffset;
	private int myMinOffset;
	private int myPosition;
	private String mySequenceFragment;
	private int numBytes;

	/**
	 * Set the sideFragment sequence (this will have been stored in the text
	 * attribute by the setText method). Then transforms the input string into
	 * an array of bytes
	 */
	@Override
	public void completeElementContent() {
		this.numBytes = 0;
		final String theElementValue = getText();
		this.mySequenceFragment = theElementValue;
		this.myByteSpecifierSequence = new ArrayList<ByteSeqSpecifier>();
		final StringBuffer allSpecifiers = new StringBuffer(theElementValue);
		while (allSpecifiers.length() > 0) {
			try {
				final ByteSeqSpecifier bss = new ByteSeqSpecifier(
						allSpecifiers);
				this.myByteSpecifierSequence.add(bss);
				this.numBytes += bss.getNumBytes();
			} catch (final Exception e) {
			}
		}

	}

	protected ByteSeqSpecifier getByteSeqSpecifier(final int index) {
		return (this.myByteSpecifierSequence.get(index));
	}

	public int getMaxOffset() {
		return this.myMaxOffset;
	}

	public int getMinOffset() {
		return this.myMinOffset;
	}

	public int getNumBytes() {
		return this.numBytes;
	} // Total number of bytes we hold

	public int getNumByteSeqSpecifiers() {
		return this.myByteSpecifierSequence.size();
	} // Number of byte sequence specifiers we hold (each of which specifies
		// at

	// least one byte)

	/* getters */
	public int getPosition() {
		return this.myPosition;
	}

	public String getSequence() {
		return this.mySequenceFragment;
	}

	@Override
	public void setAttributeValue(final String name, final String value) {
		if (name.equals("Position")) {
			setPosition(Integer.parseInt(value));
		} else if (name.equals("MinOffset")) {
			setMinOffset(Integer.parseInt(value));
		} else if (name.equals("MaxOffset")) {
			setMaxOffset(Integer.parseInt(value));
		} else {
			final String theCMDMessage = "WARNING: Unknown XML attribute "
					+ name + " found for " + getElementName() + " ";
			LOG.warn(theCMDMessage);
		}
	}

	public void setMaxOffset(final int theMaxOffset) {
		this.myMaxOffset = theMaxOffset;
	}

	public void setMinOffset(final int theMinOffset) {
		this.myMinOffset = theMinOffset;
	}

	/* setters */
	public void setPosition(final int thePosition) {
		this.myPosition = thePosition;
	}

}
