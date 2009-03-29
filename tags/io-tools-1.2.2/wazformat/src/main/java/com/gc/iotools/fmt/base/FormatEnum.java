package com.gc.iotools.fmt.base;

/*
 * Copyright (c) 2008,2009 Davide Simonetti.
 * This source code is released under the BSD Software License.
 */
import java.util.List;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.enums.ValuedEnum;

/**
 * Enum of detected formats. Some format is "simple", some other is just a way
 * of encoding another kind of content.
 * 
 * If a user need to support a new format he must extend this class. It can't be
 * a Java 5 enum because (AFAIK) they can't be extended.
 * <table>
 * <thead>
 * <tr>
 * <td>Enum name</td>
 * <td>Description</td>
 * <td>Supported versions</td>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>ADVANCED_SYSTEMS_FORMAT</td>
 * <td>Advanced Systems Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>AVI</td>
 * <td>Audio/Video Interleaved Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>DWG</td>
 * <td>AutoCAD Drawing</td>
 * <td>1.0, 1.2, 1.3, 1.4, 2.0, 2.1, 2.2, 2.5, 2.6, R9, R10, R11/12, R13, R14,
 * 2000-2002, 2004-2005</td>
 * </tr>
 * <tr>
 * <td>BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK</td>
 * <td>Binary Interchange File Format (BIFF) Workbook</td>
 * <td>4W, 5, 7, 8, 8X</td>
 * </tr>
 * <tr>
 * <td>BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET</td>
 * <td>Binary Interchange File Format (BIFF) Worksheet</td>
 * <td>2, 3, 4S</td>
 * </tr>
 * <tr>
 * <td>BROADCAST_WAVE</td>
 * <td>Broadcast WAVE</td>
 * <td>1, 0</td>
 * </tr>
 * <tr>
 * <td>BZIP2</td>
 * <td>This enum describes the
 * {@link <a href="http://en.wikipedia.org/wiki/Bzip2">BZIP2</a>}compressed file
 * format.</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>DROID_FILE_COLLECTION_FILE_FORMAT</td>
 * <td>DROID File Collection File Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>DROID_SIGNATURE_FILE_FORMAT</td>
 * <td>DROID Signature File Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>DXB</td>
 * <td>Drawing Interchange Binary Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>DRAWING_INTERCHANGE_FILE_FORMAT_ASCII</td>
 * <td>Drawing Interchange File Format (ASCII)</td>
 * <td>1.0, 1.2, 1.3, 1.4, 2.0, 2.1, 2.2, 2.5, 2.6, R9, R10, R11/12, R13, R14,
 * 2000-2002, 2004-2005, Generic</td>
 * </tr>
 * <tr>
 * <td>DRAWING_INTERCHANGE_FILE_FORMAT_BINARY</td>
 * <td>Drawing Interchange File Format (Binary)</td>
 * <td>R10, R11/12, R13, R14, 2000-2002, 2004-2005</td>
 * </tr>
 * <tr>
 * <td>EPSF</td>
 * <td>Encapsulated PostScript File Format</td>
 * <td>1.2, 3.0, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO</td>
 * <td>Exchangeable Image File Format (Audio)</td>
 * <td>2.1, 2.2, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED</td>
 * <td>Exchangeable Image File Format (Compressed)</td>
 * <td>2.1, 2.2, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED</td>
 * <td>Exchangeable Image File Format (Uncompressed)</td>
 * <td>2.2, 2.1, 2.0</td>
 * </tr>
 * <tr>
 * <td>XHTML</td>
 * <td>Extensible Hypertext Markup Language</td>
 * <td>1.0, 1.1</td>
 * </tr>
 * <tr>
 * <td>XML</td>
 * <td>Extensible Markup Language</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>FITS</td>
 * <td>Flexible Image Transport System</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>GZ</td>
 * <td>GZIP Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>GIF</td>
 * <td>Graphics Interchange Format</td>
 * <td>1987a, 1989a</td>
 * </tr>
 * <tr>
 * <td>HTML</td>
 * <td>Hypertext Markup Language</td>
 * <td>2.0, 3.2, 4.0, 4.01</td>
 * </tr>
 * <tr>
 * <td>JPEG</td>
 * <td>JPEG File Interchange Format</td>
 * <td>1.00, 1.01, 1.02</td>
 * </tr>
 * <tr>
 * <td>JAR</td>
 * <td>Java Archive Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>MPEG_1_VIDEO_FORMAT</td>
 * <td>MPEG-1 Video Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>MPEG_2_VIDEO_FORMAT</td>
 * <td>MPEG-2 Video Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>MS_DOS_EXECUTABLE</td>
 * <td>MS-DOS Executable</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>FLV</td>
 * <td>Macromedia FLV</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>SWF</td>
 * <td>Macromedia Flash</td>
 * <td>1, 2, 3, 4, 5, 6, 7</td>
 * </tr>
 * <tr>
 * <td>PPT</td>
 * <td>Microsoft Powerpoint Presentation</td>
 * <td>4.0, 95, 97-2002</td>
 * </tr>
 * <tr>
 * <td>DOC</td>
 * <td>Microsoft Word for Windows Document</td>
 * <td>6.0/95, 97-2003, 1.0, 2.0</td>
 * </tr>
 * <tr>
 * <td>OLE2_COMPOUND_DOCUMENT_FORMAT</td>
 * <td>OLE2 Compound Document Format</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>ODB</td>
 * <td>OpenDocument Database Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>ODG</td>
 * <td>OpenDocument Drawing Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>OPENDOCUMENT_FORMAT</td>
 * <td>OpenDocument Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>ODP</td>
 * <td>OpenDocument Presentation Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>ODS</td>
 * <td>OpenDocument Spreadsheet Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>ODT</td>
 * <td>OpenDocument Text Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>SXC</td>
 * <td>OpenOffice Calc</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>SXD</td>
 * <td>OpenOffice Draw</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>SXI</td>
 * <td>OpenOffice Impress</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>SXW</td>
 * <td>OpenOffice Writer</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>PCX</td>
 * <td>PCX</td>
 * <td>0, 2, 3, 4, 5</td>
 * </tr>
 * <tr>
 * <td>PDF</td>
 * <td>Portable Document Format</td>
 * <td>1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6</td>
 * </tr>
 * <tr>
 * <td>PNG</td>
 * <td>Portable Network Graphics</td>
 * <td>1.0, 1.1, 1.2</td>
 * </tr>
 * <tr>
 * <td>POSTSCRIPT</td>
 * <td>PostScript</td>
 * <td>2.0, 2.1, 3.0</td>
 * </tr>
 * <tr>
 * <td>POSTSCRIPT</td>
 * <td>Postscript</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>QTM</td>
 * <td>Quicktime</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>RAW_JPEG_STREAM</td>
 * <td>Raw JPEG Stream</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>RTF</td>
 * <td>Rich Text Format</td>
 * <td>1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8</td>
 * </tr>
 * <tr>
 * <td>SVG</td>
 * <td>Scalable Vector Graphics</td>
 * <td>1.0, 1.1</td>
 * </tr>
 * <tr>
 * <td>STILL_PICTURE_INTERCHANGE_FILE_FORMAT</td>
 * <td>Still Picture Interchange File Format</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>TAGGED_IMAGE_FILE_FORMAT</td>
 * <td>Tagged Image File Format</td>
 * <td>3, 4, 5, 6</td>
 * </tr>
 * <tr>
 * <td>WRL</td>
 * <td>Virtual Reality Modeling Language</td>
 * <td>1.0, 97</td>
 * </tr>
 * <tr>
 * <td>WAVEFORM_AUDIO</td>
 * <td>Waveform Audio</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>BMP</td>
 * <td>Windows Bitmap</td>
 * <td>1.0, 2.0, 3.0, 3.0 NT, 4.0, 5.0</td>
 * </tr>
 * <tr>
 * <td>WINDOWS_MEDIA_AUDIO</td>
 * <td>Windows Media Audio</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>WINDOWS_MEDIA_VIDEO</td>
 * <td>Windows Media Video</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>WINDOWS_NEW_EXECUTABLE</td>
 * <td>Windows New Executable</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>WINDOWS_PORTABLE_EXECUTABLE</td>
 * <td>Windows Portable Executable</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>WPG</td>
 * <td>WordPerfect Graphics Metafile</td>
 * <td>1.0</td>
 * </tr>
 * <tr>
 * <td>WORDPERFECT_FOR_MS_DOS_DOCUMENT</td>
 * <td>WordPerfect for MS-DOS Document</td>
 * <td>5.0</td>
 * </tr>
 * <tr>
 * <td>WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT</td>
 * <td>WordPerfect for MS-DOS/Windows Document</td>
 * <td>5.1</td>
 * </tr>
 * <tr>
 * <td>ZIP</td>
 * <td>ZIP Format</td>
 * <td></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @since 1.0
 * @author dvd.smnt
 */
