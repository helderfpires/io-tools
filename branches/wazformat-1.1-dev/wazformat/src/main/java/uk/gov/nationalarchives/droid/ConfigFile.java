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
 * $Id: ConfigFile.java,v 1.8 2006/03/13 15:15:25 linb Exp $
 *
 * $Log: ConfigFile.java,v $
 * Revision 1.8  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.7  2006/02/08 11:42:24  linb
 * - make saveConfiguration throw an IOException
 *
 * Revision 1.6  2006/01/31 16:47:29  linb
 * Added log messages that were missing due to the log keyword being added too late
 *
 * Revision 1.5  2006/01/31 16:21:20  linb
 * Removed the dollars from the log lines generated by the previous message, so as not to cause problems with subsequent commits
 *
 * Revision 1.4  2006/01/31 16:19:07  linb
 * Added Log: and Id: tags to these files
 *
 * Revision 1.3  2006/01/31 16:11:37  linb
 * Add support for XML namespaces to:
 * 1) The reading of the config file, spec file and file-list file
 * 2) The writing of the config file and file-list file
 * - The namespaces still need to be set to their proper URIs (currently set to example.com...)
 * - Can still read in files without namespaces
 *
 * Revision 1.2  2006/01/31 12:00:37  linb
 * - Added new text field to option dialog for proxy setting
 * - Added new get/set methods to AnalysisController for proxy settings (from ConfigFile) *
 *
 * $History: ConfigFile.java $
 *
 * *****************  Version 8  *****************
 * User: Walm         Date: 20/10/05   Time: 15:16
 * Updated in $/PRONOM4/FFIT_SOURCE
 * proxy-enabling DROID: read proxy settings from config file
 *
 * *****************  Version 7  *****************
 * User: Walm         Date: 6/06/05    Time: 11:45
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Ensure config file is saved in UTF8
 * Resolve JIRA bug PRON-15
 *
 * *****************  Version 6  *****************
 * User: Mals         Date: 20/04/05   Time: 12:17
 * Updated in $/PRONOM4/FFIT_SOURCE
 * +Saves date in XML in format yyyy-MM-ddTHH:mm:ss
 *
 * *****************  Version 5  *****************
 * User: Walm         Date: 4/04/05    Time: 17:44
 * Updated in $/PRONOM4/FFIT_SOURCE
 * move saveConfig code to ConfigFile class from AnalysisControlle
 *
 * *****************  Version 4  *****************
 * User: Walm         Date: 31/03/05   Time: 15:26
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Initialise all configuration parameters to default values
 *
 */

package uk.gov.nationalarchives.droid;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.xml.stream.events.Namespace;

import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;



/**
 * Class to hold configuration data for the uk.
 * This data is read from and saved to an XML configuration file.
 *
 * @author Martin Waller
 * @version 1.0.0
 */
public class ConfigFile extends SimpleElement {

    /**
     * The full path of the configuration file
     */
    private String myFileName = AnalysisController.CONFIG_FILE_NAME;
    /**
     * The full path of the signature file
     */
    private String mySigFileName = AnalysisController.SIGNATURE_FILE_NAME;
    /**
     * The version of the signature file referred to by mySigFileName
     */
    private int mySigFileVersion = 0;
    /**
     * The full URL for the PRONOM web service
     */
    private String mySigFileURL = AnalysisController.PRONOM_WEB_SERVICE_URL;

    /**
     * Default browser for non-window client
     */
    private String myBrowserPath = AnalysisController.BROWSER_PATH;

    /**
     * Base URL for puid resolution
     */
    private String myPuidResolutionURL = AnalysisController.PUID_RESOLUTION_URL;

    /**
     * Proxy server (IP address)
     */
    private String myProxyHost = "";
    /**
     * Proxy server port
     */
    private int myProxyPort = 0;
    /**
     * Time interval (in days) after which to check whether a newer signature file exists
     */
    private int myDownloadFreq = AnalysisController.CONFIG_DOWNLOAD_FREQ;
    /**
     * Date of last signature file download
     */
    private Date myLastDownloadDate;

