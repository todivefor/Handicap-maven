/*
 * To change this liframse header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap;

import java.awt.CardLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import net.proteanit.sql.DbUtils;
import org.todivefor.handicap.process.PrefsProc;
import org.todivefor.stringutils.StringUtils;
//import org.todivefor.rxcardlayout.RXCardLayout;                     // <RXCardlayout>

/**
 *
 * @author peterream
 * 
 * Change control
 *  0.1.2 
 *      Redid look and feel to use available classes
 *      Compiled with JDK AdoptopenJDK OpenJDK
 * 
 *  0.1.3
 *      Added java version to about
 *      Cleaned up warnings
 *      Fix preferences return problem
 *      Replaced my Stack class with Java's
 *      Moved most of initialization out constructor, left L&F
 *      Converted to maven
 * 
 * 0.1.4
 *      Multi player support (Preferrences)
 *      Fix some where to return issues
 *      Changed scoreEditingAllowed to true initially
 *      Added print returnStack in debug menu for debugging where to return
 *      Fixed add / delete scores for same day
 *
 * 0.1.5
 *      StringUtils moved to maven control - change import statement
 *      Replace user_COURSE_TBL with COURSE_TBL
 *      Add Player to Edit menu
 *      Add select player to preferences
 *      World Handicap
 *          Add low HI to preferences
 *          Add last HI to preferences
 *          Turn on / off
 *          Soft / hard caps
 *          PCC
 *          Exceptional score processing
 *      Add comboBoxCourse listener after combobox is loaded
 *      Remove and add listener when loading
 *      Select all for input fields set in "After-All-Set Code"* 
 *      Low HI maintained
 *      Added HandicapLauncher
 * 
 * 0.1.6
 *      Changed handicapIndex[] to class HandicapIndices in DisplayScores.java
 *      Fixed (DisplayScores) problem with soft/hard cap. Soft cap needs to be applied and then check result for hard cap
 * 
 * 0.1.7
 *      Setup Preferences read / write as its own class
 *      Fixed problem with uninitialized type of HI adjustment
 *      Reorganized initialization code to HandicapMainMenu, MaintainCourses, and Preferrrnces constructors
 *      Move fillCourseComboBox() to AddScores
 *      Fixed & improved delete score year - refreshScoreTable
 */
public class HandicapMain extends JFrame 
{
        public static final String VERSION = "0.1.7";               // Application version used in "About"
        public static final String REVISIONDATE = "07/20/2020";     // Revision date
        public static final boolean TEST = false;                    // use test preferences
/*
 * 	Components referenced from other classes
 */
	
        public static JFrame handicapFrame;                         // This Frame
	public static CardLayout cards = new CardLayout();          // Use RXCardLayout to get focus on card <RXCardlayout>
	
	public static boolean debug = true;                         // Debug mode	<debug>
	private boolean pathSet = false;                            // Created DB?
	private static String dbPath = null;                        // Path to DB
	
	public static String saveCourseName;                        // Save course
	
	public static String userName = null;                      // Who this is
	
	public static String operatingSystem;                       // OS
	
//	Preference file constants
	
	final static String HANDICAPDB = "DBFile";			// DB field containing path to handicap.sqlite field ID
	final static String NODB = "NODB";                                  // DB has not been initialized
	final static String HANDICAPSCORETABLENAME = "SCORETABLE";	// Preference - SCORE table name field ID
	final static String NOST = "NOST";                                  // Score table not initialized  
	final static String HANDICAPUSERNAME = "USERNAME";		// Preference - USER name field ID
	final static String NOUN = "NOUN";                                  // Tournament table not initialized 
	final static String HANDICAPNINEHOLE = "NINEHOLE";		// 9 hole table position
	final static String NONH = "NONH";                                  // 9 hole not initialized
	final static String HANDICAPDEBUG = "DEBUG";                    // Debug
	final static boolean NODG = false;                                  // Debug not initialized
	final static String HANDICAPLOOKANDFEEL = "LOOKANDFEEL";		// Look and feel
	final static String NOLF = "NOLF";                                  // Look and feel not initialized
	final static String HANDICAPTHEME = "THEME";			// Theme if metal look and feel
	final static String NOTH = "NOTH";                                  // Theme not initialized
	final static String HANDICAPLOWHI = "LOWHI";			// Low Handicap index
	final static String NOLOW = "NOLOW";                                // Low HI not initialized
	final static String HANDICAPHI = "INDEX";			// Handicap index
	final static String NOHI = "NOHI";                                  // HI not initialized
	
	public static String inicatorTournOrNineOnDB;		// T or 9 indicator on DB
	
//	CARDS
	
	final static String MAINMENU = "MainMenu";                  // Main menu card
	final static String MAINTAINCOURSES = "MaintainCourses";    // Maintain courses card
	final static String DISPLAYSCORES = "DisplayScores";        // Display scores card
	final static String ADDSCORES = "AddScores";                // Add scores card
	final static String PREFERRENCES = "Preferrences";          // Preferences card
        
        public static String lastCard = MAINMENU;                   // Last card where we were
        public static Stack<String> returnStack = 
                new Stack<String>();                                // Stack for where to return
        
	
// 	SCORE table column position constants
	
	final static int DATE_POS = 0;                              // Date column
	final static int COURSE_POS = 1;                            // Course column
	final static int T_POS = 2;                                 // T column
	final static int SCORE_POS = 3;                             // Score column
        final static int PCC_POS = 4;                               // PCC column
	final static int U_POS = 5;                                 // U column
	final static int RATING_POS = 6;                            // Rating column
	final static int SLOPE_POS = 7;                             // Slope column
	final static int DIFFERENTIAL_POS = 8;                      // Differential column
	