public class FormatEnum extends ValuedEnum {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5685026597452193393L;

	/**
	 * Constant integer for enum : ADVANCED_SYSTEMS_FORMAT .
	 */
	public static final int ADVANCED_SYSTEMS_FORMAT_INT = 691;
	/**
	 * Enum : ADVANCED_SYSTEMS_FORMAT : this enum describes format Advanced
	 * Systems Format.
	 */
	public static final FormatEnum ADVANCED_SYSTEMS_FORMAT = new FormatEnum(
			"ADVANCED_SYSTEMS_FORMAT", ADVANCED_SYSTEMS_FORMAT_INT);

	/**
	 * Constant integer for enum : AVI .
	 */
	public static final int AVI_INT = 655;
	/**
	 * Enum : AVI : this enum describes format Audio/Video Interleaved Format.
	 */
	public static final FormatEnum AVI = new FormatEnum("AVI", AVI_INT);

	/**
	 * Constant integer for enum : BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK
	 * .
	 */
	public static final int BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK_INT = 685;
	/**
	 * Enum : BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK : this enum describes
	 * format Binary Interchange File Format (BIFF) Workbook. Supported versions
	 * :4W, 5, 7, 8, 8X
	 */
	public static final FormatEnum BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK = new FormatEnum(
			"BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK",
			BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKBOOK_INT);

