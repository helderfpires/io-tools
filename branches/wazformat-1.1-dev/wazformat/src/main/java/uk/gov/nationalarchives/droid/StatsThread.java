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

package uk.gov.nationalarchives.droid;

import java.io.File;
import java.util.Date;

import uk.gov.nationalarchives.droid.binFileReader.AbstractByteReader;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.binFileReader.InputStreamByteReader;
import uk.gov.nationalarchives.droid.binFileReader.UrlByteReader;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;


/**
 * Class containing thread to run file identification analysis
 * 
 * @author walm
 */
public class StatsThread extends Thread {
	private FileCollection myFileCollection;
	private final StatsLogger logger;
	private final boolean useFileCollection;
	private boolean isRecursive = true;
	private String[] myFiles;
	private final FFSignatureFile mySigFile;
	private final AnalysisController myAnalysisController;

	// parameters used to measure performance
	private int readTime = 0;
	private int algoTime = 0;

	/**
	 * Creates a new instance of StatsThread with files stored in a file
	 * collection
	 */
	public StatsThread(final StatsLogger logger,
			final FileCollection theFileCollection,
			final FFSignatureFile theSigFile,
			final AnalysisController theAnalysisController) {
		this.myFileCollection = theFileCollection;
		this.mySigFile = theSigFile;
		this.myAnalysisController = theAnalysisController;
		this.useFileCollection = true;
		this.isRecursive = false;
		this.logger = logger;
	}

	/**
	 * Creates a new instance of StatsThread with files stored in an array
	 */
	public StatsThread(final StatsLogger logger, final String[] files,
			final FFSignatureFile theSigFile,
			final AnalysisController theAnalysisController,
			final boolean isRecursive) {
		this.myFiles = files;
		this.mySigFile = theSigFile;
		this.myAnalysisController = theAnalysisController;
		this.useFileCollection = false;
		this.isRecursive = isRecursive;
		this.logger = logger;
	}

	/**
	 * Get the StatsLogger object
	 */
	public StatsLogger getLogger() {
		return this.logger;
	}

	/**
	 * Runs the thread for file statistics generation
	 */
	@Override
	public void run() {

		// Let AnalysisController know that anlaysis has started
		this.myAnalysisController.setAnalysisStart();

		if (this.useFileCollection == true) {
			// Process each file in collection
			final java.util.Iterator<IdentificationFile> it = this.myFileCollection
					.getIterator();
			this.logger.recordPath("File list: "
					+ this.myFileCollection.getFileName());
			while (it.hasNext()
					&& !this.myAnalysisController.isAnalysisCancelled()) {
				this.processFile(it.next());
			}
		} else {
			this.addFiles();
		}
		if (this.myAnalysisController.isVerbose()) {
			System.out.println("Processed " + this.logger.getTotalFiles()
					+ " files");
			System.out.println();
			this.logger.outputTables();
		}

		// Let AnalysisController know that anlaysis is complete
		this.myAnalysisController.setStatsComplete();

	}

	/**
	 * Read in file / folder and have it processed. Recurse if needed.
	 * 
	 * @param filename
	 *            full filename or directory name
	 */
	private void addFile(final String filename) {

		try {
			final java.io.File f = new java.io.File(filename);

			// Prevent processing of unix symbolic links to folders
			if (f.getAbsolutePath().equalsIgnoreCase(f.getCanonicalPath()) == false) {
				System.out.println("Ignored symbolic link "
						+ f.getAbsolutePath() + "," + f.getCanonicalPath());
				return;
			}

			// Is file object a directory or file?
			if (f.isDirectory()) {

				// Read files in directory
				final java.io.File[] folderFiles = f.listFiles();
				int numFiles = 0;
				try {
					numFiles = folderFiles.length;

					// Loop for each file in directory
					for (int m = 0; m < numFiles
							&& !this.myAnalysisController.isAnalysisCancelled(); m++) {
						if (folderFiles[m].isFile()) {
							this.processFile(new IdentificationFile(
									folderFiles[m].getPath()));
						} else if (folderFiles[m].isDirectory()
								&& this.isRecursive) {
							// If subdirectory found and recursive is on add
							// contents of that folder
							this.addFile(folderFiles[m].getPath());
						}
					}

				} catch (final Exception e) {
					this.logger.incNumBadFolders();
				}

			} else if (f.isFile()) {
				this.processFile(new IdentificationFile(f.getPath()));
			}

		} catch (final Exception e) {
			MessageDisplay
					.generalWarning("The following error occured while adding "
							+ filename + ":\n" + e.toString());
		}

	}

