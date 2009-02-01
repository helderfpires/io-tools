package uk.gov.nationalarchives.droid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.enums.ValuedEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.base.FileFormatHit;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.binFileReader.FileByteReader;
import uk.gov.nationalarchives.droid.binFileReader.IdentificationFile;
import uk.gov.nationalarchives.droid.signatureFile.DroidFileFormat;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.xmlReader.SAXModelBuilder;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.FileDetector;

public class DroidDetectorImpl implements FileDetector {
	private static class MyValEnum extends ValuedEnum {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6198407482704087671L;

		public static org.apache.commons.lang.enums.Enum getValue(
				final Class<?> clazz, final int value) {
			return getValue(clazz, value);
		}

		protected MyValEnum(final String name, final int value) {
			super(name, value);
		}
	}

	/**
	 * Namespace for the xml file format signatures file
	 */
	public static final String SIGNATURE_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/SignatureFile";
	private final Class<?> formatEnumClass;
	private final URL confFile;

	private static final Log LOG = LogFactory.getLog(DroidDetectorImpl.class);

	public DroidDetectorImpl(final Class<?> formatEnumClass,
			final String confFileStr) {
		final Class<?> clazz = (formatEnumClass == null ? FormatEnum.class
				: formatEnumClass);
		if (!(FormatEnum.class.isAssignableFrom(clazz))) {
			throw new IllegalArgumentException(" [" + formatEnumClass
					+ "] should be an subclass of [" + FormatEnum.class + "]");
		}
		this.formatEnumClass = clazz;
		this.confFile = DroidDetectorImpl.class.getResource(confFileStr);
		try {
			if (this.confFile == null || this.confFile.openConnection() == null) {
				throw new IllegalArgumentException("Configuration file ["
						+ confFileStr + "] not found or not readable.");
			}
		} catch (final IOException e) {
			throw new IllegalArgumentException(
					"Problem reading configuration file [" + confFileStr
							+ "] url[" + this.confFile + "]", e);
		}
	}

	public FormatId detect(final FormatEnum[] enabledFormats, final File theFile) {

		final FFSignatureFile fsigfile = parseSigFile(this.confFile);

		// myAnalysis.setAnalysisStart();
		final IdentificationFile idFile = new IdentificationFile(theFile
				.getAbsolutePath());
		final ByteReader testFile = new FileByteReader(idFile, true);

		fsigfile.runFileIdentification(testFile);
		testFile.getNumHits();
		final FileFormatHit ffhit = testFile.getHit(0);
		final String version = ffhit.getFileFormatVersion();
		final DroidFileFormat fileFormat = ffhit.getFileFormat();
		final int id = fileFormat.getID();
		FormatEnum fenumId;
		final org.apache.commons.lang.enums.Enum fenum = MyValEnum.getValue(
				this.formatEnumClass, id);

		if (fenum == null) {
			LOG.error("Can't find the id [" + id + "] in class ["
					+ this.formatEnumClass + "]");
			fenumId = FormatEnum.UNKNOWN;
		} else if (!(fenum instanceof FormatEnum)) {
			throw new IllegalStateException("Object [" + fenum
					+ "] is not an instance of [" + FormatEnum.class + "]");
		} else {
			fenumId = (FormatEnum) fenum;
		}
		final FormatId fId = new FormatId(fenumId, version);
		return fId;
	}

	public FormatEnum[] getDetectedFormats() {
		// TODO Auto-generated method stub
		return null;
	}

	private XMLReader getXMLReader(final SAXModelBuilder mb) throws Exception {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		final SAXParser saxParser = factory.newSAXParser();
		final XMLReader parser = saxParser.getXMLReader();
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
			final Reader in = new java.io.InputStreamReader(
					signatureFileStream, "UTF8");
			parser.parse(new InputSource(in));
		} catch (final Exception e) {
			throw new IllegalStateException("Problema leggendo il file "
					+ "di configurazione[" + signatureFileURL + "]", e);
		}
		return (FFSignatureFile) mb.getModel();
	}
}