	/**
	 * Constant integer for enum : BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET
	 * .
	 */
	public static final int BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET_INT = 680;
	/**
	 * Enum : BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET : this enum
	 * describes format Binary Interchange File Format (BIFF) Worksheet.
	 * Supported versions :2, 3, 4S
	 */
	public static final FormatEnum BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET = new FormatEnum(
			"BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET",
			BINARY_INTERCHANGE_FILE_FORMAT_BIFF_WORKSHEET_INT);

	/**
	 * Constant integer for enum : BMP .
	 */
	public static final int BMP_INT = 732;
	/**
	 * Enum : BMP : this enum describes format Windows Bitmap. Supported
	 * versions :1.0, 2.0, 3.0, 3.0 NT, 4.0, 5.0
	 */
	public static final FormatEnum BMP = new FormatEnum("BMP", BMP_INT);

	/**
	 * Constant integer for enum : BROADCAST_WAVE .
	 */
	public static final int BROADCAST_WAVE_INT = 735;
	/**
	 * Enum : BROADCAST_WAVE : this enum describes format Broadcast WAVE.
	 * Supported versions :1, 0
	 */
	public static final FormatEnum BROADCAST_WAVE = new FormatEnum(
			"BROADCAST_WAVE", BROADCAST_WAVE_INT);

	/**
	 * Constant integer for enum : BZIP2 .
	 */
	public static final int BZIP2_INT = 102;
	/**
	 * Enum : BZIP2 : this enum describes the
	 * {@link <a href="http://en.wikipedia.org/wiki/Bzip2">BZIP2</a>} compressed
	 * file format.
	 */
	public static final FormatEnum BZIP2 = new FormatEnum("BZIP2", BZIP2_INT);

	/**
	 * Constant integer for enum : DOC .
	 */
	public static final int DOC_INT = 734;
	/**
	 * Enum : DOC : this enum describes format Microsoft Word for Windows
	 * Document. Supported versions :6.0/95, 97-2003, 1.0, 2.0
	 */
	public static final FormatEnum DOC = new FormatEnum("DOC", DOC_INT);

	/**
	 * Constant integer for enum : DRAWING_INTERCHANGE_FILE_FORMAT_ASCII .
	 */
	public static final int DRAWING_INTERCHANGE_FILE_FORMAT_ASCII_INT = 766;
	/**
	 * Enum : DRAWING_INTERCHANGE_FILE_FORMAT_ASCII : this enum describes format
	 * Drawing Interchange File Format (ASCII). Supported versions :1.0, 1.2,
	 * 1.3, 1.4, 2.0, 2.1, 2.2, 2.5, 2.6, R9, R10, R11/12, R13, R14, 2000-2002,
	 * 2004-2005, Generic
	 */
	public static final FormatEnum DRAWING_INTERCHANGE_FILE_FORMAT_ASCII = new FormatEnum(
			"DRAWING_INTERCHANGE_FILE_FORMAT_ASCII",
			DRAWING_INTERCHANGE_FILE_FORMAT_ASCII_INT);

	/**
	 * Constant integer for enum : DRAWING_INTERCHANGE_FILE_FORMAT_BINARY .
	 */
	public static final int DRAWING_INTERCHANGE_FILE_FORMAT_BINARY_INT = 744;
	/**
	 * Enum : DRAWING_INTERCHANGE_FILE_FORMAT_BINARY : this enum describes
	 * format Drawing Interchange File Format (Binary). Supported versions :R10,
	 * R11/12, R13, R14, 2000-2002, 2004-2005
	 */
	public static final FormatEnum DRAWING_INTERCHANGE_FILE_FORMAT_BINARY = new FormatEnum(
			"DRAWING_INTERCHANGE_FILE_FORMAT_BINARY",
			DRAWING_INTERCHANGE_FILE_FORMAT_BINARY_INT);

