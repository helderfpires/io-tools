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
 * $Id: AnalysisThread.java,v 1.5 2006/03/13 15:15:25 linb Exp $
 * 
 * $Log: AnalysisThread.java,v $
 * Revision 1.5  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.4  2006/02/09 13:17:41  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 *
 * $History: AnalysisThread.java $
 * 
 * *****************  Version 6  *****************
 * User: Walm         Date: 26/04/05   Time: 17:29
 * Updated in $/PRONOM4/FFIT_SOURCE
 * do not display durations at the end of a run
 * 
 * *****************  Version 5  *****************
 * User: Walm         Date: 5/04/05    Time: 17:41
 * Updated in $/PRONOM4/FFIT_SOURCE
 * review headers
 * 
 * *****************  Version 4  *****************
 * User: Walm         Date: 16/03/05   Time: 10:29
 * Updated in $/PRONOM4/FFIT_SOURCE
 * AnalysisThread class now has access to AnalysisController and so can
 * call non-static methods
 * 
 * *****************  Version 3  *****************
 * User: Walm         Date: 15/03/05   Time: 14:41
 * Updated in $/PRONOM4/FFIT_SOURCE
 * FileReader class now holds reference to identificationFile object
 * Also, a bit of a general code clean up
 *
 */

package uk.gov.nationalarchives.droid;

import java.util.Date;

import uk.gov.nationalarchives.droid.binFileReader.AbstractByteReader;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;


/**
 * Class containing thread to run file identification analysis
 * 
 * @author walm
 */
public class AnalysisThread extends Thread {
	private final FileCollection myFileCollection;
	private final FFSignatureFile mySigFile;
	private final AnalysisController myAnalysisController;
	// parameters used to measure performance
	private int readTime = 0;
	private int algoTime = 0;

	/**
	 * Creates a new instance of AnalysisThread
	 */
	public AnalysisThread(final FileCollection theFileCollection,
			final FFSignatureFile theSigFile,
			final AnalysisController theAnalysisController) {
		this.myFileCollection = theFileCollection;
		this.mySigFile = theSigFile;
		this.myAnalysisController = theAnalysisController;
	}

	/**
	 * Runs the thread for file identification analysis
	 */
	@Override
	public void run() {

		// Let AnalysisController know that anlaysis has started
		this.myAnalysisController.setAnalysisStart();

		final java.util.Iterator<IdentificationFile> it = this.myFileCollection
				.getIterator();
		while (it.hasNext() && !this.myAnalysisController.isAnalysisCancelled())
		// for (int fileNum = 0; fileNum < myFileCollection.getNumFiles() &&
		// !myAnalysisController.isAnalysisCancelled(); fileNum++)
		{
			// IdentificationFile idFile = myFileCollection.getFile(fileNum);
			final IdentificationFile idFile = it.next();
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

			/************** print out results ***************/
			// display the hits
			if (this.myAnalysisController.isVerbose()) {
				debugDisplayHits(testFile, idFileName);
			}

			// Record the fact that another file has completed
			this.myAnalysisController.incrNumCompletedFile();

		}

		// Let AnalysisController know that anlaysis is complete
		this.myAnalysisController.setAnalysisComplete();

	}

	/**
	 * Print out the list of hits (for debugging purposes only
	 */
	private void debugDisplayHits(final ByteReader testFile,
			final String fileName) {

		System.out.println("==================================");
		if (testFile.isClassified()) {

			// display file classification and any warning
			System.out.println("     " + fileName);
			if (testFile.getIdentificationWarning().length() > 0) {
				System.out.println("with warning: "
						+ testFile.getIdentificationWarning());
			}

			// display list of hits
			for (int ih = 0; ih < testFile.getNumHits(); ih++) {
				final String specificityDisplay = testFile.getHit(ih)
						.isSpecific() ? "specific" : "generic";
				System.out.println("          "
						+ testFile.getHit(ih).getHitTypeVerbose() + " "
						+ specificityDisplay + " hit for "
						+ testFile.getHit(ih).getFileFormat().getName()
						+ "  [PUID: "
						+ testFile.getHit(ih).getFileFormat().getPUID() + "]"
						+ "  [MIME: "
						+ testFile.getHit(ih).getFileFormat().getMimeType()
						+ "]");
				if (testFile.getHit(ih).getHitWarning().length() > 0) {
					System.out.println("               WARNING: "
							+ testFile.getHit(ih).getHitWarning());
				}
			}
		} else {
			System.out.println("     " + fileName + " was not classified");
		}

	}
}
