package com.gc.iotools.fmt.base;

import java.util.List;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.enums.ValuedEnum;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
 * <td>BINARY_INTERCHANGE_FILE_FORMAT__BIFF__WORKBOOK</td>
 * <td>Binary Interchange File Format (BIFF) Workbook</td>
 * <td>4W, 5, 7, 8, 8X</td>
 * </tr>
 * <tr>
 * <td>BINARY_INTERCHANGE_FILE_FORMAT__BIFF__WORKSHEET</td>
 * <td>Binary Interchange File Format (BIFF) Worksheet</td>
 * <td>2, 3, 4S</td>
 * </tr>
 * <tr>
 * <td>BROADCAST_WAVE</td>
 * <td>Broadcast WAVE</td>
 * <td>1, 0</td>
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
 * <td>DRAWING_INTERCHANGE_FILE_FORMAT__ASCII_</td>
 * <td>Drawing Interchange File Format (ASCII)</td>
 * <td>1.0, 1.2, 1.3, 1.4, 2.0, 2.1, 2.2, 2.5, 2.6, R9, R10, R11/12, R13, R14,
 * 2000-2002, 2004-2005, Generic</td>
 * </tr>
 * <tr>
 * <td>DRAWING_INTERCHANGE_FILE_FORMAT__BINARY_</td>
 * <td>Drawing Interchange File Format (Binary)</td>
 * <td>R10, R11/12, R13, R14, 2000-2002, 2004-2005</td>
 * </tr>
 * <tr>
 * <td>EPSF</td>
 * <td>Encapsulated PostScript File Format</td>
 * <td>1.2, 3.0, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT__AUDIO</td>
 * <td>Exchangeable Image File Format (Audio)</td>
 * <td>2.1, 2.2, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT__COMPRESSED_</td>
 * <td>Exchangeable Image File Format (Compressed)</td>
 * <td>2.1, 2.2, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXCHANGEABLE_IMAGE_FILE_FORMAT__UNCOMPRESSED_</td>
 * <td>Exchangeable Image File Format (Uncompressed)</td>
 * <td>2.2, 2.1, 2.0</td>
 * </tr>
 * <tr>
 * <td>EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE</td>
 * <td>Extensible Hypertext Markup Language</td>
 * <td>1.0, 1.1</td>
 * </tr>
 * <tr>
 * <td>EXTENSIBLE_MARKUP_LANGUAGE</td>
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
 * <td>HYPERTEXT_MARKUP_LANGUAGE</td>
 * <td>Hypertext Markup Language</td>
 * <td>2.0, 3.2, 4.0, 4.01</td>
 * </tr>
 * <tr>
 * <td>JPEG_FILE_INTERCHANGE_FORMAT</td>
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
	 * Constant integer for enum : ABD .
	 */
	public static final int ABD_INT = 300;
	/**
	 * Enum : ABD : this enum describes format Quicken Data File.
	 */
	public static final FormatEnum ABD = new FormatEnum("ABD", ABD_INT);

	/**
	 * Constant integer for enum : ACB .
	 */
	public static final int ACB_INT = 455;
	/**
	 * Enum : ACB : this enum describes format ACBM Graphics.
	 */
	public static final FormatEnum ACB = new FormatEnum("ACB", ACB_INT);

	/**
	 * Constant integer for enum : ACD .
	 */
	public static final int ACD_INT = 308;
	/**
	 * Enum : ACD : this enum describes format Adobe ACD.
	 */
	public static final FormatEnum ACD = new FormatEnum("ACD", ACD_INT);

	/**
	 * Constant integer for enum : ADC .
	 */
	public static final int ADC_INT = 523;
	/**
	 * Enum : ADC : this enum describes format Scanstudio 16-Colour Bitmap.
	 */
	public static final FormatEnum ADC = new FormatEnum("ADC", ADC_INT);

	/**
	 * Constant integer for enum : ADF .
	 */
	public static final int ADF_INT = 309;
	/**
	 * Enum : ADF : this enum describes format ESRI Arc/Info Binary Grid.
	 */
	public static final FormatEnum ADF = new FormatEnum("ADF", ADF_INT);

	/**
	 * Constant integer for enum : ADI .
	 */
	public static final int ADI_INT = 193;
	/**
	 * Enum : ADI : this enum describes format AutoCAD Device-Independent Binary
	 * Plotter File.
	 */
	public static final FormatEnum ADI = new FormatEnum("ADI", ADI_INT);

	/**
	 * Constant integer for enum : ADOBE_FRAMEMAKER_DOCUMENT .
	 */
	public static final int ADOBE_FRAMEMAKER_DOCUMENT_INT = 456;
	/**
	 * Enum : ADOBE_FRAMEMAKER_DOCUMENT : this enum describes format Adobe
	 * FrameMaker Document.
	 */
	public static final FormatEnum ADOBE_FRAMEMAKER_DOCUMENT = new FormatEnum(
			"ADOBE_FRAMEMAKER_DOCUMENT", ADOBE_FRAMEMAKER_DOCUMENT_INT);

	/**
	 * Constant integer for enum : ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT .
	 */
	public static final int ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT_INT = 229;
	/**
	 * Enum : ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT : this enum describes format
	 * Adobe FrameMaker Interchange Format.
	 */
	public static final FormatEnum ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT = new FormatEnum(
			"ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT",
			ADOBE_FRAMEMAKER_INTERCHANGE_FORMAT_INT);

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
	 * Constant integer for enum : AFC .
	 */
	public static final int AFC_INT = 463;
	/**
	 * Enum : AFC : this enum describes format Apple Sound.
	 */
	public static final FormatEnum AFC = new FormatEnum("AFC", AFC_INT);

	/**
	 * Constant integer for enum : AFI .
	 */
	public static final int AFI_INT = 533;
	/**
	 * Enum : AFI : this enum describes format Truevision Graphics Adapter.
	 */
	public static final FormatEnum AFI = new FormatEnum("AFI", AFI_INT);

	/**
	 * Constant integer for enum : AI .
	 */
	public static final int AI_INT = 49;
	/**
	 * Enum : AI : this enum describes format Adobe Illustrator.
	 */
	public static final FormatEnum AI = new FormatEnum("AI", AI_INT);

	/**
	 * Constant integer for enum : AIF .
	 */
	public static final int AIF_INT = 194;
	/**
	 * Enum : AIF : this enum describes format Audio Interchange File Format.
	 */
	public static final FormatEnum AIF = new FormatEnum("AIF", AIF_INT);

	/**
	 * Constant integer for enum : AIFC .
	 */
	public static final int AIFC_INT = 195;
	/**
	 * Enum : AIFC : this enum describes format Audio Interchange File Format
	 * (compressed).
	 */
	public static final FormatEnum AIFC = new FormatEnum("AIFC", AIFC_INT);

	/**
	 * Constant integer for enum : ALI .
	 */
	public static final int ALI_INT = 520;
	/**
	 * Enum : ALI : this enum describes format SAP Document.
	 */
	public static final FormatEnum ALI = new FormatEnum("ALI", ALI_INT);

	/**
	 * Constant integer for enum : AMI_DRAW_DRAWING .
	 */
	public static final int AMI_DRAW_DRAWING_INT = 441;
	/**
	 * Enum : AMI_DRAW_DRAWING : this enum describes format AMI Draw Drawing.
	 */
	public static final FormatEnum AMI_DRAW_DRAWING = new FormatEnum(
			"AMI_DRAW_DRAWING", AMI_DRAW_DRAWING_INT);

	/**
	 * Constant integer for enum : APR .
	 */
	public static final int APR_INT = 476;
	/**
	 * Enum : APR : this enum describes format ESRI Arc/View Project.
	 */
	public static final FormatEnum APR = new FormatEnum("APR", APR_INT);

	/**
	 * Constant integer for enum : APT .
	 */
	public static final int APT_INT = 497;
	/**
	 * Enum : APT : this enum describes format Lotus Approach View File.
	 * Supported versions :97
	 */
	public static final FormatEnum APT = new FormatEnum("APT", APT_INT);

	/**
	 * Constant integer for enum : ARC .
	 */
	public static final int ARC_INT = 310;
	/**
	 * Enum : ARC : this enum describes format Alexa Archive File.
	 */
	public static final FormatEnum ARC = new FormatEnum("ARC", ARC_INT);

	/**
	 * Constant integer for enum : AS .
	 */
	public static final int AS_INT = 311;
	/**
	 * Enum : AS : this enum describes format Applixware Spreadsheet.
	 */
	public static final FormatEnum AS = new FormatEnum("AS", AS_INT);

	/**
	 * Constant integer for enum : ASP .
	 */
	public static final int ASP_INT = 198;
	/**
	 * Enum : ASP : this enum describes format Active Server Page.
	 */
	public static final FormatEnum ASP = new FormatEnum("ASP", ASP_INT);

	/**
	 * Constant integer for enum : AU .
	 */
	public static final int AU_INT = 199;
	/**
	 * Enum : AU : this enum describes format Unix Sound File.
	 */
	public static final FormatEnum AU = new FormatEnum("AU", AU_INT);

	/**
	 * Constant integer for enum : AUTOCAD_DBCONNECT_QUERY_SET .
	 */
	public static final int AUTOCAD_DBCONNECT_QUERY_SET_INT = 70;
	/**
	 * Enum : AUTOCAD_DBCONNECT_QUERY_SET : this enum describes format AutoCAD
	 * dbConnect Query Set.
	 */
	public static final FormatEnum AUTOCAD_DBCONNECT_QUERY_SET = new FormatEnum(
			"AUTOCAD_DBCONNECT_QUERY_SET", AUTOCAD_DBCONNECT_QUERY_SET_INT);

	/**
	 * Constant integer for enum : AUTOCAD_DBCONNECT_TEMPLATE_SET .
	 */
	public static final int AUTOCAD_DBCONNECT_TEMPLATE_SET_INT = 71;
	/**
	 * Enum : AUTOCAD_DBCONNECT_TEMPLATE_SET : this enum describes format
	 * AutoCAD dbConnect Template Set.
	 */
	public static final FormatEnum AUTOCAD_DBCONNECT_TEMPLATE_SET = new FormatEnum(
			"AUTOCAD_DBCONNECT_TEMPLATE_SET",
			AUTOCAD_DBCONNECT_TEMPLATE_SET_INT);

	/**
	 * Constant integer for enum : AUTOCAD_FONT_MAPPING_TABLE .
	 */
	public static final int AUTOCAD_FONT_MAPPING_TABLE_INT = 88;
	/**
	 * Enum : AUTOCAD_FONT_MAPPING_TABLE : this enum describes format AutoCAD
	 * Font Mapping Table.
	 */
	public static final FormatEnum AUTOCAD_FONT_MAPPING_TABLE = new FormatEnum(
			"AUTOCAD_FONT_MAPPING_TABLE", AUTOCAD_FONT_MAPPING_TABLE_INT);

	/**
	 * Constant integer for enum : AVI .
	 */
	public static final int AVI_INT = 655;
	/**
	 * Enum : AVI : this enum describes format Audio/Video Interleaved Format.
	 */
	public static final FormatEnum AVI = new FormatEnum("AVI", AVI_INT);

	/**
	 * Constant integer for enum : AW .
	 */
	public static final int AW_INT = 489;
	/**
	 * Enum : AW : this enum describes format Hewlett Packard AdvanceWrite Text
	 * File.
	 */
	public static final FormatEnum AW = new FormatEnum("AW", AW_INT);

	/**
	 * Constant integer for enum : AWS .
	 */
	public static final int AWS_INT = 527;
	/**
	 * Enum : AWS : this enum describes format StatGraphics Data File.
	 */
	public static final FormatEnum AWS = new FormatEnum("AWS", AWS_INT);

	/**
	 * Constant integer for enum : BAK .
	 */
	public static final int BAK_INT = 52;
	/**
	 * Enum : BAK : this enum describes format Microsoft Excel Backup.
	 */
	public static final FormatEnum BAK = new FormatEnum("BAK", BAK_INT);

	/**
	 * Constant integer for enum : BAT .
	 */
	public static final int BAT_INT = 800;
	/**
	 * Enum : BAT : this enum describes format Batch file (executable).
	 */
	public static final FormatEnum BAT = new FormatEnum("BAT", BAT_INT);

	/**
	 * Constant integer for enum : BDB .
	 */
	public static final int BDB_INT = 508;
	/**
	 * Enum : BDB : this enum describes format Microsoft Works Database.
	 */
	public static final FormatEnum BDB = new FormatEnum("BDB", BDB_INT);

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
	 * Constant integer for enum : BLK .
	 */
	public static final int BLK_INT = 53;
	/**
	 * Enum : BLK : this enum describes format AutoCAD Block Attribute Template.
	 */
	public static final FormatEnum BLK = new FormatEnum("BLK", BLK_INT);

	/**
	 * Constant integer for enum : BOX .
	 */
	public static final int BOX_INT = 503;
	/**
	 * Enum : BOX : this enum describes format Lotus Notes File.
	 */
	public static final FormatEnum BOX = new FormatEnum("BOX", BOX_INT);

	/**
	 * Constant integer for enum : BP3 .
	 */
	public static final int BP3_INT = 57;
	/**
	 * Enum : BP3 : this enum describes format AutoCAD Batch Plot File.
	 * Supported versions :1.0-R14, 2000-2005
	 */
	public static final FormatEnum BP3 = new FormatEnum("BP3", BP3_INT);

	/**
	 * Constant integer for enum : BPS .
	 */
	public static final int BPS_INT = 509;
	/**
	 * Enum : BPS : this enum describes format Microsoft Works Document.
	 */
	public static final FormatEnum BPS = new FormatEnum("BPS", BPS_INT);

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
	 * Constant integer for enum : BTR .
	 */
	public static final int BTR_INT = 466;
	/**
	 * Enum : BTR : this enum describes format Btrieve Database. Supported
	 * versions :5.1
	 */
	public static final FormatEnum BTR = new FormatEnum("BTR", BTR_INT);

	/**
	 * Constant integer for enum : BW .
	 */
	public static final int BW_INT = 201;
	/**
	 * Enum : BW : this enum describes format SGI Monochrome Image.
	 */
	public static final FormatEnum BW = new FormatEnum("BW", BW_INT);

	/**
	 * Constant integer for enum : BZ .
	 */
	public static final int BZ_INT = 387;
	/**
	 * Enum : BZ : this enum describes format BZIP Compressed Archive.
	 */
	public static final FormatEnum BZ = new FormatEnum("BZ", BZ_INT);

	/**
	 * Constant integer for enum : BZ2 .
	 */
	public static final int BZ2_INT = 388;
	/**
	 * Enum : BZ2 : this enum describes format BZIP2 Compressed Archive.
	 */
	public static final FormatEnum BZ2 = new FormatEnum("BZ2", BZ2_INT);

	/**
	 * Constant integer for enum : CAB .
	 */
	public static final int CAB_INT = 801;
	/**
	 * Enum : CAB : this enum describes format Windows Cabinet File.
	 */
	public static final FormatEnum CAB = new FormatEnum("CAB", CAB_INT);

	/**
	 * Constant integer for enum : CALS_COMPRESSED_BITMAP .
	 */
	public static final int CALS_COMPRESSED_BITMAP_INT = 59;
	/**
	 * Enum : CALS_COMPRESSED_BITMAP : this enum describes format CALS
	 * Compressed Bitmap.
	 */
	public static final FormatEnum CALS_COMPRESSED_BITMAP = new FormatEnum(
			"CALS_COMPRESSED_BITMAP", CALS_COMPRESSED_BITMAP_INT);

	/**
	 * Constant integer for enum : CASCADING_STYLE_SHEET .
	 */
	public static final int CASCADING_STYLE_SHEET_INT = 316;
	/**
	 * Enum : CASCADING_STYLE_SHEET : this enum describes format Cascading Style
	 * Sheet.
	 */
	public static final FormatEnum CASCADING_STYLE_SHEET = new FormatEnum(
			"CASCADING_STYLE_SHEET", CASCADING_STYLE_SHEET_INT);

	/**
	 * Constant integer for enum : CBD .
	 */
	public static final int CBD_INT = 313;
	/**
	 * Enum : CBD : this enum describes format MapBrowser/MapWriter Vector Map
	 * Data.
	 */
	public static final FormatEnum CBD = new FormatEnum("CBD", CBD_INT);

	/**
	 * Constant integer for enum : CCE .
	 */
	public static final int CCE_INT = 202;
	/**
	 * Enum : CCE : this enum describes format Calendar Creator Plus Data File.
	 */
	public static final FormatEnum CCE = new FormatEnum("CCE", CCE_INT);

	/**
	 * Constant integer for enum : CCH .
	 */
	public static final int CCH_INT = 468;
	/**
	 * Enum : CCH : this enum describes format Corel Chart.
	 */
	public static final FormatEnum CCH = new FormatEnum("CCH", CCH_INT);

	/**
	 * Constant integer for enum : CDA .
	 */
	public static final int CDA_INT = 314;
	/**
	 * Enum : CDA : this enum describes format CD Audio.
	 */
	public static final FormatEnum CDA = new FormatEnum("CDA", CDA_INT);

	/**
	 * Constant integer for enum : CDR .
	 */
	public static final int CDR_INT = 557;
	/**
	 * Enum : CDR : this enum describes format CorelDraw Drawing. Supported
	 * versions :6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 3.0
	 */
	public static final FormatEnum CDR = new FormatEnum("CDR", CDR_INT);

	/**
	 * Constant integer for enum : CDT .
	 */
	public static final int CDT_INT = 61;
	/**
	 * Enum : CDT : this enum describes format CorelDraw Template.
	 */
	public static final FormatEnum CDT = new FormatEnum("CDT", CDT_INT);

	/**
	 * Constant integer for enum : CDX .
	 */
	public static final int CDX_INT = 62;
	/**
	 * Enum : CDX : this enum describes format CorelDraw Compressed Drawing.
	 */
	public static final FormatEnum CDX = new FormatEnum("CDX", CDX_INT);

	/**
	 * Constant integer for enum : CEL .
	 */
	public static final int CEL_INT = 315;
	/**
	 * Enum : CEL : this enum describes format Autodesk Animator CEL File
	 * Format.
	 */
	public static final FormatEnum CEL = new FormatEnum("CEL", CEL_INT);

	/**
	 * Constant integer for enum : CGM .
	 */
	public static final int CGM_INT = 203;
	/**
	 * Enum : CGM : this enum describes format Computer Graphics Metafile.
	 */
	public static final FormatEnum CGM = new FormatEnum("CGM", CGM_INT);

	/**
	 * Constant integer for enum : CH3 .
	 */
	public static final int CH3_INT = 63;
	/**
	 * Enum : CH3 : this enum describes format Harvard Graphics Chart. Supported
	 * versions :3.0
	 */
	public static final FormatEnum CH3 = new FormatEnum("CH3", CH3_INT);

	/**
	 * Constant integer for enum : CHI .
	 */
	public static final int CHI_INT = 467;
	/**
	 * Enum : CHI : this enum describes format ChiWriter Document.
	 */
	public static final FormatEnum CHI = new FormatEnum("CHI", CHI_INT);

	/**
	 * Constant integer for enum : CHT .
	 */
	public static final int CHT_INT = 488;
	/**
	 * Enum : CHT : this enum describes format Harvard Graphics Vector Graphics.
	 */
	public static final FormatEnum CHT = new FormatEnum("CHT", CHT_INT);

	/**
	 * Constant integer for enum : CIN .
	 */
	public static final int CIN_INT = 204;
	/**
	 * Enum : CIN : this enum describes format OS/2 Change Control File.
	 */
	public static final FormatEnum CIN = new FormatEnum("CIN", CIN_INT);

	/**
	 * Constant integer for enum : CLASS .
	 */
	public static final int CLASS_INT = 802;
	/**
	 * Enum : CLASS : this enum describes format Java Compiled Object Code.
	 */
	public static final FormatEnum CLASS = new FormatEnum("CLASS", CLASS_INT);

	/**
	 * Constant integer for enum : CLK .
	 */
	public static final int CLK_INT = 64;
	/**
	 * Enum : CLK : this enum describes format Corel R.A.V.E. File.
	 */
	public static final FormatEnum CLK = new FormatEnum("CLK", CLK_INT);

	/**
	 * Constant integer for enum : CMX .
	 */
	public static final int CMX_INT = 66;
	/**
	 * Enum : CMX : this enum describes format Corel Presentation Exchange File.
	 * Supported versions :5.0, 6/7
	 */
	public static final FormatEnum CMX = new FormatEnum("CMX", CMX_INT);

	/**
	 * Constant integer for enum : CPT .
	 */
	public static final int CPT_INT = 205;
	/**
	 * Enum : CPT : this enum describes format Corel Photo-Paint Image.
	 */
	public static final FormatEnum CPT = new FormatEnum("CPT", CPT_INT);

	/**
	 * Constant integer for enum : CPX .
	 */
	public static final int CPX_INT = 67;
	/**
	 * Enum : CPX : this enum describes format Corel CMX Compressed.
	 */
	public static final FormatEnum CPX = new FormatEnum("CPX", CPX_INT);

	/**
	 * Constant integer for enum : CSV .
	 */
	public static final int CSV_INT = 45;
	/**
	 * Enum : CSV : this enum describes format Comma Separated Values.
	 */
	public static final FormatEnum CSV = new FormatEnum("CSV", CSV_INT);

	/**
	 * Constant integer for enum : CT .
	 */
	public static final int CT_INT = 207;
	/**
	 * Enum : CT : this enum describes format Scitex CT Bitmap.
	 */
	public static final FormatEnum CT = new FormatEnum("CT", CT_INT);

	/**
	 * Constant integer for enum : CTB .
	 */
	public static final int CTB_INT = 68;
	/**
	 * Enum : CTB : this enum describes format AutoCAD Colour-Dependant Plot
	 * Style Table.
	 */
	public static final FormatEnum CTB = new FormatEnum("CTB", CTB_INT);

	/**
	 * Constant integer for enum : CUS .
	 */
	public static final int CUS_INT = 69;
	/**
	 * Enum : CUS : this enum describes format AutoCAD Custom Dictionary.
	 */
	public static final FormatEnum CUS = new FormatEnum("CUS", CUS_INT);

	/**
	 * Constant integer for enum : CUT .
	 */
	public static final int CUT_INT = 475;
	/**
	 * Enum : CUT : this enum describes format Dr Halo Bitmap.
	 */
	public static final FormatEnum CUT = new FormatEnum("CUT", CUT_INT);

	/**
	 * Constant integer for enum : DAT .
	 */
	public static final int DAT_INT = 317;
	/**
	 * Enum : DAT : this enum describes format ESRI MapInfo Data File.
	 */
	public static final FormatEnum DAT = new FormatEnum("DAT", DAT_INT);

	/**
	 * Constant integer for enum : DATA_INTERCHANGE_FORMAT .
	 */
	public static final int DATA_INTERCHANGE_FORMAT_INT = 72;
	/**
	 * Enum : DATA_INTERCHANGE_FORMAT : this enum describes format Data
	 * Interchange Format.
	 */
	public static final FormatEnum DATA_INTERCHANGE_FORMAT = new FormatEnum(
			"DATA_INTERCHANGE_FORMAT", DATA_INTERCHANGE_FORMAT_INT);

	/**
	 * Constant integer for enum : DB .
	 */
	public static final int DB_INT = 208;
	/**
	 * Enum : DB : this enum describes format Paradox Database Table. Supported
	 * versions :7
	 */
	public static final FormatEnum DB = new FormatEnum("DB", DB_INT);

	/**
	 * Constant integer for enum : DBASE_DATABASE .
	 */
	public static final int DBASE_DATABASE_INT = 404;
	/**
	 * Enum : DBASE_DATABASE : this enum describes format dBASE Database.
	 * Supported versions :II, III, IV, III+, V
	 */
	public static final FormatEnum DBASE_DATABASE = new FormatEnum(
			"DBASE_DATABASE", DBASE_DATABASE_INT);

	/**
	 * Constant integer for enum : DBASE_FOR_WINDOWS_DATABASE .
	 */
	public static final int DBASE_FOR_WINDOWS_DATABASE_INT = 558;
	/**
	 * Enum : DBASE_FOR_WINDOWS_DATABASE : this enum describes format dBASE for
	 * Windows database. Supported versions :5.0
	 */
	public static final FormatEnum DBASE_FOR_WINDOWS_DATABASE = new FormatEnum(
			"DBASE_FOR_WINDOWS_DATABASE", DBASE_FOR_WINDOWS_DATABASE_INT);

	/**
	 * Constant integer for enum : DBASE_TEXT_MEMO .
	 */
	public static final int DBASE_TEXT_MEMO_INT = 469;
	/**
	 * Enum : DBASE_TEXT_MEMO : this enum describes format dBASE Text Memo.
	 */
	public static final FormatEnum DBASE_TEXT_MEMO = new FormatEnum(
			"DBASE_TEXT_MEMO", DBASE_TEXT_MEMO_INT);

	/**
	 * Constant integer for enum : DBX .
	 */
	public static final int DBX_INT = 507;
	/**
	 * Enum : DBX : this enum describes format Microsoft Visual FoxPro Table.
	 */
	public static final FormatEnum DBX = new FormatEnum("DBX", DBX_INT);

	/**
	 * Constant integer for enum : DC2 .
	 */
	public static final int DC2_INT = 470;
	/**
	 * Enum : DC2 : this enum describes format DesignCAD Drawing.
	 */
	public static final FormatEnum DC2 = new FormatEnum("DC2", DC2_INT);

	/**
	 * Constant integer for enum : DCA .
	 */
	public static final int DCA_INT = 209;
	/**
	 * Enum : DCA : this enum describes format IBM DisplayWrite DCA Text File.
	 */
	public static final FormatEnum DCA = new FormatEnum("DCA", DCA_INT);

	/**
	 * Constant integer for enum : DCS .
	 */
	public static final int DCS_INT = 210;
	/**
	 * Enum : DCS : this enum describes format Desktop Color Separation File.
	 */
	public static final FormatEnum DCS = new FormatEnum("DCS", DCS_INT);

	/**
	 * Constant integer for enum : DEM .
	 */
	public static final int DEM_INT = 539;
	/**
	 * Enum : DEM : this enum describes format Vista Pro Graphics.
	 */
	public static final FormatEnum DEM = new FormatEnum("DEM", DEM_INT);

	/**
	 * Constant integer for enum : DGN .
	 */
	public static final int DGN_INT = 510;
	/**
	 * Enum : DGN : this enum describes format Microstation CAD Drawing.
	 * Supported versions :95
	 */
	public static final FormatEnum DGN = new FormatEnum("DGN", DGN_INT);

	/**
	 * Constant integer for enum : DIA .
	 */
	public static final int DIA_INT = 559;
	/**
	 * Enum : DIA : this enum describes format Dia Graphics Format.
	 */
	public static final FormatEnum DIA = new FormatEnum("DIA", DIA_INT);

	/**
	 * Constant integer for enum : DIGITAL_NEGATIVE_FORMAT_DNG .
	 */
	public static final int DIGITAL_NEGATIVE_FORMAT_DNG_INT = 795;
	/**
	 * Enum : DIGITAL_NEGATIVE_FORMAT_DNG : this enum describes format Digital
	 * Negative Format (DNG). Supported versions :1.1
	 */
	public static final FormatEnum DIGITAL_NEGATIVE_FORMAT_DNG = new FormatEnum(
			"DIGITAL_NEGATIVE_FORMAT_DNG", DIGITAL_NEGATIVE_FORMAT_DNG_INT);

	/**
	 * Constant integer for enum : DIR .
	 */
	public static final int DIR_INT = 505;
	/**
	 * Enum : DIR : this enum describes format Macromedia Director.
	 */
	public static final FormatEnum DIR = new FormatEnum("DIR", DIR_INT);

	/**
	 * Constant integer for enum : DOT .
	 */
	public static final int DOT_INT = 76;
	/**
	 * Enum : DOT : this enum describes format Microsoft Word Template.
	 */
	public static final FormatEnum DOT = new FormatEnum("DOT", DOT_INT);

	/**
	 * Constant integer for enum : DOX .
	 */
	public static final int DOX_INT = 511;
	/**
	 * Enum : DOX : this enum describes format MultiMate Text File. Supported
	 * versions :4.x
	 */
	public static final FormatEnum DOX = new FormatEnum("DOX", DOX_INT);

	/**
	 * Constant integer for enum : DQY .
	 */
	public static final int DQY_INT = 77;
	/**
	 * Enum : DQY : this enum describes format Microsoft Excel ODBC Query.
	 */
	public static final FormatEnum DQY = new FormatEnum("DQY", DQY_INT);

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
	 * Constant integer for enum : DTD .
	 */
	public static final int DTD_INT = 474;
	/**
	 * Enum : DTD : this enum describes format Document Type Definition.
	 */
	public static final FormatEnum DTD = new FormatEnum("DTD", DTD_INT);

	/**
	 * Constant integer for enum : DTED .
	 */
	public static final int DTED_INT = 473;
	/**
	 * Enum : DTED : this enum describes format Digital Terrain Elevation Data.
	 */
	public static final FormatEnum DTED = new FormatEnum("DTED", DTED_INT);

	/**
	 * Constant integer for enum : DV .
	 */
	public static final int DV_INT = 214;
	/**
	 * Enum : DV : this enum describes format Digital Video.
	 */
	public static final FormatEnum DV = new FormatEnum("DV", DV_INT);

	/**
	 * Constant integer for enum : DVB .
	 */
	public static final int DVB_INT = 79;
	/**
	 * Enum : DVB : this enum describes format Visual Basic Macro.
	 */
	public static final FormatEnum DVB = new FormatEnum("DVB", DVB_INT);

	/**
	 * Constant integer for enum : DW2 .
	 */
	public static final int DW2_INT = 472;
	/**
	 * Enum : DW2 : this enum describes format DesignCAD for Windows Drawing.
	 */
	public static final FormatEnum DW2 = new FormatEnum("DW2", DW2_INT);

	/**
	 * Constant integer for enum : DWF .
	 */
	public static final int DWF_INT = 80;
	/**
	 * Enum : DWF : this enum describes format AutoCAD Drawing Web Format.
	 */
	public static final FormatEnum DWF = new FormatEnum("DWF", DWF_INT);

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
	 * Constant integer for enum : DWS .
	 */
	public static final int DWS_INT = 82;
	/**
	 * Enum : DWS : this enum describes format AutoCAD Drawing Standards File.
	 */
	public static final FormatEnum DWS = new FormatEnum("DWS", DWS_INT);

	/**
	 * Constant integer for enum : DWT .
	 */
	public static final int DWT_INT = 83;
	/**
	 * Enum : DWT : this enum describes format AutoCAD Drawing Template.
	 */
	public static final FormatEnum DWT = new FormatEnum("DWT", DWT_INT);

	/**
	 * Constant integer for enum : DX .
	 */
	public static final int DX_INT = 437;
	/**
	 * Enum : DX : this enum describes format DEC Data Exchange File.
	 */
	public static final FormatEnum DX = new FormatEnum("DX", DX_INT);

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
	 * Constant integer for enum : DXX .
	 */
	public static final int DXX_INT = 85;
	/**
	 * Enum : DXX : this enum describes format Drawing Interchange Format Style
	 * Extract.
	 */
	public static final FormatEnum DXX = new FormatEnum("DXX", DXX_INT);

	/**
	 * Constant integer for enum : E00 .
	 */
	public static final int E00_INT = 318;
	/**
	 * Enum : E00 : this enum describes format ESRI Arc/Info Export File.
	 */
	public static final FormatEnum E00 = new FormatEnum("E00", E00_INT);

	/**
	 * Constant integer for enum : ELECTRONIC_ARTS_MUSIC .
	 */
	public static final int ELECTRONIC_ARTS_MUSIC_INT = 197;
	/**
	 * Enum : ELECTRONIC_ARTS_MUSIC : this enum describes format Electronic Arts
	 * Music.
	 */
	public static final FormatEnum ELECTRONIC_ARTS_MUSIC = new FormatEnum(
			"ELECTRONIC_ARTS_MUSIC", ELECTRONIC_ARTS_MUSIC_INT);

	/**
	 * Constant integer for enum : EMF .
	 */
	public static final int EMF_INT = 215;
	/**
	 * Enum : EMF : this enum describes format Windows Enhanced Metafile.
	 */
	public static final FormatEnum EMF = new FormatEnum("EMF", EMF_INT);

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
	 * Constant integer for enum : ESRI_ARC_VIEW_SHAPEFILE .
	 */
	public static final int ESRI_ARC_VIEW_SHAPEFILE_INT = 328;
	/**
	 * Enum : ESRI_ARC_VIEW_SHAPEFILE : this enum describes format ESRI Arc/View
	 * ShapeFile.
	 */
	public static final FormatEnum ESRI_ARC_VIEW_SHAPEFILE = new FormatEnum(
			"ESRI_ARC_VIEW_SHAPEFILE", ESRI_ARC_VIEW_SHAPEFILE_INT);

	/**
	 * Constant integer for enum : ESRI_MAPINFO_EXPORT_FILE .
	 */
	public static final int ESRI_MAPINFO_EXPORT_FILE_INT = 323;
	/**
	 * Enum : ESRI_MAPINFO_EXPORT_FILE : this enum describes format ESRI MapInfo
	 * Export File.
	 */
	public static final FormatEnum ESRI_MAPINFO_EXPORT_FILE = new FormatEnum(
			"ESRI_MAPINFO_EXPORT_FILE", ESRI_MAPINFO_EXPORT_FILE_INT);

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
	 * Constant integer for enum : EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE .
	 */
	public static final int EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE_INT = 644;
	/**
	 * Enum : EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE : this enum describes format
	 * Extensible Hypertext Markup Language. Supported versions :1.0, 1.1
	 */
	public static final FormatEnum EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE = new FormatEnum(
			"EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE",
			EXTENSIBLE_HYPERTEXT_MARKUP_LANGUAGE_INT);

	/**
	 * Constant integer for enum : EXTENSIBLE_MARKUP_LANGUAGE .
	 */
	public static final int EXTENSIBLE_MARKUP_LANGUAGE_INT = 638;
	/**
	 * Enum : EXTENSIBLE_MARKUP_LANGUAGE : this enum describes format Extensible
	 * Markup Language. Supported versions :1.0
	 */
	public static final FormatEnum EXTENSIBLE_MARKUP_LANGUAGE = new FormatEnum(
			"EXTENSIBLE_MARKUP_LANGUAGE", EXTENSIBLE_MARKUP_LANGUAGE_INT);

	/**
	 * Constant integer for enum : FFT .
	 */
	public static final int FFT_INT = 435;
	/**
	 * Enum : FFT : this enum describes format IBM DisplayWrite Final Form Text
	 * File.
	 */
	public static final FormatEnum FFT = new FormatEnum("FFT", FFT_INT);

	/**
	 * Constant integer for enum : FH .
	 */
	public static final int FH_INT = 87;
	/**
	 * Enum : FH : this enum describes format Macromedia Freehand.
	 */
	public static final FormatEnum FH = new FormatEnum("FH", FH_INT);

	/**
	 * Constant integer for enum : FH4 .
	 */
	public static final int FH4_INT = 458;
	/**
	 * Enum : FH4 : this enum describes format Aldus Freehand Drawing. Supported
	 * versions :3, 4
	 */
	public static final FormatEnum FH4 = new FormatEnum("FH4", FH4_INT);

	/**
	 * Constant integer for enum : FIF .
	 */
	public static final int FIF_INT = 482;
	/**
	 * Enum : FIF : this enum describes format Fractal Image.
	 */
	public static final FormatEnum FIF = new FormatEnum("FIF", FIF_INT);

	/**
	 * Constant integer for enum : FILEMAKER_PRO_DATABASE .
	 */
	public static final int FILEMAKER_PRO_DATABASE_INT = 478;
	/**
	 * Enum : FILEMAKER_PRO_DATABASE : this enum describes format FileMaker Pro
	 * Database. Supported versions :3, 5
	 */
	public static final FormatEnum FILEMAKER_PRO_DATABASE = new FormatEnum(
			"FILEMAKER_PRO_DATABASE", FILEMAKER_PRO_DATABASE_INT);

	/**
	 * Constant integer for enum : FITS .
	 */
	public static final int FITS_INT = 657;
	/**
	 * Enum : FITS : this enum describes format Flexible Image Transport System.
	 */
	public static final FormatEnum FITS = new FormatEnum("FITS", FITS_INT);

	/**
	 * Constant integer for enum : FIXED_WIDTH_VALUES_TEXT_FILE .
	 */
	public static final int FIXED_WIDTH_VALUES_TEXT_FILE_INT = 162;
	/**
	 * Enum : FIXED_WIDTH_VALUES_TEXT_FILE : this enum describes format Fixed
	 * Width Values Text File.
	 */
	public static final FormatEnum FIXED_WIDTH_VALUES_TEXT_FILE = new FormatEnum(
			"FIXED_WIDTH_VALUES_TEXT_FILE", FIXED_WIDTH_VALUES_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : FLC .
	 */
	public static final int FLC_INT = 216;
	/**
	 * Enum : FLC : this enum describes format AutoDesk FLIC Animation.
	 */
	public static final FormatEnum FLC = new FormatEnum("FLC", FLC_INT);

	/**
	 * Constant integer for enum : FLM .
	 */
	public static final int FLM_INT = 218;
	/**
	 * Enum : FLM : this enum describes format AutoCAD Film Roll.
	 */
	public static final FormatEnum FLM = new FormatEnum("FLM", FLM_INT);

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
	 * Constant integer for enum : FM3 .
	 */
	public static final int FM3_INT = 495;
	/**
	 * Enum : FM3 : this enum describes format Lotus 1-2-3 Spreadsheet
	 * Formatting File. Supported versions :2.0, 3.0
	 */
	public static final FormatEnum FM3 = new FormatEnum("FM3", FM3_INT);

	/**
	 * Constant integer for enum : FMV .
	 */
	public static final int FMV_INT = 89;
	/**
	 * Enum : FMV : this enum describes format Frame Vector Metafile.
	 */
	public static final FormatEnum FMV = new FormatEnum("FMV", FMV_INT);

	/**
	 * Constant integer for enum : FOXPRO_DATABASE .
	 */
	public static final int FOXPRO_DATABASE_INT = 17;
	/**
	 * Enum : FOXPRO_DATABASE : this enum describes format FoxPro Database.
	 * Supported versions :2.0, 2.5
	 */
	public static final FormatEnum FOXPRO_DATABASE = new FormatEnum(
			"FOXPRO_DATABASE", FOXPRO_DATABASE_INT);

	/**
	 * Constant integer for enum : FPT .
	 */
	public static final int FPT_INT = 506;
	/**
	 * Enum : FPT : this enum describes format Microsoft FoxPro Memo.
	 */
	public static final FormatEnum FPT = new FormatEnum("FPT", FPT_INT);

	/**
	 * Constant integer for enum : FPX .
	 */
	public static final int FPX_INT = 90;
	/**
	 * Enum : FPX : this enum describes format Kodak FlashPix Image.
	 */
	public static final FormatEnum FPX = new FormatEnum("FPX", FPX_INT);

	/**
	 * Constant integer for enum : FW4 .
	 */
	public static final int FW4_INT = 486;
	/**
	 * Enum : FW4 : this enum describes format Framework Database. Supported
	 * versions :II, III, IV
	 */
	public static final FormatEnum FW4 = new FormatEnum("FW4", FW4_INT);

	/**
	 * Constant integer for enum : GDB .
	 */
	public static final int GDB_INT = 491;
	/**
	 * Enum : GDB : this enum describes format InterBase Database.
	 */
	public static final FormatEnum GDB = new FormatEnum("GDB", GDB_INT);

	/**
	 * Constant integer for enum : GEM_METAFILE_FORMAT .
	 */
	public static final int GEM_METAFILE_FORMAT_INT = 304;
	/**
	 * Enum : GEM_METAFILE_FORMAT : this enum describes format GEM Metafile
	 * Format.
	 */
	public static final FormatEnum GEM_METAFILE_FORMAT = new FormatEnum(
			"GEM_METAFILE_FORMAT", GEM_METAFILE_FORMAT_INT);

	/**
	 * Constant integer for enum : GEN .
	 */
	public static final int GEN_INT = 219;
	/**
	 * Enum : GEN : this enum describes format Ventura Publisher.
	 */
	public static final FormatEnum GEN = new FormatEnum("GEN", GEN_INT);

	/**
	 * Constant integer for enum : GEOTIFF .
	 */
	public static final int GEOTIFF_INT = 798;
	/**
	 * Enum : GEOTIFF : this enum describes format GeoTIFF.
	 */
	public static final FormatEnum GEOTIFF = new FormatEnum("GEOTIFF",
			GEOTIFF_INT);

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
	 * Constant integer for enum : GML .
	 */
	public static final int GML_INT = 319;
	/**
	 * Enum : GML : this enum describes format Geography Markup Language.
	 */
	public static final FormatEnum GML = new FormatEnum("GML", GML_INT);

	/**
	 * Constant integer for enum : GZ .
	 */
	public static final int GZ_INT = 386;
	/**
	 * Enum : GZ : this enum describes format GZIP Format.
	 */
	public static final FormatEnum GZ = new FormatEnum("GZ", GZ_INT);

	/**
	 * Constant integer for enum : HPGL .
	 */
	public static final int HPGL_INT = 446;
	/**
	 * Enum : HPGL : this enum describes format Hewlett Packard Graphics
	 * Language.
	 */
	public static final FormatEnum HPGL = new FormatEnum("HPGL", HPGL_INT);

	/**
	 * Constant integer for enum : HQX .
	 */
	public static final int HQX_INT = 803;
	/**
	 * Enum : HQX : this enum describes format BinHex Archive format.
	 */
	public static final FormatEnum HQX = new FormatEnum("HQX", HQX_INT);

	/**
	 * Constant integer for enum : HTX .
	 */
	public static final int HTX_INT = 804;
	/**
	 * Enum : HTX : this enum describes format HTML Extension File.
	 */
	public static final FormatEnum HTX = new FormatEnum("HTX", HTX_INT);

	/**
	 * Constant integer for enum : HYPERTEXT_MARKUP_LANGUAGE .
	 */
	public static final int HYPERTEXT_MARKUP_LANGUAGE_INT = 645;
	/**
	 * Enum : HYPERTEXT_MARKUP_LANGUAGE : this enum describes format Hypertext
	 * Markup Language. Supported versions :2.0, 3.2, 4.0, 4.01
	 */
	public static final FormatEnum HYPERTEXT_MARKUP_LANGUAGE = new FormatEnum(
			"HYPERTEXT_MARKUP_LANGUAGE", HYPERTEXT_MARKUP_LANGUAGE_INT);

	/**
	 * Constant integer for enum : IBM_DISPLAYWRITE_DOCUMENT .
	 */
	public static final int IBM_DISPLAYWRITE_DOCUMENT_INT = 440;
	/**
	 * Enum : IBM_DISPLAYWRITE_DOCUMENT : this enum describes format IBM
	 * DisplayWrite Document. Supported versions :2, 3
	 */
	public static final FormatEnum IBM_DISPLAYWRITE_DOCUMENT = new FormatEnum(
			"IBM_DISPLAYWRITE_DOCUMENT", IBM_DISPLAYWRITE_DOCUMENT_INT);

	/**
	 * Constant integer for enum : ICO .
	 */
	public static final int ICO_INT = 805;
	/**
	 * Enum : ICO : this enum describes format Icon file format.
	 */
	public static final FormatEnum ICO = new FormatEnum("ICO", ICO_INT);

	/**
	 * Constant integer for enum : IDW .
	 */
	public static final int IDW_INT = 490;
	/**
	 * Enum : IDW : this enum describes format IntelliDraw Vector Graphics.
	 */
	public static final FormatEnum IDW = new FormatEnum("IDW", IDW_INT);

	/**
	 * Constant integer for enum : IFF .
	 */
	public static final int IFF_INT = 221;
	/**
	 * Enum : IFF : this enum describes format Interchange File.
	 */
	public static final FormatEnum IFF = new FormatEnum("IFF", IFF_INT);

	/**
	 * Constant integer for enum : IFO .
	 */
	public static final int IFO_INT = 806;
	/**
	 * Enum : IFO : this enum describes format DVD data file.
	 */
	public static final FormatEnum IFO = new FormatEnum("IFO", IFO_INT);

	/**
	 * Constant integer for enum : IGES .
	 */
	public static final int IGES_INT = 222;
	/**
	 * Enum : IGES : this enum describes format Initial Graphics Exchange
	 * Specification.
	 */
	public static final FormatEnum IGES = new FormatEnum("IGES", IGES_INT);

	/**
	 * Constant integer for enum : IM .
	 */
	public static final int IM_INT = 320;
	/**
	 * Enum : IM : this enum describes format Applixware Bitmap.
	 */
	public static final FormatEnum IM = new FormatEnum("IM", IM_INT);

	/**
	 * Constant integer for enum : IMG .
	 */
	public static final int IMG_INT = 223;
	/**
	 * Enum : IMG : this enum describes format GEM Image.
	 */
	public static final FormatEnum IMG = new FormatEnum("IMG", IMG_INT);

	/**
	 * Constant integer for enum : INF .
	 */
	public static final int INF_INT = 807;
	/**
	 * Enum : INF : this enum describes format Windows Setup File.
	 */
	public static final FormatEnum INF = new FormatEnum("INF", INF_INT);

	/**
	 * Constant integer for enum : ING .
	 */
	public static final int ING_INT = 321;
	/**
	 * Enum : ING : this enum describes format Intergraph Raster Image.
	 */
	public static final FormatEnum ING = new FormatEnum("ING", ING_INT);

	/**
	 * Constant integer for enum : INI .
	 */
	public static final int INI_INT = 808;
	/**
	 * Enum : INI : this enum describes format Text Configuration file.
	 */
	public static final FormatEnum INI = new FormatEnum("INI", INI_INT);

	/**
	 * Constant integer for enum : INTERLEAF_DOCUMENT .
	 */
	public static final int INTERLEAF_DOCUMENT_INT = 492;
	/**
	 * Enum : INTERLEAF_DOCUMENT : this enum describes format Interleaf
	 * Document.
	 */
	public static final FormatEnum INTERLEAF_DOCUMENT = new FormatEnum(
			"INTERLEAF_DOCUMENT", INTERLEAF_DOCUMENT_INT);

	/**
	 * Constant integer for enum : IQY .
	 */
	public static final int IQY_INT = 95;
	/**
	 * Enum : IQY : this enum describes format Microsoft Excel Web Query.
	 */
	public static final FormatEnum IQY = new FormatEnum("IQY", IQY_INT);

	/**
	 * Constant integer for enum : IRIS_GRAPHICS .
	 */
	public static final int IRIS_GRAPHICS_INT = 267;
	/**
	 * Enum : IRIS_GRAPHICS : this enum describes format IRIS Graphics.
	 */
	public static final FormatEnum IRIS_GRAPHICS = new FormatEnum(
			"IRIS_GRAPHICS", IRIS_GRAPHICS_INT);

	/**
	 * Constant integer for enum : JAR .
	 */
	public static final int JAR_INT = 777;
	/**
	 * Enum : JAR : this enum describes format Java Archive Format.
	 */
	public static final FormatEnum JAR = new FormatEnum("JAR", JAR_INT);

	/**
	 * Constant integer for enum : JAVA .
	 */
	public static final int JAVA_INT = 809;
	/**
	 * Enum : JAVA : this enum describes format Java language source code file.
	 */
	public static final FormatEnum JAVA = new FormatEnum("JAVA", JAVA_INT);

	/**
	 * Constant integer for enum : JLS .
	 */
	public static final int JLS_INT = 793;
	/**
	 * Enum : JLS : this enum describes format JPEG-LS.
	 */
	public static final FormatEnum JLS = new FormatEnum("JLS", JLS_INT);

	/**
	 * Constant integer for enum : JP2 .
	 */
	public static final int JP2_INT = 686;
	/**
	 * Enum : JP2 : this enum describes format JPEG 2000.
	 */
	public static final FormatEnum JP2 = new FormatEnum("JP2", JP2_INT);

	/**
	 * Constant integer for enum : JPEG_FILE_INTERCHANGE_FORMAT .
	 */
	public static final int JPEG_FILE_INTERCHANGE_FORMAT_INT = 669;
	/**
	 * Enum : JPEG_FILE_INTERCHANGE_FORMAT : this enum describes format JPEG
	 * File Interchange Format. Supported versions :1.00, 1.01, 1.02
	 */
	public static final FormatEnum JPEG_FILE_INTERCHANGE_FORMAT = new FormatEnum(
			"JPEG_FILE_INTERCHANGE_FORMAT", JPEG_FILE_INTERCHANGE_FORMAT_INT);

	/**
	 * Constant integer for enum : JPX .
	 */
	public static final int JPX_INT = 794;
	/**
	 * Enum : JPX : this enum describes format JPX (JPEG 2000 Extended).
	 */
	public static final FormatEnum JPX = new FormatEnum("JPX", JPX_INT);

	/**
	 * Constant integer for enum : JS .
	 */
	public static final int JS_INT = 810;
	/**
	 * Enum : JS : this enum describes format JavaScript file.
	 */
	public static final FormatEnum JS = new FormatEnum("JS", JS_INT);

	/**
	 * Constant integer for enum : JSP .
	 */
	public static final int JSP_INT = 227;
	/**
	 * Enum : JSP : this enum describes format Java Servlet Page.
	 */
	public static final FormatEnum JSP = new FormatEnum("JSP", JSP_INT);

	/**
	 * Constant integer for enum : JTIP_JPEG_TILED_IMAGE_PYRAMID .
	 */
	public static final int JTIP_JPEG_TILED_IMAGE_PYRAMID_INT = 792;
	/**
	 * Enum : JTIP_JPEG_TILED_IMAGE_PYRAMID : this enum describes format JTIP
	 * (JPEG Tiled Image Pyramid).
	 */
	public static final FormatEnum JTIP_JPEG_TILED_IMAGE_PYRAMID = new FormatEnum(
			"JTIP_JPEG_TILED_IMAGE_PYRAMID",
			JTIP_JPEG_TILED_IMAGE_PYRAMID_INT);

	/**
	 * Constant integer for enum : JW .
	 */
	public static final int JW_INT = 493;
	/**
	 * Enum : JW : this enum describes format JustWrite Text Document.
	 */
	public static final FormatEnum JW = new FormatEnum("JW", JW_INT);

	/**
	 * Constant integer for enum : LAS .
	 */
	public static final int LAS_INT = 100;
	/**
	 * Enum : LAS : this enum describes format AutoCAD Last Saved Layer State.
	 */
	public static final FormatEnum LAS = new FormatEnum("LAS", LAS_INT);

	/**
	 * Constant integer for enum : LBM .
	 */
	public static final int LBM_INT = 811;
	/**
	 * Enum : LBM : this enum describes format Deluxe Paint bitmap.
	 */
	public static final FormatEnum LBM = new FormatEnum("LBM", LBM_INT);

	/**
	 * Constant integer for enum : LIB .
	 */
	public static final int LIB_INT = 812;
	/**
	 * Enum : LIB : this enum describes format generic library file.
	 */
	public static final FormatEnum LIB = new FormatEnum("LIB", LIB_INT);

	/**
	 * Constant integer for enum : LIC .
	 */
	public static final int LIC_INT = 813;
	/**
	 * Enum : LIC : this enum describes format License file.
	 */
	public static final FormatEnum LIC = new FormatEnum("LIC", LIC_INT);

	/**
	 * Constant integer for enum : LIN .
	 */
	public static final int LIN_INT = 101;
	/**
	 * Enum : LIN : this enum describes format AutoCAD Linetype Definition File.
	 */
	public static final FormatEnum LIN = new FormatEnum("LIN", LIN_INT);

	/**
	 * Constant integer for enum : LLI .
	 */
	public static final int LLI_INT = 102;
	/**
	 * Enum : LLI : this enum describes format AutoCAD Landscape Library.
	 */
	public static final FormatEnum LLI = new FormatEnum("LLI", LLI_INT);

	/**
	 * Constant integer for enum : LNG .
	 */
	public static final int LNG_INT = 814;
	/**
	 * Enum : LNG : this enum describes format Acrobat Language definition file.
	 */
	public static final FormatEnum LNG = new FormatEnum("LNG", LNG_INT);

	/**
	 * Constant integer for enum : LNK .
	 */
	public static final int LNK_INT = 815;
	/**
	 * Enum : LNK : this enum describes format Windows shortcut file.
	 */
	public static final FormatEnum LNK = new FormatEnum("LNK", LNK_INT);

	/**
	 * Constant integer for enum : LOG .
	 */
	public static final int LOG_INT = 103;
	/**
	 * Enum : LOG : this enum describes format Log File.
	 */
	public static final FormatEnum LOG = new FormatEnum("LOG", LOG_INT);

	/**
	 * Constant integer for enum : LOTUS_1_2_3_CHART .
	 */
	public static final int LOTUS_1_2_3_CHART_INT = 126;
	/**
	 * Enum : LOTUS_1_2_3_CHART : this enum describes format Lotus 1-2-3 Chart.
	 */
	public static final FormatEnum LOTUS_1_2_3_CHART = new FormatEnum(
			"LOTUS_1_2_3_CHART", LOTUS_1_2_3_CHART_INT);

	/**
	 * Constant integer for enum : LSP .
	 */
	public static final int LSP_INT = 104;
	/**
	 * Enum : LSP : this enum describes format AutoLISP File.
	 */
	public static final FormatEnum LSP = new FormatEnum("LSP", LSP_INT);

	/**
	 * Constant integer for enum : LWP .
	 */
	public static final int LWP_INT = 504;
	/**
	 * Enum : LWP : this enum describes format Lotus WordPro Document. Supported
	 * versions :96/97
	 */
	public static final FormatEnum LWP = new FormatEnum("LWP", LWP_INT);

	/**
	 * Constant integer for enum : M3U .
	 */
	public static final int M3U_INT = 425;
	/**
	 * Enum : M3U : this enum describes format MPEG 1/2 Audio Layer 3 Streaming.
	 */
	public static final FormatEnum M3U = new FormatEnum("M3U", M3U_INT);

	/**
	 * Constant integer for enum : MAC .
	 */
	public static final int MAC_INT = 228;
	/**
	 * Enum : MAC : this enum describes format MacPaint Image.
	 */
	public static final FormatEnum MAC = new FormatEnum("MAC", MAC_INT);

	/**
	 * Constant integer for enum : MACINTOSH_TEXT_FILE .
	 */
	public static final int MACINTOSH_TEXT_FILE_INT = 41;
	/**
	 * Enum : MACINTOSH_TEXT_FILE : this enum describes format Macintosh Text
	 * File.
	 */
	public static final FormatEnum MACINTOSH_TEXT_FILE = new FormatEnum(
			"MACINTOSH_TEXT_FILE", MACINTOSH_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : MAS .
	 */
	public static final int MAS_INT = 498;
	/**
	 * Enum : MAS : this enum describes format Lotus Freelance Smartmaster
	 * Graphics.
	 */
	public static final FormatEnum MAS = new FormatEnum("MAS", MAS_INT);

	/**
	 * Constant integer for enum : MCW .
	 */
	public static final int MCW_INT = 106;
	/**
	 * Enum : MCW : this enum describes format Microsoft Word for Macintosh
	 * Document. Supported versions :3.0, 6.0, 4.0, 5.0
	 */
	public static final FormatEnum MCW = new FormatEnum("MCW", MCW_INT);

	/**
	 * Constant integer for enum : MDB .
	 */
	public static final int MDB_INT = 353;
	/**
	 * Enum : MDB : this enum describes format Microsoft Access Database.
	 * Supported versions :2.0, 95, 97, 2000, 2002
	 */
	public static final FormatEnum MDB = new FormatEnum("MDB", MDB_INT);

	/**
	 * Constant integer for enum : MET_METAFILE .
	 */
	public static final int MET_METAFILE_INT = 108;
	/**
	 * Enum : MET_METAFILE : this enum describes format Met Metafile.
	 */
	public static final FormatEnum MET_METAFILE = new FormatEnum(
			"MET_METAFILE", MET_METAFILE_INT);

	/**
	 * Constant integer for enum : MHT .
	 */
	public static final int MHT_INT = 820;
	/**
	 * Enum : MHT : this enum describes format Microsoft Web Archive.
	 */
	public static final FormatEnum MHT = new FormatEnum("MHT", MHT_INT);

	/**
	 * Constant integer for enum : MICROGRAFX_DESIGNER .
	 */
	public static final int MICROGRAFX_DESIGNER_INT = 449;
	/**
	 * Enum : MICROGRAFX_DESIGNER : this enum describes format Micrografx
	 * Designer. Supported versions :7.0, 3.1
	 */
	public static final FormatEnum MICROGRAFX_DESIGNER = new FormatEnum(
			"MICROGRAFX_DESIGNER", MICROGRAFX_DESIGNER_INT);

	/**
	 * Constant integer for enum : MICROGRAFX_DRAW .
	 */
	public static final int MICROGRAFX_DRAW_INT = 448;
	/**
	 * Enum : MICROGRAFX_DRAW : this enum describes format Micrografx Draw.
	 * Supported versions :2.0, 3.0, 4.0
	 */
	public static final FormatEnum MICROGRAFX_DRAW = new FormatEnum(
			"MICROGRAFX_DRAW", MICROGRAFX_DRAW_INT);

	/**
	 * Constant integer for enum : MICROSOFT_EXCEL_ADD_IN .
	 */
	public static final int MICROSOFT_EXCEL_ADD_IN_INT = 179;
	/**
	 * Enum : MICROSOFT_EXCEL_ADD_IN : this enum describes format Microsoft
	 * Excel Add-In.
	 */
	public static final FormatEnum MICROSOFT_EXCEL_ADD_IN = new FormatEnum(
			"MICROSOFT_EXCEL_ADD_IN", MICROSOFT_EXCEL_ADD_IN_INT);

	/**
	 * Constant integer for enum : MICROSOFT_EXCEL_CHART .
	 */
	public static final int MICROSOFT_EXCEL_CHART_INT = 181;
	/**
	 * Enum : MICROSOFT_EXCEL_CHART : this enum describes format Microsoft Excel
	 * Chart. Supported versions :4.0
	 */
	public static final FormatEnum MICROSOFT_EXCEL_CHART = new FormatEnum(
			"MICROSOFT_EXCEL_CHART", MICROSOFT_EXCEL_CHART_INT);

	/**
	 * Constant integer for enum : MICROSOFT_EXCEL_MACRO .
	 */
	public static final int MICROSOFT_EXCEL_MACRO_INT = 178;
	/**
	 * Enum : MICROSOFT_EXCEL_MACRO : this enum describes format Microsoft Excel
	 * Macro. Supported versions :4.0
	 */
	public static final FormatEnum MICROSOFT_EXCEL_MACRO = new FormatEnum(
			"MICROSOFT_EXCEL_MACRO", MICROSOFT_EXCEL_MACRO_INT);

	/**
	 * Constant integer for enum : MICROSOFT_EXCEL_WORKSPACE .
	 */
	public static final int MICROSOFT_EXCEL_WORKSPACE_INT = 186;
	/**
	 * Enum : MICROSOFT_EXCEL_WORKSPACE : this enum describes format Microsoft
	 * Excel Workspace.
	 */
	public static final FormatEnum MICROSOFT_EXCEL_WORKSPACE = new FormatEnum(
			"MICROSOFT_EXCEL_WORKSPACE", MICROSOFT_EXCEL_WORKSPACE_INT);

	/**
	 * Constant integer for enum : MICROSOFT_FOXPRO_DATABASE .
	 */
	public static final int MICROSOFT_FOXPRO_DATABASE_INT = 355;
	/**
	 * Enum : MICROSOFT_FOXPRO_DATABASE : this enum describes format Microsoft
	 * FoxPro Database. Supported versions :2.6
	 */
	public static final FormatEnum MICROSOFT_FOXPRO_DATABASE = new FormatEnum(
			"MICROSOFT_FOXPRO_DATABASE", MICROSOFT_FOXPRO_DATABASE_INT);

	/**
	 * Constant integer for enum : MICROSOFT_PROJECT .
	 */
	public static final int MICROSOFT_PROJECT_INT = 362;
	/**
	 * Enum : MICROSOFT_PROJECT : this enum describes format Microsoft Project.
	 * Supported versions :4.0, 95, 98, 2000
	 */
	public static final FormatEnum MICROSOFT_PROJECT = new FormatEnum(
			"MICROSOFT_PROJECT", MICROSOFT_PROJECT_INT);

	/**
	 * Constant integer for enum : MICROSOFT_PROJECT_FILE .
	 */
	public static final int MICROSOFT_PROJECT_FILE_INT = 363;
	/**
	 * Enum : MICROSOFT_PROJECT_FILE : this enum describes format Microsoft
	 * Project file. Supported versions :2002
	 */
	public static final FormatEnum MICROSOFT_PROJECT_FILE = new FormatEnum(
			"MICROSOFT_PROJECT_FILE", MICROSOFT_PROJECT_FILE_INT);

	/**
	 * Constant integer for enum : MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT .
	 */
	public static final int MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT_INT = 188;
	/**
	 * Enum : MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT : this enum describes format
	 * Microsoft Word for Macintosh Document. Supported versions :X
	 */
	public static final FormatEnum MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT = new FormatEnum(
			"MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT",
			MICROSOFT_WORD_FOR_MACINTOSH_DOCUMENT_INT);

	/**
	 * Constant integer for enum : MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT .
	 */
	public static final int MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT_INT = 408;
	/**
	 * Enum : MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT : this enum describes format
	 * Microsoft Word for MS-DOS Document. Supported versions :3.0, 4.0, 5.0,
	 * 5.5
	 */
	public static final FormatEnum MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT = new FormatEnum(
			"MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT",
			MICROSOFT_WORD_FOR_MS_DOS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT .
	 */
	public static final int MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT_INT = 734;
	/**
	 * Enum : MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT : this enum describes format
	 * Microsoft Word for Windows Document. Supported versions :6.0/95, 97-2003,
	 * 1.0, 2.0
	 */
	public static final FormatEnum MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT = new FormatEnum(
			"MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT",
			MICROSOFT_WORD_FOR_WINDOWS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : MID .
	 */
	public static final int MID_INT = 322;
	/**
	 * Enum : MID : this enum describes format MIDI Audio.
	 */
	public static final FormatEnum MID = new FormatEnum("MID", MID_INT);

	/**
	 * Constant integer for enum : MNC .
	 */
	public static final int MNC_INT = 109;
	/**
	 * Enum : MNC : this enum describes format AutoCAD Compiled Menu.
	 */
	public static final FormatEnum MNC = new FormatEnum("MNC", MNC_INT);

	/**
	 * Constant integer for enum : MNL .
	 */
	public static final int MNL_INT = 110;
	/**
	 * Enum : MNL : this enum describes format AutoLISP Menu Source File.
	 */
	public static final FormatEnum MNL = new FormatEnum("MNL", MNL_INT);

	/**
	 * Constant integer for enum : MNR .
	 */
	public static final int MNR_INT = 111;
	/**
	 * Enum : MNR : this enum describes format AutoCAD Menu Resource File.
	 */
	public static final FormatEnum MNR = new FormatEnum("MNR", MNR_INT);

	/**
	 * Constant integer for enum : MNS .
	 */
	public static final int MNS_INT = 112;
	/**
	 * Enum : MNS : this enum describes format AutoCAD Source Menu File.
	 */
	public static final FormatEnum MNS = new FormatEnum("MNS", MNS_INT);

	/**
	 * Constant integer for enum : MNU .
	 */
	public static final int MNU_INT = 114;
	/**
	 * Enum : MNU : this enum describes format AutoCAD Template Menu File.
	 */
	public static final FormatEnum MNU = new FormatEnum("MNU", MNU_INT);

	/**
	 * Constant integer for enum : MP3 .
	 */
	public static final int MP3_INT = 687;
	/**
	 * Enum : MP3 : this enum describes format MPEG 1/2 Audio Layer 3.
	 */
	public static final FormatEnum MP3 = new FormatEnum("MP3", MP3_INT);

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
	 * Constant integer for enum : MPX .
	 */
	public static final int MPX_INT = 324;
	/**
	 * Enum : MPX : this enum describes format Microsoft Project Export File.
	 */
	public static final FormatEnum MPX = new FormatEnum("MPX", MPX_INT);

	/**
	 * Constant integer for enum : MSP .
	 */
	public static final int MSP_INT = 302;
	/**
	 * Enum : MSP : this enum describes format Windows Paint. Supported versions
	 * :3.x
	 */
	public static final FormatEnum MSP = new FormatEnum("MSP", MSP_INT);

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
	 * Constant integer for enum : MS_DOS_TEXT_FILE .
	 */
	public static final int MS_DOS_TEXT_FILE_INT = 42;
	/**
	 * Enum : MS_DOS_TEXT_FILE : this enum describes format MS-DOS Text File.
	 */
	public static final FormatEnum MS_DOS_TEXT_FILE = new FormatEnum(
			"MS_DOS_TEXT_FILE", MS_DOS_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : MS_DOS_TEXT_FILE_WITH_LINE_BREAKS .
	 */
	public static final int MS_DOS_TEXT_FILE_WITH_LINE_BREAKS_INT = 189;
	/**
	 * Enum : MS_DOS_TEXT_FILE_WITH_LINE_BREAKS : this enum describes format
	 * MS-DOS Text File with line breaks.
	 */
	public static final FormatEnum MS_DOS_TEXT_FILE_WITH_LINE_BREAKS = new FormatEnum(
			"MS_DOS_TEXT_FILE_WITH_LINE_BREAKS",
			MS_DOS_TEXT_FILE_WITH_LINE_BREAKS_INT);

	/**
	 * Constant integer for enum : MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS .
	 */
	public static final int MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS_INT = 512;
	/**
	 * Enum : MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS : this enum describes
	 * format Multipage Zsoft Paintbrush Bitmap Graphics.
	 */
	public static final FormatEnum MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS = new FormatEnum(
			"MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS",
			MULTIPAGE_ZSOFT_PAINTBRUSH_BITMAP_GRAPHICS_INT);

	/**
	 * Constant integer for enum : N3D_STUDIO .
	 */
	public static final int N3D_STUDIO_INT = 48;
	/**
	 * Enum : N3D_STUDIO : this enum describes format 3D Studio.
	 */
	public static final FormatEnum N3D_STUDIO = new FormatEnum("N3D_STUDIO",
			N3D_STUDIO_INT);

	/**
	 * Constant integer for enum : N3D_STUDIO_SHAPES .
	 */
	public static final int N3D_STUDIO_SHAPES_INT = 150;
	/**
	 * Enum : N3D_STUDIO_SHAPES : this enum describes format 3D Studio Shapes.
	 */
	public static final FormatEnum N3D_STUDIO_SHAPES = new FormatEnum(
			"N3D_STUDIO_SHAPES", N3D_STUDIO_SHAPES_INT);

	/**
	 * Constant integer for enum : N7_BIT_ANSI_TEXT .
	 */
	public static final int N7_BIT_ANSI_TEXT_INT = 50;
	/**
	 * Enum : N7_BIT_ANSI_TEXT : this enum describes format 7-bit ANSI Text.
	 */
	public static final FormatEnum N7_BIT_ANSI_TEXT = new FormatEnum(
			"N7_BIT_ANSI_TEXT", N7_BIT_ANSI_TEXT_INT);

	/**
	 * Constant integer for enum : N7_BIT_ASCII_TEXT .
	 */
	public static final int N7_BIT_ASCII_TEXT_INT = 51;
	/**
	 * Enum : N7_BIT_ASCII_TEXT : this enum describes format 7-bit ASCII Text.
	 */
	public static final FormatEnum N7_BIT_ASCII_TEXT = new FormatEnum(
			"N7_BIT_ASCII_TEXT", N7_BIT_ASCII_TEXT_INT);

	/**
	 * Constant integer for enum : N8_BIT_ANSI_TEXT .
	 */
	public static final int N8_BIT_ANSI_TEXT_INT = 433;
	/**
	 * Enum : N8_BIT_ANSI_TEXT : this enum describes format 8-bit ANSI Text.
	 */
	public static final FormatEnum N8_BIT_ANSI_TEXT = new FormatEnum(
			"N8_BIT_ANSI_TEXT", N8_BIT_ANSI_TEXT_INT);

	/**
	 * Constant integer for enum : N8_BIT_ASCII_TEXT .
	 */
	public static final int N8_BIT_ASCII_TEXT_INT = 434;
	/**
	 * Enum : N8_BIT_ASCII_TEXT : this enum describes format 8-bit ASCII Text.
	 */
	public static final FormatEnum N8_BIT_ASCII_TEXT = new FormatEnum(
			"N8_BIT_ASCII_TEXT", N8_BIT_ASCII_TEXT_INT);

	/**
	 * Constant integer for enum : NAP .
	 */
	public static final int NAP_INT = 235;
	/**
	 * Enum : NAP : this enum describes format NAP Metafile.
	 */
	public static final FormatEnum NAP = new FormatEnum("NAP", NAP_INT);

	/**
	 * Constant integer for enum : NB .
	 */
	public static final int NB_INT = 513;
	/**
	 * Enum : NB : this enum describes format Nota Bene Text File.
	 */
	public static final FormatEnum NB = new FormatEnum("NB", NB_INT);

	/**
	 * Constant integer for enum : NS4 .
	 */
	public static final int NS4_INT = 501;
	/**
	 * Enum : NS4 : this enum describes format Lotus Notes Database. Supported
	 * versions :2, 3, 4
	 */
	public static final FormatEnum NS4 = new FormatEnum("NS4", NS4_INT);

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
	 * Constant integer for enum : OLK .
	 */
	public static final int OLK_INT = 115;
	/**
	 * Enum : OLK : this enum describes format Microsoft Outlook Address Book.
	 */
	public static final FormatEnum OLK = new FormatEnum("OLK", OLK_INT);

	/**
	 * Constant integer for enum : OMNIPAGE_PRO_DOCUMENT .
	 */
	public static final int OMNIPAGE_PRO_DOCUMENT_INT = 514;
	/**
	 * Enum : OMNIPAGE_PRO_DOCUMENT : this enum describes format OmniPage Pro
	 * Document.
	 */
	public static final FormatEnum OMNIPAGE_PRO_DOCUMENT = new FormatEnum(
			"OMNIPAGE_PRO_DOCUMENT", OMNIPAGE_PRO_DOCUMENT_INT);

	/**
	 * Constant integer for enum : ONLINE_DESCRIPTION_TOOL_FORMAT .
	 */
	public static final int ONLINE_DESCRIPTION_TOOL_FORMAT_INT = 13;
	/**
	 * Enum : ONLINE_DESCRIPTION_TOOL_FORMAT : this enum describes format Online
	 * Description Tool Format.
	 */
	public static final FormatEnum ONLINE_DESCRIPTION_TOOL_FORMAT = new FormatEnum(
			"ONLINE_DESCRIPTION_TOOL_FORMAT",
			ONLINE_DESCRIPTION_TOOL_FORMAT_INT);

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
	 * Constant integer for enum : OPENDOCUMENT_TEXT_FORMAT .
	 */
	public static final int OPENDOCUMENT_TEXT_FORMAT_INT = 779;
	/**
	 * Enum : OPENDOCUMENT_TEXT_FORMAT : this enum describes format OpenDocument
	 * Text Format. Supported versions :1.0
	 */
	public static final FormatEnum OPENDOCUMENT_TEXT_FORMAT = new FormatEnum(
			"OPENDOCUMENT_TEXT_FORMAT", OPENDOCUMENT_TEXT_FORMAT_INT);

	/**
	 * Constant integer for enum : OQY .
	 */
	public static final int OQY_INT = 116;
	/**
	 * Enum : OQY : this enum describes format Microsoft Excel OLAP Query.
	 */
	public static final FormatEnum OQY = new FormatEnum("OQY", OQY_INT);

	/**
	 * Constant integer for enum : OS_2_BITMAP .
	 */
	public static final int OS_2_BITMAP_INT = 402;
	/**
	 * Enum : OS_2_BITMAP : this enum describes format OS/2 Bitmap. Supported
	 * versions :1.0, 2.0
	 */
	public static final FormatEnum OS_2_BITMAP = new FormatEnum(
			"OS_2_BITMAP", OS_2_BITMAP_INT);

	/**
	 * Constant integer for enum : PAB .
	 */
	public static final int PAB_INT = 117;
	/**
	 * Enum : PAB : this enum describes format Microsoft Outlook Personal
	 * Address Book.
	 */
	public static final FormatEnum PAB = new FormatEnum("PAB", PAB_INT);

	/**
	 * Constant integer for enum : PARADOX_DATABASE_MEMO_FIELD .
	 */
	public static final int PARADOX_DATABASE_MEMO_FIELD_INT = 465;
	/**
	 * Enum : PARADOX_DATABASE_MEMO_FIELD : this enum describes format Paradox
	 * Database Memo Field.
	 */
	public static final FormatEnum PARADOX_DATABASE_MEMO_FIELD = new FormatEnum(
			"PARADOX_DATABASE_MEMO_FIELD", PARADOX_DATABASE_MEMO_FIELD_INT);

	/**
	 * Constant integer for enum : PAT .
	 */
	public static final int PAT_INT = 118;
	/**
	 * Enum : PAT : this enum describes format CorelDraw Pattern.
	 */
	public static final FormatEnum PAT = new FormatEnum("PAT", PAT_INT);

	/**
	 * Constant integer for enum : PBM .
	 */
	public static final int PBM_INT = 236;
	/**
	 * Enum : PBM : this enum describes format Portable Bitmap Graphic.
	 */
	public static final FormatEnum PBM = new FormatEnum("PBM", PBM_INT);

	/**
	 * Constant integer for enum : PCD .
	 */
	public static final int PCD_INT = 237;
	/**
	 * Enum : PCD : this enum describes format Kodak PhotoCD Image. Supported
	 * versions :1.0
	 */
	public static final FormatEnum PCD = new FormatEnum("PCD", PCD_INT);

	/**
	 * Constant integer for enum : PCP .
	 */
	public static final int PCP_INT = 121;
	/**
	 * Enum : PCP : this enum describes format AutoCAD Plot Configuration File.
	 * Supported versions :R14, 2000, 1.0-R13
	 */
	public static final FormatEnum PCP = new FormatEnum("PCP", PCP_INT);

	/**
	 * Constant integer for enum : PCS .
	 */
	public static final int PCS_INT = 238;
	/**
	 * Enum : PCS : this enum describes format PICS Animation.
	 */
	public static final FormatEnum PCS = new FormatEnum("PCS", PCS_INT);

	/**
	 * Constant integer for enum : PCT .
	 */
	public static final int PCT_INT = 122;
	/**
	 * Enum : PCT : this enum describes format Macintosh PICT Image.
	 */
	public static final FormatEnum PCT = new FormatEnum("PCT", PCT_INT);

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
	 * Constant integer for enum : PC_PAINT_BITMAP .
	 */
	public static final int PC_PAINT_BITMAP_INT = 242;
	/**
	 * Enum : PC_PAINT_BITMAP : this enum describes format PC Paint Bitmap.
	 */
	public static final FormatEnum PC_PAINT_BITMAP = new FormatEnum(
			"PC_PAINT_BITMAP", PC_PAINT_BITMAP_INT);

	/**
	 * Constant integer for enum : PDD .
	 */
	public static final int PDD_INT = 239;
	/**
	 * Enum : PDD : this enum describes format Adobe PhotoDeluxe.
	 */
	public static final FormatEnum PDD = new FormatEnum("PDD", PDD_INT);

	/**
	 * Constant integer for enum : PDP .
	 */
	public static final int PDP_INT = 240;
	/**
	 * Enum : PDP : this enum describes format Broderbund Print Shop Deluxe.
	 */
	public static final FormatEnum PDP = new FormatEnum("PDP", PDP_INT);

	/**
	 * Constant integer for enum : PDT .
	 */
	public static final int PDT_INT = 125;
	/**
	 * Enum : PDT : this enum describes format Inkwriter/Notetaker Template.
	 */
	public static final FormatEnum PDT = new FormatEnum("PDT", PDT_INT);

	/**
	 * Constant integer for enum : PHP .
	 */
	public static final int PHP_INT = 241;
	/**
	 * Enum : PHP : this enum describes format PHP Script Page.
	 */
	public static final FormatEnum PHP = new FormatEnum("PHP", PHP_INT);

	/**
	 * Constant integer for enum : PIX .
	 */
	public static final int PIX_INT = 244;
	/**
	 * Enum : PIX : this enum describes format Inset Systems Bitmap.
	 */
	public static final FormatEnum PIX = new FormatEnum("PIX", PIX_INT);

	/**
	 * Constant integer for enum : PLAIN_TEXT_FILE .
	 */
	public static final int PLAIN_TEXT_FILE_INT = 163;
	/**
	 * Enum : PLAIN_TEXT_FILE : this enum describes format Plain Text File.
	 */
	public static final FormatEnum PLAIN_TEXT_FILE = new FormatEnum(
			"PLAIN_TEXT_FILE", PLAIN_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : PLB .
	 */
	public static final int PLB_INT = 245;
	/**
	 * Enum : PLB : this enum describes format Microsoft FoxPro Library.
	 */
	public static final FormatEnum PLB = new FormatEnum("PLB", PLB_INT);

	/**
	 * Constant integer for enum : PLT .
	 */
	public static final int PLT_INT = 127;
	/**
	 * Enum : PLT : this enum describes format Hewlett Packard Vector Graphic
	 * Plotter File.
	 */
	public static final FormatEnum PLT = new FormatEnum("PLT", PLT_INT);

	/**
	 * Constant integer for enum : PM4 .
	 */
	public static final int PM4_INT = 516;
	/**
	 * Enum : PM4 : this enum describes format PageMaker Document. Supported
	 * versions :5.0, 6.0, 6.5, 3.0, 4.0
	 */
	public static final FormatEnum PM4 = new FormatEnum("PM4", PM4_INT);

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
	 * Constant integer for enum : PNT .
	 */
	public static final int PNT_INT = 248;
	/**
	 * Enum : PNT : this enum describes format MacPaint Graphics.
	 */
	public static final FormatEnum PNT = new FormatEnum("PNT", PNT_INT);

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
	 * Constant integer for enum : PDF_A .
	 */
	public static final int PDF_A_INT = 770;
	/**
	 * Enum : PDF_A : this enum describes format Portable Document Format -
	 * Archival. Supported versions :1
	 */
	public static final FormatEnum PDF_A = new FormatEnum("PDF_A", PDF_A_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001_INT = 818;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001 : this enum describes
	 * format Portable Document Format - Exchange 1a:2001.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2001_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003_INT = 789;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003 : this enum describes
	 * format Portable Document Format - Exchange 1a:2003.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1A_2003_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999_INT = 787;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999 : this enum describes
	 * format Portable Document Format - Exchange 1:1999.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_1999_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001_INT = 788;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001 : this enum describes
	 * format Portable Document Format - Exchange 1:2001.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_1_2001_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003_INT = 790;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003 : this enum describes
	 * format Portable Document Format - Exchange 2:2003.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_2_2003_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002_INT = 819;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002 : this enum describes
	 * format Portable Document Format - Exchange 3:2002.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2002_INT);

	/**
	 * Constant integer for enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003 .
	 */
	public static final int PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003_INT = 791;
	/**
	 * Enum : PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003 : this enum describes
	 * format Portable Document Format - Exchange 3:2003.
	 */
	public static final FormatEnum PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003 = new FormatEnum(
			"PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003",
			PORTABLE_DOCUMENT_FORMAT__EXCHANGE_3_2003_INT);

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
	 * Constant integer for enum : POT .
	 */
	public static final int POT_INT = 129;
	/**
	 * Enum : POT : this enum describes format Microsoft Powerpoint Design
	 * Template.
	 */
	public static final FormatEnum POT = new FormatEnum("POT", POT_INT);

	/**
	 * Constant integer for enum : PP4 .
	 */
	public static final int PP4_INT = 249;
	/**
	 * Enum : PP4 : this enum describes format Picture Publisher Bitmap.
	 * Supported versions :5.0, 4
	 */
	public static final FormatEnum PP4 = new FormatEnum("PP4", PP4_INT);

	/**
	 * Constant integer for enum : PPA .
	 */
	public static final int PPA_INT = 131;
	/**
	 * Enum : PPA : this enum describes format Microsoft Powerpoint Add-In.
	 */
	public static final FormatEnum PPA = new FormatEnum("PPA", PPA_INT);

	/**
	 * Constant integer for enum : PPI .
	 */
	public static final int PPI_INT = 250;
	/**
	 * Enum : PPI : this enum describes format Microsoft PowerPoint Graphics
	 * File.
	 */
	public static final FormatEnum PPI = new FormatEnum("PPI", PPI_INT);

	/**
	 * Constant integer for enum : PPM .
	 */
	public static final int PPM_INT = 251;
	/**
	 * Enum : PPM : this enum describes format Portable Pixelmap Bitmap.
	 */
	public static final FormatEnum PPM = new FormatEnum("PPM", PPM_INT);

	/**
	 * Constant integer for enum : PPS .
	 */
	public static final int PPS_INT = 132;
	/**
	 * Enum : PPS : this enum describes format Microsoft Powerpoint Show.
	 */
	public static final FormatEnum PPS = new FormatEnum("PPS", PPS_INT);

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
	 * Constant integer for enum : PPZ .
	 */
	public static final int PPZ_INT = 306;
	/**
	 * Enum : PPZ : this enum describes format Microsoft Powerpoint Packaged
	 * Presentation.
	 */
	public static final FormatEnum PPZ = new FormatEnum("PPZ", PPZ_INT);

	/**
	 * Constant integer for enum : PRE .
	 */
	public static final int PRE_INT = 136;
	/**
	 * Enum : PRE : this enum describes format Freelance File. Supported
	 * versions :1.0-2.1
	 */
	public static final FormatEnum PRE = new FormatEnum("PRE", PRE_INT);

	/**
	 * Constant integer for enum : PRN .
	 */
	public static final int PRN_INT = 137;
	/**
	 * Enum : PRN : this enum describes format Microsoft Print File.
	 */
	public static final FormatEnum PRN = new FormatEnum("PRN", PRN_INT);

	/**
	 * Constant integer for enum : PSD .
	 */
	public static final int PSD_INT = 139;
	/**
	 * Enum : PSD : this enum describes format Adobe Photoshop.
	 */
	public static final FormatEnum PSD = new FormatEnum("PSD", PSD_INT);

	/**
	 * Constant integer for enum : PSF .
	 */
	public static final int PSF_INT = 140;
	/**
	 * Enum : PSF : this enum describes format Postscript Support File.
	 */
	public static final FormatEnum PSF = new FormatEnum("PSF", PSF_INT);

	/**
	 * Constant integer for enum : PSP .
	 */
	public static final int PSP_INT = 555;
	/**
	 * Enum : PSP : this enum describes format Paint Shop Pro Image. Supported
	 * versions :3, 5, 6, 7, 8, 4
	 */
	public static final FormatEnum PSP = new FormatEnum("PSP", PSP_INT);

	/**
	 * Constant integer for enum : PST .
	 */
	public static final int PST_INT = 367;
	/**
	 * Enum : PST : this enum describes format Microsoft Outlook Personal
	 * Folders. Supported versions :97, 98, 2000, 2002
	 */
	public static final FormatEnum PST = new FormatEnum("PST", PST_INT);

	/**
	 * Constant integer for enum : PSW .
	 */
	public static final int PSW_INT = 141;
	/**
	 * Enum : PSW : this enum describes format Pocket Word Document.
	 */
	public static final FormatEnum PSW = new FormatEnum("PSW", PSW_INT);

	/**
	 * Constant integer for enum : PTL .
	 */
	public static final int PTL_INT = 252;
	/**
	 * Enum : PTL : this enum describes format Microsoft Visual Modeller Petal
	 * file (ASCII).
	 */
	public static final FormatEnum PTL = new FormatEnum("PTL", PTL_INT);

	/**
	 * Constant integer for enum : PUB .
	 */
	public static final int PUB_INT = 375;
	/**
	 * Enum : PUB : this enum describes format Microsoft Publisher. Supported
	 * versions :2.0, 95, 97, 98, 2000, 2002
	 */
	public static final FormatEnum PUB = new FormatEnum("PUB", PUB_INT);

	/**
	 * Constant integer for enum : PVD .
	 */
	public static final int PVD_INT = 253;
	/**
	 * Enum : PVD : this enum describes format Instalit Script.
	 */
	public static final FormatEnum PVD = new FormatEnum("PVD", PVD_INT);

	/**
	 * Constant integer for enum : PW .
	 */
	public static final int PW_INT = 518;
	/**
	 * Enum : PW : this enum describes format Professional Write Text File.
	 */
	public static final FormatEnum PW = new FormatEnum("PW", PW_INT);

	/**
	 * Constant integer for enum : PWI .
	 */
	public static final int PWI_INT = 143;
	/**
	 * Enum : PWI : this enum describes format Inkwriter/Notetaker Document.
	 */
	public static final FormatEnum PWI = new FormatEnum("PWI", PWI_INT);

	/**
	 * Constant integer for enum : PWT .
	 */
	public static final int PWT_INT = 144;
	/**
	 * Enum : PWT : this enum describes format Pocket Word Template.
	 */
	public static final FormatEnum PWT = new FormatEnum("PWT", PWT_INT);

	/**
	 * Constant integer for enum : QTM .
	 */
	public static final int QTM_INT = 658;
	/**
	 * Enum : QTM : this enum describes format Quicktime.
	 */
	public static final FormatEnum QTM = new FormatEnum("QTM", QTM_INT);

	/**
	 * Constant integer for enum : QXD .
	 */
	public static final int QXD_INT = 255;
	/**
	 * Enum : QXD : this enum describes format Quark Xpress Data File.
	 */
	public static final FormatEnum QXD = new FormatEnum("QXD", QXD_INT);

	/**
	 * Constant integer for enum : RA .
	 */
	public static final int RA_INT = 424;
	/**
	 * Enum : RA : this enum describes format Real Audio.
	 */
	public static final FormatEnum RA = new FormatEnum("RA", RA_INT);

	/**
	 * Constant integer for enum : RAM .
	 */
	public static final int RAM_INT = 256;
	/**
	 * Enum : RAM : this enum describes format RealAudio Metafile.
	 */
	public static final FormatEnum RAM = new FormatEnum("RAM", RAM_INT);

	/**
	 * Constant integer for enum : RAR .
	 */
	public static final int RAR_INT = 384;
	/**
	 * Enum : RAR : this enum describes format RAR Format.
	 */
	public static final FormatEnum RAR = new FormatEnum("RAR", RAR_INT);

	/**
	 * Constant integer for enum : RAS .
	 */
	public static final int RAS_INT = 257;
	/**
	 * Enum : RAS : this enum describes format Sun Raster Image.
	 */
	public static final FormatEnum RAS = new FormatEnum("RAS", RAS_INT);

	/**
	 * Constant integer for enum : RAW .
	 */
	public static final int RAW_INT = 258;
	/**
	 * Enum : RAW : this enum describes format Raw Bitmap.
	 */
	public static final FormatEnum RAW = new FormatEnum("RAW", RAW_INT);

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
	 * Constant integer for enum :
	 * REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE .
	 */
	public static final int REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE_INT = 27;
	/**
	 * Enum : REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE : this enum
	 * describes format Revisable-Form-Text Document Content Architecture.
	 */
	public static final FormatEnum REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE = new FormatEnum(
			"REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE",
			REVISABLE_FORM_TEXT_DOCUMENT_CONTENT_ARCHITECTURE_INT);

	/**
	 * Constant integer for enum : RFT .
	 */
	public static final int RFT_INT = 436;
	/**
	 * Enum : RFT : this enum describes format IBM DisplayWrite Revisable Form
	 * Text File.
	 */
	public static final FormatEnum RFT = new FormatEnum("RFT", RFT_INT);

	/**
	 * Constant integer for enum : RGB .
	 */
	public static final int RGB_INT = 259;
	/**
	 * Enum : RGB : this enum describes format Silicon Graphics RGB File.
	 */
	public static final FormatEnum RGB = new FormatEnum("RGB", RGB_INT);

	/**
	 * Constant integer for enum : RIF .
	 */
	public static final int RIF_INT = 260;
	/**
	 * Enum : RIF : this enum describes format Fractal Design Painter RIFF
	 * Bitmap Graphics.
	 */
	public static final FormatEnum RIF = new FormatEnum("RIF", RIF_INT);

	/**
	 * Constant integer for enum : RLA .
	 */
	public static final int RLA_INT = 261;
	/**
	 * Enum : RLA : this enum describes format SDSC Image Tool Wavefront Raster
	 * Image.
	 */
	public static final FormatEnum RLA = new FormatEnum("RLA", RLA_INT);

	/**
	 * Constant integer for enum : RLE .
	 */
	public static final int RLE_INT = 262;
	/**
	 * Enum : RLE : this enum describes format SDSC Image Tool Run-Length
	 * Encoded Bitmap.
	 */
	public static final FormatEnum RLE = new FormatEnum("RLE", RLE_INT);

	/**
	 * Constant integer for enum : RM .
	 */
	public static final int RM_INT = 263;
	/**
	 * Enum : RM : this enum describes format RealAudio Video.
	 */
	public static final FormatEnum RM = new FormatEnum("RM", RM_INT);

	/**
	 * Constant integer for enum : RQY .
	 */
	public static final int RQY_INT = 145;
	/**
	 * Enum : RQY : this enum describes format Microsoft Excel OLE DB Query.
	 */
	public static final FormatEnum RQY = new FormatEnum("RQY", RQY_INT);

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
	 * Constant integer for enum : RV .
	 */
	public static final int RV_INT = 422;
	/**
	 * Enum : RV : this enum describes format Real Video.
	 */
	public static final FormatEnum RV = new FormatEnum("RV", RV_INT);

	/**
	 * Constant integer for enum : SAM .
	 */
	public static final int SAM_INT = 264;
	/**
	 * Enum : SAM : this enum describes format AMI Professional Document.
	 */
	public static final FormatEnum SAM = new FormatEnum("SAM", SAM_INT);

	/**
	 * Constant integer for enum : SAS_DATA_FILE .
	 */
	public static final int SAS_DATA_FILE_INT = 521;
	/**
	 * Enum : SAS_DATA_FILE : this enum describes format SAS Data File.
	 */
	public static final FormatEnum SAS_DATA_FILE = new FormatEnum(
			"SAS_DATA_FILE", SAS_DATA_FILE_INT);

	/**
	 * Constant integer for enum : SAS_FOR_MS_DOS_DATABASE .
	 */
	public static final int SAS_FOR_MS_DOS_DATABASE_INT = 522;
	/**
	 * Enum : SAS_FOR_MS_DOS_DATABASE : this enum describes format SAS for
	 * MS-DOS Database.
	 */
	public static final FormatEnum SAS_FOR_MS_DOS_DATABASE = new FormatEnum(
			"SAS_FOR_MS_DOS_DATABASE", SAS_FOR_MS_DOS_DATABASE_INT);

	/**
	 * Constant integer for enum : SAT .
	 */
	public static final int SAT_INT = 146;
	/**
	 * Enum : SAT : this enum describes format AutoCAD ACIS Export File.
	 */
	public static final FormatEnum SAT = new FormatEnum("SAT", SAT_INT);

	/**
	 * Constant integer for enum : SCD .
	 */
	public static final int SCD_INT = 147;
	/**
	 * Enum : SCD : this enum describes format Schedule+ Contacts.
	 */
	public static final FormatEnum SCD = new FormatEnum("SCD", SCD_INT);

	/**
	 * Constant integer for enum : SCR .
	 */
	public static final int SCR_INT = 148;
	/**
	 * Enum : SCR : this enum describes format AutoCAD Script.
	 */
	public static final FormatEnum SCR = new FormatEnum("SCR", SCR_INT);

	/**
	 * Constant integer for enum : SCT .
	 */
	public static final int SCT_INT = 265;
	/**
	 * Enum : SCT : this enum describes format SAS for MS-DOS Catalog.
	 */
	public static final FormatEnum SCT = new FormatEnum("SCT", SCT_INT);

	/**
	 * Constant integer for enum : SDA .
	 */
	public static final int SDA_INT = 756;
	/**
	 * Enum : SDA : this enum describes format StarOffice Draw. Supported
	 * versions :5.1, 5.2
	 */
	public static final FormatEnum SDA = new FormatEnum("SDA", SDA_INT);

	/**
	 * Constant integer for enum : SDC .
	 */
	public static final int SDC_INT = 758;
	/**
	 * Enum : SDC : this enum describes format StarOffice Calc. Supported
	 * versions :5.1, 5.2
	 */
	public static final FormatEnum SDC = new FormatEnum("SDC", SDC_INT);

	/**
	 * Constant integer for enum : SDD .
	 */
	public static final int SDD_INT = 759;
	/**
	 * Enum : SDD : this enum describes format StarOffice Impress. Supported
	 * versions :5.1, 5.2
	 */
	public static final FormatEnum SDD = new FormatEnum("SDD", SDD_INT);

	/**
	 * Constant integer for enum : SDF .
	 */
	public static final int SDF_INT = 266;
	/**
	 * Enum : SDF : this enum describes format Unisys (Sperry) System Data File.
	 */
	public static final FormatEnum SDF = new FormatEnum("SDF", SDF_INT);

	/**
	 * Constant integer for enum : SGML .
	 */
	public static final int SGML_INT = 268;
	/**
	 * Enum : SGML : this enum describes format Standard Generalized Markup
	 * Language.
	 */
	public static final FormatEnum SGML = new FormatEnum("SGML", SGML_INT);

	/**
	 * Constant integer for enum : SH3 .
	 */
	public static final int SH3_INT = 149;
	/**
	 * Enum : SH3 : this enum describes format Harvard Graphics Show. Supported
	 * versions :3.0
	 */
	public static final FormatEnum SH3 = new FormatEnum("SH3", SH3_INT);

	/**
	 * Constant integer for enum : SHW .
	 */
	public static final int SHW_INT = 487;
	/**
	 * Enum : SHW : this enum describes format Harvard Graphics Presentation.
	 * Supported versions :2.0
	 */
	public static final FormatEnum SHW = new FormatEnum("SHW", SHW_INT);

	/**
	 * Constant integer for enum : SHX .
	 */
	public static final int SHX_INT = 151;
	/**
	 * Enum : SHX : this enum describes format AutoCAD Shape/Font File.
	 */
	public static final FormatEnum SHX = new FormatEnum("SHX", SHX_INT);

	/**
	 * Constant integer for enum : SILICON_GRAPHICS_GRAPHICS_FILE .
	 */
	public static final int SILICON_GRAPHICS_GRAPHICS_FILE_INT = 524;
	/**
	 * Enum : SILICON_GRAPHICS_GRAPHICS_FILE : this enum describes format
	 * Silicon Graphics Graphics File.
	 */
	public static final FormatEnum SILICON_GRAPHICS_GRAPHICS_FILE = new FormatEnum(
			"SILICON_GRAPHICS_GRAPHICS_FILE",
			SILICON_GRAPHICS_GRAPHICS_FILE_INT);

	/**
	 * Constant integer for enum : SKF .
	 */
	public static final int SKF_INT = 464;
	/**
	 * Enum : SKF : this enum describes format AutoSketch Drawing.
	 */
	public static final FormatEnum SKF = new FormatEnum("SKF", SKF_INT);

	/**
	 * Constant integer for enum : SLB .
	 */
	public static final int SLB_INT = 152;
	/**
	 * Enum : SLB : this enum describes format AutoCAD Slide Library.
	 */
	public static final FormatEnum SLB = new FormatEnum("SLB", SLB_INT);

	/**
	 * Constant integer for enum : SLD .
	 */
	public static final int SLD_INT = 153;
	/**
	 * Enum : SLD : this enum describes format AutoCAD Slide.
	 */
	public static final FormatEnum SLD = new FormatEnum("SLD", SLD_INT);

	/**
	 * Constant integer for enum : SLK .
	 */
	public static final int SLK_INT = 154;
	/**
	 * Enum : SLK : this enum describes format Microsoft Symbolic Link File.
	 */
	public static final FormatEnum SLK = new FormatEnum("SLK", SLK_INT);

	/**
	 * Constant integer for enum : SND .
	 */
	public static final int SND_INT = 269;
	/**
	 * Enum : SND : this enum describes format NeXt Sound.
	 */
	public static final FormatEnum SND = new FormatEnum("SND", SND_INT);

	/**
	 * Constant integer for enum : SPELLER_CUSTOM_DICTIONARY .
	 */
	public static final int SPELLER_CUSTOM_DICTIONARY_INT = 191;
	/**
	 * Enum : SPELLER_CUSTOM_DICTIONARY : this enum describes format Speller
	 * Custom Dictionary.
	 */
	public static final FormatEnum SPELLER_CUSTOM_DICTIONARY = new FormatEnum(
			"SPELLER_CUSTOM_DICTIONARY", SPELLER_CUSTOM_DICTIONARY_INT);

	/**
	 * Constant integer for enum : SPELLER_EXCLUDE_DICTIONARY .
	 */
	public static final int SPELLER_EXCLUDE_DICTIONARY_INT = 192;
	/**
	 * Enum : SPELLER_EXCLUDE_DICTIONARY : this enum describes format Speller
	 * Exclude Dictionary.
	 */
	public static final FormatEnum SPELLER_EXCLUDE_DICTIONARY = new FormatEnum(
			"SPELLER_EXCLUDE_DICTIONARY", SPELLER_EXCLUDE_DICTIONARY_INT);

	/**
	 * Constant integer for enum : STAROFFICE_WRITE .
	 */
	public static final int STAROFFICE_WRITE_INT = 757;
	/**
	 * Enum : STAROFFICE_WRITE : this enum describes format StarOffice Write.
	 * Supported versions :5.1, 5.2
	 */
	public static final FormatEnum STAROFFICE_WRITE = new FormatEnum(
			"STAROFFICE_WRITE", STAROFFICE_WRITE_INT);

	/**
	 * Constant integer for enum : STATIONARY_FOR_MAC_OS_X .
	 */
	public static final int STATIONARY_FOR_MAC_OS_X_INT = 190;
	/**
	 * Enum : STATIONARY_FOR_MAC_OS_X : this enum describes format Stationary
	 * for Mac OS X.
	 */
	public static final FormatEnum STATIONARY_FOR_MAC_OS_X = new FormatEnum(
			"STATIONARY_FOR_MAC_OS_X", STATIONARY_FOR_MAC_OS_X_INT);

	/**
	 * Constant integer for enum : STATS_DATA_FILE .
	 */
	public static final int STATS_DATA_FILE_INT = 206;
	/**
	 * Enum : STATS_DATA_FILE : this enum describes format Stats+ Data File.
	 */
	public static final FormatEnum STATS_DATA_FILE = new FormatEnum(
			"STATS_DATA_FILE", STATS_DATA_FILE_INT);

	/**
	 * Constant integer for enum : STB .
	 */
	public static final int STB_INT = 155;
	/**
	 * Enum : STB : this enum describes format AutoCAD Named Plot Style Table.
	 */
	public static final FormatEnum STB = new FormatEnum("STB", STB_INT);

	/**
	 * Constant integer for enum : STILL_PICTURE_INTERCHANGE_FILE_FORMAT .
	 */
	public static final int STILL_PICTURE_INTERCHANGE_FILE_FORMAT_INT = 677;
	/**
	 * Enum : STILL_PICTURE_INTERCHANGE_FILE_FORMAT : this enum describes format
	 * Still Picture Interchange File Format. Supported versions :1.0, 2.0
	 */
	public static final FormatEnum STILL_PICTURE_INTERCHANGE_FILE_FORMAT = new FormatEnum(
			"STILL_PICTURE_INTERCHANGE_FILE_FORMAT",
			STILL_PICTURE_INTERCHANGE_FILE_FORMAT_INT);

	/**
	 * Constant integer for enum : STL .
	 */
	public static final int STL_INT = 156;
	/**
	 * Enum : STL : this enum describes format Lithograph Export File.
	 */
	public static final FormatEnum STL = new FormatEnum("STL", STL_INT);

	/**
	 * Constant integer for enum : STRATGRAPHICS_DATA_FILE .
	 */
	public static final int STRATGRAPHICS_DATA_FILE_INT = 528;
	/**
	 * Enum : STRATGRAPHICS_DATA_FILE : this enum describes format StratGraphics
	 * Data File.
	 */
	public static final FormatEnum STRATGRAPHICS_DATA_FILE = new FormatEnum(
			"STRATGRAPHICS_DATA_FILE", STRATGRAPHICS_DATA_FILE_INT);

	/**
	 * Constant integer for enum : SUPERCALC_SPREADSHEET .
	 */
	public static final int SUPERCALC_SPREADSHEET_INT = 530;
	/**
	 * Enum : SUPERCALC_SPREADSHEET : this enum describes format SuperCalc
	 * Spreadsheet. Supported versions :4, 5
	 */
	public static final FormatEnum SUPERCALC_SPREADSHEET = new FormatEnum(
			"SUPERCALC_SPREADSHEET", SUPERCALC_SPREADSHEET_INT);

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
	 * Constant integer for enum : SVGZ .
	 */
	public static final int SVGZ_INT = 158;
	/**
	 * Enum : SVGZ : this enum describes format Scaleable Vector Graphics
	 * Compressed.
	 */
	public static final FormatEnum SVGZ = new FormatEnum("SVGZ", SVGZ_INT);

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
	 * Constant integer for enum : TAB_DELIMITED_TEXT_FILE .
	 */
	public static final int TAB_DELIMITED_TEXT_FILE_INT = 40;
	/**
	 * Enum : TAB_DELIMITED_TEXT_FILE : this enum describes format Tab-Delimited
	 * Text File.
	 */
	public static final FormatEnum TAB_DELIMITED_TEXT_FILE = new FormatEnum(
			"TAB_DELIMITED_TEXT_FILE", TAB_DELIMITED_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : TAG .
	 */
	public static final int TAG_INT = 271;
	/**
	 * Enum : TAG : this enum describes format DataFlex Query Tag Name.
	 */
	public static final FormatEnum TAG = new FormatEnum("TAG", TAG_INT);

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
	 * Constant integer for enum :
	 * TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP .
	 */
	public static final int TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP_INT = 797;
	/**
	 * Enum :
	 * TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP :
	 * this enum describes format Tagged Image File Format for Electronic Still
	 * Picture Imaging (TIFF/EP).
	 */
	public static final FormatEnum TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP = new FormatEnum(
			"TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP",
			TAGGED_IMAGE_FILE_FORMAT_FOR_ELECTRONIC_STILL_PICTURE_IMAGING_TIFF_EP_INT);

	/**
	 * Constant integer for enum :
	 * TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT .
	 */
	public static final int TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT_INT = 796;
	/**
	 * Enum : TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT : this enum
	 * describes format Tagged Image File Format for Image Technology (TIFF/IT).
	 */
	public static final FormatEnum TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT = new FormatEnum(
			"TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT",
			TAGGED_IMAGE_FILE_FORMAT_FOR_IMAGE_TECHNOLOGY_TIFF_IT_INT);

	/**
	 * Constant integer for enum :
	 * TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX .
	 */
	public static final int TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX_INT = 799;
	/**
	 * Enum : TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX : this enum
	 * describes format Tagged Image File Format for Internet Fax (TIFF-FX).
	 */
	public static final FormatEnum TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX = new FormatEnum(
			"TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX",
			TAGGED_IMAGE_FILE_FORMAT_FOR_INTERNET_FAX_TIFF_FX_INT);

	/**
	 * Constant integer for enum : TAR .
	 */
	public static final int TAR_INT = 385;
	/**
	 * Enum : TAR : this enum describes format Tape Archive Format.
	 */
	public static final FormatEnum TAR = new FormatEnum("TAR", TAR_INT);

	/**
	 * Constant integer for enum : TBL .
	 */
	public static final int TBL_INT = 272;
	/**
	 * Enum : TBL : this enum describes format Pagemaker TableEditor Graphics.
	 */
	public static final FormatEnum TBL = new FormatEnum("TBL", TBL_INT);

	/**
	 * Constant integer for enum : TDK .
	 */
	public static final int TDK_INT = 273;
	/**
	 * Enum : TDK : this enum describes format Turbo Debugger Keystroke
	 * Recording File.
	 */
	public static final FormatEnum TDK = new FormatEnum("TDK", TDK_INT);

	/**
	 * Constant integer for enum : TEX_BINARY_FILE .
	 */
	public static final int TEX_BINARY_FILE_INT = 531;
	/**
	 * Enum : TEX_BINARY_FILE : this enum describes format TeX Binary File.
	 */
	public static final FormatEnum TEX_BINARY_FILE = new FormatEnum(
			"TEX_BINARY_FILE", TEX_BINARY_FILE_INT);

	/**
	 * Constant integer for enum : TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT .
	 */
	public static final int TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT_INT = 532;
	/**
	 * Enum : TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT : this enum describes format
	 * TeX/LaTeX Device-Independent Document.
	 */
	public static final FormatEnum TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT = new FormatEnum(
			"TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT",
			TEX_LATEX_DEVICE_INDEPENDENT_DOCUMENT_INT);

	/**
	 * Constant integer for enum : TYM .
	 */
	public static final int TYM_INT = 274;
	/**
	 * Enum : TYM : this enum describes format PageMaker Time Stamp File.
	 * Supported versions :4.0
	 */
	public static final FormatEnum TYM = new FormatEnum("TYM", TYM_INT);

	/**
	 * Constant integer for enum : UDL .
	 */
	public static final int UDL_INT = 164;
	/**
	 * Enum : UDL : this enum describes format AutoCAD External Database
	 * Configuration File.
	 */
	public static final FormatEnum UDL = new FormatEnum("UDL", UDL_INT);

	/**
	 * Constant integer for enum : ULAW .
	 */
	public static final int ULAW_INT = 275;
	/**
	 * Enum : ULAW : this enum describes format CCITT G.711 Audio.
	 */
	public static final FormatEnum ULAW = new FormatEnum("ULAW", ULAW_INT);

	/**
	 * Constant integer for enum : UNICODE_TEXT_FILE .
	 */
	public static final int UNICODE_TEXT_FILE_INT = 43;
	/**
	 * Enum : UNICODE_TEXT_FILE : this enum describes format Unicode Text File.
	 */
	public static final FormatEnum UNICODE_TEXT_FILE = new FormatEnum(
			"UNICODE_TEXT_FILE", UNICODE_TEXT_FILE_INT);

	/**
	 * Constant integer for enum : VENTURA_PUBLISHER_VECTOR_GRAPHICS .
	 */
	public static final int VENTURA_PUBLISHER_VECTOR_GRAPHICS_INT = 91;
	/**
	 * Enum : VENTURA_PUBLISHER_VECTOR_GRAPHICS : this enum describes format
	 * Ventura Publisher Vector Graphics.
	 */
	public static final FormatEnum VENTURA_PUBLISHER_VECTOR_GRAPHICS = new FormatEnum(
			"VENTURA_PUBLISHER_VECTOR_GRAPHICS",
			VENTURA_PUBLISHER_VECTOR_GRAPHICS_INT);

	/**
	 * Constant integer for enum : VISICALC_DATABASE .
	 */
	public static final int VISICALC_DATABASE_INT = 538;
	/**
	 * Enum : VISICALC_DATABASE : this enum describes format VisiCalc Database.
	 */
	public static final FormatEnum VISICALC_DATABASE = new FormatEnum(
			"VISICALC_DATABASE", VISICALC_DATABASE_INT);

	/**
	 * Constant integer for enum : VISUAL_FOXPRO_DATABASE_CONTAINER_FILE .
	 */
	public static final int VISUAL_FOXPRO_DATABASE_CONTAINER_FILE_INT = 211;
	/**
	 * Enum : VISUAL_FOXPRO_DATABASE_CONTAINER_FILE : this enum describes format
	 * Visual FoxPro Database Container File.
	 */
	public static final FormatEnum VISUAL_FOXPRO_DATABASE_CONTAINER_FILE = new FormatEnum(
			"VISUAL_FOXPRO_DATABASE_CONTAINER_FILE",
			VISUAL_FOXPRO_DATABASE_CONTAINER_FILE_INT);

	/**
	 * Constant integer for enum : VSD .
	 */
	public static final int VSD_INT = 377;
	/**
	 * Enum : VSD : this enum describes format Microsoft Visio Drawing.
	 * Supported versions :5.0, 2000, 2002
	 */
	public static final FormatEnum VSD = new FormatEnum("VSD", VSD_INT);

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
	 * Constant integer for enum : WAVEFORM_AUDIO_PCMWAVEFORMAT .
	 */
	public static final int WAVEFORM_AUDIO_PCMWAVEFORMAT_INT = 784;
	/**
	 * Enum : WAVEFORM_AUDIO_PCMWAVEFORMAT : this enum describes format Waveform
	 * Audio (PCMWAVEFORMAT).
	 */
	public static final FormatEnum WAVEFORM_AUDIO_PCMWAVEFORMAT = new FormatEnum(
			"WAVEFORM_AUDIO_PCMWAVEFORMAT", WAVEFORM_AUDIO_PCMWAVEFORMAT_INT);

	/**
	 * Constant integer for enum : WAVEFORM_AUDIO_WAVEFORMATEX .
	 */
	public static final int WAVEFORM_AUDIO_WAVEFORMATEX_INT = 785;
	/**
	 * Enum : WAVEFORM_AUDIO_WAVEFORMATEX : this enum describes format Waveform
	 * Audio (WAVEFORMATEX).
	 */
	public static final FormatEnum WAVEFORM_AUDIO_WAVEFORMATEX = new FormatEnum(
			"WAVEFORM_AUDIO_WAVEFORMATEX", WAVEFORM_AUDIO_WAVEFORMATEX_INT);

	/**
	 * Constant integer for enum : WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE .
	 */
	public static final int WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE_INT = 786;
	/**
	 * Enum : WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE : this enum describes format
	 * Waveform Audio (WAVEFORMATEXTENSIBLE).
	 */
	public static final FormatEnum WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE = new FormatEnum(
			"WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE",
			WAVEFORM_AUDIO_WAVEFORMATEXTENSIBLE_INT);

	/**
	 * Constant integer for enum : WI .
	 */
	public static final int WI_INT = 279;
	/**
	 * Enum : WI : this enum describes format Corel Wavelet Compressed Bitmap.
	 */
	public static final FormatEnum WI = new FormatEnum("WI", WI_INT);

	/**
	 * Constant integer for enum : WINDOWS_BITMAP .
	 */
	public static final int WINDOWS_BITMAP_INT = 732;
	/**
	 * Enum : WINDOWS_BITMAP : this enum describes format Windows Bitmap.
	 * Supported versions :1.0, 2.0, 3.0, 3.0 NT, 4.0, 5.0
	 */
	public static final FormatEnum WINDOWS_BITMAP = new FormatEnum(
			"WINDOWS_BITMAP", WINDOWS_BITMAP_INT);

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
	 * Constant integer for enum : WK5 .
	 */
	public static final int WK5_INT = 299;
	/**
	 * Enum : WK5 : this enum describes format Lotus 1-2-3 Worksheet. Supported
	 * versions :2.0, 3.0, 4.0, 1.0, 5.0
	 */
	public static final FormatEnum WK5 = new FormatEnum("WK5", WK5_INT);

	/**
	 * Constant integer for enum : WKQ .
	 */
	public static final int WKQ_INT = 177;
	/**
	 * Enum : WKQ : this enum describes format Quattro Pro Spreadsheet.
	 * Supported versions :5
	 */
	public static final FormatEnum WKQ = new FormatEnum("WKQ", WKQ_INT);

	/**
	 * Constant integer for enum : WKS .
	 */
	public static final int WKS_INT = 170;
	/**
	 * Enum : WKS : this enum describes format Microsoft Works Spreadsheet.
	 * Supported versions :2.0
	 */
	public static final FormatEnum WKS = new FormatEnum("WKS", WKS_INT);

	/**
	 * Constant integer for enum : WMF .
	 */
	public static final int WMF_INT = 171;
	/**
	 * Enum : WMF : this enum describes format Windows Metafile.
	 */
	public static final FormatEnum WMF = new FormatEnum("WMF", WMF_INT);

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
	 * format WordPerfect for MS-DOS/Windows Document. Supported versions :6.0,
	 * 5.1
	 */
	public static final FormatEnum WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT = new FormatEnum(
			"WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT",
			WORDPERFECT_FOR_MS_DOS_WINDOWS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WORDPERFECT_FOR_WINDOWS_DOCUMENT .
	 */
	public static final int WORDPERFECT_FOR_WINDOWS_DOCUMENT_INT = 281;
	/**
	 * Enum : WORDPERFECT_FOR_WINDOWS_DOCUMENT : this enum describes format
	 * WordPerfect for Windows Document. Supported versions :5.2
	 */
	public static final FormatEnum WORDPERFECT_FOR_WINDOWS_DOCUMENT = new FormatEnum(
			"WORDPERFECT_FOR_WINDOWS_DOCUMENT",
			WORDPERFECT_FOR_WINDOWS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WORDPERFECT_SECONDARY_FILE .
	 */
	public static final int WORDPERFECT_SECONDARY_FILE_INT = 74;
	/**
	 * Enum : WORDPERFECT_SECONDARY_FILE : this enum describes format
	 * Wordperfect Secondary File. Supported versions :5.0, 5.1/5.2
	 */
	public static final FormatEnum WORDPERFECT_SECONDARY_FILE = new FormatEnum(
			"WORDPERFECT_SECONDARY_FILE", WORDPERFECT_SECONDARY_FILE_INT);

	/**
	 * Constant integer for enum : WORDSTAR_FOR_MS_DOS_DOCUMENT .
	 */
	public static final int WORDSTAR_FOR_MS_DOS_DOCUMENT_INT = 542;
	/**
	 * Enum : WORDSTAR_FOR_MS_DOS_DOCUMENT : this enum describes format WordStar
	 * for MS-DOS Document. Supported versions :5.0, 5.5, 6.0, 4.0, 7.0, 3.0
	 */
	public static final FormatEnum WORDSTAR_FOR_MS_DOS_DOCUMENT = new FormatEnum(
			"WORDSTAR_FOR_MS_DOS_DOCUMENT", WORDSTAR_FOR_MS_DOS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WORDSTAR_FOR_WINDOWS_DOCUMENT .
	 */
	public static final int WORDSTAR_FOR_WINDOWS_DOCUMENT_INT = 380;
	/**
	 * Enum : WORDSTAR_FOR_WINDOWS_DOCUMENT : this enum describes format
	 * WordStar for Windows Document. Supported versions :1.0, 2.0
	 */
	public static final FormatEnum WORDSTAR_FOR_WINDOWS_DOCUMENT = new FormatEnum(
			"WORDSTAR_FOR_WINDOWS_DOCUMENT",
			WORDSTAR_FOR_WINDOWS_DOCUMENT_INT);

	/**
	 * Constant integer for enum : WORKS_FOR_MACINTOSH_DOCUMENT .
	 */
	public static final int WORKS_FOR_MACINTOSH_DOCUMENT_INT = 15;
	/**
	 * Enum : WORKS_FOR_MACINTOSH_DOCUMENT : this enum describes format Works
	 * for Macintosh Document. Supported versions :4.0
	 */
	public static final FormatEnum WORKS_FOR_MACINTOSH_DOCUMENT = new FormatEnum(
			"WORKS_FOR_MACINTOSH_DOCUMENT", WORKS_FOR_MACINTOSH_DOCUMENT_INT);

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
	 * Constant integer for enum : WPL .
	 */
	public static final int WPL_INT = 438;
	/**
	 * Enum : WPL : this enum describes format DEC WPS Plus Document.
	 */
	public static final FormatEnum WPL = new FormatEnum("WPL", WPL_INT);

	/**
	 * Constant integer for enum : WPM .
	 */
	public static final int WPM_INT = 284;
	/**
	 * Enum : WPM : this enum describes format Microsoft Word for Windows Macro.
	 */
	public static final FormatEnum WPM = new FormatEnum("WPM", WPM_INT);

	/**
	 * Constant integer for enum : WPS .
	 */
	public static final int WPS_INT = 175;
	/**
	 * Enum : WPS : this enum describes format Microsoft Works for Windows.
	 * Supported versions :4.0
	 */
	public static final FormatEnum WPS = new FormatEnum("WPS", WPS_INT);

	/**
	 * Constant integer for enum : WRI .
	 */
	public static final int WRI_INT = 28;
	/**
	 * Enum : WRI : this enum describes format Write for Windows Document.
	 * Supported versions :3.1, 3.0
	 */
	public static final FormatEnum WRI = new FormatEnum("WRI", WRI_INT);

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
	 * Constant integer for enum : XBM .
	 */
	public static final int XBM_INT = 452;
	/**
	 * Enum : XBM : this enum describes format X-Windows Bitmap. Supported
	 * versions :X11, X10
	 */
	public static final FormatEnum XBM = new FormatEnum("XBM", XBM_INT);

	/**
	 * Constant integer for enum : XDM .
	 */
	public static final int XDM_INT = 453;
	/**
	 * Enum : XDM : this enum describes format X-Windows Dump File. Supported
	 * versions :X10
	 */
	public static final FormatEnum XDM = new FormatEnum("XDM", XDM_INT);

	/**
	 * Constant integer for enum : XLB .
	 */
	public static final int XLB_INT = 180;
	/**
	 * Enum : XLB : this enum describes format Microsoft Excel Toolbar.
	 */
	public static final FormatEnum XLB = new FormatEnum("XLB", XLB_INT);

	/**
	 * Constant integer for enum : XLG .
	 */
	public static final int XLG_INT = 182;
	/**
	 * Enum : XLG : this enum describes format AutoCAD Xref Log.
	 */
	public static final FormatEnum XLG = new FormatEnum("XLG", XLG_INT);

	/**
	 * Constant integer for enum : XLT .
	 */
	public static final int XLT_INT = 44;
	/**
	 * Enum : XLT : this enum describes format Microsoft Excel Template.
	 * Supported versions :2000
	 */
	public static final FormatEnum XLT = new FormatEnum("XLT", XLT_INT);

	/**
	 * Constant integer for enum : XPM .
	 */
	public static final int XPM_INT = 289;
	/**
	 * Enum : XPM : this enum describes format X-Windows Pixmap. Supported
	 * versions :X10
	 */
	public static final FormatEnum XPM = new FormatEnum("XPM", XPM_INT);

	/**
	 * Constant integer for enum : XSD .
	 */
	public static final int XSD_INT = 429;
	/**
	 * Enum : XSD : this enum describes format XML Schema Definition.
	 */
	public static final FormatEnum XSD = new FormatEnum("XSD", XSD_INT);

	/**
	 * Constant integer for enum : XSL .
	 */
	public static final int XSL_INT = 430;
	/**
	 * Enum : XSL : this enum describes format Extensible Stylesheet Language.
	 */
	public static final FormatEnum XSL = new FormatEnum("XSL", XSL_INT);

	/**
	 * Constant integer for enum : XWD .
	 */
	public static final int XWD_INT = 290;
	/**
	 * Enum : XWD : this enum describes format SDSC Image Tool X Window Dump
	 * Format.
	 */
	public static final FormatEnum XWD = new FormatEnum("XWD", XWD_INT);

	/**
	 * Constant integer for enum : XY4 .
	 */
	public static final int XY4_INT = 549;
	/**
	 * Enum : XY4 : this enum describes format XYWrite Document. Supported
	 * versions :III, III+, IV
	 */
	public static final FormatEnum XY4 = new FormatEnum("XY4", XY4_INT);

	/**
	 * Constant integer for enum : XYW .
	 */
	public static final int XYW_INT = 547;
	/**
	 * Enum : XYW : this enum describes format XYWrite for Windows Document.
	 * Supported versions :4.0
	 */
	public static final FormatEnum XYW = new FormatEnum("XYW", XYW_INT);

	/**
	 * Constant integer for enum : ZIP .
	 */
	public static final int ZIP_INT = 382;
	/**
	 * Enum : ZIP : this enum describes format ZIP Format.
	 */
	public static final FormatEnum ZIP = new FormatEnum("ZIP", ZIP_INT);

	/**
	 * Constant integer for enum : ZOO .
	 */
	public static final int ZOO_INT = 389;
	/**
	 * Enum : ZOO : this enum describes format ZOO Compressed Archive.
	 */
	public static final FormatEnum ZOO = new FormatEnum("ZOO", ZOO_INT);

	public static final int UNLISTED_INT = 0;
	public static final FormatEnum UNLISTED = new FormatEnum("unlisted",
			UNLISTED_INT);

	public static final int BASE64_INT = 50;
	public static final FormatEnum BASE64 = new FormatEnum("BASE64",
			BASE64_INT);

	public static final int JPEG_INT = 200;
	public static final FormatEnum JPEG = new FormatEnum("JPEG", JPEG_INT);
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
	public static final int XML_INT = 1000;
	public static final FormatEnum XML = new FormatEnum("XML", XML_INT);

	public static FormatEnum getEnum(final Class clazz, final String name) {
		return (FormatEnum) Enum.getEnum(clazz, name);
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

	@SuppressWarnings("unchecked")
	public static FormatEnum[] values() {
		final List<FormatEnum> enumList = getEnumList(FormatEnum.class);
		return enumList.toArray(new FormatEnum[0]);
	}
}
