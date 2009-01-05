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
 */

package com.gc.iotools.fmt.detector.droid;

import java.net.URL;

import com.gc.iotools.fmt.detector.droid.binFileReader.ByteReader;

/**
 * Public interface for droid programming API
 */
public class Droid {

	private AnalysisController analysisControl = null;
	private String version = null;

	/**
	 * No-args constructor. To be used when no config file is required.
	 * 
	 * @throws Exception
	 */
	public Droid() throws Exception {
		this.analysisControl = new AnalysisController();
	}

	/**
	 * Create the AnalysisController and set the config and signature file.
	 * 
	 * @param configFile
	 */
	public Droid(final URL configFile) throws Exception {
		this.analysisControl = new AnalysisController();
		this.analysisControl.readConfiguration(configFile);
	}

	/**
	 * Create the AnalysisController and set the config and signature file.
	 * 
	 * @param configFile
	 */
	public Droid(final URL configFile, final URL sigFileURL) throws Exception {
		this.analysisControl = new AnalysisController();
		this.analysisControl.readConfiguration(configFile);
	}

	/**
	 * Downloads a new signature file using the setting in the DROID config file
	 * but does not load it into DROID.
	 * 
	 * @param fileName
	 */
	public void downloadSigFile(final String fileName) {
		this.analysisControl.downloadwwwSigFile(fileName, false);
	}

	/**
	 * get the signature file version
	 * 
	 * @return
	 */
	public String getSignatureFileVersion() {
		return this.version;
	}

	/**
	 * identify files using droid
	 * 
	 * @param file
	 *            full path to a disk file
	 * @return IdentificationFile
	 */
	public IdentificationFile identify(final String file) {

		final IdentificationFile identificationFile = new IdentificationFile(
				file);
		ByteReader byteReader = null;
		byteReader = newByteReader(identificationFile);
		this.analysisControl.getSigFile().runFileIdentification(byteReader);

		return identificationFile;
	}

	/**
	 * Determines whether Pronom has a newer signature file available.
	 * 
	 * @param currentVersion
	 * @return
	 */
	public boolean isNewerSigFileAvailable(final int currentVersion) {
		return this.analysisControl.isNewerSigFileAvailable(currentVersion);
	}

	/**
	 * Read the signature file
	 * 
	 * @param signatureFile
	 */
	public void readSignatureFile(final String signatureFile) throws Exception {
		this.version = this.analysisControl.readSigFile(signatureFile);
	}

	/**
	 * Read the signature file
	 * 
	 * @param signatureFile
	 */
	public void readSignatureFile(final URL signatureFile) throws Exception {
		this.version = this.analysisControl.readSigFile(signatureFile);
	}

	/**
	 * Sets the URL of the signature file webservices
	 * 
	 * @param sigFileURL
	 */
	public void setSigFileURL(final String sigFileURL) {
		this.analysisControl.setSigFileURL(sigFileURL);
	}
}