	// Miscellaneous constants
	
	final static int MAXCOURSENAMELENGTH = 50;		// max course name length
	final static String TOURNINDICATOR = "T";		// Tournament indicator
	final static String NINEINDICATOR = "9";		// Nine hole indicator
	
	public static String scoreTableName = null;             // Score table name
	public static String courseTableName = "COURSE_TBL";    // Course table name
	
	// Specify the look and feel to use by defining the LOOKANDFEEL constant
	// Valid values are: null (use the default), "Metal", "System", "Motif", Windows
	// and "GTK"
	final static String LOOKANDFEEL = "System";             //  Default look and feel

	// If you choose the Metal L&F, you can also choose a theme.
	// Specify the theme to use by defining the THEME constant
	// Valid values are: "DefaultMetal", "Ocean",  and "Test"

	//final static String THEME = "DefaultMetal";
	final static String THEME = "Ocean";                    // Default theme for metal
	public static String lookAndFeel = null;                // Set to nothing
        public static final int WORLDHCYEAR = 2020;
        String className;                                       // Fully qualified class name for preferences node

    /**
     * Creates new form HandicapMain
     */
    public HandicapMain() 
    {
        
/*
 * 		Initialization
 */
		
/*
 * 
 * Preferences 
 * MAC OS X
 * 		~/library/Preferences/com.apple.java.util.prefs.plist
 * Windows
 * 		Registry - HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs
 * 			It produces a warning
 * 
 * 
 */
        className = this.getClass().getName();                                      // Class name (use as node)
        if (TEST)                                                                   // Is this TEST?
        {
            JOptionPane.showMessageDialog(null, "Running with TEST preferences");   // Yes - tell
            className = className + ".test";                                        // Override package name
        }
        PrefsProc.prefsNode(className);                                             // "org.handicap.HandicapMain"

/*
        Setup look and feel from prefs
*/

        lookAndFeel = PrefsProc.getPref(HANDICAPLOOKANDFEEL, NOLF);                 // Get look and feel from preferences
        String theme = PrefsProc.getPref(HANDICAPTHEME, NOTH);                      // Get theme if metal
        if (lookAndFeel.equals(NOLF))                                               // L & F setup?
        {
            initLookAndFeel(LOOKANDFEEL, THEME);                                    // No, pass default System, Ocean
        } 
        else                                                                        // Yes
        {
            initLookAndFeel(lookAndFeel, theme);                                    // Use what we got for L & F
        }
        
//      Build Frame

        initComponents();
        
        handicapFrame = this;                           // Main frame

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jMenuBar1 = new javax.swing.JMenuBar();
        mnHandicap = new javax.swing.JMenu();
        mntmAbout = new javax.swing.JMenuItem();
        mntmPreferrences = new javax.swing.JMenuItem();
        mntmExiHandicap = new javax.swing.JMenuItem();
        mnFile = new javax.swing.JMenu();
        mntmNewDb = new javax.swing.JMenuItem();
        mntmSwitchDb = new javax.swing.JMenuItem();
        mnEdit = new javax.swing.JMenu();
        mntmAddScore = new javax.swing.JMenuItem();
        mntmEditCourse = new javax.swing.JMenuItem();
        mntmAddPlayer = new javax.swing.JMenuItem();
        mnView = new javax.swing.JMenu();
        mntmDisplayScores = new javax.swing.JMenuItem();
        mntmDisplayArchiveScores = new javax.swing.JMenuItem();
        mntmDisplayTournamentScores = new javax.swing.JMenuItem();
        mnTools = new javax.swing.JMenu();
        mntmArchiveScores = new javax.swing.JMenuItem();
        mntmDeleteYearScores = new javax.swing.JMenuItem();
        mnDebug = new javax.swing.JMenu();
        mntmRemoveNineHole = new javax.swing.JMenuItem();
        mntmRemoveDbPath = new javax.swing.JMenuItem();
        mntmRemoveNode = new javax.swing.JMenuItem();
        mntmRemoveLF = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(200, 0, 750, 700));
        getContentPane().setLayout(new java.awt.CardLayout());

        mnHandicap.setText("Handicap");

