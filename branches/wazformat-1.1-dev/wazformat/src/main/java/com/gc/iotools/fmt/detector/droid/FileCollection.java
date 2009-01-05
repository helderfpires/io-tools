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
 */

package com.gc.iotools.fmt.detector.droid;

import java.util.Iterator;

import com.gc.iotools.fmt.detector.droid.binFileReader.InputStreamByteReader;
import com.gc.iotools.fmt.detector.droid.binFileReader.UrlByteReader;
import com.gc.iotools.fmt.detector.droid.xmlReader.SimpleElement;

/**
 * Class to hold configuration data for the uk. Elements of this collection are
 * IdentificationFile objects. This data is read from and saved to an XML
 * configuration file.
 * 
 */
public class FileCollection extends SimpleElement {

	/**
	 * file path to store file list
	 */
	private String myFileName;
	/**
	 * Holds IdentificationFile objects
	 */
	private final IndexedHashSet<Integer, IdentificationFile> myFiles;

	/**
	 * Signature file version for a file that is read in - only used to check it
	 * is the same as currently loaded
	 */
	private int mySigFileVersion = 0;

	private long startTime;
	private long stopTime;
	private final long lastNumFiles = -1;

	/**
	 * Creates a FileCollection object
	 */
	public FileCollection() {
		this.myFileName = AnalysisController.FILE_LIST_FILE_NAME;
		this.myFiles = new IndexedHashSet<Integer, IdentificationFile>();
	}

	/**
	 * Adds an element/elements to the collection If filepath is a path to file
	 * then add that file If filepath is a folder path then add contents of the
	 * folder
	 * 
	 * @param theFile
	 *            Filepath of file or folder
	 * @param isRecursive
	 *            if true add all subfolders and subsubfolders , etc
	 */
	public void addFile(final String theFile, final boolean isRecursive) {

		if (UrlByteReader.isURL(theFile)) {
			// File object is a URL: add if it isn't a duplicate
			this.myFiles.add(this.getNumFiles(),
					new IdentificationFile(theFile));
			return;
		}

		if (InputStreamByteReader.isInputStream(theFile)) {
			// File is an input stream: add if it isn't a duplicate
			this.myFiles.add(this.getNumFiles(),
					new IdentificationFile(theFile));
		}

		try {
			final java.io.File f = new java.io.File(theFile);

			// Is file object a directory or file?
			if (f.isDirectory()) {

				// File object is a directory/folder
				// Iterate through directory ,create IdentificationFile objects
				// and add them to the collection
				final java.io.File[] folderFiles = f.listFiles();
				int numFiles = 0;
				try {
					numFiles = folderFiles.length;
				} catch (final Exception e) {
					MessageDisplay
							.generalWarning("Unable to read directory "
									+ theFile
									+ "\nThis may be because you do not have the correct permissions.");
				}
				for (int m = 0; m < numFiles; m++) {
					if (folderFiles[m].isFile()) {
						// If file exists and not duplicate then add
						final IdentificationFile idFile = new IdentificationFile(
								folderFiles[m].getPath());
						this.myFiles.add(this.getNumFiles(), idFile);
					} else if (folderFiles[m].isDirectory() && isRecursive) {
						// If subdirectory found and recursive is on add
						// contents of that folder
						addFile(folderFiles[m].getPath(), isRecursive);
					}
				}

			} else if (f.isFile()) {
				// File object is a File then add file if it isn't a duplicate
				final IdentificationFile idFile = new IdentificationFile(f
						.getPath());
				this.myFiles.add(this.getNumFiles(), idFile);
			}

		} catch (final Exception e) {
			MessageDisplay
					.generalWarning("The following error occured while adding "
							+ theFile + ":\n" + e.toString());
		}

	}

	/**
	 * Add a new identification file to list (used when reading in an XML file
	 * collection file)
	 * 
	 * @param theFile
	 *            A new IdentificationFile object which will be populated from
	 *            file
	 */
	public void addIdentificationFile(final IdentificationFile theFile) {
		this.myFiles.add(this.getNumFiles(), theFile);
	}

	public boolean containsIndex(final int index) {
		return this.myFiles.containsKey(index);
	}

	/**
	 * Gets the IdentificationFile object by position in collection
	 * 
	 * @param theIndex
	 *            position of element in collection
	 * @return Specified IdentificationFile object
	 */
	public IdentificationFile getFile(final int theIndex) {
		return (IdentificationFile) this.myFiles.get(theIndex);
	}

	/**
	 * Gets the file name where file list stored
	 * 
	 * @return file name where file list stored
	 */
	public String getFileName() {
		return this.myFileName;
	}

	public java.util.Enumeration getIndexKeys() {
		return this.myFiles.getIndexKeys();
	}

	/**
	 * Get iterator for files in the collection
	 * 
	 * @return list iterator
	 */
	public Iterator<IdentificationFile> getIterator() {
		return this.myFiles.iterator();
	}

	/**
	 * returns the signature file version recorded in the file collection file
	 */
	public int getLoadedFileSigFileVersion() {
		return this.mySigFileVersion;
	}

	/**
	 * Get the number of files in the collection
	 * 
	 * @return no. of files in the collection
	 */
	public int getNumFiles() {
		return this.myFiles.size();
	}

	/**
	 * Empty file list
	 */
	public void removeAll() {
		this.myFiles.clear();
	}

	/**
	 * Remove file from the file list
	 * 
	 * @param theFileIndex
	 *            Index of file to remove
	 */
	public void removeFile(final int theFileIndex) {
		this.myFiles.removeByIndex(theFileIndex);
	}

	/**
	 * Remove file from the file list
	 * 
	 * @param theFileName
	 *            Full file name of file to remove
	 */
	public void removeFile(final String theFileName) {
		final IdentificationFile comparisonFile = new IdentificationFile(
				theFileName);
		this.myFiles.remove(comparisonFile);
	}

	/**
	 * Populates the details of the FileCollection when read in from XML file
	 * 
	 * @param theName
	 *            Name of the attribute read in
	 * @param theValue
	 *            Value of the attribute read in
	 */
	@Override
	public void setAttributeValue(final String theName, final String theValue) {
		if (theName.equals(AnalysisController.LABEL_APPLICATION_VERSION)) {
			setDROIDVersion(theValue);
		} else if (theName.equals("SigFileVersion")) {
			setSignatureFileVersion(theValue);
		} else if (theName.equals(AnalysisController.LABEL_DATE_CREATED)) {
			setDateCreated(theValue);
		} else {
			MessageDisplay.unknownAttributeWarning(theName, this
					.getElementName());
		}
	}

	public void setDateCreated(final String value) {
		// Ignore the contents of this element
	}

	public void setDROIDVersion(final String value) {
		if (!value.equals(AnalysisController.getDROIDVersion())) {
			MessageDisplay
					.generalWarning("This file was generated with DROID version "
							+ value
							+ ".  Current version is "
							+ AnalysisController.getDROIDVersion());
		}
	}

	/**
	 * Add a single file or folder to the collection
	 * 
	 * @param theFile
	 *            path to file or folder
	 */
	public void setFile(final String theFile) {
		this.addFile(theFile, false);
	}

	/**
	 * Specify the file path for where to store the file list
	 * 
	 * @param theFileName
	 *            path of where to save the file
	 */
	public void setFileName(final String theFileName) {
		this.myFileName = theFileName;
	}

	public void setSignatureFileVersion(final String value) {
		try {
			this.mySigFileVersion = Integer.parseInt(value);
		} catch (final NumberFormatException e) {
			MessageDisplay
					.generalWarning("The SigFileVersion attribute should be an integer");
		}
	}

}
