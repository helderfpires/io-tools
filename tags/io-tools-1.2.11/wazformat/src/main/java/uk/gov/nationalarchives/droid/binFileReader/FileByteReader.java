/*
 * c The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4826
 * PRONOM 5a
 *
 * $Id: FileByteReader.java,v 1.8 2006/03/13 15:15:28 linb Exp $
 *
 * $Logger: FileByteReader.java,v $
 * Revision 1.8  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.7  2006/02/09 15:34:10  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.5  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.5  2006/02/09 13:17:42  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.4  2006/02/09 12:14:16  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 */

package uk.gov.nationalarchives.droid.binFileReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import uk.gov.nationalarchives.droid.base.DroidConstants;

/**
 * The <code>FileByteReader</code> class is a <code>ByteReader</code> that reads
 * its data from a file.
 * <p/>
 * FIXME: the decision whether use randomAccess mode or buffered mode is done on
 * an OutOfMemory basis... while should be done on a fixed threshold.
 * <p>
 * This class can have two files associated with it: The file represented by it
 * (its <code>IdentificationFile</code>) and a (possibly different) backing
 * file. The purpose of this separation is so that this object can represent a
 * URL that has been downloaded or an InputStream that has been saved to disk.
 * 
 * @author linb
 */
// TODO from UCDetector: Change visibility of Class "FileByteReader" to default - May cause compile errors!
public class FileByteReader extends AbstractByteReader { // NO_UCD

	private int randomFileBufferSize = DroidConstants.FILE_BUFFER_SIZE;

	private boolean isRandomAccess = false;

	private byte[] fileBytes;
	private long myNumBytes;

	private long fileMarker;
	private RandomAccessFile myRandomAccessFile;
	private long myRAFoffset = 0L;

	private static final int MIN_RAF_BUFFER_SIZE = 65536;
	private static final int RAF_BUFFER_REDUCTION_FACTOR = 4;

	private final File file;

	/**
	 * Creates a new instance of FileByteReader
	 * <p/>
	 * <p>
	 * This constructor uses the same file to contain the data as is specified
	 * by <code>theIDFile</code>.
	 * 
	 * @param theIDFile
	 *            the source file from which the bytes will be read.
	 * @param readFile
	 *            <code>true</code> if the file is to be read
	 */
	protected FileByteReader(final IdentificationFile theIDFile,
			final boolean readFile) {
		this(theIDFile, readFile, theIDFile.getFilePath());
	}

	/**
	 * Creates a new instance of FileByteReader.
	 * <p/>
	 * <p>
	 * This constructor can set the <code>IdentificationFile</code> to a
	 * different file than the actual file used. For example, if
	 * <code>theIDFile</code> is a URL or stream, and is too big to be buffered
	 * in memory, it could be written to a temporary file. This file would then
	 * be used as a backing file to store the data.
	 * 
	 * @param theIDFile
	 *            the file represented by this object
	 * @param readFile
	 *            <code>true</code> if the file is to be read
	 * @param filePath
	 *            the backing file (containing the data)
	 */
	FileByteReader(final IdentificationFile theIDFile,
			final boolean readFile, final String filePath) {
		super(theIDFile);
		this.file = new File(filePath);
		if (readFile) {
			readFile();
		}

	}

	public byte[] getbuffer() {
		return this.fileBytes;
	}

	/**
	 * Get a byte from file
	 * 
	 * @param fileIndex
	 *            position of required byte in the file
	 * @return the byte at position <code>fileIndex</code> in the file
	 */
	public byte getByte(final long fileIndex) {

		byte theByte = 0;
		if (this.isRandomAccess) {
			// If the file is being read via random acces,
			// then read byte from buffer, otherwise read in a new buffer.
			final long theArrayIndex = fileIndex - this.myRAFoffset;
			if ((fileIndex >= this.myRAFoffset)
					&& (theArrayIndex < this.randomFileBufferSize)) {
				theByte = this.fileBytes[(int) (theArrayIndex)];
			} else {
				try {
					// Create a new buffer:
					/*
					 * //When a new buffer is created, the requesting file
					 * position is //taken to be the middle of the buffer. This
					 * is so that it will //perform equally well whether the
					 * file is being examined from //start to end or from end to
					 * start myRAFoffset = fileIndex - (myRAFbuffer/2);
					 * if(myRAFoffset<0L) { myRAFoffset = 0L; }
					 * System.out.println("    re-read file buffer");
					 * myRandomAccessFile.seek(myRAFoffset);
					 * myRandomAccessFile.read(fileBytes); theByte =
					 * fileBytes[(int)(fileIndex-myRAFoffset)];
					 */
					if (fileIndex < this.randomFileBufferSize) {
						this.myRAFoffset = 0L;
					} else if (fileIndex < this.myRAFoffset) {
						this.myRAFoffset = fileIndex
								- this.randomFileBufferSize + 1;
					} else {
						this.myRAFoffset = fileIndex;
					}
					// System.out.println("    re-read file buffer from "+myRAFoffset+
					// " for "+myRAFbuffer+" bytes");
					// System.out.println("    seek start");
					this.myRandomAccessFile.seek(this.myRAFoffset);
					// System.out.println("        read start");
					this.myRandomAccessFile.read(this.fileBytes);
					// System.out.println(fileIndex);

					// System.out.println("            read end");
					theByte = this.fileBytes[(int) (fileIndex - this.myRAFoffset)];

				} catch (final Exception e) {
					throw new RuntimeException("Problem reading byte ["
							+ fileIndex + "]", e);
				}
			}
		} else {
			// If the file is not being read by random access, then the byte
			// should be in the buffer array
			theByte = this.fileBytes[(int) fileIndex];
		}
		return theByte;
	}

