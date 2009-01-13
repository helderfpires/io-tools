/*
 * � The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * email: info@tessella.com
 * web:   www.tessella.com
 *
 * Project Number:  Tessella/NPD/4305
 *                  
 *
 * Project Title:   File Format Identification Tool
 * Project Identifier: uk
 *
 * Nested Classes:
 *                  FileListTableModel      - Used to display file list in JTable 
 *                  HitListTableModel       - Used to dislay file format hits in a JTable
 *                  FileListHeaderRenderer  - Renders the column headers in the file list JTable
 *                  CellRenderer            - Renders text cells in the filelist JTable
 *                  IconCellRenderer        - Renders cells with icons in the filelist JTable
 *
 * Version      Date        Author      Short Description
 *
 * V1.R0.M0     08-Mar-2005 S.Malik     Created
 *$History: FileIdentificationPane.java $   
 * 
 * *****************  Version 65  *****************
 * User: Walm         Date: 20/10/05   Time: 15:15
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * When using the PRONOM web service, check whether the connection failed,
 * and if so provide a helpful message
 * 
 * *****************  Version 64  *****************
 * User: Mals         Date: 20/06/05   Time: 14:43
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Calling parameters for Print Preview changed, so needed updating
 * 
 * *****************  Version 63  *****************
 * User: Walm         Date: 6/06/05    Time: 16:55
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Change appearance of form:
 * - resize frames
 * - frame around toolbar
 * - appearance of "Save as" button when hovering over it
 * - remove vertical line in toolbar
 * 
 * *****************  Version 62  *****************
 * User: Mals         Date: 12/05/05   Time: 13:03
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Added tooltip to open list button on toolbar
 * 
 * *****************  Version 61  *****************
 * User: Mals         Date: 10/05/05   Time: 13:47
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Changed path to image for Down arrow which shows image on file list
 * header
 * 
 * *****************  Version 60  *****************
 * User: Mals         Date: 10/05/05   Time: 11:47
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * + Frame icon set to image supplied by TNA
 * + Help window icon set to same as help icon on menubar
 * 
 * *****************  Version 59  *****************
 * User: Mals         Date: 6/05/05    Time: 16:27
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Toolbar and menubar icons changed to set supplied by TNA (
 * Tessella Ref: NPD/4305/CL/CSC/2005MAY06/11:35:51 )
 * 
 * +Fixed bug discovered in tests 7.7,7.8( STS V1.R1.M1)  -  4 May 2005
 * 
 * *****************  Version 58  *****************
 * User: Mals         Date: 3/05/05    Time: 15:44
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/CL/CSC/2005MAY03/09:01:53
 * 7: I  think we need�one more minor adjustment to the spacing in the
 * layout: can  we increase the space between the file list and id results
 * tables and the  borders at the sides to match that at top and bottom.
 * 
 * *****************  Version 57  *****************
 * User: Mals         Date: 3/05/05    Time: 11:54
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005MAY03/08:51:16
 * 
 * 6: In Identification results, the width of the Status column should be
 * wide enough to display the longest status text completely.
 * 
 * *****************  Version 56  *****************
 * User: Mals         Date: 3/05/05    Time: 11:40
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005MAY03/08:51:16
 * 
 * 1: Can we have the application window open not-maximised?
 * 
 * *****************  Version 55  *****************
 * User: Mals         Date: 3/05/05    Time: 11:35
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005MAY03/08:51:16
 * 5: I think genuinely unidentified files (i.e. not as a result of an
 * error) should be displayed as an ID result with a status of
 * "Unidentified", and  a warning to elaborate - "The format could not be
 * identified". 
 * 
 * *****************  Version 54  *****************
 * User: Mals         Date: 3/05/05    Time: 9:59
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005MAY03/08:51:16
 * 3: Agreed - Error is better. - When selecting an ERROR status file from
 * the file list, the "Identification results" pane shows a text box with
 * a "warnings" header - this should really be "Error" 
 * 
 * *****************  Version 53  *****************
 * User: Mals         Date: 28/04/05   Time: 15:28
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Changed to Plastic Look and Feels from PlasticXP Look and feel, as
 * buttons and toolbars were not displayed correctly on Max OS X. 
 * 
 * *****************  Version 52  *****************
 * User: Mals         Date: 28/04/05   Time: 15:12
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Only allows printing and print previewing if there is 1 or more files
 * in the file list
 * 
 * *****************  Version 51  *****************
 * User: Mals         Date: 28/04/05   Time: 10:03
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR28/09:57:44
 * Update the DateLastDownload in the configuration file after new sig
 * file is checked for- this is so that the user is not asked to check
 * signature file until another "DownloadFrequency" days have elapsed
 * 
 * *****************  Version 50  *****************
 * User: Mals         Date: 28/04/05   Time: 9:52
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Warning column removed from file list and identification results grid
 * turns into text box if file is identified as error.
 * 
 * Tessella Ref: NPD/4305/CL/CSC/2005APR21/16:33:49
 * 
 * *****************  Version 49  *****************
 * User: Mals         Date: 20/04/05   Time: 17:32
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Fixed bug : Resize cursor wasn't shown when resizing columns on
 * identification results
 * 
 * *****************  Version 48  *****************
 * User: Mals         Date: 20/04/05   Time: 16:35
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Help window centred when opened
 * 
 * *****************  Version 47  *****************
 * User: Mals         Date: 19/04/05   Time: 16:50
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tool tips on File List cells
 * 
 * *****************  Version 46  *****************
 * User: Mals         Date: 19/04/05   Time: 11:16
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR18/09:51:03
 * Issue 32 - Change background of file list and identification results to
 * white
 * 
 * *****************  Version 45  *****************
 * User: Mals         Date: 18/04/05   Time: 16:51
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR18/09:51:03
 * 1.The main application window should open centralised on screen, as the
 * splash screen does.
 * 5.The rightmost toolbar divider is no longer required.
 * 6.Toolbar tips text should have all initial letters capitalised, and
 * should be consistent with menu items text.
 * 7.All command buttons should have keyboard shortcuts indicated by
 * underlined text.
 * 8.Command button tips text should have all initial letters capitalised,
 * and should be consistent with menu items text.
 * 9.The progress bar text should be of the form �File x of y analysed�.
 * 10.Menu bar changes as in document.
 * 11.All dialog boxes should have the same title as the menu item or
 * button which opens them: Open List, Save List, Export to CSV, Add
 * Files, Options, DROID Help, About DROID.
 * 23.The infill square at top right above the vertical scroll bar appears
 * a different shade of grey.
 * 24.Add files should be 'Add Files'.
 * 25.Remove should be 'Remove Files'.
 * 26.'Remove all' should be 'Remove All'.
 * 27.I think the icons should be removed from these buttons � this will
 * help to make the Identify and Cancel buttons stand out more.
 * 28.The Remove and Remove All buttons should be disabled unless the file
 * list contains at least one file.
 * 30.The infill square at top right above the vertical scroll bar should
 * be grey to match (this is only visible with multiple identifications).
 * 31.The file name text box is not editable and should have a grey
 * background, with a dark grey border. The scroll bar can be removed. The
 * label should be centralised with the text box.
 * 35.The default column widths could be adjusted a little � PUID and
 * Version could be a little narrower, and Warning a little wider.
 * 
 * 
 * *****************  Version 44  *****************
 * User: Mals         Date: 15/04/05   Time: 10:29
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Hour glasses on print preview and help launch
 * 
 * *****************  Version 43  *****************
 * User: Mals         Date: 14/04/05   Time: 17:13
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Splash Image URL corrected
 * 
 * *****************  Version 42  *****************
 * User: Mals         Date: 14/04/05   Time: 15:54
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR13/17:56:51
 * 4.Key press on BackSpace calls remove all
 * 
 * *****************  Version 41  *****************
 * User: Mals         Date: 14/04/05   Time: 14:34
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR13/17:56:51
 * 8. Some of the dialog text needs to be corrected. 
 * 
 * *****************  Version 40  *****************
 * User: Mals         Date: 14/04/05   Time: 11:09
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Tessella Ref: NPD/4305/PR/IM/2005APR13/17:56:51
 * GUI changes
 * 1.Make the screen 800x600 pixles by default.
 * 2. Can we size the columns Identification Results columns intelligently
 * (in particular, so most Formats can be seen).
 * 
 * 
 * *****************  Version 39  *****************
 * User: Mals         Date: 13/04/05   Time: 12:33
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Ref:Email from A.Brown NPD/4305/CL/CSC/2005APR12/13:11  
 * File ID GUI comments
 * -----------------------------------
 * 
 * Changes made in light of comments
 * 
 * +name for the tool - DROID (Digital Record Object Identification)
 * +Add/Removeactions as command buttons on the form, rather than on the
 * tool bar
 * +"Remove All" instead of "new list"
 * +The Add files button should be the default button.
 * +"File details" renamed "Identification results"
 * 
 * 
 * *****************  Version 38  *****************
 * User: Mals         Date: 13/04/05   Time: 10:06
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Ref:Email from A.Brown NPD/4305/CL/CSC/2005APR12/13:11  File ID GUI
 * comments
 * -Only have one save function, which saves results if they exist
 * 
 * *****************  Version 37  *****************
 * User: Mals         Date: 7/04/05    Time: 16:31
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Add application and signature version numbers as parameters to print
 * and print preview functions
 * 
 * *****************  Version 36  *****************
 * User: Mals         Date: 7/04/05    Time: 14:03
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Generate mnemonics code in NetBeans 3.6 turned off , so openide
 * library not needed
 * +Mnemonics(keyboard shortcuts) set on all menu items
 * 
 * *****************  Version 35  *****************
 * User: Walm         Date: 7/04/05    Time: 11:43
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * investigate mouse display in column resizing
 * 
 * *****************  Version 34  *****************
 * User: Walm         Date: 5/04/05    Time: 11:36
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Correct bug in removeFiles method
 * 
 * *****************  Version 33  *****************
 * User: Walm         Date: 4/04/05    Time: 17:45
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * modify startup procedure
 * 
 * *****************  Version 32  *****************
 * User: Mals         Date: 4/04/05    Time: 9:18
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * No Change - checked in for code sharing
 * 
 * *****************  Version 31  *****************
 * User: Mals         Date: 31/03/05   Time: 15:13
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Displays Error file classification/status icon
 * 
 * *****************  Version 30  *****************
 * User: Mals         Date: 31/03/05   Time: 12:20
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Added parameters to the method to show the about box
 * 
 * *****************  Version 29  *****************
 * User: Mals         Date: 31/03/05   Time: 10:43
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * -Removed nested class XMLFileFilter
 * +Uses CustomFileFilter instead
 * 
 * *****************  Version 28  *****************
 * User: Mals         Date: 30/03/05   Time: 16:41
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Saving file list and saving results are now seperate methods 
 * 
 * *****************  Version 27  *****************
 * User: Mals         Date: 30/03/05   Time: 15:43
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Export to CSV
 * +Passes analysis controller object to OptionsDialog
 * 
 * *****************  Version 26  *****************
 * User: Mals         Date: 30/03/05   Time: 11:23
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Checks for is signature file download is due on startup 
 * +Asks user whether they would like to download new file and then
 * downloads if yes
 * 
 * *****************  Version 25  *****************
 * User: Mals         Date: 29/03/05   Time: 17:42
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Open list on menu item click now works
 * 
 * *****************  Version 24  *****************
 * User: Mals         Date: 29/03/05   Time: 17:37
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Direct print runs in its own thread as this may take some time
 * 
 * *****************  Version 23  *****************
 * User: Walm         Date: 29/03/05   Time: 16:55
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Initialise the main pane used for displaying warning messages
 * 
 * *****************  Version 22  *****************
 * User: Mals         Date: 29/03/05   Time: 12:01
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * REF: Email from A.Brown NPD/4305/CL/CSC/2005MAR21/12:25:13 
 * Menu Bar
 * ----------------
 * +All menu items have keyboard shortcuts indicated
 * + ... added to menu items that open dialogs
 * + Save menu item goes directly to dialog
 * 
 * Tool bar
 * -------------
 * +Simply toolbar text 
 * 
 * File List
 * -----------
 * +Horizontal scrollbar when file names exceed width of box
 * +white space in top right hand corner between scrollbar and column head
 * is infilled
 * 
 * File details
 * ----------------
 * +Title is now "File details" instead of "File Details"
 * 
 * *****************  Version 21  *****************
 * User: Mals         Date: 29/03/05   Time: 9:22
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Vertical gridlines shown on file formats hits table 
 * REF: Email from A.Brown NPD/4305/CL/CSC/2005MAR21/12:25:13 
 * 
 * *****************  Version 20  *****************
 * User: Mals         Date: 24/03/05   Time: 16:52
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Print menu item and print button on toolbar call print function
 * 
 * *****************  Version 19  *****************
 * User: Mals         Date: 24/03/05   Time: 13:23
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * +Added print previewing 
 * +Changed look and feel to Jgoodies Plastic XP L&F with the Sky Bluer
 * theme 
 * 
 * *****************  Version 18  *****************
 * User: Mals         Date: 21/03/05   Time: 14:04
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Launches help window
 * 
 * *****************  Version 17  *****************
 * User: Mals         Date: 21/03/05   Time: 9:57
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Correctected copyright statement
 * Sorting by filename now ignores case.
 * 
 * *****************  Version 16  *****************
 * User: Mals         Date: 16/03/05   Time: 17:13
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Check file save on window closing 
 * 
 * *****************  Version 15  *****************
 * User: Mals         Date: 16/03/05   Time: 16:32
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * -Opens and saves file lists without identification and format hits
 * -Validates list is saved before: new , open or close 
 * 
 * *****************  Version 14  *****************
 * User: Mals         Date: 16/03/05   Time: 12:29
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * -File list sort by column is highlighted
 * -Cancel button now cancels run , but cannot restart identification at
 * the moment
 * -Identify and cancel butons enable/disable depending on run and/or file
 * list
 * -Add files doesn't open when identification process is running 
 * -Menu bar item now call functions(i.e Identify , Cancel , Remove  , Add
 * Files) 
 * 
 * *****************  Version 13  *****************
 * User: Mals         Date: 16/03/05   Time: 10:27
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * File list sort by column gets highlighted
 * 
 * *****************  Version 12  *****************
 * User: Mals         Date: 15/03/05   Time: 15:15
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Remove files functionality added - but can also remove identified files
 * at this point and files while identification is running
 * 
 * Changes made to acces to IdentificationFile objects as classification
 * is now an int instead of string 
 * 
 * *****************  Version 11  *****************
 * User: Mals         Date: 15/03/05   Time: 10:54
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Implemented sorting by status or filename on file list 
 * 
 * *****************  Version 10  *****************
 * User: Mals         Date: 14/03/05   Time: 18:12
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Progress bar working on run
 * 
 * *****************  Version 9  *****************
 * User: Mals         Date: 14/03/05   Time: 9:42
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * launch method added so GUI can be run from another class
 * 
 * *****************  Version 8  *****************
 * User: Mals         Date: 11/03/05   Time: 17:12
 * Updated in $/PRONOM4/FFIT_SOURCE/GUI
 * Displays files when added
 * 
 */