	/**
	 * Constant integer for enum : DROID_FILE_COLLECTION_FILE_FORMAT .
	 */
	public static final int DROID_FILE_COLLECTION_FILE_FORMAT_INT = 769;
	/**
	 * Enum : DROID_FILE_COLLECTION_FILE_FORMAT : this enum describes format
	 * DROID File Collection File Format. Supported versions :1.0
	 */
	public static final FormatEnum DROID_FILE_COLLECTION_FILE_FORMAT = new FormatEnum(
			"DROID_FILE_COLLECTION_FILE_FORMAT",
			DROID_FILE_COLLECTION_FILE_FORMAT_INT);

	/**
	 * Constant integer for enum : DROID_SIGNATURE_FILE_FORMAT .
	 */
	public static final int DROID_SIGNATURE_FILE_FORMAT_INT = 768;
	/**
	 * Enum : DROID_SIGNATURE_FILE_FORMAT : this enum describes format DROID
	 * Signature File Format. Supported versions :1.0
	 */
	public static final FormatEnum DROID_SIGNATURE_FILE_FORMAT = new FormatEnum(
			"DROID_SIGNATURE_FILE_FORMAT", DROID_SIGNATURE_FILE_FORMAT_INT);

	/**
	 * Constant integer for enum : DWG .
	 */
	public static final int DWG_INT = 709;
	/**
	 * Enum : DWG : this enum describes format AutoCAD Drawing. Supported
	 * versions :1.0, 1.2, 1.3, 1.4, 2.0, 2.1, 2.2, 2.5, 2.6, R9, R10, R11/12,
	 * R13, R14, 2000-2002, 2004-2005
	 */
	public static final FormatEnum DWG = new FormatEnum("DWG", DWG_INT);

	/**
	 * Constant integer for enum : DXB .
	 */
	public static final int DXB_INT = 761;
	/**
	 * Enum : DXB : this enum describes format Drawing Interchange Binary
	 * Format. Supported versions :1.0
	 */
	public static final FormatEnum DXB = new FormatEnum("DXB", DXB_INT);

	/**
	 * Constant integer for enum : EPSF .
	 */
	public static final int EPSF_INT = 332;
	/**
	 * Enum : EPSF : this enum describes format Encapsulated PostScript File
	 * Format. Supported versions :1.2, 3.0, 2.0
	 */
	public static final FormatEnum EPSF = new FormatEnum("EPSF", EPSF_INT);

	/**
	 * Constant integer for enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO .
	 */
	public static final int EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO_INT = 750;
	/**
	 * Enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO : this enum describes format
	 * Exchangeable Image File Format (Audio). Supported versions :2.1, 2.2, 2.0
	 */
	public static final FormatEnum EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO = new FormatEnum(
			"EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO",
			EXCHANGEABLE_IMAGE_FILE_FORMAT_AUDIO_INT);

	/**
	 * Constant integer for enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED .
	 */
	public static final int EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED_INT = 751;
	/**
	 * Enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED : this enum describes
	 * format Exchangeable Image File Format (Compressed). Supported versions
	 * :2.1, 2.2, 2.0
	 */
	public static final FormatEnum EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED = new FormatEnum(
			"EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED",
			EXCHANGEABLE_IMAGE_FILE_FORMAT_COMPRESSED_INT);

	/**
	 * Constant integer for enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED .
	 */
	public static final int EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED_INT = 752;
	/**
	 * Enum : EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED : this enum describes
	 * format Exchangeable Image File Format (Uncompressed). Supported versions
	 * :2.2, 2.1, 2.0
	 */
	public static final FormatEnum EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED = new FormatEnum(
			"EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED",
			EXCHANGEABLE_IMAGE_FILE_FORMAT_UNCOMPRESSED_INT);

	/**
	 * Constant integer for enum : XHTML .
	 */
	public static final int XHTML_INT = 644;
	/**
	 * Enum : XHTML : this enum describes format Extensible Hypertext Markup
	 * Language. Supported versions :1.0, 1.1
	 */
	public static final FormatEnum XHTML = new FormatEnum("XHTML", XHTML_INT);

	/**
	 * Constant integer for enum : XML .
	 */
	public static final int XML_INT = 638;
	/**
	 * Enum : XML : this enum describes format Extensible Markup Language.
	 * Supported versions :1.0
	 */
	public static final FormatEnum XML = new FormatEnum("XML", XML_INT);

