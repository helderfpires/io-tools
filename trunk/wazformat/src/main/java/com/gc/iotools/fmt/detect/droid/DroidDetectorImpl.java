package com.gc.iotools.fmt.detect.droid;

/*
 * Copyright (c) 2008,2012 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.base.FileFormatHit;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.binFileReader.IdentificationFile;
import uk.gov.nationalarchives.droid.binFileReader.RandomAccessByteReader;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.signatureFile.FileFormat;
import uk.gov.nationalarchives.droid.signatureFile.InternalSignature;
import uk.gov.nationalarchives.droid.xmlReader.SAXModelBuilder;

import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;

/**
 * Implementation of a FileDetector that relies on droid classes.
 * 
 * @author dvd.smnt
 */
public class DroidDetectorImpl implements DetectionLibrary {
	/**
	 * Namespace for the xml file format signatures file.
	 */
	private static final String SIGNATURE_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/SignatureFile";
	private static final String SIGNATURE_FILE = "DROID_SignatureFile_V18.xml";
	private static final String MAPPING_FILE = "mapping.properties";

	private final Class<? extends FormatEnum> formatEnumClass;
	private static final Map<String, FFSignatureFile> CONF_MAP = new HashMap<String, FFSignatureFile>();
	private final String configFile;

	private final Properties mapping;
	private static final Logger LOG = LoggerFactory
			.getLogger(DroidDetectorImpl.class);

	public DroidDetectorImpl() {
		this(FormatEnum.class, SIGNATURE_FILE, MAPPING_FILE);
	}