package uk.gov.nationalarchives.droid;

import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import uk.gov.nationalarchives.droid.xmlReader.PronomWebService;


/**
 * Entry form for the GUI front end This is the entry form for the GUI This form
 * : displays identication file list , initiates identification process,
 * displays progress of identification process, displays format hits after
 * identification process Based on fiMain.java from the FileIDPrototype
 * <p/>
 * Created in netBeans IDE 3.6 Related file FileIdentificationPane.Form (for use
 * in netBeans)
 * 
 * @author Shahzad Malik
 * @version V1.R0.M.0, 08-Mar-2005
 */
public class FileIdentificationPane extends javax.swing.JFrame {

	/**
	 * Renderer for the file list header to highlight sort by column
	 */
	private class CellRenderer extends javax.swing.JLabel implements
			TableCellRenderer {

		public void firePropertyChange(final String propertyName,
				final boolean oldValue, final boolean newValue) {
		}

		/**
		 * This method is called each time a cell in a column using this
		 * renderer needs to be rendered.
		 * 
		 * @param table
		 *            Table whos cells are to be rendered
		 * @param value
		 *            object in cell to be rendered
		 * @param isSelected
		 *            Is the cell selected
		 * @param hasFocus
		 *            Does the cell have focus
		 * @param rowIndex
		 *            Cells Row position
		 * @param vColIndex
		 *            Cells column position
		 */
		public java.awt.Component getTableCellRendererComponent(
				final javax.swing.JTable table, final Object value,
				final boolean isSelected, final boolean hasFocus,
				final int rowIndex, final int vColIndex) {
			// 'value' is value contained in the cell located at
			// (rowIndex, vColIndex)

			if (isSelected) {
				this.setOpaque(true);
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());

			} else {
				super.setForeground(table.getForeground());
				super.setBackground(table.getBackground());
			}

			if (hasFocus) {

			}

			// if (value instanceof javax.swing.ImageIcon){
			// setIcon((javax.swing.ImageIcon)value) ;
			// }
			// else if (value instanceof String){
			setText((String) value);
			// }

			final IdentificationFile idFile = FileIdentificationPane.this.analysisControl
					.getFile(selectedRowToFileIndex(rowIndex));

			String toolTipText = idFile.getClassificationText();
			if (!idFile.getWarning().equals("")) {
				toolTipText += " (" + idFile.getWarning() + ")";
			}

			toolTipText += " " + idFile.getFilePath();

			// Set tool tip if desired
			setToolTipText(toolTipText);

			// Since the renderer is a component, return itself
			return this;
		}

		public void revalidate() {
		}

		// The following methods override the defaults for performance reasons
		public void validate() {
		}