        mntmAbout.setText("About");
        mntmAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmAboutActionPerformed(evt);
            }
        });
        mnHandicap.add(mntmAbout);

        mntmPreferrences.setText("Preferrences");
        mntmPreferrences.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmPreferrencesActionPerformed(evt);
            }
        });
        mnHandicap.add(mntmPreferrences);

        mntmExiHandicap.setText("Exit Handicap");
        mntmExiHandicap.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmExiHandicapActionPerformed(evt);
            }
        });
        mnHandicap.add(mntmExiHandicap);

        jMenuBar1.add(mnHandicap);

        mnFile.setText("File");

        mntmNewDb.setText("New DB");
        mntmNewDb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmNewDbActionPerformed(evt);
            }
        });
        mnFile.add(mntmNewDb);

        mntmSwitchDb.setText("Switch DB");
        mntmSwitchDb.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmSwitchDbActionPerformed(evt);
            }
        });
        mnFile.add(mntmSwitchDb);

        jMenuBar1.add(mnFile);

        mnEdit.setText("Edit");

        mntmAddScore.setText("Add Score");
        mntmAddScore.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmAddScoreActionPerformed(evt);
            }
        });
        mnEdit.add(mntmAddScore);

        mntmEditCourse.setText("Edit Course");
        mntmEditCourse.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmEditCourseActionPerformed(evt);
            }
        });
        mnEdit.add(mntmEditCourse);

        mntmAddPlayer.setText("Add Player");
        mntmAddPlayer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmAddPlayerActionPerformed(evt);
            }
        });
        mnEdit.add(mntmAddPlayer);

        jMenuBar1.add(mnEdit);

        mnView.setText("View");

        mntmDisplayScores.setText("Display Scores");
        mntmDisplayScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmDisplayScoresActionPerformed(evt);
            }
        });
        mnView.add(mntmDisplayScores);

        mntmDisplayArchiveScores.setText("Display Archive Scores");
        mntmDisplayArchiveScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmDisplayArchiveScoresActionPerformed(evt);
            }
        });
        mnView.add(mntmDisplayArchiveScores);

        mntmDisplayTournamentScores.setText("Display Tournament Scores");
        mntmDisplayTournamentScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmDisplayTournamentScoresActionPerformed(evt);
            }
        });
        mnView.add(mntmDisplayTournamentScores);

        jMenuBar1.add(mnView);

        mnTools.setText("Tools");

        mntmArchiveScores.setText("Archive Scores");
        mntmArchiveScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmArchiveScoresActionPerformed(evt);
            }
        });
        mnTools.add(mntmArchiveScores);

        mntmDeleteYearScores.setText("Delete Year Scores");
        mntmDeleteYearScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmDeleteYearScoresActionPerformed(evt);
            }
        });
        mnTools.add(mntmDeleteYearScores);

        jMenuBar1.add(mnTools);

        mnDebug.setText("Debug");

        mntmRemoveNineHole.setText("Remove 9 Hole");
        mntmRemoveNineHole.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmRemoveNineHoleActionPerformed(evt);
            }
        });
        mnDebug.add(mntmRemoveNineHole);

        mntmRemoveDbPath.setText("Remove DB Path");
        mntmRemoveDbPath.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmRemoveDbPathActionPerformed(evt);
            }
        });
        mnDebug.add(mntmRemoveDbPath);

        mntmRemoveNode.setText("Remove Node");
        mntmRemoveNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmRemoveNodeActionPerformed(evt);
            }
        });
        mnDebug.add(mntmRemoveNode);

        mntmRemoveLF.setText("Remove L&F");
        mntmRemoveLF.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mntmRemoveLFActionPerformed(evt);
            }
        });
        mnDebug.add(mntmRemoveLF);

        jMenuItem1.setText("Print returnStack");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnDebug.add(jMenuItem1);

        jMenuBar1.add(mnDebug);

        setJMenuBar(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents
    
    private void mntmPreferrencesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmPreferrencesActionPerformed
    {//GEN-HEADEREND:event_mntmPreferrencesActionPerformed
      
        String lowHI = PrefsProc.getPref(userName + HANDICAPLOWHI, NOHI);           // Get low HI
        if (!lowHI.equals(NOHI))                                                    // Get one?
        {
            Preferrences.textPreferencesLHI.setText(lowHI);
        }
        
        
        Preferrences.comboBoxPreferencesPlayer.setSelectedItem(userName);
        
        if (HandicapMain.returnStack.empty())                                       // Come frome place?
        {
            HandicapMain.returnStack.push(HandicapMain.MAINMENU);                   // No, push MAINMENU
        }
        else                                                                        // Yes
        {     
            returnStack.push(lastCard);                                             // Where we came from
        }
        setFrameTitle("Handicap Preferences - " + userName);                        // Set frame title
        cards.show(getContentPane(), PREFERRENCES);                                 // Show preferences card
    }//GEN-LAST:event_mntmPreferrencesActionPerformed

    private void mntmAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmAboutActionPerformed
    {//GEN-HEADEREND:event_mntmAboutActionPerformed
        JOptionPane.showMessageDialog(null, "Handicap Version: " + VERSION + "\n" +
                "Compile Date: " + REVISIONDATE + "\n" +
                "Java Version: " + System.getProperty("java.runtime.version") + "\n" +
                "Author: Peter Ream",
                "About",
                JOptionPane.INFORMATION_MESSAGE);                       // About information
    }//GEN-LAST:event_mntmAboutActionPerformed

    private void mntmExiHandicapActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmExiHandicapActionPerformed
    {//GEN-HEADEREND:event_mntmExiHandicapActionPerformed
        if (pathSet)                                                        // Have we setup DB?
            SQLiteConnection.closeHandicapDB(SQLiteConnection.connection);  // Yes, close it
        System.exit(0);                                                     // Exit Handicap
    }//GEN-LAST:event_mntmExiHandicapActionPerformed

    private void mntmNewDbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmNewDbActionPerformed
    {//GEN-HEADEREND:event_mntmNewDbActionPerformed
        Preferrences.fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	// Only look at directories
        int returnVal = Preferrences.fc.showOpenDialog(null);                   // New DB dialog
        if (returnVal == JFileChooser.APPROVE_OPTION)                           // DB selected?
        {
//	    			File file = fc.getSelectedFile();
            //This is where a real application would open the file.

            String path = Preferrences.fc.getSelectedFile().getAbsolutePath();  // Yes, get path

/*
 * 	Set buttons on main menu
 */
            String hDBName = JOptionPane.showInputDialog("Enter the name of the new handicap DB");  // DB name
            path = path + "/" + hDBName + ".sqlite";                                                // /path/name.sqlite

            if (!pathSet)                                                            // First time thru?
            {
                SQLiteConnection.connection = SQLiteConnection.dbConnector(path);   // No - Open the new DB
                HandicapMainMenu.btnAddScores.setVisible(true);                     // Display Add Scores button
                HandicapMainMenu.btnDisplayScores.setVisible(true);                 // Display Display Scores button
                HandicapMainMenu.btnEditCourses.setVisible(true);                   // Display Edit Courses button
                pathSet = true;                                                     // Handicap DB has been initialized
                SQLiteConnection.connection = SQLiteConnection.dbConnector(dbPath); // Open handicap DB
            } 
            else                                                                    // Yes - first time thru
            {
                SQLiteConnection.closeHandicapDB(SQLiteConnection.connection);      // DB open so, close it
                PrefsProc.removePref(HANDICAPDB);                                   // Remove path from prefs
                SQLiteConnection.connection = SQLiteConnection.dbConnector(path);   // Open the new DB
            }
            userName = JOptionPane.showInputDialog("Your first name");              // Get user name
            setTitle("Handicap - " + userName);                                     // Set screen title "Handicap - userName"
            PrefsProc.putPref(HANDICAPUSERNAME, userName);                          // Save user name in preferences
            AddScores.createScoreTable(userName);                                   // Create SCORE table
            MaintainCourses.createCourseTable(userName);                            // Create COURSE Table
            // New DB
            DisplayScores.scoreDataChanged = true;                                  // Must force redisplay scores
            PrefsProc.putPref(HANDICAPDB, path);                                    // Save the new DB in preference
            PrefsProc.flushPref();                                                  // Make changes permanent
            pathSet = true;
        } 
    }//GEN-LAST:event_mntmNewDbActionPerformed

    private void mntmSwitchDbActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmSwitchDbActionPerformed
    {//GEN-HEADEREND:event_mntmSwitchDbActionPerformed
        Preferrences.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);              // Only look at files
        int returnVal = Preferrences.fc.showOpenDialog(null);                       // Switch DB dialog
        if (returnVal == JFileChooser.APPROVE_OPTION)                               // Get good DB?
        {

            String path = Preferrences.fc.getSelectedFile().getAbsolutePath();      // Get path

            if (pathSet)                                                            // path to DB set?
            {
                SQLiteConnection.closeHandicapDB(SQLiteConnection.connection);      // Yes, DB open so, close it
            } 
            else                                                                    // No
            {
                HandicapMainMenu.btnAddScores.setVisible(true);                     // Display Add Scores button 
                HandicapMainMenu.btnDisplayScores.setVisible(true);                 // Display Display Scores button
                HandicapMainMenu.btnEditCourses.setVisible(true);                   // Display Edit Courses button
                pathSet = true;                                                     // Handicap DB has been initialized
            }
            PrefsProc.removePref(HANDICAPDB);                                       // Remove path from prefs
            SQLiteConnection.connection = SQLiteConnection.dbConnector(path);       // Open the new DB
            userName = JOptionPane.showInputDialog(
                    "Enter your first name for tables:");                           // Get userName
            PrefsProc.putPref(HANDICAPUSERNAME, userName);                          // Save username
            scoreTableName = userName + "_SCORE_TBL";                               // scoreTable Name "userName_SCORE_TBL"
            PrefsProc.putPref(HANDICAPSCORETABLENAME, scoreTableName);              // Save the new in preference
            setTitle("Handicap - " + userName);                                     // Set screen title
//            PrefsProc.flushPref();                                                  // Make all preferences changes permanent
            DisplayScores.scoreDataChanged = true;                                  // Force re-display
            MaintainCourses.refreshCourseTable(SQLiteConnection.connection, 
                    courseTableName);                                               // Fill initial course table
            PrefsProc.putPref(HANDICAPDB, path);                                    // Save the new in preference
            PrefsProc.flushPref();                                                  // Make all preferences changes permanent
            Preferrences.prefsLoadPlayerCombo();                                    // Load preferences player combo
            pathSet = true;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Switch command cancelled by user");
        }
    }//GEN-LAST:event_mntmSwitchDbActionPerformed

    private void mntmAddScoreActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmAddScoreActionPerformed
    {//GEN-HEADEREND:event_mntmAddScoreActionPerformed
        if (returnStack.empty())                                            // Any return address?
        {
            returnStack.push(MAINMENU);                                     // No, return MAINMENU
        }
//        else                                                                // Yes
//        {
//            if (!lastCard.equals(ADDSCORES))                                // Come from ADDSCORES?
//            {
//                returnStack.push(lastCard);                                 // No, Save where we came from
//            }
//        }
        lastCard = ADDSCORES;                                               // Where we are
        HandicapMainMenu.addScore();                                        // Perform addScore
        setFrameTitle("Handicap Add Scores - " + userName);                 // Title
        HandicapMain.cards.show(getContentPane(), HandicapMain.ADDSCORES);  // Show ADDSCORES
    }//GEN-LAST:event_mntmAddScoreActionPerformed

    private void mntmEditCourseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmEditCourseActionPerformed
    {//GEN-HEADEREND:event_mntmEditCourseActionPerformed
        if (returnStack.empty())                          // Any return address?
        {
            returnStack.push(MAINMENU);                   // No, return MAINMENU
        }
        lastCard = MAINTAINCOURSES;                                     // Where we are     
        
        setFrameTitle("Handicap Maintain Courses - " + userName);         // Title
        HandicapMain.cards.show(getContentPane(), MAINTAINCOURSES);
    }//GEN-LAST:event_mntmEditCourseActionPerformed

    private void mntmDisplayScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmDisplayScoresActionPerformed
    {//GEN-HEADEREND:event_mntmDisplayScoresActionPerformed
        if (!lastCard.equals(DISPLAYSCORES))                            // Come from DISPLAYSCORES?
        {
            returnStack.push(lastCard);                                 // No, Save where we came from
        }
//        }
        lastCard = DISPLAYSCORES;                                       // Where we are
        
        DisplayScores.scoreEditingAllowed = true;                       // Allow editing of scores
	if (DisplayScores.scoreDataChanged)                             // Has anything changed?
        {
            DisplayScores.tournament = false;                           // Yes, not displaying tournament scores
            DisplayScores.refreshScoreTable(scoreTableName);            // Refresh scores
            DisplayScores.scoreDataChanged = false;                     // No need to display until next change
        }
        setFrameTitle("Handicap Display Scores - " + userName);         // Title
        HandicapMain.cards.show(getContentPane(), DISPLAYSCORES);       // Just display with no changes
    }//GEN-LAST:event_mntmDisplayScoresActionPerformed

    private void mntmDisplayArchiveScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmDisplayArchiveScoresActionPerformed
    {//GEN-HEADEREND:event_mntmDisplayArchiveScoresActionPerformed
        if (!lastCard.equals(DISPLAYSCORES))                            // Come from DISPLAYSCORES?
        {
            returnStack.push(lastCard);                                 // No, Save where we came from
        }
//        }
        lastCard = DISPLAYSCORES;                                       // Where we are
        
        String archiveYear = JOptionPane.showInputDialog(null, "Enter year of Archive (YYYY) to display", "Archive Year", JOptionPane.DEFAULT_OPTION);
        if (archiveYear == null)                            // Did we get a year
        {
            return;                                         // No, get out
        }
        if (!StringUtils.isInteger(archiveYear))            // Valid year?
        {
            JOptionPane.showMessageDialog(null, "Archive year is invalid.", "Archive Year", JOptionPane.ERROR_MESSAGE); // No, message
            return;                                         // Get out
        }
        DisplayScores.tournament = false;                         // Not tournament
//        refreshScoreTable(SQLiteConnection.connection, "YrEnd" + archiveYear + scoreTableName, tournament);
        DisplayScores.refreshScoreTable(userName + "_YrEnd" + archiveYear + "_SCORE_TBL"); // Open archive
        DisplayScores.scoreEditingAllowed = false;          // Set not allowed to edit data
        DisplayScores.scoreDataChanged = true;              // Redisplay actual scores when done
        setFrameTitle("Handicap Display Archive Scores - " + userName); // Title
        HandicapMain.cards.show(getContentPane(), DISPLAYSCORES);   // Just display with no changes
    }//GEN-LAST:event_mntmDisplayArchiveScoresActionPerformed

    private void mntmDisplayTournamentScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmDisplayTournamentScoresActionPerformed
    {//GEN-HEADEREND:event_mntmDisplayTournamentScoresActionPerformed
        DisplayScores.tournament = true;                        // Display tournament data
        DisplayScores.refreshScoreTable(scoreTableName);        // Refresh score table
        DisplayScores.scoreEditingAllowed = false;              // No editting
        DisplayScores.scoreDataChanged = true;                  // Force rdeisplay score data
        returnStack.push(lastCard);                             // Where we came from
        setFrameTitle("Handicap Display Tournament Scores - " + 
                userName);                                      // Title
        cards.show(getContentPane(),DISPLAYSCORES);             // Just display with no changes
    }//GEN-LAST:event_mntmDisplayTournamentScoresActionPerformed

    private void mntmArchiveScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmArchiveScoresActionPerformed
    {//GEN-HEADEREND:event_mntmArchiveScoresActionPerformed
/*
 * 			This will archive a years worth of data to a new table userName_YrEndnnn_TBL
 * 			If recalcindex is true redisplay scores file
 * 			Ask for year to archive
 * 			archive year data
 * 			Count scores > year to archive
 * 					delete scores from current
 * 				else
 * 					delete scores from current leaving some from archive year to make 20 total			
  */
        String archiveYear = JOptionPane.showInputDialog(null, "Enter year to Archive (YYYY)", "Archive Year", JOptionPane.DEFAULT_OPTION);
        if (archiveYear == null)                                    // Year entered?
        {
            return;                                                 // No, get out
        }
        if (!StringUtils.isInteger(archiveYear))                    // Valid year?
        {
            JOptionPane.showMessageDialog(null, "Archive year is invalid.", "Archive Year", JOptionPane.ERROR_MESSAGE); // No
            return;                                                 // Get out
        }
        String startDate, endDate;
        startDate = archiveYear + "-01-01";				// Start 01/01/YY
        endDate = archiveYear + "-12-31";				// End   12/31/YY
//        String tempScoreTableName = "YrEnd" + archiveYear + userName;   // Temp score table
        String tempScoreTableName = userName + "_YrEnd" + archiveYear;  // Temp score table
        AddScores.createScoreTable(tempScoreTableName);			// Create a temporary score table
        tempScoreTableName = tempScoreTableName + "_Score_TBL";         // Finish off name 

//        try
//        {
            String query = "insert into " + tempScoreTableName + " SELECT  "
                    + "DateField,"
                    + "Course,"
                    + "T,"
                    + "Score,"
                    + "PCC,"
                    + "U,"
                    + "Rating,"
                    + "Slope,"
                    + "Differential"
                    + " FROM " + scoreTableName
                    + " WHERE DateField BETWEEN '" + startDate + "' AND '" + endDate + "'"
                    + " Order by DateField DESC";
            if (debug)                                                  // Debug?
            {
                System.out.println("Query = " + query);                 // Yes, display query
            }

            try (
                    PreparedStatement pst = SQLiteConnection.
                    connection.prepareStatement(query)                  // // PST
                )
            {
                pst.execute();                                          // Execute query
                JOptionPane.showMessageDialog(null, scoreTableName + 
                        " copied to " + tempScoreTableName + "\n" +
                        "Use Delete Year Data to delete archived" +
                        " year rows from DB");                          // Completion message
            }
//        }
        catch (SQLException e1)
        {
            JOptionPane.showMessageDialog(null, "Something happened here!");
            if (debug)
            {
                e1.printStackTrace();
            }
        }
    }//GEN-LAST:event_mntmArchiveScoresActionPerformed

    private void mntmRemoveNineHoleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmRemoveNineHoleActionPerformed
    {//GEN-HEADEREND:event_mntmRemoveNineHoleActionPerformed
        PrefsProc.removePref(HANDICAPNINEHOLE);                                     // Remove hanging nine hole <debug>
    }//GEN-LAST:event_mntmRemoveNineHoleActionPerformed

    private void mntmRemoveDbPathActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmRemoveDbPathActionPerformed
    {//GEN-HEADEREND:event_mntmRemoveDbPathActionPerformed
//	setPathSet(false);					// set to no path
        PrefsProc.removePref(HANDICAPDB);                                           // Remove path from prefs
    }//GEN-LAST:event_mntmRemoveDbPathActionPerformed

    private void mntmRemoveNodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmRemoveNodeActionPerformed
    {//GEN-HEADEREND:event_mntmRemoveNodeActionPerformed
/*
* 
* 	Remove preferences  mac  ~/library/com.apple.java.util.prefs.plist
* 	In the PLIST stored as className + class name making call
* 	ie "org.handicap.Handicap"
* 
*/
        int answer = JOptionPane.showConfirmDialog(null, 
                "This will remove all preferences.  Confirm:",
                "Delete Preferences", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION)                                       // Really want to remove all prefs?
        {
            PrefsProc.removeNodePref();                                             // Remove node
            PrefsProc.flushPref();                                                  // Make changes permanent
            PrefsProc.prefsNode(className);                                         // Restablish node
        }
    }//GEN-LAST:event_mntmRemoveNodeActionPerformed

    private void mntmDeleteYearScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmDeleteYearScoresActionPerformed
    {//GEN-HEADEREND:event_mntmDeleteYearScoresActionPerformed
        returnStack.push(lastCard);       // Save where we came from
        String deleteYear = JOptionPane.showInputDialog(null, "Enter year to Delete (YYYY)", "Delete Year", JOptionPane.DEFAULT_OPTION);
        if (deleteYear == null)                                    // Year entered?
        {
            return;                                                 // No, get out
        }
        if (!StringUtils.isInteger(deleteYear))                    // Valid year?
        {
            JOptionPane.showMessageDialog(null, "Delete year is invalid.", "Delete Year", JOptionPane.ERROR_MESSAGE); // No
            return;                                                 // Get out
        }
        String startDate, endDate;
        startDate = deleteYear + "-01-01";                          // Start YYYY-01-01
        endDate = deleteYear + "-12-31";                            // End   YYYY-12-31
//        try
//        {
/*			String query = "select Date, Course as 'Course Name', T, Score, U,"
 *					+ "Rating, Slope, Differential as 'Index' from " + scoreTableName + " Order by Date DESC";
 *
 * 			Display date mm/dd/yy
 */
        String query = " SELECT  "
                 + "DateField,"
                 + "Course,"
                 + "T,"
                 + "Score,"
                 + "PCC,"
                 + "U,"
                 + "Rating,"
                 + "Slope,"
                 + "Differential"
                 + " FROM " + scoreTableName
                 + " WHERE DateField BETWEEN '" + startDate + "' AND '" + endDate + "'"
                 + " Order by DateField DESC";

        try 
            (
                PreparedStatement pst = SQLiteConnection.connection.prepareStatement(query); 
                    ResultSet rs = pst.executeQuery()
            )
        {
            DisplayScores.tableDisplayScores.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e1)
        {
            if (HandicapMain.debug)
            {
                e1.printStackTrace();
            }
            return;							// If error, just get out of here
        }
        int lastRow = DisplayScores.tableDisplayScores.getRowCount();	// Rows in table
        if (lastRow <= 0)                                               // Any rows to delete?
        {
            JOptionPane.showMessageDialog(null, "No scores in year",
                    "Delete year", JOptionPane.ERROR_MESSAGE);          // No date
//            return;                                                     // Get out
        }
        else
        {
            DisplayScores.tableDisplayScores.setVisible(false);                     // Stop displaying score table
            int nineCount = 0, tCount = 0, deleteCount = 0;                         // Nine hole, tournament, and deleted counters
            int answer = JOptionPane.showConfirmDialog(null, lastRow + 
                    " - total rows for year " + deleteYear
                            + " will be deleted",
                "Delete year", JOptionPane.OK_CANCEL_OPTION);                       // Number to delete
            if (answer == JOptionPane.CANCEL_OPTION)                                // CANCEL?
            {
                redisplayScores();                                                  // Yes, redisplay & enable scores table
                HandicapMain.returnStack.pop();                                     // Pop return 
                return;                                                             // Get out
            }
            for (int row = 0; row < lastRow; row++)                                 // Loop thru all scores
            {
                String t = (String) DisplayScores.tableDisplayScores.getModel().
                        getValueAt(row, HandicapMain.T_POS);            // tourn / nine hole
                if (t.equals(TOURNINDICATOR))                           // Tournament score?
                {
                    tCount++;                                           // Yes, increment counter
                }
                else
                {
                    if (t.equals(NINEINDICATOR))                        // Nine hole score?
                    {
                        nineCount++;                                    // Yes, increment counter
                    }
                    else
                    {
                        deleteCount++;                                  // Increment delete count
                        DisplayScores.saveDate = (String) DisplayScores.tableDisplayScores.getModel().
                        getValueAt(row, HandicapMain.DATE_POS);         // Get date of record to delete
                        AddScores.deleteRow();                          // Delete row
                    }
                }
            }
            JOptionPane.showMessageDialog(null, nineCount + " - nine record(s) kept\n" +
                    tCount + " - tournament record(s) kept\n" +
                    deleteCount + " - record(s) deleted",
                    "Delete year", JOptionPane.INFORMATION_MESSAGE);                // totals message
            redisplayScores();                                                      // Redisplay & enable scores table
            HandicapMain.returnStack.pop();                                         // Pop return                
        }

    }//GEN-LAST:event_mntmDeleteYearScoresActionPerformed

    private void redisplayScores()
    {
        DisplayScores.refreshScoreTable(scoreTableName);                            // Refresh scores
        DisplayScores.tableDisplayScores.setVisible(true);                          // Enable displaying score table
    }
    
    private void mntmRemoveLFActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmRemoveLFActionPerformed
    {//GEN-HEADEREND:event_mntmRemoveLFActionPerformed
        PrefsProc.removePref(HANDICAPLOOKANDFEEL);                                  // Remove L&F
        PrefsProc.removePref(HANDICAPTHEME);                                        // Remove theme
        PrefsProc.flushPref();                                                      // Make changes permanent
    }//GEN-LAST:event_mntmRemoveLFActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        System.out.println(HandicapMain.returnStack.toString().replaceAll("\\[", "").replaceAll("]", ""));
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mntmAddPlayerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mntmAddPlayerActionPerformed
    {//GEN-HEADEREND:event_mntmAddPlayerActionPerformed
        userName = JOptionPane.showInputDialog("Your first name");                  // Get user name
        setTitle("Handicap - " + userName);                                         // Set screen title "Handicap - userName"
        PrefsProc.putPref(HANDICAPUSERNAME, userName);                              // Save user name in preferences
        AddScores.createScoreTable(userName);                                       // Create SCORE table
        Preferrences.addPlayerToCombo(userName);                                    // Add to player combobox
        // New DB
        DisplayScores.scoreDataChanged = true;                                      // Must force redisplay scores
        PrefsProc.flushPref();                                                      // Make changes permanent
        pathSet = true;
    }//GEN-LAST:event_mntmAddPlayerActionPerformed
   
    /*
    Beginning of common methods within HandicapMain class
    */
    
    /**
     * This method sets the look and feel
     *
     * metal is cross platform Theme can be either "DefaultMetal" or "Ocean"
     * System Motif GTK Windows
     *
     * @param lookAndFeelName
     * @param THEME
     */
    public static void initLookAndFeel(String lookAndFeelName, String THEME)
    {
        String lookAndFeelClass = null;
//        if (lookAndFeelName != null)
//        {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
        {
            if (lookAndFeelName.equals(info.getName()))         // Name we are looking for? 
            {
                lookAndFeelClass = (info.getClassName());        // Yes, get class
                lookAndFeel = info.getName();                   // Set name
                break;
            }
        }
        if (lookAndFeelClass == null)                           // Did we find what we were looking for?
        {
            lookAndFeelClass = UIManager.
                    getSystemLookAndFeelClassName();            // No, set class to system
        }
        try
        {
            UIManager.setLookAndFeel(lookAndFeelClass);

            // If L&F = "Metal", set the theme
            if (lookAndFeelName.equals("Metal"))
            {
                if (THEME.equals("DefaultMetal"))
                {
                    MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                } 
                else if (THEME.equals("Ocean"))
                {
                    MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                }
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            }
        } 
        catch (ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "Couldn't find class for specified look and feel: "
                    + lookAndFeel + "\n"
                    + "Did you include the L&F library in the class path?" + "\n"
                    + "Using the default look and feel.");
        } 
        catch (UnsupportedLookAndFeelException e)
        {
            JOptionPane.showMessageDialog(null, "Can't use the specified look and feel (" + lookAndFeel
                    + ") on this platform.\n"
                    + "Using the default look and feel.\n\n"
                    + "Select another in preferences.");
        } 
        catch (IllegalAccessException | InstantiationException e)
        {
            JOptionPane.showMessageDialog(null, "Couldn't get specified look and feel (" + lookAndFeel + "), for some reason. \n"
                    + "Using the default look and feel.");
            System.err.println("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
//            lookAndFeel = lookAndFeelName;
//        }
    }
    /**
     * This method sets the frame title to the passed string
     * @param title 
     */
    
        public static void setFrameTitle(String title)
        {
            handicapFrame.setTitle(title);                          // Set title in main frame
        }
        
    /**
     * This method determines where we are returning to and sets the frame title
     */
        
        public static void resetTitle()
        {
            if (!returnStack.empty())                       // Where we are returning, empty?                      
            {
                switch ((String) returnStack.peek())        // No, set frame title
                {
                    case HandicapMain.MAINMENU:
                        setFrameTitle("Handicap Main Menu - " + userName);
                        break;
                    case HandicapMain.DISPLAYSCORES:
                        setFrameTitle("Handicap Display Scores - " + userName);
                        break;
                    case HandicapMain.MAINTAINCOURSES:
                        setFrameTitle("Handicap Maintain Courses - " + userName);
                        break;
                    case HandicapMain.ADDSCORES:
                        setFrameTitle("Handicap Add Scores - " + userName);
                        break;
                    default:
                        setFrameTitle("Handicap - " + userName);
                        break;
                } 
            }
            else                                            // Yes, set frame title default
                setFrameTitle("Handicap - " + userName);
        }
        
 /**    Method to print returnStack Stack
  * invoke with
        int lineNum = Thread.currentThread().getStackTrace()[1].
                getLineNumber();                                        // *****debug*****
        printReturnStack(this.getClass().getName() +
                " " + lineNum);                                         // *****debug*****
  * @param where 
  */       
        void printReturnStack (String where)
        {
            System.out.println(where);
//            System.out.println(returnStack.toString().replaceAll("\\[", "").replaceAll("]", ""));
            System.out.println(returnStack.toString());
        }
    
/**
 *  Initialization
 */
    void start(String args[])
    {        
        handicapFrame = this;                                                       // Main frame

        dbPath = PrefsProc.getPref(HANDICAPDB, NODB);                               // Path in preferences
        debug = PrefsProc.getBooleanPref(HANDICAPDEBUG, NODG);                      // Debug in preferences

        operatingSystem = System.getProperty("os.name");                            // Get OS
        if (debug)
        {
            System.out.println(operatingSystem);                                    // <debug>

            System.out.println("Java version: " +
                    System.getProperty("java.runtime.version"));                    // <debug>
        }

        scoreTableName = PrefsProc.getPref(HANDICAPSCORETABLENAME, NOST);           // SCORE table
        userName = PrefsProc.getPref(HANDICAPUSERNAME, NOUN);                       // User name

        pathSet = true;                                                             // Assume handicap DB has been initialized
        if (dbPath.equals(NODB))                                                    // Have we setup DB?
            pathSet = false;                                                        // Handicap DB has not been initialized
        /*
        Setup the display panels. The order is important because some of the 
        constructors use fields in earlier classes
        */
        HandicapMainMenu mm = new HandicapMainMenu(dbPath);                         // Handicap main menu class (path to DB)
        AddScores as = new AddScores();                                             // Add scores class
        MaintainCourses mc = new MaintainCourses(pathSet);                          // Maintain courses class
        DisplayScores ds = new DisplayScores();                                     // Display scores class
        Preferrences pr = new Preferrences(pathSet);                                // Preferences class
        
        /*
            Setup card layout as layout manager
        */
        
        setLayout(cards);                                                           // Set card layout
        add(mm, MAINMENU);                                                          // Add main menu to card layout
        add(mc, MAINTAINCOURSES);                                                   // Add maintain courses to card layout
        add(ds, DISPLAYSCORES);                                                     // Add display scores to card layout
        add(as, ADDSCORES);                                                         // Add scores to card layout
        add(pr, PREFERRENCES);                                                      // Add preferences to card layout
        this.setLocationRelativeTo(null);                                           // Center JFrame on screen
        
        if (!debug)                                                                 // Debug on?
        {
            mnDebug.setVisible(false);                                              // No, set debug menu invisible
        }
        setFrameTitle("Handicap Main Menu - " + HandicapMain.userName);
        handicapFrame.setVisible(true);                                             // HandicapMain visible
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    public static javax.swing.JMenu mnDebug;
    private javax.swing.JMenu mnEdit;
    private javax.swing.JMenu mnFile;
    private javax.swing.JMenu mnHandicap;
    private javax.swing.JMenu mnTools;
    private javax.swing.JMenu mnView;
    private javax.swing.JMenuItem mntmAbout;
    private javax.swing.JMenuItem mntmAddPlayer;
    private javax.swing.JMenuItem mntmAddScore;
    private javax.swing.JMenuItem mntmArchiveScores;
    private javax.swing.JMenuItem mntmDeleteYearScores;
    private javax.swing.JMenuItem mntmDisplayArchiveScores;
    private javax.swing.JMenuItem mntmDisplayScores;
    private javax.swing.JMenuItem mntmDisplayTournamentScores;
    private javax.swing.JMenuItem mntmEditCourse;
    private javax.swing.JMenuItem mntmExiHandicap;
    private javax.swing.JMenuItem mntmNewDb;
    private javax.swing.JMenuItem mntmPreferrences;
    private javax.swing.JMenuItem mntmRemoveDbPath;
    private javax.swing.JMenuItem mntmRemoveLF;
    private javax.swing.JMenuItem mntmRemoveNineHole;
    private javax.swing.JMenuItem mntmRemoveNode;
    private javax.swing.JMenuItem mntmSwitchDb;
    // End of variables declaration//GEN-END:variables

}
