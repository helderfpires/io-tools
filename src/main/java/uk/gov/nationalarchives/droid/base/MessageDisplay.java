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
 * Tessella/NPD/4305
 * PRONOM 4
 *
 * $History: messageDisplay.java $
 * 
 * *****************  Version 7  *****************
 * User: Walm         Date: 26/04/05   Time: 17:29
 * Updated in $/PRONOM4/FFIT_SOURCE
 * allow different messages in GUI and on command line
 * 
 * *****************  Version 6  *****************
 * User: Mals         Date: 19/04/05   Time: 9:36
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Tessella Ref: NPD/4305/PR/IM/2005APR18/09:51:03 Issue 36
 * +Changed extension warning to text in email from A.Brown (Tessella Ref:
 * NPD/4305/CL/CSC/2005FEB17/16:34:13)
 * 
 * +Changed any reference of uk to DROID
 * 
 * *****************  Version 5  *****************
 * User: Walm         Date: 4/04/05    Time: 17:44
 * Updated in $/PRONOM4/FFIT_SOURCE
 * code for responding to missing signature file on startup
 * 
 * *****************  Version 4  *****************
 * User: Walm         Date: 29/03/05   Time: 17:02
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Display messages in the GUI if one has been defined
 *
 * Created on 21 February 2005, 14:47
 */

package uk.gov.nationalarchives.droid.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Allows messages to be displayed in the most appropriate manner to the user.
 * 
 * @author Martin Waller
 * @version 1.0.0
 */
public final class MessageDisplay {
	public static String FILEEXTENSIONWARNING = "Possible file extension mismatch";
	public static String POSITIVEIDENTIFICATIONSTATUS = "Positively identified";
	public static String TENTATIVEIDENTIFICATIONSTATUS = "Tentatively identified";
	public static String UNIDENTIFIEDSTATUS = "Unable to identify";

	private static final Log LOG = LogFactory.getLog(MessageDisplay.class);

	/**
	 * Displays a special warning for unknown XML elements when reading XML
	 * files
	 * 
	 * @param unknownElement
	 *            The name of the element which was not recognised
	 * @param containerElement
	 *            The name of the element which contains the unrecognised
	 *            element
	 */
	public static void unknownElementWarning(String unknownElement,
			String containerElement) {
		String theCMDMessage = "WARNING: Unknown XML element " + unknownElement
				+ " found under " + containerElement + " ";
		LOG.warn(theCMDMessage);
	}

	/**
	 * Displays a special warning for unknown XML attributes when reading XML
	 * files
	 * 
	 * @param unknownAttribute
	 *            The name of the attribute which was not recognised
	 * @param containerElement
	 *            The name of the element which contains the unrecognised
	 *            attribute
	 */
	public static void unknownAttributeWarning(String unknownAttribute,
			String containerElement) {
		String theCMDMessage = "WARNING: Unknown XML attribute "
				+ unknownAttribute + " found for " + containerElement + " ";
		LOG.warn(theCMDMessage);
	}

	/**
	 * Displays a general warning
	 * 
	 * @param theWarning
	 *            The text to be displayed
	 */
	public static void generalWarning(String theWarning) {
		String theMessage = "WARNING: "
				+ theWarning.replaceFirst("java.lang.Exception: ", "");
		LOG.warn(theMessage);
	}

	/**
	 * Displays general information
	 * 
	 * @param theMessage
	 *            The text to be displayed
	 */
	public static void generalInformation(String theMessage) {
		LOG.debug(theMessage);
	}

	/**
	 * Displays general information
	 * 
	 * @param theGUIMessage
	 *            The text to be displayed in GUI mode
	 * @param theCMDMessage
	 *            The text to be displayed in command line mode
	 */
	public static void generalInformation(String theGUIMessage,
			String theCMDMessage) {
		LOG.debug(theCMDMessage);
	}

	/**
	 * Displays general information in the status bar
	 * 
	 * @param theMessage
	 *            The text to be displayed
	 */
	public static void setStatusText(String theMessage) {
		LOG.info(theMessage);
	}

	/**
	 * Displays general information in the status bar
	 * 
	 * @param theGUIMessage
	 *            The text to be displayed in the status bar
	 * @param theCMDMessage
	 *            The text to be displayed in command line mode
	 */
	public static void setStatusText(String theGUIMessage, String theCMDMessage) {
		LOG.info(theCMDMessage);
	}

	/**
	 * Displays a general error
	 * 
	 * @param theWarning
	 *            The text to be displayed
	 */
	public static void generalError(String theWarning) throws Exception {
		String theMessage = "Error: " + theWarning;
		LOG.error(theMessage);
		throw new Exception(theWarning);
	}

	/**
	 * Displays a fatal error and then exits
	 * 
	 * @param theWarning
	 *            The text to be displayed
	 */
	public static void fatalError(String theWarning) {
			LOG.error(theWarning);
	}






}