    /**
     * Set the configuration file name
     */
    public void setFileName(String theFileName) {
        myFileName = theFileName;
    }

    /**
     * set the signature file name
     */
    public void setSigFile(String theFileName) {
        this.mySigFileName = theFileName;
    }


    /**
     * Set the puid resolution URL
     *
     * @param theBaseURL
     */
    public void setPuidResolution(String theBaseURL) {
        this.myPuidResolutionURL = theBaseURL;
        if (!this.myPuidResolutionURL.endsWith("/")) {
            this.myPuidResolutionURL += "/";
        }
    }

    /**
     * Set the path for web browser
     */
    public void setBrowserPath(String path) {
        this.myBrowserPath = path;
    }

    /**
     * get the web browser path
     */
    public String getBrowserPath() {
        return this.myBrowserPath;
    }

    /**
     * Get the PUID resolution Base URL
     *
     * @return String
     */
    public String getPuidResolution() {
        return this.myPuidResolutionURL;
    }

    /**
     * Set the signature file version
     */
    public void setSigFileVersion(String theVersion) {
        try {
            this.mySigFileVersion = Integer.parseInt(theVersion);
        } catch (Exception e) {
        }
    }

    /**
     * Set the signature file version
     */
    public void setSigFileVersion(int theVersion) {
        this.mySigFileVersion = theVersion;
    }

    /**
     * Set the URL for PRONOM web service
     */
    public void setSigFileURL(String theURL) {
        this.mySigFileURL = theURL;
    }

    /**
     * Set the Proxy Server name (IP address)
     */
    public void setProxyHost(String theProxyHost) {
        this.myProxyHost = theProxyHost;
    }

    /**
     * Set the proxy server port
     */
    public void setProxyPort(String theProxyPort) {
        if (theProxyPort.trim().length() > 0) {
            try {
                this.myProxyPort = Integer.parseInt(theProxyPort);
            } catch (NumberFormatException e) {
                //the port number is not translatable to an integer
                this.myProxyPort = 0;
                MessageDisplay.generalWarning("Unable to read the proxy server port settings\nMake sure that the <ProxyPort> element in the configuration file is an integer.");
            }
        }
    }

    /**
     * Set the interval (in days) after which to check for newer signature file
     */
    public void setSigFileCheckFreq(String theFreq) {
        try {
            this.myDownloadFreq = Integer.parseInt(theFreq);
        } catch (Exception e) {
            MessageDisplay.generalWarning("Unable to read the signature file download frequency\nMake sure that the <SigFileCheckFreq> element in the configuration file is an integer number of days.\nThe default value of " + Integer.toString(AnalysisController.CONFIG_DOWNLOAD_FREQ) + " days will be used");
        }
    }

