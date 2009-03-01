package com.gc.iotools.fmt.file.droid;

import java.io.File;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.base.FileFormatHit;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.binFileReader.FileByteReader;
import uk.gov.nationalarchives.droid.binFileReader.IdentificationFile;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.signatureFile.FileFormat;
import uk.gov.nationalarchives.droid.xmlReader.SAXModelBuilder;

import com.gc.iotools.fmt.base.FileDetector;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;

/**
 * Implementation of a FileDetector that relies on droid classes.
 * 
 * @author dvd.smnt
 * 
 */
public class DroidDetectorImpl implements FileDetector {
	/**
	 * Namespace for the xml file format signatures file.
	 */
	public static final String SIGNATURE_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/SignatureFile";
	private static final String SIGNATURE_FILE = "DROID_SignatureFile_V13.xml";
	private static final String MAPPING_FILE = "mapping.properties";

	private final Class<?> formatEnumClass;
	private static final Map<String, FFSignatureFile> CONF_MAP = new HashMap<String, FFSignatureFile>();
	private final String configFile;

	private final Properties mapping;
	private static final Logger LOG = LoggerFactory
			.getLogger(DroidDetectorImpl.class);

	public DroidDetectorImpl() {
		this(FormatEnum.class, SIGNATURE_FILE, MAPPING_FILE);
	}

	public DroidDetectorImpl(final Class<?> formatEnumClass,
			final String signatureFile, final String mappingFileStr) {
		final Class<?> clazz = (formatEnumClass == null ? FormatEnum.class
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
			this.mapping.load(DroidDetectorImpl.class
					.getResourceAsStream(mappingFileStr));
		} catch (final IOException e) {
			throw new IllegalArgumentException("can't load resource["
					+ mappingFileStr + "]", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatId detect(final FormatEnum[] enabledFormats,
			final File theFile) {
		final IdentificationFile idFile = new IdentificationFile(theFile
				.getAbsolutePath());
		final ByteReader testFile = new FileByteReader(idFile, true);
		final FFSignatureFile fsigfile = CONF_MAP.get(this.configFile);
		fsigfile.runFileIdentification(testFile);
		final int n = testFile.getNumHits();
		FormatId fenumId = new FormatId(FormatEnum.UNKNOWN, null);
		Collection<FormatEnum> enabledFormatCollection = Arrays
				.asList(enabledFormats);
		for (int i = 0; (i < n)
				&& (FormatEnum.UNKNOWN.equals(fenumId.format)); i++) {
			final FileFormatHit ffhit = testFile.getHit(i);
			final uk.gov.nationalarchives.droid.signatureFile.FileFormat fileFormat = ffhit
					.getFileFormat();
			FormatId tmpFid = getFormatEnum(fileFormat);
			if (enabledFormatCollection.contains(tmpFid.format)) {
				fenumId = tmpFid;
			}
		}
		return fenumId;
	}

	/**
	 * {@inheritDoc}
	 */
	public FormatEnum[] getDetectedFormats() {
		final FFSignatureFile fsigfile = CONF_MAP.get(this.configFile);
		Collection<FormatEnum> result = new ArrayList<FormatEnum>();
		for (int i = 0; i < fsigfile.getNumFileFormats(); i++) {
			FileFormat fformat = fsigfile.getFileFormat(i);
			FormatId fid = getFormatEnum(fformat);
			FormatEnum fenum = fid.format;
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
			LOG
					.warn("Format number[" + id.getID()
							+ "] not found in configured mapping. format ["
							+ id.getText() + "] was returned as ["
							+ FormatEnum.UNLISTED + "] version["
							+ id.getName() + "]");
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