	/**
	 * Gets the current position of the file marker.
	 * 
	 * @return the current position of the file marker
	 */
	public long getFileMarker() {
		return this.fileMarker;
	}

	public RandomAccessFile getMyRandomAccessFile() {
		return this.myRandomAccessFile;
	}

	/**
	 * Returns the number of bytes in the file
	 */
	public long getNumBytes() {
		return this.myNumBytes;
	}

	public int getRandomFileBufferSize() {
		return this.randomFileBufferSize;
	}

	public boolean isRandomAccess() {
		return this.isRandomAccess;
	}

	/**
	 * Position the file marker at a given byte position.
	 * <p/>
	 * <p>
	 * The file marker is used to record how far through the file the byte
	 * sequence matching algorithm has got.
	 * 
	 * @param markerPosition
	 *            The byte number in the file at which to position the marker
	 */
	public void setFileMarker(final long markerPosition) {
		if ((markerPosition < -1L) || (markerPosition > getNumBytes())) {
			throw new IllegalArgumentException(
					"  Unable to place a fileMarker at byte "
							+ Long.toString(markerPosition) + " in file "
							+ this.myIDFile.getFilePath() + " (size = "
							+ Long.toString(getNumBytes()) + " bytes)");
		} else {
			this.fileMarker = markerPosition;
		}
	}

	/**
	 * Reads in the binary file specified.
	 * <p/>
	 * <p>
	 * If there are any problems reading in the file, it gets classified as
	 * unidentified, with an explanatory warning message.
	 */
	private void readFile() {

		// If file is not readable or is empty, then it gets classified
		// as unidentified (with an explanatory warning)

		if (!this.file.exists()) {
			setErrorIdent();
			setIdentificationWarning("File does not exist");
			return;
		}

		if (!this.file.canRead()) {
			setErrorIdent();
			setIdentificationWarning("File cannot be read");
			return;
		}

		if (this.file.isDirectory()) {
			setErrorIdent();
			setIdentificationWarning("This is a directory, not a file");
			return;
		}

		FileInputStream binStream;
		try {
			binStream = new FileInputStream(this.file);
		} catch (final FileNotFoundException ex) {
			setErrorIdent();
			setIdentificationWarning("File disappeared or cannot be read");
			return;
		}

		try {

			final int numBytes = binStream.available();

			if (numBytes > 0) {
				final BufferedInputStream buffStream = new BufferedInputStream(
						binStream);

				this.fileBytes = new byte[numBytes];
				final int len = buffStream.read(this.fileBytes, 0, numBytes);

				if (len != numBytes) {
					// This means that all bytes were not successfully read
					setErrorIdent();
					setIdentificationWarning("Error reading file: "
							+ Integer.toString(len)
							+ " bytes read from file when "
							+ Integer.toString(numBytes) + " were expected");
				} else if (buffStream.read() != -1) {
					// This means that the end of the file was not reached
					setErrorIdent();
					setIdentificationWarning("Error reading file: Unable to read to the end");
				} else {
					this.myNumBytes = numBytes;
				}

				buffStream.close();
			} else {
				// If file is empty , status is error
				// this.setErrorIdent();
				this.myNumBytes = 0L;
				setIdentificationWarning("Zero-length file");

			}
			binStream.close();

			this.isRandomAccess = false;
		} catch (final IOException e) {
			setErrorIdent();
			setIdentificationWarning("Error reading file: " + e.toString());
		} catch (final OutOfMemoryError e) {
			try {
				this.myRandomAccessFile = new RandomAccessFile(this.file, "r");
				this.isRandomAccess = true;

				// record the file size
				this.myNumBytes = this.myRandomAccessFile.length();
				// try reading in a buffer
				this.myRandomAccessFile.seek(0L);
				boolean tryAgain = true;
				while (tryAgain) {
					try {
						this.fileBytes = new byte[this.randomFileBufferSize];
						this.myRandomAccessFile.read(this.fileBytes);
						tryAgain = false;
					} catch (final OutOfMemoryError e4) {
						this.randomFileBufferSize = this.randomFileBufferSize
								/ RAF_BUFFER_REDUCTION_FACTOR;
						if (this.randomFileBufferSize < MIN_RAF_BUFFER_SIZE) {
							throw e4;
						}

					}
				}

				this.myRAFoffset = 0L;
			} catch (final FileNotFoundException e2) {
				setErrorIdent();
				setIdentificationWarning("File disappeared or cannot be read");
			} catch (final Exception e2) {
				try {
					this.myRandomAccessFile.close();
				} catch (final IOException e3) {

				}
				setErrorIdent();
				setIdentificationWarning("Error reading file: "
						+ e2.toString());
			}

		}
	}
}