		protected void firePropertyChange(final String propertyName,
				final Object oldValue, final Object newValue) {
		}
	}

	/**
	 * Renderer for the file list header to highlight sort by column
	 */
	private class FileListHeaderRenderer extends DefaultTableCellRenderer {

		/**
		 * Overrides method in DefaultTableCellRender sets the column text to
		 * red if that column is used to sort list
		 * 
		 * @param table
		 *            table to apply renderer to
		 * @param value
		 *            object stored in the cell
		 * @param isSelected
		 *            Is cell selected?
		 * @param hasFocus
		 *            Does cell have focus
		 * @param row
		 *            cell row in table
		 * @param column
		 *            cell column in table
		 * @return DefaultTableCellRenderer object
		 */
		public java.awt.Component getTableCellRendererComponent(
				final javax.swing.JTable table, final Object value,
				final boolean isSelected, final boolean hasFocus,
				final int row, final int column) {
			// Inherit the colors and font from the header component
			if (table != null) {
				final JTableHeader header = table.getTableHeader();
				if (header != null) {
					setForeground(header.getForeground());
					setIcon(null);
					// Check if column selected
					if (column == FileIdentificationPane.this.FileListSortByColumn) {
						setForeground(java.awt.Color.RED);
						setIcon(new javax.swing.ImageIcon(
								getClass()
										.getResource(
												"/uk/gov/nationalarchives/droid/GUI/Icons/Down16.gif")));

					}
					setBackground(header.getBackground());
					setFont(header.getFont());
					setText(header.getColumnModel().getColumn(column)
							.getHeaderValue().toString());
					setBorder(javax.swing.UIManager
							.getBorder("TableHeader.cellBorder"));
					setHorizontalAlignment(javax.swing.JLabel.CENTER);
				}
			}

			return this;
		}
	}

	/**
	 * Table model to hold hold the file list data
	 */
	private class FileListTableModel extends AbstractTableModel {

		/**
		 * Get the Class for a column
		 * 
		 * @param col
		 *            column to find class
		 * @return Class type found
		 */
		public Class getColumnClass(final int col) {
			if (col == 0) {
				return javax.swing.ImageIcon.class;
			}

			return String.class;
		}

		/**
		 * Returns number of columns in the table Only two columns required
		 * Status and Filename
		 * 
		 * @return number of columns in table
		 */
		public int getColumnCount() {
			return 2;
		}

		public IdentificationFile getIdentFileAt(final int row) {
			return FileIdentificationPane.this.analysisControl
					.getFile((Integer) FileIdentificationPane.this.fileList
							.get(row));
		}

		/**
		 * Gets the number of rows displayed in the table
		 * 
		 * @return Number of files held by the filelist
		 */
		public int getRowCount() {
			return FileIdentificationPane.this.fileList.size();
		}

		// public String getColumnName(int column) {return columnNames[column];}

		/**
		 * Gets the object for a specified cell
		 * 
		 * @param row
		 *            row id
		 * @param col
		 *            column id
		 * @return object to display in cell
		 */
		public Object getValueAt(final int row, final int col) {
			final Integer controlIndex = (Integer) FileIdentificationPane.this.fileList
					.get(row);

			final IdentificationFile idFile = FileIdentificationPane.this.analysisControl
					.getFile(controlIndex.intValue());

			switch (col) {
			case 0:
				return getStatusIcon(idFile.getClassification());
			case 1:
				if (FileIdentificationPane.this.jCheckBoxShowFilePaths
						.isSelected()) {
					return idFile.getFilePath();
				} else {

					return idFile.getFileName();

				}

			}
			return "Some Value row: " + row + " col: " + col;
		}

		/**
		 * Determines wether table cell is editable ALWAYS returns false
		 * 
		 * @param row
		 *            Row of cell
		 * @param col
		 *            Column of cell
		 * @return true if cell editable , false otherwise
		 */
		public boolean isCellEditable(final int row, final int col) {

			return false;
		}

		/**
		 * Sets the value for a specific cell DOES NOTHING IN THIS
		 * IMPLEMENTATION
		 * 
		 * @param aValue
		 *            Object to set in cell
		 * @param row
		 *            Row of cell to enter object
		 * @param column
		 *            Column of cell to enter object
		 */
		public void setValueAt(final Object aValue, final int row,
				final int column) {
			// DOES NOTHING IN THIS IMPLEMENTATION

		}
	}

	/**
	 * Table model to hold hold the File Hits
	 */
	private class HitListTableModel extends AbstractTableModel {

		private final IdentificationFile idFile;
		private final String[] columnNames;

		public HitListTableModel(final IdentificationFile idFile) {
			this.idFile = idFile;
			this.columnNames = new String[] { "PUID", "MIME", "Format",
					"Version", "Status", "Warning" };
		}

		/**
		 * Get the Class for a column
		 * 
		 * @param col
		 *            column to find class
		 * @return Class type found
		 */
		public Class getColumnClass(final int col) {
			try {
				return getValueAt(0, col).getClass();
			} catch (final NullPointerException e) {
				return "".getClass();
			}
		}

		/**
		 * Returns number of columns in the table
		 * PUID,Format,Status,Version,Warnings
		 * 
		 * @return number of columns in table
		 */
		public int getColumnCount() {
			return this.columnNames.length;
		}

		public String getColumnName(final int column) {
			return this.columnNames[column];
		}

		/**
		 * Gets the number of rows displayed in the table
		 * 
		 * @return Number of files held by the filelist
		 */
		public int getRowCount() {
			return this.idFile.getNumHits();
		}

		// public String getColumnName(int column) {return columnNames[column];}

		/**
		 * Gets the object for a specified cell
		 * 
		 * @param row
		 *            row id
		 * @param col
		 *            column id
		 * @return object to display in cell
		 */
		public Object getValueAt(final int row, final int col) {

			final FileFormatHit hit = this.idFile.getHit(row);

			switch (col) {
			case 0:
				return hit.getFileFormatPUID();
			case 1:
				return hit.getMimeType();
			case 2:
				return hit.getFileFormatName();
			case 3:
				return hit.getFileFormatVersion();
			case 4:
				return hit.getHitTypeVerbose();
			case 5:
				return hit.getHitWarning();

			}
			return "Some Value row: " + row + " col: " + col;
		}

		/**
		 * Determines wether table cell is editable ALWAYS returns false
		 * 
		 * @param row
		 *            Row of cell
		 * @param col
		 *            Column of cell
		 * @return true if cell editable , false otherwise
		 */
		public boolean isCellEditable(final int row, final int col) {

			return false;
		}

		/**
		 * Sets the value for a specific cell DOES NOTHING IN THIS
		 * IMPLEMENTATION
		 * 
		 * @param aValue
		 *            Object to set in cell
		 * @param row
		 *            Row of cell to enter object
		 * @param column
		 *            Column of cell to enter object
		 */
		public void setValueAt(final Object aValue, final int row,
				final int column) {
			// data[row][column] = aValue;

		}
	}

	/**
	 * Renderer for the file list header to highlight sort by column
	 */
	private class IconCellRenderer extends javax.swing.JLabel implements
			TableCellRenderer {

		public void firePropertyChange(final String propertyName,
				final boolean oldValue, final boolean newValue) {
		}

		/**
		 * This method is called each time a cell in a column using this
		 * renderer needs to be rendered.
		 * 
		 * @param table
		 *            Table whos cells are to be rendered
		 * @param value
		 *            object in cell to be rendered
		 * @param isSelected
		 *            Is the cell selected
		 * @param hasFocus
		 *            Does the cell have focus
		 * @param rowIndex
		 *            Cells Row position
		 * @param vColIndex
		 *            Cells column position
		 */
		public java.awt.Component getTableCellRendererComponent(
				final javax.swing.JTable table, final Object value,
				final boolean isSelected, final boolean hasFocus,
				final int rowIndex, final int vColIndex) {
			// 'value' is value contained in the cell located at
			// (rowIndex, vColIndex)

			if (isSelected) {
				this.setOpaque(true);
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());

			} else {
				super.setForeground(table.getForeground());
				super.setBackground(table.getBackground());
			}

			if (hasFocus) {
				// this cell is the anchor and the table has the focus
			}

			setIcon((javax.swing.ImageIcon) value);
			this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

			final IdentificationFile idFile = FileIdentificationPane.this.analysisControl
					.getFile(selectedRowToFileIndex(rowIndex));

			String toolTipText = idFile.getClassificationText();
			if (!idFile.getWarning().equals("")) {
				toolTipText += " (" + idFile.getWarning() + ")";
			}

			toolTipText += " " + idFile.getFilePath();

			// Set tool tip if desired
			setToolTipText(toolTipText);

			// Since the renderer is a component, return itself
			return this;
		}

		public void revalidate() {
		}

		// The following methods override the defaults for performance reasons
		public void validate() {
		}

		protected void firePropertyChange(final String propertyName,
				final Object oldValue, final Object newValue) {
		}
	}

	/**
	 * Displays the GUI
	 * 
	 * @param ac
	 *            Object to perform all application functions
	 */
	public static void launch(final AnalysisController ac) {
		ac.setVerbose(false);
		new FileIdentificationPane(ac);
	}

	/**
	 * Object to peform File Identification analysis
	 */
	private final AnalysisController analysisControl;

	/**
	 * Used for status text delay
	 */
	private javax.swing.Timer timer;

	/**
	 * Used to poll controller when identification run is in progess
	 */
	private javax.swing.Timer identifyTimer;
	/**
	 * Has the fileList been saved
	 */
	private boolean fileListSaved;
	/**
	 * Column to sort file list by
	 */
	private int FileListSortByColumn;

	/**
	 * Flags when identification process is running
	 */
	private boolean identificationRunning;

	/**
	 * List of IdentificationFile indexes
	 */
	private final java.util.List fileList;
	/**
	 * List of ImageIcons used to display file identification status
	 */
	private java.util.List statusIcons;

	/**
	 * Position of file identification status column in file list
	 */
	private final int FILELIST_COL_STATUS = 0;
	/**
	 * Position of file name column in file list
	 */
	private final int FILELIST_COL_FILENAME = 1;

	// Query and cofirm dialog messages

	/**
	 * Position of file identification warning column in file list
	 */
	private final int FILELIST_COL_WARNING = 2;

	/**
	 * Name of tool to be displayed in title bar *
	 */
	private final String APPLICATION_NAME = "DROID (Digital Record Object Identification)";

	/**
	 * File extension to save and open file lists
	 */
	private final String FILE_COLLECTION_FILE_EXTENSTION = "xml";

	/**
	 * File descriptions for open and save dialogs when opening and saving file
	 * lists
	 */
	private final String FILE_COLLECTION_FILE_DESCRIPTION = "DROID file collection(*.xml)";

	/**
	 * File extension for Comma separated value file (CSV) files)
	 */
	private final String CSV_FILE_EXTENSION = "csv";

	/**
	 * File description for Comma separated value file (CSV) files)
	 */
	private final String CSV_FILE_DESCRIPTION = "Comma separated value file (*.csv)";

	/**
	 * Message when opening a saved file list
	 */
	private final String MSG_SAVE_FILE_LIST = "Opening a new file list will cause the current list to be lost.  Do you wish to save this first?";

	/**
	 * Message when attempting to remove all files from list
	 */
	private final String MSG_REMOVE_ALL = "Are you sure you would like to remove ALL the files in the current list?";

	/**
	 * Message when attempting to remove one or more files from list
	 */
	private final String MSG_REMOVE_FILE = "Are you sure you would like to remove the selected file(s)?";

	/**
	 * Message when no files are selected when user attempts to use remove
	 */
	private final String MSG_NOT_REMOVED_FILE = "One or more files must be selected";

	/**
	 * Message when user is selects an exisiting file when saving or exporting
	 */
	private final String MSG_OVERWRITE = "The specified file exists, overwrite?";
	/**
	 * Message when list not saved on exit
	 */
	private final String MSG_EXIT_SAVE_CHECK = "Do you want to save the current file list before exiting?";
	/**
	 * Message to check if a signature file is available for download
	 */
	private final String MSG_CHECK_SIG_FILE_UPDATE = "Would you like to check the web for a newer signature file?";

	/**
	 * Message to download signature file
	 */
	private final String MSG_DOWNLOAD_SIG_FILE = "A newer signature file is available.  Would you like to download it?";

	/**
	 * Message when file list is empty and trying to print
	 */
	private final String MSG_PRINT_FILE_LIST_EMPTY = "One or more files must be in the file list";

	/**
	 * Message when file list is empty and trying to print preview
	 */
	private final String MSG_PRINT_PREVIEW_FILE_LIST_EMPTY = "One or more files must be in the file list";

	/**
	 * Warning box title when file has an Error status
	 */
	private final String TITLE_WARNING_BOX_ERROR = "Error";

	/**
	 * Warning box title when file is has an uidentified status
	 */
	private final String TITLE_WARNING_BOX_NOT_IDENTIFIED = "Unidentified";

	/**
	 * Detailed message for warning box when file is not identified
	 */
	private final String MSG_UNIDENTIFIED = "The format could not be identified";

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonAdd;

	private javax.swing.JButton jButtonCancel;

	private javax.swing.JButton jButtonIdentify;

	private javax.swing.JButton jButtonOpenList;

	private javax.swing.JButton jButtonPrint;

	private javax.swing.JButton jButtonPrintPreview;

	private javax.swing.JButton jButtonRemove;

	private javax.swing.JButton jButtonRemoveAll;

	private javax.swing.JButton jButtonSaveResults;

	private javax.swing.JCheckBoxMenuItem jCheckBoxShowFilePaths;

	private javax.swing.JLabel jLabelFileName;

	private javax.swing.JMenuBar jMenuBar1;

	private javax.swing.JMenu jMenuEdit;

	private javax.swing.JMenu jMenuFile;

	private javax.swing.JMenu jMenuHelp;

	private javax.swing.JMenu jMenuIdentify;

	private javax.swing.JMenuItem jMenuItemAdd;

	private javax.swing.JMenuItem jMenuItemStats;

	private javax.swing.JMenuItem jMenuItemCancelidentify;

	private javax.swing.JMenuItem jMenuItemExit;

	private javax.swing.JMenuItem jMenuItemExportCSV;

	private javax.swing.JMenuItem jMenuItemHelpAbout;

	private javax.swing.JMenuItem jMenuItemHelpContents;

	private javax.swing.JMenuItem jMenuItemIdentify;

	private javax.swing.JMenuItem jMenuItemOpenList;

	private javax.swing.JMenuItem jMenuItemOptions;

	private javax.swing.JMenuItem jMenuItemPrint;

	private javax.swing.JMenuItem jMenuItemPrintPreview;

	private javax.swing.JMenuItem jMenuItemRemove;

	private javax.swing.JMenuItem jMenuItemRemoveAll;

	private javax.swing.JMenuItem jMenuItemSaveResults;

	private javax.swing.JMenu jMenuTools;

	private javax.swing.JPanel jPanelActionsAndHits;

	private javax.swing.JPanel jPanelAddRemoveButtons;

	private javax.swing.JPanel jPanelButtons;

	private javax.swing.JPanel jPanelButtonsAndProgress;

	private javax.swing.JPanel jPanelFileDetails;

	private javax.swing.JPanel jPanelFileIdentification;

	private javax.swing.JPanel jPanelFileList;

	private javax.swing.JPanel jPanelHitFileDetails;

	private javax.swing.JPanel jPanelIdentificationResults;

	private javax.swing.JPanel jPanelProgress;

	// Button/Menu Click and key events
	// ================================

	private javax.swing.JPanel jPanelStatusBar;

	private javax.swing.JPanel jPanelWarnings;

	private javax.swing.JProgressBar jProgressIdentification;

	private javax.swing.JScrollPane jScrollPaneFileList;

	private javax.swing.JScrollPane jScrollPaneHitList;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JSeparator jSeparator3;

	private javax.swing.JLabel jStatus;

	private javax.swing.JTable jTableFileList;

	private HyperLinkTable jTableHitList;

	private javax.swing.JTextField jTextFieldSelectedFile;

	private javax.swing.JTextPane jTextPaneNoIDMessage;

	private javax.swing.JToolBar jToolBar1;

	// End of variables declaration//GEN-END:variables

	/**
	 * Creates new form FileIdentificationPane
	 * 
	 * @param ac
	 *            Analysis controller object that provided functionality
	 */
	private FileIdentificationPane(final AnalysisController ac) {

		this.fileListSaved = false; // Default as false
		this.identificationRunning = false; // Default as false
		this.fileList = new java.util.ArrayList();
		this.analysisControl = ac; // Initialise the controller object
		this.FileListSortByColumn = this.FILELIST_COL_FILENAME;
		this.fileListSaved = true;

		setStatusIconList();

		setCustomLookAndFeel();
		initComponents();

		// Set the icon for the window
		try {
			this
					.setIconImage(javax.imageio.ImageIO
							.read(getClass()
									.getResource(
											"/uk/gov/nationalarchives/droid/GUI/Icons/DROID16.gif")));
		} catch (final java.io.IOException e) {
			// Silently ignore exception
		}

		// Set "Add files" as the default button
		this.jPanelFileIdentification.getRootPane().setDefaultButton(
				this.jButtonAdd);

		setMnemonics();
		this.setTitle(this.APPLICATION_NAME);

		pack(); // display form

		refreshFileList();

		// Set the column width for the jTables in the form
		setTableColumnWidths();

		// Set file list event listeners
		setFileListListener();
		setFileListHeaderListner();
		// Set the fil list header renderer
		setFileListHeaderRenderer();
		setFileListCellRenderers();

		MessageDisplay.initialiseMainPane(this);

		// draw a splash window
		final java.awt.Frame f = javax.swing.JOptionPane
				.getFrameForComponent(this);
		final SplashWindow mySplish = new SplashWindow(
				"/uk/gov/nationalarchives/droid/GUI/Icons/splash_image.gif", f);

		// Initialise Config file
		try {
			this.analysisControl.readConfiguration();
			final String theSigFileName = this.analysisControl
					.getSignatureFileName();
			this.analysisControl.readSigFile(theSigFileName, true, true);
			this.analysisControl.checkSignatureFile();
		} catch (final Exception e) {
			javax.swing.JOptionPane.showMessageDialog(this, e.toString());
		}

		setFocusable(true);
		requestFocus();

		// Centre on screen
		this.setLocationRelativeTo(null);

		// Show the form
		this.setVisible(true);

		// set config parameters on the results table
		// for linking to pronom web site
		this.jTableHitList.setbaseURL(ac.getPuidResolutionURL());
		this.jTableHitList.setBrowserPath(ac.getBrowserPath());

		setKeyListeners();

		// hide splash window
		mySplish.endSplash();

		// Check if signature file update is due
		sigFileDownloadDue();
	}

	/**
	 * Specify the text displayed on the status bar
	 * 
	 * @param statusText
	 *            Text to display
	 */
	public void setStatusText(final String statusText) {
		this.jStatus.setText(statusText);
	}

	/**
	 * Perform commands on keypress BACKSPACE --> Calls "Remove All" files
	 * function
	 * 
	 * @param e
	 *            KeyEvent
	 */
	private void actionOnKeyPress(final java.awt.event.KeyEvent e) {
		// BACKSPACE KeyCode = 8
		if (e.getKeyCode() == 8) {
			newFileList();
		}
	}

	/**
	 * Opens File Selection dialog when user chooses to select files in modal
	 * view
	 */
	private void addFiles() {

		// Doesn't allow add files dialog to open if identification process is
		// running
		if (this.identificationRunning) {
			JOptionPane.showMessageDialog(this,
					"Cannot add files while identication process is running",
					"Cannot Add files", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// Set cursor to wait (Egg timer)
		this.setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		// Get the number of files currently in the collection
		final int fileCountBefore = this.analysisControl.getNumFiles();

		// Show the Add files selction dialog
		final FileSelectReturnParameter returnObj = FileSelectDialog
				.showDialog(this);

		// If add was selected then add files to analysis object
		if (returnObj.getAction() == FileSelectDialog.ACTION_ADD) {

			this.setStatusText("Adding files");

			// Run in worker thread as this may a considerable amount of time
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					// DEBUG System.out.print("DEBUG Paths selected:" +
					// returnObj.getPaths().length ) ;
					// Add files to collection for each path selected
					for (int n = 0; n < returnObj.getPaths().length; n++) {
						FileIdentificationPane.this.analysisControl.addFile(
								returnObj.getPaths()[n], returnObj
										.isRecursive());
					}
					// Set the status to the number of files that were added
					setStatusText("");
					setStatusText((FileIdentificationPane.this.analysisControl
							.getNumFiles() - fileCountBefore)
							+ " files added", 3000);
					// refresh the file list
					refreshFileList();
					// Set the cursor back to default
					setCursor(null);
					// File list has been changed so file list needs to be saved
					FileIdentificationPane.this.fileListSaved = false;
					return "";
				}

			};

			// Start the thread
			worker.start();

		} else {
			this.setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
			; // Set the cursor to default
		}

	}

	/**
	 * Alerts user that analysis has finished
	 * 
	 * @param message
	 *            message to tell user
	 * @param title
	 *            title of message
	 */
	private void analysisFinishedMessage(final String message,
			final String title) {
		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Cancels the identification process Should only cancel if a run is
	 * actually taking place
	 */
	private void cancelIdentifyFiles() {
		this.analysisControl.cancelAnalysis();
	}

	/**
	 * Checks whether a new signature file download is due
	 */
	private void checkSigFileAndDownload() {
		// Message for confirm dialog
		final String confirmMessage = this.MSG_DOWNLOAD_SIG_FILE;

		// Set cursor to wait
		this.setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		// Show in status bar that sig file update search is taking place
		setStatusText("Checking for signature file update...");

		// Check if newer sig file available
		if (this.analysisControl.isNewerSigFileAvailable()) {

			// Reset status bar text and mouse cursor
			setStatusText("");
			this.setCursor(null);

			// Confirm with user whether they would like to download new
			// signature file
			final int confirmValue = JOptionPane.showConfirmDialog(this,
					confirmMessage, "Signature file update available",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (confirmValue == JOptionPane.YES_OPTION) {
				// if they confirm they would like to then
				// download
				// Set cursor to wait
				this.setCursor(java.awt.Cursor
						.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
				// Show in status bar that sig file update search is taking
				// place
				setStatusText("Downloading signature file update...");
				this.analysisControl.downloadwwwSigFile();
				this.setCursor(null);

			}
		} else if (PronomWebService.isCommSuccess) {
			// Newer sig file not found
			// Reset status text and cursor
			setStatusText("");
			setStatusText("No updates found for signature file", 2000);
			this.setCursor(null);
			// Update the DateLastDownload in the configuration file - this is
			// so that the user is not asked to check signature file until
			// another "DownloadFrequency" days have elapsed
			this.analysisControl.updateDateLastDownload();
			// Tell user newer sig file not found
			JOptionPane.showMessageDialog(this,
					"Your signature file is up to date.",
					"No update available", JOptionPane.INFORMATION_MESSAGE);
		} else {
			// failed to connect to web service

			// Reset status text and cursor
			setStatusText("");
			setStatusText("Error connecting to web service", 2000);
			this.setCursor(null);
			// Message to warn user
			String failureMessage = "Unable to connect to the PRONOM web service. Make sure that the following settings in your configuration file (DROID_config.xml) are correct:\n";
			failureMessage += "    1- <SigFileURL> is the URL of the PRONOM web service.  This should be '"
					+ AnalysisController.PRONOM_WEB_SERVICE_URL + "'\n";
			failureMessage += "    2- <ProxyHost> is the IP address of the proxy server if one is required\n";
			failureMessage += "    3- <ProxyPort> is the port to use on the proxy server if one is required";
			// Warn the user that the connection failed
			javax.swing.JOptionPane.showMessageDialog(this, failureMessage,
					"Web service connection error",
					javax.swing.JOptionPane.WARNING_MESSAGE);

		}
	}

	/**
	 * Enables/Disables Cancel button and file menu item depending on whether
	 * analysis is running
	 */
	private void enableCancelIdentify() {
		final boolean enableCancel = this.identificationRunning;

		this.jButtonCancel.setEnabled(enableCancel);
		this.jMenuItemCancelidentify.setEnabled(enableCancel);
	}

	/**
	 * Enables/Disables identify button and file menu item depending whether
	 * there are files to identify
	 */
	private void enableIdentifyActions() {
		final boolean enableIdentify = (this.analysisControl.getNumFiles() > this.analysisControl
				.getNumCompletedFiles())
				&& !this.identificationRunning;

		this.jButtonIdentify.setEnabled(enableIdentify);
		this.jMenuItemIdentify.setEnabled(enableIdentify);
	}

	/**
	 * Remove and Remove all buttons and menu items enabled if the file list is
	 * not empty.If it is empty buttons are disabled
	 */
	private void enableRemoveActions() {

		// Flag to allow remove buttons and menu item
		final boolean allowRemove = (this.analysisControl.getNumFiles() > 0);

		// Set menu items
		this.jMenuItemRemove.setEnabled(allowRemove);
		this.jMenuItemRemoveAll.setEnabled(allowRemove);

		// Set buttons
		this.jButtonRemove.setEnabled(allowRemove);
		this.jButtonRemoveAll.setEnabled(allowRemove);
	}

	/**
	 * Exits form but throws up dialog if file list hasn't been saved
	 */
	private void exitAndCheckSave() {
		// Check if file list not saved before exiting
		if (!this.fileListSaved) {
			// If not saved ask user if they would like to save
			final int returnval = javax.swing.JOptionPane.showConfirmDialog(
					this, this.MSG_EXIT_SAVE_CHECK, "File list not saved",
					javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
			switch (returnval) {
			case javax.swing.JOptionPane.YES_OPTION:
				// If YES save results but don't exit
				saveResults();
				break;
			case javax.swing.JOptionPane.NO_OPTION:
				// If no , just exit
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:
				// if cancel , don't save and don't exit
				break;

			}

		} else {
			// Exit if file list is saved
			System.exit(0);
		}
	}

	/**
	 * Exit the Application
	 */
	private void exitForm(final java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		exitAndCheckSave();

	}// GEN-LAST:event_exitForm

	/**
	 * Export the file list to a CSV file
	 */
	private void exportFileListAsCSV() {
		java.io.File path = null;

		// Setup save file dialog
		final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		fc.addChoosableFileFilter(new CustomFileFilter(this.CSV_FILE_EXTENSION,
				this.CSV_FILE_DESCRIPTION));
		fc.setAcceptAllFileFilterUsed(false);

		// Set dialog title to same as menu item text
		fc.setDialogTitle(this.jMenuItemExportCSV.getText());

		// show file dialog
		final int returnVal = fc.showSaveDialog(this);

		// Save file if user has chosen a file
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

			// Get file user selected
			path = fc.getSelectedFile();

			// if no extension was specified add one
			if (!(path.getName().endsWith("." + this.CSV_FILE_EXTENSION))) {
				path = new java.io.File(path.getParentFile(), path.getName()
						+ "." + this.CSV_FILE_EXTENSION);
			}

			// if path exists check confirm with user if they want to overwrite
			if (path.exists()) {
				final int option = javax.swing.JOptionPane.showConfirmDialog(
						this, this.MSG_OVERWRITE);
				if (option != javax.swing.JOptionPane.YES_OPTION) {
					return;
				}
			}

			// Get filepath of selected file
			final String selectedFilePath = path.getPath();

			// Show in status bar that saving is taking place
			setStatusText("Exporting as CSV... " + selectedFilePath);

			// Save file
			this.analysisControl.exportFileCollectionAsCSV(selectedFilePath);
			// Show in status file has been saved
			setStatusText("Exported As CSV to " + selectedFilePath);

		}

	}

	/**
	 * Gets the status icon(for the file list) to display for a given file
	 * classification
	 * 
	 * @param fileClassfication
	 *            File Classification (see
	 *            uk.AnalysisController.FILE_CLASSIFICATION_[X], where X is a
	 *            classsification type)
	 * @return icon corresponding to given file classification
	 */
	private javax.swing.ImageIcon getStatusIcon(final int fileClassfication) {

		switch (fileClassfication) {
		case AnalysisController.FILE_CLASSIFICATION_ERROR:
			return (javax.swing.ImageIcon) this.statusIcons.get(3);
		case AnalysisController.FILE_CLASSIFICATION_NOHIT:
			return (javax.swing.ImageIcon) this.statusIcons.get(0);
		case AnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED:
			return null;
		case AnalysisController.FILE_CLASSIFICATION_TENTATIVE:
			return (javax.swing.ImageIcon) this.statusIcons.get(1);
		case AnalysisController.FILE_CLASSIFICATION_POSITIVE:
			return (javax.swing.ImageIcon) this.statusIcons.get(2);
		default:
			return null;
		}

	}

	/**
	 * Gets the text displayed in the status bar
	 * 
	 * @return text in status bar
	 */
	private String getStatusText() {
		return this.jStatus.getText();
	}

	/**
	 * Run the identification process on the files in the list only runs when a
	 * run is not taking place
	 */
	private void identifyFiles() {
		if (!this.identificationRunning) {
			this.analysisControl.runFileFormatAnalysis();
			pollController();
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		this.jToolBar1 = new javax.swing.JToolBar();
		this.jButtonOpenList = new javax.swing.JButton();
		this.jButtonSaveResults = new javax.swing.JButton();
		this.jButtonPrint = new javax.swing.JButton();
		this.jButtonPrintPreview = new javax.swing.JButton();
		this.jPanelFileIdentification = new javax.swing.JPanel();
		this.jPanelFileList = new javax.swing.JPanel();
		this.jPanelAddRemoveButtons = new javax.swing.JPanel();
		this.jButtonAdd = new javax.swing.JButton();
		this.jButtonRemove = new javax.swing.JButton();
		this.jButtonRemoveAll = new javax.swing.JButton();
		this.jScrollPaneFileList = new javax.swing.JScrollPane();
		this.jTableFileList = new javax.swing.JTable();
		this.jPanelActionsAndHits = new javax.swing.JPanel();
		this.jPanelButtonsAndProgress = new javax.swing.JPanel();
		this.jPanelButtons = new javax.swing.JPanel();
		this.jButtonIdentify = new javax.swing.JButton();
		this.jButtonCancel = new javax.swing.JButton();
		this.jPanelProgress = new javax.swing.JPanel();
		this.jProgressIdentification = new javax.swing.JProgressBar();
		this.jPanelFileDetails = new javax.swing.JPanel();
		this.jPanelHitFileDetails = new javax.swing.JPanel();
		this.jLabelFileName = new javax.swing.JLabel();
		this.jTextFieldSelectedFile = new javax.swing.JTextField();
		this.jPanelIdentificationResults = new javax.swing.JPanel();
		this.jScrollPaneHitList = new javax.swing.JScrollPane();
		this.jTableHitList = new HyperLinkTable(this.analysisControl
				.getPuidResolutionURL());
		this.jPanelWarnings = new javax.swing.JPanel();
		this.jTextPaneNoIDMessage = new javax.swing.JTextPane();
		this.jPanelStatusBar = new javax.swing.JPanel();
		this.jStatus = new javax.swing.JLabel();
		this.jMenuBar1 = new javax.swing.JMenuBar();
		this.jMenuFile = new javax.swing.JMenu();
		this.jMenuItemStats = new javax.swing.JMenuItem();
		this.jMenuItemOpenList = new javax.swing.JMenuItem();
		this.jSeparator1 = new javax.swing.JSeparator();
		this.jMenuItemSaveResults = new javax.swing.JMenuItem();
		this.jMenuItemExportCSV = new javax.swing.JMenuItem();
		this.jSeparator2 = new javax.swing.JSeparator();
		this.jMenuItemPrintPreview = new javax.swing.JMenuItem();
		this.jMenuItemPrint = new javax.swing.JMenuItem();
		this.jSeparator3 = new javax.swing.JSeparator();
		this.jMenuItemExit = new javax.swing.JMenuItem();
		this.jMenuEdit = new javax.swing.JMenu();
		this.jMenuItemAdd = new javax.swing.JMenuItem();
		this.jMenuItemRemove = new javax.swing.JMenuItem();
		this.jMenuItemRemoveAll = new javax.swing.JMenuItem();
		this.jMenuIdentify = new javax.swing.JMenu();
		this.jMenuItemIdentify = new javax.swing.JMenuItem();
		this.jMenuItemCancelidentify = new javax.swing.JMenuItem();
		this.jMenuTools = new javax.swing.JMenu();
		this.jMenuItemOptions = new javax.swing.JMenuItem();
		this.jCheckBoxShowFilePaths = new javax.swing.JCheckBoxMenuItem();
		this.jMenuHelp = new javax.swing.JMenu();
		this.jMenuItemHelpContents = new javax.swing.JMenuItem();
		this.jMenuItemHelpAbout = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("File Format Identification Tool");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(final java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		this.jToolBar1.setBorder(new javax.swing.border.EtchedBorder());
		this.jToolBar1.setFloatable(false);
		this.jToolBar1.setRollover(true);
		this.jButtonOpenList
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 24 n g.gif")));
		this.jButtonOpenList.setToolTipText("Open List...");
		this.jButtonOpenList
				.setDisabledSelectedIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 24 d g.gif")));
		this.jButtonOpenList
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 24 h g.gif")));
		this.jButtonOpenList
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonOpenListActionPerformed(evt);
					}
				});
		this.jButtonOpenList
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(final java.awt.event.MouseEvent evt) {
						jButtonOpenListMouseEntered(evt);
					}
				});

		this.jToolBar1.add(this.jButtonOpenList);

		this.jButtonSaveResults
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 24 n g.gif")));
		this.jButtonSaveResults.setToolTipText("Save List...");
		this.jButtonSaveResults
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 24 d g.gif")));
		this.jButtonSaveResults
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 24 h g.gif")));
		this.jButtonSaveResults
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonSaveResultsActionPerformed(evt);
					}
				});

		this.jToolBar1.add(this.jButtonSaveResults);

		this.jButtonPrint
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 24 n g.gif")));
		this.jButtonPrint.setToolTipText("Print");
		this.jButtonPrint
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 16 d g.gif")));
		this.jButtonPrint
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 24 h g.gif")));
		this.jButtonPrint
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonPrintActionPerformed(evt);
					}
				});

		this.jToolBar1.add(this.jButtonPrint);

		this.jButtonPrintPreview
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 24 n g.gif")));
		this.jButtonPrintPreview.setToolTipText("Print Preview");
		this.jButtonPrintPreview
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 24 d g.gif")));
		this.jButtonPrintPreview
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 24 h g.gif")));
		this.jButtonPrintPreview
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonPrintPreviewActionPerformed(evt);
					}
				});

		this.jToolBar1.add(this.jButtonPrintPreview);

		getContentPane().add(this.jToolBar1, java.awt.BorderLayout.NORTH);

		this.jPanelFileIdentification.setLayout(new java.awt.BorderLayout());

		this.jPanelFileIdentification.setMinimumSize(new java.awt.Dimension(
				600, 450));
		this.jPanelFileIdentification.setPreferredSize(new java.awt.Dimension(
				750, 400));
		this.jPanelFileIdentification.setRequestFocusEnabled(false);
		this.jPanelFileList.setLayout(new java.awt.BorderLayout());

		this.jPanelFileList.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(), "File list"),
				new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 6, 1,
						6))));
		this.jPanelFileList.setMinimumSize(new java.awt.Dimension(500, 40));
		this.jPanelFileList.setPreferredSize(new java.awt.Dimension(572, 307));
		this.jButtonAdd.setText("Add Files");
		this.jButtonAdd.setToolTipText("Add Files");
		this.jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButtonAddActionPerformed(evt);
			}
		});

		this.jPanelAddRemoveButtons.add(this.jButtonAdd);

		this.jButtonRemove.setText("Remove Files");
		this.jButtonRemove.setToolTipText("Remove Files");
		this.jButtonRemove.setEnabled(false);
		this.jButtonRemove
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonRemoveActionPerformed(evt);
					}
				});

		this.jPanelAddRemoveButtons.add(this.jButtonRemove);

		this.jButtonRemoveAll.setText("Remove All");
		this.jButtonRemoveAll.setToolTipText("Remove All");
		this.jButtonRemoveAll.setEnabled(false);
		this.jButtonRemoveAll
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonRemoveAllActionPerformed(evt);
					}
				});

		this.jPanelAddRemoveButtons.add(this.jButtonRemoveAll);

		this.jPanelFileList.add(this.jPanelAddRemoveButtons,
				java.awt.BorderLayout.SOUTH);

		this.jScrollPaneFileList.setBorder(null);
		this.jScrollPaneFileList
				.setMaximumSize(new java.awt.Dimension(550, 300));
		this.jScrollPaneFileList.setPreferredSize(new java.awt.Dimension(550,
				235));
		// Set background to white
		this.jScrollPaneFileList.getViewport().setBackground(
				java.awt.Color.WHITE);
		this.jTableFileList.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {

				}, new String[] { "Status", "File" }) {
			boolean[] canEdit = new boolean[] { false, false };

			public boolean isCellEditable(final int rowIndex,
					final int columnIndex) {
				return this.canEdit[columnIndex];
			}
		});
		this.jTableFileList.setAutoCreateColumnsFromModel(false);
		this.jTableFileList.getTableHeader().setReorderingAllowed(false);
		this.jTableFileList.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(final java.awt.event.KeyEvent evt) {
				jTableFileListKeyPressed(evt);
			}
		});

		this.jScrollPaneFileList.setViewportView(this.jTableFileList);

		this.jPanelFileList.add(this.jScrollPaneFileList,
				java.awt.BorderLayout.CENTER);

		this.jPanelFileIdentification.add(this.jPanelFileList,
				java.awt.BorderLayout.CENTER);

		this.jPanelActionsAndHits.setLayout(new java.awt.BorderLayout());

		this.jPanelButtonsAndProgress.setLayout(new java.awt.BorderLayout());

		this.jButtonIdentify
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 n g.gif")));
		this.jButtonIdentify.setText("Identify");
		this.jButtonIdentify.setToolTipText("Identify");
		this.jButtonIdentify
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 d g.gif")));
		this.jButtonIdentify
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 h g.gif")));
		this.jButtonIdentify.setEnabled(false);
		this.jButtonIdentify
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonIdentifyActionPerformed(evt);
					}
				});

		this.jPanelButtons.add(this.jButtonIdentify);

		this.jButtonCancel
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 n g.gif")));
		this.jButtonCancel.setText("Cancel");
		this.jButtonCancel.setToolTipText("Cancel");
		this.jButtonCancel
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 d g.gif")));
		this.jButtonCancel
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 h g.gif")));
		this.jButtonCancel.setEnabled(false);
		this.jButtonCancel
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jButtonCancelActionPerformed(evt);
					}
				});

		this.jPanelButtons.add(this.jButtonCancel);

		this.jPanelButtonsAndProgress.add(this.jPanelButtons,
				java.awt.BorderLayout.NORTH);

		this.jProgressIdentification.setMinimumSize(new java.awt.Dimension(50,
				18));
		this.jProgressIdentification.setPreferredSize(new java.awt.Dimension(
				300, 18));
		this.jProgressIdentification.setString("");
		this.jProgressIdentification.setStringPainted(true);
		this.jPanelProgress.add(this.jProgressIdentification);

		this.jPanelButtonsAndProgress.add(this.jPanelProgress,
				java.awt.BorderLayout.CENTER);

		this.jPanelActionsAndHits.add(this.jPanelButtonsAndProgress,
				java.awt.BorderLayout.NORTH);

		this.jPanelFileDetails.setLayout(new java.awt.BorderLayout());

		this.jPanelFileDetails.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						"Identification results"),
				new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 6, 6,
						6))));
		this.jPanelFileDetails.setName("File details");
		this.jPanelFileDetails
				.setPreferredSize(new java.awt.Dimension(10, 150));
		this.jPanelFileDetails.setRequestFocusEnabled(false);
		this.jPanelHitFileDetails.setLayout(new javax.swing.BoxLayout(
				this.jPanelHitFileDetails, javax.swing.BoxLayout.X_AXIS));

		this.jLabelFileName.setText("  File  ");
		this.jLabelFileName
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		this.jPanelHitFileDetails.add(this.jLabelFileName);

		this.jTextFieldSelectedFile.setEditable(false);
		this.jTextFieldSelectedFile
				.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		this.jTextFieldSelectedFile
				.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.EmptyBorder(new java.awt.Insets(
								3, 3, 3, 3)),
						new javax.swing.border.LineBorder(new java.awt.Color(
								102, 102, 102))));
		this.jPanelHitFileDetails.add(this.jTextFieldSelectedFile);

		this.jPanelFileDetails.add(this.jPanelHitFileDetails,
				java.awt.BorderLayout.NORTH);

		this.jPanelIdentificationResults.setLayout(new java.awt.CardLayout());

		this.jPanelIdentificationResults
				.setPreferredSize(new java.awt.Dimension(550, 85));
		this.jScrollPaneHitList.setMaximumSize(new java.awt.Dimension(32767,
				15000));
		this.jScrollPaneHitList
				.setPreferredSize(new java.awt.Dimension(550, 85));
		// Set background to white
		this.jScrollPaneHitList.getViewport().setBackground(
				java.awt.Color.WHITE);
		this.jTableHitList.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {

				}, new String[] { "PUID", "MIME", "Format", "Version",
						"Status", "Warning" }) {
			boolean[] canEdit = new boolean[] { false, false, false, false,
					false, false };

			public boolean isCellEditable(final int rowIndex,
					final int columnIndex) {
				return this.canEdit[columnIndex];
			}
		});
		this.jTableHitList.setAutoCreateColumnsFromModel(false);
		this.jTableHitList.getTableHeader().setReorderingAllowed(false);
		this.jScrollPaneHitList.setViewportView(this.jTableHitList);

		this.jPanelIdentificationResults.add(this.jScrollPaneHitList,
				"cardResults");

		this.jPanelWarnings.setLayout(new java.awt.GridLayout(1, 0));

		this.jPanelWarnings.setBorder(new javax.swing.border.TitledBorder(
				"Error"));
		this.jTextPaneNoIDMessage.setBorder(null);
		this.jTextPaneNoIDMessage.setEditable(false);
		this.jPanelWarnings.add(this.jTextPaneNoIDMessage);

		this.jPanelIdentificationResults.add(this.jPanelWarnings,
				"cardWarnings");

		this.jPanelFileDetails.add(this.jPanelIdentificationResults,
				java.awt.BorderLayout.SOUTH);

		this.jPanelActionsAndHits.add(this.jPanelFileDetails,
				java.awt.BorderLayout.CENTER);

		this.jPanelFileIdentification.add(this.jPanelActionsAndHits,
				java.awt.BorderLayout.SOUTH);

		getContentPane().add(this.jPanelFileIdentification,
				java.awt.BorderLayout.CENTER);

		this.jPanelStatusBar.setLayout(new java.awt.GridLayout(1, 0));

		this.jStatus.setMaximumSize(new java.awt.Dimension(100, 20));
		this.jStatus.setMinimumSize(new java.awt.Dimension(0, 10));
		this.jStatus.setPreferredSize(new java.awt.Dimension(100, 20));
		this.jStatus.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		this.jPanelStatusBar.add(this.jStatus);

		getContentPane().add(this.jPanelStatusBar, java.awt.BorderLayout.SOUTH);

		this.jMenuBar1.setBorder(null);
		this.jMenuFile.setText("File");
		this.jMenuItemOpenList
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 16 n g.gif")));
		this.jMenuItemOpenList.setText("Open List...");
		this.jMenuItemOpenList
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 16 d g.gif")));
		this.jMenuItemOpenList
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Open File or Folder 16 h g.gif")));
		this.jMenuItemOpenList
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemOpenListActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemOpenList);

		this.jMenuFile.add(this.jSeparator1);

		this.jMenuItemStats.setText("Generate profile");
		this.jMenuFile.add(this.jMenuItemStats);
		this.jMenuItemStats
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemStatsActionPerformed(evt);
					}
				});

		this.jMenuFile.add(new javax.swing.JSeparator());

		this.jMenuItemSaveResults
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 16 n g.gif")));
		this.jMenuItemSaveResults.setText("Save List...");
		this.jMenuItemSaveResults
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 16 d g.gif")));
		this.jMenuItemSaveResults
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Save Blue 16 h g.gif")));
		this.jMenuItemSaveResults
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemSaveResultsActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemSaveResults);

		this.jMenuItemExportCSV.setText("Export to CSV...");
		this.jMenuItemExportCSV
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemExportCSVActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemExportCSV);

		this.jMenuFile.add(this.jSeparator2);

		this.jMenuItemPrintPreview
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 16 n g.gif")));
		this.jMenuItemPrintPreview.setText("Print Preview");
		this.jMenuItemPrintPreview
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 16 d g.gif")));
		this.jMenuItemPrintPreview
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Print Preview 16 h g.gif")));
		this.jMenuItemPrintPreview
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemPrintPreviewActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemPrintPreview);

		this.jMenuItemPrint
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 16 n g.gif")));
		this.jMenuItemPrint.setText("Print...");
		this.jMenuItemPrint
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 16 d g.gif")));
		this.jMenuItemPrint
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Printer 16 h g.gif")));
		this.jMenuItemPrint
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemPrintActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemPrint);

		this.jMenuFile.add(this.jSeparator3);

		this.jMenuItemExit.setText("Exit");
		this.jMenuItemExit
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemExitActionPerformed(evt);
					}
				});

		this.jMenuFile.add(this.jMenuItemExit);

		this.jMenuBar1.add(this.jMenuFile);

		this.jMenuEdit.setText("Edit");
		this.jMenuItemAdd
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Add Document 2 16 n g.gif")));
		this.jMenuItemAdd.setText("Add Files\u2026");
		this.jMenuItemAdd
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Add Document 2 16 d g.gif")));
		this.jMenuItemAdd
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Add Document 2 16 h g.gif")));
		this.jMenuItemAdd
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemAddActionPerformed(evt);
					}
				});

		this.jMenuEdit.add(this.jMenuItemAdd);

		this.jMenuItemRemove
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Delete Document 2 16 n g.gif")));
		this.jMenuItemRemove.setText("Remove Files");
		this.jMenuItemRemove
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Delete Document 2 16 d g.gif")));
		this.jMenuItemRemove
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Delete Document 2 16 h g.gif")));
		this.jMenuItemRemove
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemRemoveActionPerformed(evt);
					}
				});

		this.jMenuEdit.add(this.jMenuItemRemove);

		this.jMenuItemRemoveAll.setText("Remove All");
		this.jMenuItemRemoveAll
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemRemoveAllActionPerformed(evt);
					}
				});

		this.jMenuEdit.add(this.jMenuItemRemoveAll);

		this.jMenuBar1.add(this.jMenuEdit);

		this.jMenuIdentify.setText("Identify");
		this.jMenuItemIdentify
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 n g.gif")));
		this.jMenuItemIdentify.setText("Identify");
		this.jMenuItemIdentify
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 d g.gif")));
		this.jMenuItemIdentify
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Forward or Next 16 h g.gif")));
		this.jMenuItemIdentify.setEnabled(false);
		this.jMenuItemIdentify
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemIdentifyActionPerformed(evt);
					}
				});

		this.jMenuIdentify.add(this.jMenuItemIdentify);

		this.jMenuItemCancelidentify
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 n g.gif")));
		this.jMenuItemCancelidentify.setText("Cancel");
		this.jMenuItemCancelidentify
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 d g.gif")));
		this.jMenuItemCancelidentify
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Stop Play 16 h g.gif")));
		this.jMenuItemCancelidentify.setEnabled(false);
		this.jMenuItemCancelidentify
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemCancelidentifyActionPerformed(evt);
					}
				});

		this.jMenuIdentify.add(this.jMenuItemCancelidentify);

		this.jMenuBar1.add(this.jMenuIdentify);

		this.jMenuTools.setText("Tools");
		this.jMenuItemOptions
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Cog 2 16 n g.gif")));
		this.jMenuItemOptions.setText("Options");
		this.jMenuItemOptions
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Cog 2 16 d g.gif")));
		this.jMenuItemOptions
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Cog 2 16 h g.gif")));
		this.jMenuItemOptions
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemOptionsActionPerformed(evt);
					}
				});

		this.jMenuTools.add(this.jMenuItemOptions);

		this.jCheckBoxShowFilePaths.setSelected(true);
		this.jCheckBoxShowFilePaths.setText("Show File Paths");
		this.jCheckBoxShowFilePaths
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jCheckBoxShowFilePathsActionPerformed(evt);
					}
				});

		this.jMenuTools.add(this.jCheckBoxShowFilePaths);

		this.jMenuBar1.add(this.jMenuTools);

		this.jMenuHelp.setText("Help");
		this.jMenuItemHelpContents
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Help Green 16 n g.gif")));
		this.jMenuItemHelpContents.setText("DROID Help");
		this.jMenuItemHelpContents
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Help Green 16 d g.gif")));
		this.jMenuItemHelpContents
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Help Green 16 h g.gif")));
		this.jMenuItemHelpContents
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemHelpContentsActionPerformed(evt);
					}
				});

		this.jMenuHelp.add(this.jMenuItemHelpContents);

		this.jMenuItemHelpAbout
				.setIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Info Round Blue 16 n g.gif")));
		this.jMenuItemHelpAbout.setText("About DROID");
		this.jMenuItemHelpAbout
				.setDisabledIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Info Round Blue 16 d g.gif")));
		this.jMenuItemHelpAbout
				.setRolloverIcon(new javax.swing.ImageIcon(
						getClass()
								.getResource(
										"/uk/gov/nationalarchives/droid/GUI/Icons/Info Round Blue 16 h g.gif")));
		this.jMenuItemHelpAbout
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						jMenuItemHelpAboutActionPerformed(evt);
					}
				});

		this.jMenuHelp.add(this.jMenuItemHelpAbout);

		this.jMenuBar1.add(this.jMenuHelp);

		setJMenuBar(this.jMenuBar1);

	}// GEN-END:initComponents

	/**
	 * Opens file selection dialog when Add Files button pressed on Toolbar
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonAddActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddActionPerformed
		addFiles();
	}// GEN-LAST:event_jButtonAddActionPerformed

	/**
	 * Cancel the identification process, if process is running on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonCancelActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCancelActionPerformed
		cancelIdentifyFiles();
	}// GEN-LAST:event_jButtonCancelActionPerformed

	/**
	 *Opens a file dialog to open a file list on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	/**
	 * Intiatiates a file identification run on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonIdentifyActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonIdentifyActionPerformed
		this.identifyFiles();
	}// GEN-LAST:event_jButtonIdentifyActionPerformed

	/**
	 * Open file list on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonOpenListActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOpenListActionPerformed
		openFileList();
	}// GEN-LAST:event_jButtonOpenListActionPerformed

	private void jButtonOpenListMouseEntered(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButtonOpenListMouseEntered
		// nothing
	}// GEN-LAST:event_jButtonOpenListMouseEntered

	/**
	 * Print on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonPrintActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPrintActionPerformed
		printFileList();
	}// GEN-LAST:event_jButtonPrintActionPerformed

	/**
	 * Print preview on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonPrintPreviewActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPrintPreviewActionPerformed
		launchPrintPreview();
	}// GEN-LAST:event_jButtonPrintPreviewActionPerformed

	/**
	 * Remove files on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonRemoveActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveActionPerformed
		removeFiles();
	}// GEN-LAST:event_jButtonRemoveActionPerformed

	/**
	 * Clears the file list on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonRemoveAllActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRemoveAllActionPerformed
		newFileList();
	}// GEN-LAST:event_jButtonRemoveAllActionPerformed

	/**
	 * Save file list with results on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jButtonSaveResultsActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveResultsActionPerformed
		saveResults();
	}// GEN-LAST:event_jButtonSaveResultsActionPerformed

	/**
	 * Toggle show file paths on menu click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jCheckBoxShowFilePathsActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxShowFilePathsActionPerformed
		this.refreshFileList();
	}// GEN-LAST:event_jCheckBoxShowFilePathsActionPerformed

	/**
	 * Opens file selection dialog when Add Files button pressed on Toolbar
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemAddActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemAddActionPerformed
		addFiles();
	}// GEN-LAST:event_jMenuItemAddActionPerformed

	/**
	 * Cancel the identification process, if process is running on menu item
	 * click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemCancelidentifyActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemCancelidentifyActionPerformed
		cancelIdentifyFiles();
	}// GEN-LAST:event_jMenuItemCancelidentifyActionPerformed

	/**
	 * Exit form on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemExitActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemExitActionPerformed
		exitAndCheckSave();
	}// GEN-LAST:event_jMenuItemExitActionPerformed

	/**
	 * Export file list as CSV on menu item event
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemExportCSVActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemExportCSVActionPerformed
		exportFileListAsCSV();
	}// GEN-LAST:event_jMenuItemExportCSVActionPerformed

	/**
	 * Show about box on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemHelpAboutActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemHelpAboutActionPerformed
		showAboutBox();
	}// GEN-LAST:event_jMenuItemHelpAboutActionPerformed

	/**
	 * Opens help on menu item selected
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemHelpContentsActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemHelpContentsActionPerformed
		launchHelp();
	}// GEN-LAST:event_jMenuItemHelpContentsActionPerformed

	/**
	 * Intiatiates a file identification run on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemIdentifyActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemIdentifyActionPerformed
		identifyFiles();
	}// GEN-LAST:event_jMenuItemIdentifyActionPerformed

	/**
	 * Open list on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemOpenListActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemOpenListActionPerformed
		openFileList();
	}// GEN-LAST:event_jMenuItemOpenListActionPerformed

	/**
	 * Opens the Options dialog when selected on menu bar under Tools.
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemOptionsActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemOptionsActionPerformed
		showOptions();
	}// GEN-LAST:event_jMenuItemOptionsActionPerformed

	/**
	 * Print on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemPrintActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemPrintActionPerformed
		printFileList();
	}// GEN-LAST:event_jMenuItemPrintActionPerformed

	/**
	 * Print preview on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemPrintPreviewActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemPrintPreviewActionPerformed
		launchPrintPreview();
	}// GEN-LAST:event_jMenuItemPrintPreviewActionPerformed

	/**
	 * Remove files on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemRemoveActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemRemoveActionPerformed
		removeFiles();
	}// GEN-LAST:event_jMenuItemRemoveActionPerformed

	/**
	 * Removes all files from file list on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemRemoveAllActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemRemoveAllActionPerformed
		newFileList();
	}// GEN-LAST:event_jMenuItemRemoveAllActionPerformed

	/**
	 *Save file list with results on button click
	 * 
	 * @param evt
	 *            Action event object
	 */
	/**
	 * Save file list with results on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemSaveResultsActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSaveResultsActionPerformed
		saveResults();
	}// GEN-LAST:event_jMenuItemSaveResultsActionPerformed

	/**
	 * Generate statistics on menu item click
	 * 
	 * @param evt
	 *            Action event object
	 */
	private void jMenuItemStatsActionPerformed(
			final java.awt.event.ActionEvent evt) {
		openStatsWindow();
	}

	/**
	 * Fires action on any key press on the File list jTable
	 * 
	 * @param evt
	 *            Key event
	 */
	private void jTableFileListKeyPressed(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTableFileListKeyPressed
		actionOnKeyPress(evt);
	}// GEN-LAST:event_jTableFileListKeyPressed

	/**
	 * Launch the help window
	 */
	private void launchHelp() {
		JHelp helpViewer = null;
		boolean foundHelpSet = false;

		// Set cursor to wait (Egg timer)
		this.setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		setStatusText("Loading help set...");

		// Get the classloader of this class.
		final ClassLoader cl = FileIdentificationPane.class.getClassLoader();
		final URL url = getClass().getResource("Help/jhelpset.hs");

		try {

			// Create a new JHelp object with a new HelpSet.
			helpViewer = new JHelp(new HelpSet(cl, url));
			// Set the initial entry point in the table of contents.
			helpViewer.setCurrentID("Simple.Introduction");
			foundHelpSet = true;
		} catch (final Exception e) {
			System.err.println("API Help Set not found");
			System.err.println(e.toString());
			foundHelpSet = false;
			setCursor(null);
			setStatusText("");
			JOptionPane.showMessageDialog(this, "DROID Help set not found at "
					+ url.getPath());
		}

		// If the help set has been found and intialised then show window
		if (foundHelpSet) {
			setStatusText("Lanching help window...");
			// Create a new frame.
			final javax.swing.JFrame frame = new javax.swing.JFrame();

			// Set title same as menu item
			frame.setTitle(this.jMenuItemHelpContents.getText());

			// Set it's size.
			frame.setSize(500, 500);
			// Add the created helpViewer to it.
			frame.getContentPane().add(helpViewer);
			// Set a default close operation.
			frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
			// Set location of help window relative to application window
			frame.setLocationRelativeTo(this);

			try {
				frame
						.setIconImage(javax.imageio.ImageIO
								.read(getClass()
										.getResource(
												"/uk/gov/nationalarchives/droid/GUI/Icons/Help Green 16 h g.gif")));
			} catch (final java.io.IOException e) {
				// silently ignore exception
			}
			// Make the frame visible.
			frame.setVisible(true);

		}

		setStatusText("");
		setCursor(null);
	}

	/**
	 * Show a print preview frame for current file list
	 */
	private void launchPrintPreview() {

		// Only continue if there are files in the file list
		if (this.analysisControl.getNumFiles() < 1) {
			JOptionPane.showMessageDialog(this,
					this.MSG_PRINT_PREVIEW_FILE_LIST_EMPTY,
					"File list is empty", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Set cursor to wait (Egg timer) and put message in status bar
		this.setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		setStatusText("Preparing for print preview...");

		// Launch print preiview pane
		PrintPreview.launchPrintPreview((FileCollection) this.analysisControl
				.getFileCollection(), (java.util.List) this.fileList,
				AnalysisController.getDROIDVersion(), this.analysisControl
						.getSigFileVersion()
						+ "");

		// Reset cursor and status bar text
		setCursor(null);
		setStatusText("");

	}

	/**
	 * Clears the open file list but checks if saved before
	 */
	private void newFileList() {

		// Ask user to confirm they would like to remove all files
		final int returnval = javax.swing.JOptionPane.showConfirmDialog(this,
				this.MSG_REMOVE_ALL, "Remove all",
				javax.swing.JOptionPane.YES_NO_OPTION);

		// If yes
		if (returnval == javax.swing.JOptionPane.YES_OPTION) {
			// Reset file list , progress bar, identification results and
			// application title
			this.analysisControl.resetFileList();
			resetProgressBar();
			refreshFileList();
			showFileHits(-1);
			this.fileListSaved = true;
			this.setTitle(this.APPLICATION_NAME);
		}

	}

	/**
	 * Show open dialog and populate file collection
	 */
	private void openFileList() {

		// Check if file list is saved first
		if (!this.fileListSaved) {
			// If not, ask user whether they would like to save current file
			// list
			final int returnval = javax.swing.JOptionPane.showConfirmDialog(
					this, this.MSG_SAVE_FILE_LIST, "File list not saved",
					javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);

			switch (returnval) {
			case javax.swing.JOptionPane.YES_OPTION:
				// if user wants to save list, then do so and exit from
				// openining a file
				saveFileList();
				return;
			case JOptionPane.CANCEL_OPTION:
				// if user selected cancel, do nothing
				return;

			}

		}

		// User must have clicked cancel to be at this point , so show open file
		// dialog

		// Intialise file chooser dialog
		final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		// Add file filter so only shows XML files
		fc.addChoosableFileFilter(new CustomFileFilter(
				this.FILE_COLLECTION_FILE_EXTENSTION,
				this.FILE_COLLECTION_FILE_DESCRIPTION));
		fc.setAcceptAllFileFilterUsed(false);

		// Sets the dialog title to same as menu item text
		fc.setDialogTitle(this.jMenuItemOpenList.getText());

		// Show the dialog
		final int returnVal = fc.showOpenDialog(this);

		// Decide if user chose OK or Cancel
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
			// User chose ok so open file list

			// Get path selected
			final String selectedFilePath = fc.getSelectedFile().getPath();
			// Set cursor to wait
			this.setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
			// Show in status bar that file is opening
			setStatusText("Loading " + selectedFilePath);

			// Run reading files in a thread , as this may take some time
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					try {
						FileIdentificationPane.this.analysisControl
								.readFileCollection(selectedFilePath);
					} catch (final Exception e) {
						javax.swing.JOptionPane.showMessageDialog(null, e
								.toString());
					}
					// Set status text to number of files in collection
					setStatusText(selectedFilePath
							+ " contains "
							+ FileIdentificationPane.this.analysisControl
									.getNumFiles() + " files");
					// update the file list jTable
					refreshFileList();
					// The file list is saved
					FileIdentificationPane.this.fileListSaved = true;
					// Cursor is set back to default
					setCursor(java.awt.Cursor
							.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
					// File list location appended to titlebar
					setTitle(FileIdentificationPane.this.APPLICATION_NAME
							+ " [" + selectedFilePath + "]");
					return "";

				}
			};

			// Start the thread
			worker.start();

		}
	}

	private void openStatsWindow() {
		final StatsReturnParameter returnObj = StatsDialog.showDialog(this);

		// If add was selected then add files to analysis object
		if (returnObj.getAction() == FileSelectDialog.ACTION_ADD) {

			final StatsThread thread = this.analysisControl.runStatsGathering(
					"", "", returnObj.getPaths(), returnObj.isRecursive());
			/*
			 * //Run in worker thread as this may a considerable amount of time
			 * final SwingWorker worker = new SwingWorker() { public Object
			 * construct() { // Perform stats on each file for (int n = 0; n <
			 * returnObj.getPaths().length; n++) {
			 * 
			 * setStatusText(""); }
			 * 
			 * return ""; }
			 * 
			 * };
			 * 
			 * //Start the thread worker.start();
			 */
			StatsResultDialog.showDialog(this, thread, this.analysisControl);
		} else {
			this.setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
			; // Set the cursor to default
		}
	}

	/**
	 * Polls the controller to refresh file list when identification process is
	 * running
	 */
	private void pollController() {
		// Reset progrss bar
		resetProgressBar();
		// Progress bar set to maximum
		this.jProgressIdentification.setMaximum(this.analysisControl
				.getNumFiles());

		// Set flag the identification process is running
		this.identificationRunning = true;

		// Enable cancel identifcation buttons
		enableCancelIdentify();

		// Every n milliseconds refresh file list and progress bar
		this.identifyTimer = new javax.swing.Timer(200,
				new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {

						FileIdentificationPane.this.fileListSaved = false;

						final int numFiles = FileIdentificationPane.this.analysisControl
								.getNumFiles();
						final int numFilesCompleted = FileIdentificationPane.this.analysisControl
								.getNumCompletedFiles();

						// Update progress bar
						FileIdentificationPane.this.jProgressIdentification
								.setValue(numFilesCompleted);
						FileIdentificationPane.this.jProgressIdentification
								.setString("File " + numFilesCompleted + " of "
										+ numFiles + " analysed");

						// Check if analyis identification process has finished
						// or been cancelled
						if (FileIdentificationPane.this.analysisControl
								.isAnalysisComplete()) {
							// Stop the timer
							FileIdentificationPane.this.identifyTimer.stop();
							FileIdentificationPane.this.identificationRunning = false;
							// Update progress bar
							FileIdentificationPane.this.jProgressIdentification
									.setValue(numFilesCompleted);
							FileIdentificationPane.this.jProgressIdentification
									.setString("File " + numFilesCompleted
											+ " of " + numFiles + " analysed");
							// refresh file list
							refreshFileList();
							// Disable cancel identify buttons
							enableCancelIdentify();

							// Alert user that process has completed or has been
							// cancelled
							String cancelledOrComplete = "Identification complete";
							if (FileIdentificationPane.this.analysisControl
									.isAnalysisCancelled()) {
								cancelledOrComplete = "Identification cancelled";
							}

							analysisFinishedMessage(numFilesCompleted
									+ " files analysed", cancelledOrComplete);

							// Set the first file in the list to be selected and
							// scroll to the top
							FileIdentificationPane.this.jTableFileList
									.setRowSelectionInterval(0, 0);
							FileIdentificationPane.this.jScrollPaneFileList
									.getVerticalScrollBar().setValue(0);
						}

						// Refresh file list
						refreshFileList();

					}
				});

		this.identifyTimer.start();

	}

	/**
	 * Prints the file list (without previewing) (Runs in a worker thread) as
	 * this can take some time
	 */
	private void printFileList() {

		// Only continue if there are files in the file list
		if (this.analysisControl.getNumFiles() < 1) {
			JOptionPane.showMessageDialog(this, this.MSG_PRINT_FILE_LIST_EMPTY,
					"File list is empty", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Set cursor to wait (Egg timer)
		this.setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		setStatusText("Preparing for printing...");

		// Run in worker thread as this may a considerable amount of time
		final SwingWorker worker = new SwingWorker() {

			public Object construct() {

				// Call print method
				PrintPreview.printPrinterFriendly(
						FileIdentificationPane.this.analysisControl
								.getFileCollection(),
						FileIdentificationPane.this.fileList,
						AnalysisController.getDROIDVersion(),
						FileIdentificationPane.this.analysisControl
								.getSigFileVersion()
								+ "");

				// Reset cursor and status bar text
				setCursor(null);
				setStatusText("");
				return "";
			}
		};

		// Start the thread
		worker.start();
	}

	/**
	 * refreshes data the File list table and shows file hits
	 */
	private void refreshFileList() {

		final int currentlySelectedRow = this.jTableFileList.getSelectedRow();

		if (this.fileList.size() != this.analysisControl.getNumFiles()) {
			this.fileList.clear();
			final java.util.Enumeration<Integer> it = this.analysisControl
					.getFileCollection().getIndexKeys();
			while (it.hasMoreElements()) {
				this.fileList.add(it.nextElement());
			}

		}

		sortFileList(this.FileListSortByColumn);
		this.jTableFileList
				.setModel(new FileIdentificationPane.FileListTableModel());

		if (this.jTableFileList.getModel().getRowCount() > currentlySelectedRow
				&& currentlySelectedRow >= 0) {
			this.jTableFileList.setRowSelectionInterval(currentlySelectedRow,
					currentlySelectedRow);
		}

		enableIdentifyActions();
		enableRemoveActions();
	}

	/**
	 * Remove selected files from the file list User has to confirm before files
	 * are removed
	 */
	private void removeFiles() {
		// Get selected rows
		final int[] selectedRows = this.jTableFileList.getSelectedRows();

		// Check if 1 or more files are selected
		if (selectedRows.length >= 1) {

			// Confirm with user
			final int confirm = JOptionPane.showConfirmDialog(this,
					this.MSG_REMOVE_FILE, "Remove files",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {

				// Build list of files to remove
				final java.util.List theFilesToRemove = new java.util.ArrayList();
				for (final int selectedRow : selectedRows) {
					final Integer iRow = (Integer) this.fileList
							.get(selectedRow);
					theFilesToRemove.add(iRow);
				}
				// sort the list of file indexes
				java.util.Collections.sort(theFilesToRemove);
				// remove files in order from the largest index to the smallest
				for (int n = theFilesToRemove.size() - 1; n >= 0; n--) {
					this.analysisControl.removeFile(((Integer) theFilesToRemove
							.get(n)).intValue());
					this.fileList.remove(theFilesToRemove.get(n));
				}
				// Refresh file list after files removed
				refreshFileList();

				// Flag that file list is not saved
				this.fileListSaved = false;

			}

		} else {
			// Show message dialog if no files selected
			JOptionPane.showMessageDialog(this, this.MSG_NOT_REMOVED_FILE,
					"No files selected", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Resets progress bar value and text used when new run is started or new
	 * file list is displayed
	 */
	private void resetProgressBar() {
		this.jProgressIdentification.setString("");
		this.jProgressIdentification.setValue(0);

	}

	/**
	 * Shows a file dialog to save file list Queries user if file already exists
	 * saves file collection in chosen format to chosen destination
	 * 
	 * @param saveResults
	 *            saves the file format hits aswell if true
	 */
	private void saveFileCollection(final boolean saveResults) {

		java.io.File path = null;

		String dialogTitle = " file list";

		if (saveResults) {
			dialogTitle = " results";
		}

		// Setup save file dialog
		final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		fc.addChoosableFileFilter(new CustomFileFilter(
				this.FILE_COLLECTION_FILE_EXTENSTION,
				this.FILE_COLLECTION_FILE_DESCRIPTION));
		fc.setAcceptAllFileFilterUsed(false);
		// Set dialog title to same as dialog boxess
		fc.setDialogTitle(this.jMenuItemSaveResults.getText());

		// show file dialog
		final int returnVal = fc.showSaveDialog(this);

		// Save file if user has chosen a file
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

			// Get file user selected
			path = fc.getSelectedFile();

			// if no extension was specified add one
			if (!(path.getName().endsWith("."
					+ this.FILE_COLLECTION_FILE_EXTENSTION))) {
				path = new java.io.File(path.getParentFile(), path.getName()
						+ "." + this.FILE_COLLECTION_FILE_EXTENSTION);
			}

			// if path exists check confirm with user if they want to overwrite
			if (path.exists()) {
				final int option = javax.swing.JOptionPane.showConfirmDialog(
						this, this.MSG_OVERWRITE);
				if (option != javax.swing.JOptionPane.YES_OPTION) {
					return;
				}
			}

			// Get filepath of selected file
			final String selectedFilePath = path.getPath();

			// Show in status bar that saving is taking place
			setStatusText("Saving " + selectedFilePath);

			// Save file
			this.analysisControl.saveFileList(selectedFilePath, saveResults);
			// Show in status file has been saved
			setStatusText("List saved to " + selectedFilePath);
			// Append saved file name to title bar
			this
					.setTitle(this.APPLICATION_NAME + " [" + selectedFilePath
							+ "]");

			// Saved flag set to true if saving results

			if (saveResults) {
				this.fileListSaved = true;
			}

		}// End Save file if user has chosen a file

	}

	/**
	 * Save file list without saving file format hits
	 */
	private void saveFileList() {
		saveFileCollection(false);
	}

	/**
	 * Save file list with file format hits if they exist
	 */
	private void saveResults() {
		saveFileCollection(true);
	}

	/**
	 * For a selected row in file list find the index for the file in reference
	 * to its position in the AnalysisController object.
	 * 
	 * @param selectedRow
	 *            Selected row in jTable
	 * @return index for the file
	 */
	private int selectedRowToFileIndex(final int selectedRow) {
		final Integer i = (Integer) this.fileList.get(selectedRow);
		return i.intValue();
	}

	/**
	 * Sets the look and feel for the form Must be called before
	 * initComponents() in the constructor
	 * <p/>
	 * Jgoodies Plastic L&F with the Sky Bluer theme (Changed from PlasticXP as
	 * this didn't work on Mac OS X) All options as default except General
	 * options: Popup Shadow: On REF: Email from A.Brown
	 * NPD/4305/CL/CSC/2005MAR21/12:25:13
	 */
	private void setCustomLookAndFeel() {
		try {

			final com.jgoodies.plaf.plastic.PlasticLookAndFeel lf = new com.jgoodies.plaf.plastic.PlasticLookAndFeel();
			lf
					.setMyCurrentTheme(new com.jgoodies.plaf.plastic.theme.SkyBluer());

			javax.swing.UIManager.setLookAndFeel(lf);
			javax.swing.UIManager.put("jgoodies.popupDropShadowEnabled",
					Boolean.TRUE);

		} catch (final Exception e) {
			// Silently ignore exception
		}
	}

	/**
	 * Set the cell renderers for the File List jTable
	 */
	private void setFileListCellRenderers() {
		this.jTableFileList.getColumnModel().getColumn(0).setCellRenderer(
				new FileIdentificationPane.IconCellRenderer());
		this.jTableFileList.getColumnModel().getColumn(1).setCellRenderer(
				new FileIdentificationPane.CellRenderer());

	}

	/**
	 * Sets a mouse listener on the table header. When a column is clicked ,
	 * file list is sorted by that column
	 */
	private void setFileListHeaderListner() {

		final java.awt.event.MouseAdapter listMouseListener = new java.awt.event.MouseAdapter() {
			public void mouseClicked(final java.awt.event.MouseEvent e) {
				final javax.swing.table.TableColumnModel columnModel = FileIdentificationPane.this.jTableFileList
						.getColumnModel();
				final int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				final int column = FileIdentificationPane.this.jTableFileList
						.convertColumnIndexToModel(viewColumn);
				if (e.getClickCount() == 1 && column != -1) {
					// System.out.println("Sorting ...");
					final int shiftPressed = e.getModifiers()
							& java.awt.event.InputEvent.SHIFT_MASK;
					final boolean ascending = (shiftPressed == 0);
					FileIdentificationPane.this.FileListSortByColumn = column;
					refreshFileList();
				}
			}
		};
		final JTableHeader th = this.jTableFileList.getTableHeader();
		th.addMouseListener(listMouseListener);

	}

	/**
	 * Sets a uk.GUI.FileIdentificationPane.FileListHeaderRenderer() object as
	 * the Renderer for each column header
	 */
	private void setFileListHeaderRenderer() {
		final TableCellRenderer fileListRenderer = new FileIdentificationPane.FileListHeaderRenderer();
		this.jTableFileList.getColumnModel().getColumn(0).setHeaderRenderer(
				fileListRenderer);
		this.jTableFileList.getColumnModel().getColumn(1).setHeaderRenderer(
				fileListRenderer);

	}

	/**
	 * Sets the JTable list model listener to recognise row selections
	 */
	private void setFileListListener() {
		final ListSelectionModel rowSM = this.jTableFileList
				.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting()) {
					return;
				}

				final ListSelectionModel lsm = (ListSelectionModel) e
						.getSource();
				if (lsm.isSelectionEmpty()) {
					// System.out.println("No rows are selected.");
				} else {
					final int selectedRow = lsm.getMinSelectionIndex();
					// System.out.println("Row " + selectedRow
					// + " is now selected.");

					showFileHits(selectedRow);
				}
			}
		});
	}

	/**
	 * Sets the preferred ,min and max column widhts for the
	 * hitlist(identification results) table
	 */
	private void setIdentificationResultsColumnWidths() {
		// Set Identification results column widths

		// Format name column
		this.jTableHitList.getColumnModel().getColumn(2).setMinWidth(200);
		this.jTableHitList.getColumnModel().getColumn(2).setPreferredWidth(250);
		// Warning column
		this.jTableHitList.getColumnModel().getColumn(5).setMinWidth(100);
		this.jTableHitList.getColumnModel().getColumn(5).setPreferredWidth(250);
		// Status Column
		this.jTableHitList.getColumnModel().getColumn(4).setMinWidth(100);
		this.jTableHitList.getColumnModel().getColumn(4).setPreferredWidth(200);
		// Version column
		this.jTableHitList.getColumnModel().getColumn(3).setMinWidth(50);
		this.jTableHitList.getColumnModel().getColumn(3).setPreferredWidth(50);

	}

	/**
	 * Add key listener to form
	 */
	private void setKeyListeners() {

		this.addKeyListener(new java.awt.event.KeyListener() {

			public void keyPressed(final java.awt.event.KeyEvent e) {
				actionOnKeyPress(e);
			}

			public void keyReleased(final java.awt.event.KeyEvent e) {
				// Do nothing
			}

			public void keyTyped(final java.awt.event.KeyEvent e) {
				// Do nothing
			}

		});
	}

	/**
	 * Set the mnemonics (Keyboard shortcuts) for menu items on this form Can
	 * only be called after initComponents()
	 */
	private void setMnemonics() {

		// Menu mnemonics
		this.jMenuFile.setMnemonic('F');
		this.jMenuEdit.setMnemonic('E');
		this.jMenuIdentify.setMnemonic('I');
		this.jMenuTools.setMnemonic('T');
		this.jMenuHelp.setMnemonic('H');

		// File Menu mnemonics
		this.jMenuItemOpenList.setMnemonic('O');

		this.jMenuItemSaveResults.setMnemonic('S');
		this.jMenuItemPrintPreview.setMnemonic('v');
		this.jMenuItemPrint.setMnemonic('P');
		this.jMenuItemExportCSV.setMnemonic('C');
		this.jMenuItemExit.setMnemonic('x');

		// Edit Menu mnemonics
		this.jMenuItemAdd.setMnemonic('A');
		this.jMenuItemRemove.setMnemonic('R');
		this.jMenuItemRemoveAll.setMnemonic('l');

		// Identify Menu mnemonics
		this.jMenuItemIdentify.setMnemonic('I');
		this.jMenuItemCancelidentify.setMnemonic('C');

		// Tools Menu mnemonics
		this.jMenuItemOptions.setMnemonic('O');
		this.jCheckBoxShowFilePaths.setMnemonic('S');

		// Help Menu mnemonics
		this.jMenuItemHelpContents.setMnemonic('H');
		this.jMenuItemHelpAbout.setMnemonic('A');

		// Command button mnemonics
		this.jButtonAdd.setMnemonic('A');
		this.jButtonRemove.setMnemonic('R');
		this.jButtonRemoveAll.setMnemonic('l');

		this.jButtonIdentify.setMnemonic('I');
		this.jButtonCancel.setMnemonic('C');

	}

	/**
	 * Populates the icon list with ImageIcon objects
	 */
	private void setStatusIconList() {
		this.statusIcons = new java.util.ArrayList();
		this.statusIcons.add(new javax.swing.ImageIcon(getClass().getResource(
				"/uk/gov/nationalarchives/droid/GUI/Icons/noHit.GIF")));
		this.statusIcons.add(new javax.swing.ImageIcon(getClass().getResource(
				"/uk/gov/nationalarchives/droid/GUI/Icons/tentitive.GIF")));
		this.statusIcons.add(new javax.swing.ImageIcon(getClass().getResource(
				"/uk/gov/nationalarchives/droid/GUI/Icons/positive.GIF")));
		this.statusIcons.add(new javax.swing.ImageIcon(getClass().getResource(
				"/uk/gov/nationalarchives/droid/GUI/Icons/error.GIF")));
	}

	/**
	 * Specify the text displayed on the status bar , for a given amount of time
	 * 
	 * @param statusText
	 *            Text to display
	 * @param timeToDisplay
	 *            Time in ms
	 */
	private void setStatusText(final String statusText, final int timeToDisplay) {
		final String previous = getStatusText();
		setStatusText(statusText);
		this.timer = new javax.swing.Timer(timeToDisplay,
				new java.awt.event.ActionListener() {
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						setStatusText(previous);
						FileIdentificationPane.this.timer.stop();
					}
				});

		this.timer.start();
	}

	/**
	 * Sets the preferred , min and max column widths for the FileList and
	 * HitList tables
	 */
	private void setTableColumnWidths() {
		// Set the status column max and min widths
		this.jTableFileList.getColumnModel().getColumn(0).setPreferredWidth(65);
		this.jTableFileList.getColumnModel().getColumn(0).setMaxWidth(70);
		// jTableFileList.getColumnModel().getColumn(0).setMinWidth(50) ;

		// Set File name column width
		final double remainingColWidth = this.jTableFileList.getWidth()
				- this.jTableFileList.getColumnModel().getColumn(0).getWidth();
		final double warningColWidth = (double) remainingColWidth * 0.3;

		this.jTableFileList.getColumnModel().getColumn(1).setMinWidth(250);

		setIdentificationResultsColumnWidths();

	}

	/**********************************************************************
	 *NESTED CLASSES
	 ********************************************************************** 
	 */

	/**
	 * Displays About box
	 */
	private void showAboutBox() {
		AboutDialog.showDialog(this, this.APPLICATION_NAME, AnalysisController
				.getDROIDVersion(), this.analysisControl.getSigFileVersion()
				+ "");
	}

	/**
	 * Show the file hits for a selected file
	 */
	private void showFileHits(final int selectedRow) {

		// Get the file selected
		boolean foundDetails = false;

		// Get the card layout for the identification results panel
		final java.awt.CardLayout cl = (java.awt.CardLayout) (this.jPanelIdentificationResults
				.getLayout());

		IdentificationFile idFile = new IdentificationFile("");
		this.jTextPaneNoIDMessage.setText("");
		if (selectedRow >= 0) {
			// Get the file object corresponding to selection
			final Integer i = (Integer) this.fileList.get(selectedRow);
			idFile = this.analysisControl.getFile(i.intValue());
			foundDetails = true;
			// Set the text in the warning box
			this.jTextPaneNoIDMessage.setText(idFile.getWarning());
		}

		// If file selected has an Error status , hide the results and show the
		// warning box
		if (idFile.getClassification() == AnalysisController.FILE_CLASSIFICATION_ERROR) {
			// Set the title of the group as "Warning"
			this.jPanelWarnings.setBorder(new javax.swing.border.TitledBorder(
					this.TITLE_WARNING_BOX_ERROR));
			// Show the text box panel
			cl.show(this.jPanelIdentificationResults, "cardWarnings");
		} else if (idFile.getClassification() == AnalysisController.FILE_CLASSIFICATION_NOHIT) {
			// Set the title of the group as "Errors"
			this.jPanelWarnings.setBorder(new javax.swing.border.TitledBorder(
					this.TITLE_WARNING_BOX_NOT_IDENTIFIED));
			// Show the text box panel
			cl.show(this.jPanelIdentificationResults, "cardWarnings");
			String message = this.MSG_UNIDENTIFIED;
			// If warning exists, append it
			if (!idFile.getWarning().equals("")) {
				message = message + " (" + idFile.getWarning() + ")";
			}
			this.jTextPaneNoIDMessage.setText(message);
		} else {
			// Otherwise show the results
			cl.show(this.jPanelIdentificationResults, "cardResults");
		}

		this.jTableHitList
				.setModel(new FileIdentificationPane.HitListTableModel(idFile));
		this.jTableHitList.getColumnModel().getColumn(0).setCellRenderer(
				this.jTableHitList.getCellRenderer());
		// Show the file path only if it has changed
		if (!this.jTextFieldSelectedFile.getText().equals(idFile.getFilePath())) {
			this.jTextFieldSelectedFile.setText(idFile.getFilePath());
		}

	}

	/**
	 * Displays Options dialog in modal view
	 */
	private void showOptions() {
		final boolean value = OptionsDialog.showDialog(this,
				this.analysisControl);
	}

	/**
	 * Checks whether a new signature file download is due
	 */
	private void sigFileDownloadDue() {

		// Message for confirmm dialog
		final String confirmMessage = this.MSG_CHECK_SIG_FILE_UPDATE;

		// Check if a signature download file is due and that a
		if (this.analysisControl.isSigFileDownloadDue()) {
			// if download is due ask user if they would like to check for a new
			// signature file
			final int confirmValue = JOptionPane.showConfirmDialog(this,
					confirmMessage, "Signature file update",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (confirmValue == JOptionPane.YES_OPTION) {
				// if they confirm they would like to then
				// check if a newer file exists and download
				checkSigFileAndDownload();
			}
		}

	}

	/**
	 * Sort the displayed file list by a given column
	 * 
	 * @param col
	 *            index of column to sort by
	 */
	private void sortFileList(final int col) {

		java.util.Comparator c;

		// Declare comparator to sort by file name
		final java.util.Comparator fileCompare = new java.util.Comparator() {
			public int compare(final Object o1, final Object o2) {

				Integer i = (Integer) o1;
				final int i1 = i.intValue();

				i = (Integer) o2;
				final int i2 = i.intValue();

				String path1 = FileIdentificationPane.this.analysisControl
						.getFile(i1).getFilePath();
				String path2 = FileIdentificationPane.this.analysisControl
						.getFile(i2).getFilePath();

				// Get file names if showing paths is switched off
				if (!FileIdentificationPane.this.jCheckBoxShowFilePaths
						.isSelected()) {
					path1 = FileIdentificationPane.this.analysisControl
							.getFile(i1).getFileName();
					path2 = FileIdentificationPane.this.analysisControl
							.getFile(i2).getFileName();
				}
				// Compare the strings ignoring case
				return path1.compareToIgnoreCase(path2);

			}
		};

		// Declare comparotor to compare by status
		final java.util.Comparator statusCompare = new java.util.Comparator() {
			public int compare(final Object o1, final Object o2) {
				Integer i = (Integer) o1;
				final int i1 = i.intValue();

				i = (Integer) o2;
				final int i2 = i.intValue();

				// Get the status values for both objects
				final int status1 = FileIdentificationPane.this.analysisControl
						.getFile(i1).getClassification();
				final int status2 = FileIdentificationPane.this.analysisControl
						.getFile(i2).getClassification();

				return status1 - status2;
			}
		};

		// Declare comparator to sort by warnings
		final java.util.Comparator warningCompare = new java.util.Comparator() {
			public int compare(final Object o1, final Object o2) {
				Integer i = (Integer) o1;
				final int i1 = i.intValue();

				i = (Integer) o2;
				final int i2 = i.intValue();

				final String warn1 = FileIdentificationPane.this.analysisControl
						.getFile(i1).getWarning();
				final String warn2 = FileIdentificationPane.this.analysisControl
						.getFile(i2).getWarning();

				return warn2.compareTo(warn1);
			}
		};

		// Decide which comparator to use depending on whihc column selected
		switch (col) {
		case FILELIST_COL_STATUS:
			c = statusCompare;
			break;
		case FILELIST_COL_FILENAME:
			c = fileCompare;
			break;
		case FILELIST_COL_WARNING:
			c = warningCompare;
			break;
		default:
			c = statusCompare;
			break;

		}

		// Sort the filelist by chosen comparator
		java.util.Collections.sort(this.fileList, c);

		// Refresh the table header , so sort by column is highlighted
		this.jTableFileList.getTableHeader().resizeAndRepaint();

	}

}