	/**
	 * Add files set by constructor to StatsLogger
	 */
	private void addFiles() {
		// Process each file in array of filenames
		for (int fileNum = 0; fileNum < this.myFiles.length
				&& !this.myAnalysisController.isAnalysisCancelled(); fileNum++) {
			this.logger.recordPath(this.myFiles[fileNum]);
			if (UrlByteReader.isURL(this.myFiles[fileNum])) {
				// File is a URL
				processFile(new IdentificationFile(this.myFiles[fileNum]));
			} else if (InputStreamByteReader
					.isInputStream(this.myFiles[fileNum])) {
				// File is an input stream
				processFile(new IdentificationFile(this.myFiles[fileNum]));
			} else {
				// File is local
				addFile(this.myFiles[fileNum]);
			}
		}
	}

	/**
	 * Identify file, get basic file properties and send to StatsLogger
	 * 
	 * @param idFile
	 *            the file to work process
	 */
	private void processFile(final IdentificationFile idFile) {

		if (this.myAnalysisController.isVerbose()) {
			if (this.logger.getTotalFiles() % 500 == 0) {
				System.out.println("Processed " + this.logger.getTotalFiles()
						+ " files");
			}
		}

		final String idFileName = idFile.getFilePath();
		final Date startRead = new Date();
		ByteReader testFile = null;
		try {
			testFile = AbstractByteReader.newByteReader(idFile);
		} catch (final OutOfMemoryError e) {
			testFile = AbstractByteReader.newByteReader(idFile, false);
			testFile.setErrorIdent();
			testFile
					.setIdentificationWarning("The application ran out of memory while loading this file ("
							+ e.toString() + ")");
		}
		final Date endRead = new Date();
		this.readTime += (endRead.getTime() - startRead.getTime());

		// Identify file's type
		if (!testFile.isClassified()) {

			final Date startAlgo = new Date();
			try {
				this.mySigFile.runFileIdentification(testFile);
			} catch (final Exception e) {
				testFile.setErrorIdent();
				testFile
						.setIdentificationWarning("Error during identification attempt: "
								+ e.toString());
			}
			final Date endAlgo = new Date();
			this.algoTime += (endAlgo.getTime() - startAlgo.getTime());

		}

		// Get file's size and last modified date
		final File fileObject = new java.io.File(idFileName);
		final long size = testFile.getNumBytes();
		final Date modified = new Date(fileObject.lastModified());

		// Add to StatsLogger
		if (testFile.getClassification() == AnalysisController.FILE_CLASSIFICATION_ERROR) {
			this.logger.incNumBadFiles();
		} else if (testFile.getNumHits() < 1) {
			// File unidentified
			this.logger.analyseFile(null, null, null, null, size, modified);
		} else {
			// Take the first PUID as authoritative
			final String puid = testFile.getHit(0).getFileFormatPUID();
			final String mime = testFile.getHit(0).getMimeType();
			final String format = testFile.getHit(0).getFileFormatName();
			final String formatv = testFile.getHit(0).getFileFormatVersion();
			this.logger
					.analyseFile(puid, mime, format, formatv, size, modified);
		}
		// Record the fact that another file has completed
		this.myAnalysisController.incrNumCompletedFile();

	}
}
