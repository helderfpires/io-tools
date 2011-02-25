/*
 * The National Archives 2005-2006. All rights reserved. See Licence.txt for
 * full licence details. Developed by: Tessella Support Services plc 3
 * Vineyard Chambers Abingdon, OX14 3PX United Kingdom http://www.tessella.com
 * Tessella/NPD/4305 PRONOM 4 $History: FileFormatCollection.java $
 * ***************** Version 2 ***************** User: Walm Date: 5/04/05
 * Time: 18:07 Updated in $/PRONOM4/FFIT_SOURCE/signatureFile review headers
 */
package uk.gov.nationalarchives.droid.signatureFile;

import java.util.ArrayList;
import java.util.List;

import uk.gov.nationalarchives.droid.base.SimpleElement;

/**
 * holds a collection of file formats used by the XML parsing code
 * 
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormatCollection extends SimpleElement {
	List<FileFormat> formats = new ArrayList<FileFormat>();

	/* setters */
	public void addFileFormat(final FileFormat format) {
		this.formats.add(format);
	}

	/* getters */
	public List getFileFormats() {
		return this.formats;
	}

	public void setFileFormats(final List<FileFormat> formats) {
		this.formats = formats;
	}
}