	/**
	 * Constant integer for enum : FITS .
	 */
	public static final int FITS_INT = 657;
	/**
	 * Enum : FITS : this enum describes format Flexible Image Transport System.
	 */
	public static final FormatEnum FITS = new FormatEnum("FITS", FITS_INT);

	/**
	 * Constant integer for enum : FLV .
	 */
	public static final int FLV_INT = 653;
	/**
	 * Enum : FLV : this enum describes format Macromedia FLV. Supported
	 * versions :1
	 */
	public static final FormatEnum FLV = new FormatEnum("FLV", FLV_INT);

	/**
	 * Constant integer for enum : GIF .
	 */
	public static final int GIF_INT = 620;
	/**
	 * Enum : GIF : this enum describes format Graphics Interchange Format.
	 * Supported versions :1987a, 1989a
	 */
	public static final FormatEnum GIF = new FormatEnum("GIF", GIF_INT);

	/**
	 * Constant integer for enum : GZ .
	 */
	public static final int GZ_INT = 386;
	/**
	 * Enum : GZ : this enum describes format GZIP Format.
	 */
	public static final FormatEnum GZ = new FormatEnum("GZ", GZ_INT);

	/**
	 * Constant integer for enum : HTML .
	 */
	public static final int HTML_INT = 645;
	/**
	 * Enum : HTML : this enum describes format Hypertext Markup Language.
	 * Supported versions :2.0, 3.2, 4.0, 4.01
	 */
	public static final FormatEnum HTML = new FormatEnum("HTML", HTML_INT);

	/**
	 * Constant integer for enum : JAR .
	 */
	public static final int JAR_INT = 777;
	/**
	 * Enum : JAR : this enum describes format Java Archive Format.
	 */
	public static final FormatEnum JAR = new FormatEnum("JAR", JAR_INT);

	/**
	 * Constant integer for enum : JPEG .
	 */
	public static final int JPEG_INT = 669;
	/**
	 * Enum : JPEG : this enum describes format JPEG File Interchange Format.
	 * Supported versions :1.00, 1.01, 1.02
	 */
	public static final FormatEnum JPEG = new FormatEnum("JPEG", JPEG_INT);

	/**
	 * Constant integer for enum : MPEG_1_VIDEO_FORMAT .
	 */
	public static final int MPEG_1_VIDEO_FORMAT_INT = 659;
	/**
	 * Enum : MPEG_1_VIDEO_FORMAT : this enum describes format MPEG-1 Video
	 * Format.
	 */
	public static final FormatEnum MPEG_1_VIDEO_FORMAT = new FormatEnum(
			"MPEG_1_VIDEO_FORMAT", MPEG_1_VIDEO_FORMAT_INT);

	/**
	 * Constant integer for enum : MPEG_2_VIDEO_FORMAT .
	 */
	public static final int MPEG_2_VIDEO_FORMAT_INT = 660;
	/**
	 * Enum : MPEG_2_VIDEO_FORMAT : this enum describes format MPEG-2 Video
	 * Format.
	 */
	public static final FormatEnum MPEG_2_VIDEO_FORMAT = new FormatEnum(
			"MPEG_2_VIDEO_FORMAT", MPEG_2_VIDEO_FORMAT_INT);

	/**
	 * Constant integer for enum : MS_DOS_EXECUTABLE .
	 */
	public static final int MS_DOS_EXECUTABLE_INT = 774;
	/**
	 * Enum : MS_DOS_EXECUTABLE : this enum describes format MS-DOS Executable.
	 */
	public static final FormatEnum MS_DOS_EXECUTABLE = new FormatEnum(
			"MS_DOS_EXECUTABLE", MS_DOS_EXECUTABLE_INT);

	/**
	 * Constant integer for enum : ODB .
	 */
	public static final int ODB_INT = 783;
	/**
	 * Enum : ODB : this enum describes format OpenDocument Database Format.
	 * Supported versions :1.0
	 */
	public static final FormatEnum ODB = new FormatEnum("ODB", ODB_INT);

	/**
	 * Constant integer for enum : ODG .
	 */
	public static final int ODG_INT = 782;
	/**
	 * Enum : ODG : this enum describes format OpenDocument Drawing Format.
	 * Supported versions :1.0
	 */
	public static final FormatEnum ODG = new FormatEnum("ODG", ODG_INT);

	/**
	 * Constant integer for enum : ODP .
	 */
	public static final int ODP_INT = 781;
	/**
	 * Enum : ODP : this enum describes format OpenDocument Presentation Format.
	 * Supported versions :1.0
	 */
	public static final FormatEnum ODP = new FormatEnum("ODP", ODP_INT);