	public DroidDetectorImpl(
			final Class<? extends FormatEnum> formatEnumClass,
			final String signatureFile, final String mappingFileStr) {
		final Class<? extends FormatEnum> clazz = (formatEnumClass == null ? FormatEnum.class
				: formatEnumClass);
		if (!(FormatEnum.class.isAssignableFrom(clazz))) {
			throw new IllegalArgumentException(" [" + formatEnumClass
					+ "] should be an subclass of [" + FormatEnum.class + "]");
		}
		this.formatEnumClass = clazz;
		this.configFile = StringUtils.isBlank(signatureFile) ? SIGNATURE_FILE
				: signatureFile;

		if (!CONF_MAP.containsKey(this.configFile)) {
			final URL confFile = DroidDetectorImpl.class
					.getResource(this.configFile);
			try {
				if ((confFile == null) || (confFile.openConnection() == null)) {
					throw new IllegalArgumentException("Configuration file ["
							+ signatureFile + "] not found or not readable.");
				}
			} catch (final IOException e) {
				throw new IllegalArgumentException(
						"Problem reading configuration file ["
								+ signatureFile + "] url[" + confFile + "]",
						e);
			}
			final FFSignatureFile fsigfile = parseSigFile(confFile);
			CONF_MAP.put(this.configFile, fsigfile);
		}
		this.mapping = new Properties();
		try {
			final InputStream mappingIs = DroidDetectorImpl.class
					.getResourceAsStream(mappingFileStr);
			this.mapping.load(mappingIs);
			mappingIs.close();
		} catch (final IOException e) {
			throw new IllegalArgumentException("can't load resource["
					+ mappingFileStr + "]", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatId detect(final FormatEnum[] enabledFormats,
			final ResettableInputStream stream) throws IOException {
		final IdentificationFile idFile = new IdentificationFile("-");
//		File file = File.createTempFile("io-tools-doc", ".doc");
//		FileOutputStream fos = new FileOutputStream(file);
//		IOUtils.copy(stream, fos);
//		fos.close();
		// System.out.println("Data written to [" + file.getName() + "]");
		stream.resetToBeginning();
		final ByteReader testFile = new RandomAccessByteReader(idFile, stream);
//		FileByteReader testFile = new FileByteReader(new IdentificationFile(
//				file.getAbsolutePath()), true, file.getAbsolutePath());
		final FFSignatureFile fsigfile = CONF_MAP.get(this.configFile);
		FFSignatureFile reduced = reduceDetectedSequences(fsigfile,
				enabledFormats);
		reduced.runFileIdentification(testFile);
		final int n = testFile.getNumHits();
		FormatId fenumId = new FormatId(FormatEnum.UNKNOWN, null);
		final Collection<FormatEnum> enabledFormatCollection = Arrays
				.asList(enabledFormats);
		for (int i = 0; (i < n)
				&& (FormatEnum.UNKNOWN.equals(fenumId.format)); i++) {
			final FileFormatHit ffhit = testFile.getHit(i);
			final uk.gov.nationalarchives.droid.signatureFile.FileFormat fileFormat = ffhit
					.getFileFormat();
			final FormatId tmpFid = getFormatEnum(fileFormat);
			if (FormatEnum.UNLISTED.equals(tmpFid.format)) {
				LOG.warn("Format number[" + fileFormat.getID()
						+ "] not found in configured mapping. format ["
						+ fileFormat.getName() + "] was returned as ["
						+ FormatEnum.UNLISTED + "] version["
						+ fileFormat.getName() + "]");
			}
			if (enabledFormatCollection.contains(tmpFid.format)) {
				fenumId = tmpFid;
			}
		}
		return fenumId;
	}

	private FFSignatureFile reduceDetectedSequences(FFSignatureFile fsig,
			FormatEnum[] enabled) {
		Collection<FileFormat> fformats = new ArrayList<FileFormat>();
		Collection<InternalSignature> intSigs = new ArrayList<InternalSignature>();
		Collection<FormatEnum> enabledColl = new ArrayList<FormatEnum>(
				Arrays.asList(enabled));
		Map<Integer, InternalSignature> internalSignatureMap = new HashMap<Integer, InternalSignature>();

		for (int i = 0; i < fsig.getNumInternalSignatures(); i++) {
			InternalSignature intSignature = fsig.getInternalSignature(i);
			internalSignatureMap.put(intSignature.getID(), intSignature);
		}
		int n = fsig.getNumFileFormats();
		for (int i = 0; i < n; i++) {
			FileFormat ff = fsig.getFileFormat(i);
			final FormatId formatEnum = getFormatEnum(ff);
			if (ff.getNumInternalSignatures() > 0
					&& enabledColl.contains(formatEnum.format)) {
				fformats.add(ff);
				for (int j = 0; j < ff.getNumInternalSignatures(); j++) {
					int intSigId = ff.getInternalSignatureID(j);
					InternalSignature intSig = internalSignatureMap
							.get(intSigId);
					if (intSig != null) {
						intSigs.add(intSig);
					} else {
						LOG.warn("Internal signature id[" + intSig
								+ "] not found.");
					}
				}
			}
		}
		return new FFSignatureFile(fformats, intSigs);
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum[] getDetectedFormats() {
		final FFSignatureFile fsigfile = CONF_MAP.get(this.configFile);
		final Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (int i = 0; i < fsigfile.getNumFileFormats(); i++) {
			final FileFormat fformat = fsigfile.getFileFormat(i);
			final FormatId fid = getFormatEnum(fformat);
			final FormatEnum fenum = fid.format;
			if (!FormatEnum.UNLISTED.equals(fenum)
					&& !FormatEnum.UNKNOWN.equals(fenum)) {
				result.add(fenum);
			}
		}
		return result.toArray(new FormatEnum[result.size()]);
	}

	private FormatId getFormatEnum(final FileFormat id) {
		FormatEnum fenum = null;
		for (final Object key : this.mapping.keySet()) {
			final String value = this.mapping.getProperty((String) key);
			if (value.contains(Integer.toString(id.getID()))) {
				final String kname = (String) key;
				fenum = FormatEnum.getEnum(this.formatEnumClass, kname);
				break;
			}
		}
		FormatId result;
		if (fenum == null) {
			result = new FormatId(FormatEnum.UNLISTED, id.getName());
		} else {
			result = new FormatId(fenum, id.getVersion());
		}
		return result;
	}

	private XMLReader getXMLReader(final SAXModelBuilder mb) throws Exception {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		// factory.setValidating(true);
		final SAXParser saxParser = factory.newSAXParser();
		final XMLReader parser = saxParser.getXMLReader();
		// URL url = DroidDetectorImpl.class
		// .getResource("DROID_SignatureFile.xsd");
		// parser.setProperty(
		// "http://java.sun.com/xml/jaxp/properties/schemaSource", url);
		mb.setupNamespace(SIGNATURE_FILE_NS, true);
		parser.setContentHandler(mb);
		return parser;
	}

	// private void addFiles() {
	// // Process each file in array of filenames
	// for (int fileNum = 0; fileNum < this.myFiles.length
	// && !this.myAnalysisController.isAnalysisCancelled(); fileNum++) {
	// this.logger.recordPath(this.myFiles[fileNum]);
	// if (UrlByteReader.isURL(this.myFiles[fileNum])) {
	// // File is a URL
	// processFile(new IdentificationFile(this.myFiles[fileNum]));
	// } else if (InputStreamByteReader
	// .isInputStream(this.myFiles[fileNum])) {
	// // File is an input stream
	// processFile(new IdentificationFile(this.myFiles[fileNum]));
	// } else {
	// // File is local
	// addFile(this.myFiles[fileNum]);
	// }
	// }
	// }

	private FFSignatureFile parseSigFile(final URL signatureFileURL) {

		final SAXModelBuilder mb = new SAXModelBuilder();
		try {
			final XMLReader parser = getXMLReader(mb);
			final InputStream signatureFileStream = signatureFileURL
					.openStream();
			parser.parse(new InputSource(signatureFileStream));
			signatureFileStream.close();
		} catch (final Exception e) {
			throw new IllegalStateException(
					"Error reading configuration file " + "["
							+ signatureFileURL + "]", e);
		}
		final FFSignatureFile fsgf = (FFSignatureFile) mb.getModel();
		fsgf.prepareForUse();
		return fsgf;
	}
}
