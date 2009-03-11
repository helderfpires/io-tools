package com.gc.iotools.fmt.detect.wzf.custom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;

/**
 * 
 * @since 02/oct/08
 * @author dvd.smnt
 */
public final class ASN1Reader {
	private static final int CONSTRUCTED = 32;
	private static final int SEQUENCE = 16;

	private static final int TAGGED = 128;

	private boolean eofFound = false;

	private InputStream istream = null;

	public ASN1Reader(final InputStream istream) {
		this.istream = istream;
	}

	/**
	 * 
	 * @param expectedObjIdentifier
	 *            for pkcs7: PKCSObjectIdentifiers.signedData, <br>
	 *            for timestamp: PKCSObjectIdentifiers.id_ct_TSTInfo.getId()
	 * @throws IOException
	 * @throws FormatException
	 */
	public void check(final DERObjectIdentifier expectedObjIdentifier)
			throws IOException, FormatException {
		try {
			readSequence(this.istream);
			readTagID(this.istream, expectedObjIdentifier);
		} catch (final RuntimeException e) {
			throw new FormatException("Error reading pkcs7", e);
		}
	}

	private int getTagNo(final InputStream is, final int tag)
			throws IOException {
		final int baseTagNo = tag & ~CONSTRUCTED;
		int tagNo = baseTagNo;
		if ((tag & TAGGED) != 0) {
			tagNo = tag & 0x1f;

			//
			// with tagged object tag number is bottom 5 bits, or stored at the
			// start of the content
			//
			if (tagNo == 0x1f) {
				tagNo = 0;

				int b = is.read();

				while ((b >= 0) && ((b & 0x80) != 0)) {
					tagNo |= (b & 0x7f);
					tagNo <<= 7;
					b = is.read();
				}

				tagNo |= (b & 0x7f);
			}
		}
		return tagNo;
	}

	private int readLength(final InputStream stream) throws IOException,
			FormatException {
		int length = stream.read();
		if (length < 0) {
			throw new FormatException("EOF found when length expected");
		}

		if (length == 0x80) {
			return -1; // indefinite-length encoding
		}

		if (length > 127) {
			final int size = length & 0x7f;

			if (size > 4) {
				throw new FormatException("DER length more than 4 bytes");
			}

			length = 0;
			for (int i = 0; i < size; i++) {
				final int next = stream.read();

				if (next < 0) {
					throw new FormatException("EOF found reading length");
				}

				length = (length << 8) + next;
			}

			if (length < 0) {
				throw new FormatException(
						"corrupted steam - negative length found");
			}
		}

		return length;
	}

	private void readSequence(final InputStream is) throws IOException,
			FormatException {
		final int tag = readTag(is);
		readLength(is);
		getTagNo(is, tag);
		final int baseTagNo = tag & ~CONSTRUCTED;
		final boolean result = (SEQUENCE == baseTagNo);
		if (result == false) {
			throw new FormatException("Formato PKCS#7 non riconosciuto. "
					+ "atteso tag SEQUENCE[" + SEQUENCE + "] ottenuto ["
					+ baseTagNo + "]");
		}

	}

	private int readTag(final InputStream in) throws IOException {
		final int tag = in.read();
		if (tag == -1) {
			if (this.eofFound) {
				throw new EOFException("attempt to read past end of file.");
			}

			this.eofFound = true;
			return 0;
		}
		return tag;
	}

	private void readTagID(final InputStream is,
			final DERObjectIdentifier expectedIdentifier) throws IOException,
			FormatException {
		final ASN1InputStream asn1Stream = new ASN1InputStream(is);
		final DERObject doi = asn1Stream.readObject();
		if (!expectedIdentifier.equals(doi)) {
			throw new FormatException("Expected oid signedData["
					+ expectedIdentifier + "] got[" + doi + "]");
		}
	}
}