	/**
	 * Constant integer for enum : ODS .
	 */
	public static final int ODS_INT = 780;
	/**
	 * Enum : ODS : this enum describes format OpenDocument Spreadsheet Format.
	 * Supported versions :1.0
	 */
	public static final FormatEnum ODS = new FormatEnum("ODS", ODS_INT);

	/**
	 * Constant integer for enum : ODT .
	 */
	public static final int ODT_INT = 779;
	/**
	 * Enum : ODT : this enum describes format OpenDocument Text Format.
	 * Supported versions :1.0
	 */
	public static final FormatEnum ODT = new FormatEnum("ODT", ODT_INT);

	/**
	 * Constant integer for enum : OLE2_COMPOUND_DOCUMENT_FORMAT .
	 */
	public static final int OLE2_COMPOUND_DOCUMENT_FORMAT_INT = 767;
	/**
	 * Enum : OLE2_COMPOUND_DOCUMENT_FORMAT : this enum describes format OLE2
	 * Compound Document Format.
	 */
	public static final FormatEnum OLE2_COMPOUND_DOCUMENT_FORMAT = new FormatEnum(
			"OLE2_COMPOUND_DOCUMENT_FORMAT",
			OLE2_COMPOUND_DOCUMENT_FORMAT_INT);

	/**
	 * Constant integer for enum : OPENDOCUMENT_FORMAT .
	 */
	public static final int OPENDOCUMENT_FORMAT_INT = 778;
	/**
	 * Enum : OPENDOCUMENT_FORMAT : this enum describes format OpenDocument
	 * Format. Supported versions :1.0
	 */
	public static final FormatEnum OPENDOCUMENT_FORMAT = new FormatEnum(
			"OPENDOCUMENT_FORMAT", OPENDOCUMENT_FORMAT_INT);

	/**
	 * Constant integer for enum : PCX .
	 */
	public static final int PCX_INT = 625;
	/**
	 * Enum : PCX : this enum describes format PCX. Supported versions :0, 2, 3,
	 * 4, 5
	 */
	public static final FormatEnum PCX = new FormatEnum("PCX", PCX_INT);

	/**
	 * Constant integer for enum : PDF .
	 */
	public static final int PDF_INT = 637;
	/**
	 * Enum : PDF : this enum describes format Portable Document Format.
	 * Supported versions :1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
	 */
	public static final FormatEnum PDF = new FormatEnum("PDF", PDF_INT);

	/**
	 * Constant integer for enum : PNG .
	 */
	public static final int PNG_INT = 666;
	/**
	 * Enum : PNG : this enum describes format Portable Network Graphics.
	 * Supported versions :1.0, 1.1, 1.2
	 */
	public static final FormatEnum PNG = new FormatEnum("PNG", PNG_INT);

	/**
	 * Constant integer for enum : POSTSCRIPT .
	 */
	public static final int POSTSCRIPT_INT = 138;
	/**
	 * Enum : POSTSCRIPT : this enum describes format Postscript. Supported
	 * versions :1.0
	 */
	public static final FormatEnum POSTSCRIPT = new FormatEnum("POSTSCRIPT",
			POSTSCRIPT_INT);

	/**
	 * Constant integer for enum : PPT .
	 */
	public static final int PPT_INT = 135;
	/**
	 * Enum : PPT : this enum describes format Microsoft Powerpoint
	 * Presentation. Supported versions :4.0, 95, 97-2002
	 */
	public static final FormatEnum PPT = new FormatEnum("PPT", PPT_INT);

	/**
	 * Constant integer for enum : QTM .
	 */
	public static final int QTM_INT = 658;
	/**
	 * Enum : QTM : this enum describes format Quicktime.
	 */
	public static final FormatEnum QTM = new FormatEnum("QTM", QTM_INT);

	/**
	 * Constant integer for enum : RAW_JPEG_STREAM .
	 */
	public static final int RAW_JPEG_STREAM_INT = 670;
	/**
	 * Enum : RAW_JPEG_STREAM : this enum describes format Raw JPEG Stream.
	 */
	public static final FormatEnum RAW_JPEG_STREAM = new FormatEnum(
			"RAW_JPEG_STREAM", RAW_JPEG_STREAM_INT);

	/**
	 * Constant integer for enum : RTF .
	 */
	public static final int RTF_INT = 753;
	/**
	 * Enum : RTF : this enum describes format Rich Text Format. Supported
	 * versions :1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8
	 */
	public static final FormatEnum RTF = new FormatEnum("RTF", RTF_INT);