    /**
     * Set the date of the last signature file download
     *
     * @param theDate the date to use entered as a string
     */
    public void setDateLastDownload(String theDate) {
        java.text.DateFormat df;
        try {
            this.myLastDownloadDate = AnalysisController.parseXMLDate(theDate);
        } catch (ParseException pe) {
            try {
                df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.MEDIUM);
                this.myLastDownloadDate = df.parse(theDate);
            } catch (ParseException e1) {
                try {
                    df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                    this.myLastDownloadDate = df.parse(theDate);
                } catch (ParseException e2) {
                    this.myLastDownloadDate = null;
                }

            }
        }

    }

    /**
     * Set the date of the last signature file download to now
     */
    public void setDateLastDownload() {
        java.util.Date now = new java.util.Date();
        try {
            this.myLastDownloadDate = now;
        } catch (Exception e) {
            this.myLastDownloadDate = null;
        }
    }

    /**
     * Get the name of the configuration file
     */
    public String getFileName() {
        return myFileName;
    }

    /**
     * Get the name of the signature file
     */
    public String getSigFileName() {
        return mySigFileName;
    }

    /**
     * Get the version of the current signature file
     */
    public int getSigFileVersion() {
        return mySigFileVersion;
    }

    /**
     * Get the URL for the PRONOM web service
     */
    public String getSigFileURL() {
        return mySigFileURL;
    }

    /**
     * Get the proxy server IP address
     */
    public String getProxyHost() {
        return myProxyHost;
    }

    /**
     * Get the proxy server port
     */
    public int getProxyPort() {
        return myProxyPort;
    }

    /**
     * Get the interval in days after which to check whether a newer signature file exists
     */
    public int getSigFileCheckFreq() {
        return myDownloadFreq;
    }

    /**
     * Get the date of the last signature file download
     */
    public java.util.Date getLastDownloadDate() {
        return myLastDownloadDate;
    }

    /**
     * Check whether a newer signature file is available on the PRONOM web service
     */
    public boolean isDownloadDue() {
        if (myLastDownloadDate == null) {
            return true;
        }
        Date theNow = new Date();
        long elapsedTime = (theNow.getTime() - myLastDownloadDate.getTime());
        long theThreshold = myDownloadFreq * 24L * 3600L * 1000L;

        if (elapsedTime > theThreshold) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves the current configuration to file in XML format
     */
    public void saveConfiguration() throws IOException {
        java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(myFileName), "UTF8"));
        out.write(getConfigurationXMLJDOM());
        out.close();
    }

    /**
     * Creates a Configuration XML Document representing the current configuration
     * Uses JDOM
     *
     * @return the xml document
     */
    private String getConfigurationXMLJDOM() {

        //create all elements that will be used
        Namespace ns = Namespace.getNamespace(AnalysisController.CONFIG_FILE_NS);
        org.jdom.Element config_file = new org.jdom.Element("ConfigFile", ns);
        org.jdom.Element sig_file = new org.jdom.Element("SigFile", ns);
        org.jdom.Element sig_file_version = new org.jdom.Element("SigFileVersion", ns);
        org.jdom.Element sig_file_url = new org.jdom.Element("SigFileURL", ns);
        org.jdom.Element proxy_host = new org.jdom.Element("ProxyHost", ns);
        org.jdom.Element proxy_port = new org.jdom.Element("ProxyPort", ns);
        org.jdom.Element puid_resolution = new org.jdom.Element("PuidResolution", ns);
        org.jdom.Element browser_path = new org.jdom.Element("BrowserPath", ns);

        org.jdom.Element sig_file_check_freq = new org.jdom.Element("SigFileCheckFreq", ns);
        org.jdom.Element date_last_download = new org.jdom.Element("DateLastDownload", ns);

        //populate the elements
        sig_file.setText(getSigFileName());
        sig_file_version.setText(Integer.toString(getSigFileVersion()));
        sig_file_url.setText(getSigFileURL());
        browser_path.setText(getBrowserPath());
        puid_resolution.setText(getPuidResolution());
        proxy_host.setText(getProxyHost());

        int aProxyPort = getProxyPort();
        if (aProxyPort > 0) {
            proxy_port.setText(Integer.toString(aProxyPort));
        } else {
            proxy_port.setText("");
        }
        sig_file_check_freq.setText(Integer.toString(getSigFileCheckFreq()));
        try {
            date_last_download.setText(AnalysisController.writeXMLDate(getLastDownloadDate()));
        } catch (Exception e) {
            date_last_download.setText("");
        }
        config_file.addContent(sig_file);
        config_file.addContent(sig_file_version);
        config_file.addContent(sig_file_url);
        config_file.addContent(proxy_host);
        config_file.addContent(proxy_port);
        config_file.addContent(puid_resolution);
        config_file.addContent(browser_path);

        config_file.addContent(sig_file_check_freq);
        config_file.addContent(date_last_download);

        org.jdom.Document theJDOMdocument = new org.jdom.Document(config_file);

        //write it all to a String
        org.jdom.output.Format xmlFormat = org.jdom.output.Format.getPrettyFormat();
        org.jdom.output.XMLOutputter outputter = new org.jdom.output.XMLOutputter(xmlFormat);
        java.io.StringWriter writer = new java.io.StringWriter();

        try {
            outputter.output(theJDOMdocument, writer);
            writer.close();
        } catch (java.io.IOException e) {

        }
        return writer.toString();
    }

}