	/**
	 * Constant integer for enum : STILL_PICTURE_INTERCHANGE_FILE_FORMAT .
	 */
	public static final int STILL_PICTURE_INTERCHANGE_FILE_FORMAT_INT = 671;
	/**
	 * Enum : STILL_PICTURE_INTERCHANGE_FILE_FORMAT : this enum describes format
	 * Still Picture Interchange File Format. Supported versions :1.0
	 */
	public static final FormatEnum STILL_PICTURE_INTERCHANGE_FILE_FORMAT = new FormatEnum(
			"STILL_PICTURE_INTERCHANGE_FILE_FORMAT",
			STILL_PICTURE_INTERCHANGE_FILE_FORMAT_INT);

	/**
	 * Constant integer for enum : SVG .
	 */
	public static final int SVG_INT = 635;
	/**
	 * Enum : SVG : this enum describes format Scalable Vector Graphics.
	 * Supported versions :1.0, 1.1
	 */
	public static final FormatEnum SVG = new FormatEnum("SVG", SVG_INT);

	/**
	 * Constant integer for enum : SWF .
	 */
	public static final int SWF_INT = 652;
	/**
	 * Enum : SWF : this enum describes format Macromedia Flash. Supported
	 * versions :1, 2, 3, 4, 5, 6, 7
	 */
	public static final FormatEnum SWF = new FormatEnum("SWF", SWF_INT);

	/**
	 * Constant integer for enum : SXC .
	 */
	public static final int SXC_INT = 746;
	/**
	 * Enum : SXC : this enum describes format OpenOffice Calc. Supported
	 * versions :1.0
	 */
	public static final FormatEnum SXC = new FormatEnum("SXC", SXC_INT);

	/**
	 * Constant integer for enum : SXD .
	 */
	public static final int SXD_INT = 748;
	/**
	 * Enum : SXD : this enum describes format OpenOffice Draw. Supported
	 * versions :1.0
	 */
	public static final FormatEnum SXD = new FormatEnum("SXD", SXD_INT);

	/**
	 * Constant integer for enum : SXI .
	 */
	public static final int SXI_INT = 747;
	/**
	 * Enum : SXI : this enum describes format OpenOffice Impress. Supported
	 * versions :1.0
	 */
	public static final FormatEnum SXI = new FormatEnum("SXI", SXI_INT);

	/**
	 * Constant integer for enum : SXW .
	 */
	public static final int SXW_INT = 745;
	/**
	 * Enum : SXW : this enum describes format OpenOffice Writer. Supported
	 * versions :1.0
	 */
	public static final FormatEnum SXW = new FormatEnum("SXW", SXW_INT);

	/**
	 * Constant integer for enum : TAGGED_IMAGE_FILE_FORMAT .
	 */
	public static final int TAGGED_IMAGE_FILE_FORMAT_INT = 612;
	/**
	 * Enum : TAGGED_IMAGE_FILE_FORMAT : this enum describes format Tagged Image
	 * File Format. Supported versions :3, 4, 5, 6
	 */
	public static final FormatEnum TAGGED_IMAGE_FILE_FORMAT = new FormatEnum(
			"TAGGED_IMAGE_FILE_FORMAT", TAGGED_IMAGE_FILE_FORMAT_INT);

	/**
	 * Constant integer for enum : WAVEFORM_AUDIO .
	 */
	public static final int WAVEFORM_AUDIO_INT = 654;
	/**
	 * Enum : WAVEFORM_AUDIO : this enum describes format Waveform Audio.
	 */
	public static final FormatEnum WAVEFORM_AUDIO = new FormatEnum(
			"WAVEFORM_AUDIO", WAVEFORM_AUDIO_INT);

	/**
	 * Constant integer for enum : WINDOWS_MEDIA_AUDIO .
	 */
	public static final int WINDOWS_MEDIA_AUDIO_INT = 692;
	/**
	 * Enum : WINDOWS_MEDIA_AUDIO : this enum describes format Windows Media
	 * Audio.
	 */
	public static final FormatEnum WINDOWS_MEDIA_AUDIO = new FormatEnum(
			"WINDOWS_MEDIA_AUDIO", WINDOWS_MEDIA_AUDIO_INT);

	/**
	 * Constant integer for enum : WINDOWS_MEDIA_VIDEO .
	 */
	public static final int WINDOWS_MEDIA_VIDEO_INT = 693;
	/**
	 * Enum : WINDOWS_MEDIA_VIDEO : this enum describes format Windows Media
	 * Video.
	 */
	public static final FormatEnum WINDOWS_MEDIA_VIDEO = new FormatEnum(
			"WINDOWS_MEDIA_VIDEO", WINDOWS_MEDIA_VIDEO_INT);

	/**
	 * Constant integer for enum : WINDOWS_NEW_EXECUTABLE .
	 */
	public static final int WINDOWS_NEW_EXECUTABLE_INT = 775;
	/**
	 * Enum : WINDOWS_NEW_EXECUTABLE : this enum describes format Windows New
	 * Executable.
	 */
	public static final FormatEnum WINDOWS_NEW_EXECUTABLE = new FormatEnum(
			"WINDOWS_NEW_EXECUTABLE", WINDOWS_NEW_EXECUTABLE_INT);

	/**
	 * Constant integer for enum : WINDOWS_PORTABLE_EXECUTABLE .
	 */
	public static final int WINDOWS_PORTABLE_EXECUTABLE_INT = 776;
	/**
	 * Enum : WINDOWS_PORTABLE_EXECUTABLE : this enum describes format Windows
	 * Portable Executable.
	 */
	public static final FormatEnum WINDOWS_PORTABLE_EXECUTABLE = new FormatEnum(
			"WINDOWS_PORTABLE_EXECUTABLE", WINDOWS_PORTABLE_EXECUTABLE_INT);

	/**
	 * Constant integer for enum : WORDPERFECT_FOR_MS_DOS_DOCUMENT .
	 */
	public static final int WORDPERFECT_FOR_MS_DOS_DOCUMENT_INT = 736;
	/**
	 * Enum : WORDPERFECT_FOR_MS_DOS_DOCUMENT : this enum describes format
	 * WordPerfect for MS-DOS Document. Supported versions :5.0
	 */
	public static final FormatEnum WORDPERFECT_FOR_MS_DOS_DOCUMENT = new FormatEnum(
			"WORDPERFECT_FOR_MS_DOS_DOCUMENT",
			WORDPERFECT_FOR_MS_DOS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT .
	 */
	public static final int WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT_INT = 737;
	/**
	 * Enum : WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT : this enum describes
	 * format WordPerfect for MS-DOS/Windows Document. Supported versions :5.1
	 */
	public static final FormatEnum WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT = new FormatEnum(
			"WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT",
			WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WPG .
	 */
	public static final int WPG_INT = 738;
	/**
	 * Enum : WPG : this enum describes format WordPerfect Graphics Metafile.
	 * Supported versions :1.0
	 */
	public static final FormatEnum WPG = new FormatEnum("WPG", WPG_INT);

	/**
	 * Constant integer for enum : WRL .
	 */
	public static final int WRL_INT = 662;
	/**
	 * Enum : WRL : this enum describes format Virtual Reality Modeling
	 * Language. Supported versions :1.0, 97
	 */
	public static final FormatEnum WRL = new FormatEnum("WRL", WRL_INT);

	/**
	 * Constant integer for enum : ZIP .
	 */
	public static final int ZIP_INT = 382;
	/**
	 * Enum : ZIP : this enum describes format ZIP Format.
	 */
	public static final FormatEnum ZIP = new FormatEnum("ZIP", ZIP_INT);

	public static final int UNLISTED_INT = 0;
	public static final FormatEnum UNLISTED = new FormatEnum("unlisted",
			UNLISTED_INT);

	public static final int BASE64_INT = 50;
	public static final FormatEnum BASE64 = new FormatEnum("BASE64",
			BASE64_INT);

	public static final int M7M_INT = 300;
	public static final FormatEnum M7M = new FormatEnum("M7M", M7M_INT);

	public static final int PEM_INT = 600;
	public static final FormatEnum PEM = new FormatEnum("PEM", PEM_INT);
	public static final int PKCS7_INT = 700;
	public static final FormatEnum PKCS7 = new FormatEnum("PKCS7", PKCS7_INT);
	public static final int TIMESTAMP_INT = 800;
	public static final FormatEnum TIMESTAMP = new FormatEnum("TIMESTAMP",
			TIMESTAMP_INT);
	public static final int UNKNOWN_INT = 900;
	public static final FormatEnum UNKNOWN = new FormatEnum("UNKNOWN",
			UNKNOWN_INT);

	public static FormatEnum getEnum(final Class clazz, final String name) {
		return (FormatEnum) Enum.getEnum(clazz, name);
	}

	@SuppressWarnings("unchecked")
	public static FormatEnum[] values() {
		final List<FormatEnum> enumList = getEnumList(FormatEnum.class);
		return enumList.toArray(new FormatEnum[0]);
	}

	/**
	 * constructor is protected to allow extensions.
	 * 
	 * @param enumName
	 * @param enumInt
	 */
	protected FormatEnum(final String enumName, final int enumInt) {
		super(enumName, enumInt);
	}

	@Override
	public String toString() {
		return "[" + getName() + "]";
	}
}
