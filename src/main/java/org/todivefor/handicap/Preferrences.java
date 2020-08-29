/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap;

import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.todivefor.handicap.process.PrefsProc;

/**
 *
 * @author peterream
 */
public class Preferrences extends javax.swing.JPanel
{
    public static JFileChooser fc;
    public static ActionListener lookandFeelListener;                   // Add look and feel listener listener
    public static ActionListener lookandFeelThemeListener;              // Add look and feel theme listener listener
    public static ActionListener playerListener;                        // Player combobox listener

    /**
     * Creates new form Preferences
     */
    public Preferrences(boolean pathSet)
    {
        initComponents();
        
/*
 * 		End of GUI setup
 */

/*
* 	Load themes for "metal"
 */
        comboBoxPreferencesTheme.addItem("DefaultMetal");                           // Ocean or DefaultMetal if Metal
        comboBoxPreferencesTheme.addItem("Ocean");

        String lAndF = PrefsProc.getPref(HandicapMain.
                HANDICAPTHEME, HandicapMain.NOTH);                                  // Them from prefs
        if (!lAndF.equals(HandicapMain.NOLF))                                       // Any theme there
                comboBoxPreferencesTheme.setSelectedItem(lAndF);                    // Yes, display in combobox
		
//      Get look and feel names in JDK and add to L&F combobox

        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks)
        {
            if (HandicapMain.debug)
                System.out.println(look.getName() + ":  " + look.getClassName());
            comboBoxPreferencesLookAndFeel.addItem(look.getName());                     // Add look & feel name
        }
		
        lAndF = PrefsProc.getPref(HandicapMain.HANDICAPLOOKANDFEEL, 
                HandicapMain.NOLF);                                                 // Look and feel from prefs
        if (!lAndF.equals(HandicapMain.NOLF))                                       // Anything set?
        {
            comboBoxPreferencesLookAndFeel.setSelectedItem(lAndF);                  // Yes, show it in combobox
            if (!lAndF.equals("Metal"))                                             // Meta?
            {
                comboBoxPreferencesTheme.setVisible(false);                         // No, make theme box invisible
                lblPreferencesTheme.setVisible(false);
                PrefsProc.removePref(HandicapMain.HANDICAPTHEME);                   // Delete any theme from prefs
            }
        }
        comboBoxPreferencesLookAndFeel.addActionListener(
                lookandFeelListener);                                               // Add listener for L&F combobox
        comboBoxPreferencesTheme.addActionListener(
                lookandFeelThemeListener);                                          // Add listener for L&F theme combobox
        if (pathSet)                                                                // DB open?
            prefsLoadPlayerCombo();                                                 // Yes, load player combo
    }
    
    public static void prefsLoadPlayerCombo()
    {
//      Load players combo box
        
        comboBoxPreferencesPlayer.removeActionListener(playerListener);             // Remove listener while loading 
        comboBoxPreferencesPlayer.removeAllItems();
        DatabaseMetaData md;                                                        // DB meta data
        ResultSet rs;
        String dbPath = PrefsProc.getPref(HandicapMain.HANDICAPDB,
                HandicapMain.NODB);                                                 // Get DB path
        if (dbPath.equals(HandicapMain.NODB))                                       // Have DB?
            return;                                                                 // No
        try
        {
            md = SQLiteConnection.connection.getMetaData();                         // Retrieve DB meta data
            rs = md.getTables(null, null, "%", null);                               // Table names
            while (rs.next())                                                       // Loop thru table names
            {
                String table = rs.getString(3);                                     // Table name
                if (table.contains("SCORE"))                                        // SCORE table?
                    if (!table.contains("YrEnd"))                                   // Yes, but not YrEnd?
                    {
                        int pastName = table.indexOf("_");                          // Index past name
                        table = table.substring(0, pastName);
                        comboBoxPreferencesPlayer.addItem(table);                   // Add to combo box
                    }
                if (HandicapMain.debug)
                    System.out.println(rs.getString(3));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Preferrences.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }       
        comboBoxPreferencesPlayer.addActionListener(playerListener);                // Add action listener for player
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelCenter = new javax.swing.JPanel();
        //Create a file chooser
        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("sqlite DB", "sqlite");
        fc.addChoosableFileFilter(filter1);

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
            //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //        Default is file only
        //        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        lblLookAndFeel = new javax.swing.JLabel();
        comboBoxPreferencesLookAndFeel = new javax.swing.JComboBox<>();
        lblPreferencesTheme = new javax.swing.JLabel();
        comboBoxPreferencesTheme = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        comboBoxPreferencesPlayer = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        textPreferencesLHI = new javax.swing.JTextField();
        btnPreferencesDebug = new javax.swing.JButton();
        chkBoxPreferencesWHC = new javax.swing.JCheckBox();

        lblLookAndFeel.setText("Look and Feel");

        comboBoxPreferencesLookAndFeel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{} ));
        lookandFeelListener = new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                comboBoxPreferencesLookAndFeelActionPerformed(evt);
            }
        };

        lblPreferencesTheme.setText("Theme");

        comboBoxPreferencesTheme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{} ));
        lookandFeelThemeListener = new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                comboBoxPreferencesThemeActionPerformed(evt);
            }
        };

        jLabel1.setText("Player");

        playerListener = new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                comboBoxPreferencesPlayerActionPerformed(evt);
            }
        };

        jLabel2.setText("Low HI:");

        textPreferencesLHI.setText("99.9");
        textPreferencesLHI.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                textPreferencesLHIActionPerformed(evt);
            }
        });

        btnPreferencesDebug.setText("Debug");
        btnPreferencesDebug.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreferencesDebugActionPerformed(evt);
            }
        });

        chkBoxPreferencesWHC.setText("Old HC");
        chkBoxPreferencesWHC.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkBoxPreferencesWHCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCenterLayout = new javax.swing.GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenterLayout.setHorizontalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLookAndFeel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPreferencesTheme, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(chkBoxPreferencesWHC, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCenterLayout.createSequentialGroup()
                        .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxPreferencesTheme, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxPreferencesLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCenterLayout.createSequentialGroup()
                        .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelCenterLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnPreferencesDebug, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCenterLayout.createSequentialGroup()
                                .addComponent(comboBoxPreferencesPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(textPreferencesLHI, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97)))
                        .addGap(267, 267, 267))))
        );

        panelCenterLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comboBoxPreferencesLookAndFeel, comboBoxPreferencesPlayer, comboBoxPreferencesTheme});

        panelCenterLayout.setVerticalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCenterLayout.createSequentialGroup()
                .addGap(0, 100, Short.MAX_VALUE)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLookAndFeel)
                    .addComponent(comboBoxPreferencesLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPreferencesTheme)
                    .addComponent(comboBoxPreferencesTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxPreferencesPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(textPreferencesLHI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPreferencesDebug)
                    .addComponent(chkBoxPreferencesWHC))
                .addGap(32, 32, 32))
        );

        panelCenterLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {comboBoxPreferencesLookAndFeel, comboBoxPreferencesPlayer, comboBoxPreferencesTheme, textPreferencesLHI});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCenter, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        textPreferencesLHI.addFocusListener(new java.awt.event.FocusAdapter()
            {
                public void focusGained(java.awt.event.FocusEvent evt)
                {
                    SwingUtilities.invokeLater(() ->
                        {
                            textPreferencesLHI.selectAll();
                        });
                    }
                });
            }// </editor-fold>//GEN-END:initComponents

    static void addPlayerToCombo(String userName)
    {
        comboBoxPreferencesPlayer.addItem(userName);                // Add to combo box
    }
    
    private void btnPreferencesDebugActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPreferencesDebugActionPerformed
    {//GEN-HEADEREND:event_btnPreferencesDebugActionPerformed
   
/*
 * 					Toggle debug						
 */
        if (HandicapMain.debug)                                                     // Debug on
        {
            HandicapMain.debug = false;                                             // Yes, turn off
            HandicapMain.mnDebug.setVisible(false);                                 // Turn off debug menu
            PrefsProc.putBooleanPref(HandicapMain.HANDICAPDEBUG, false);            // Set debug off
        } 
        else                                                                        // No
        {
            HandicapMain.debug = true;                                              // Turn off
            HandicapMain.mnDebug.setVisible(true);                                  // Turn debug menu
            PrefsProc.putBooleanPref(HandicapMain.HANDICAPDEBUG, true);             // Set debug on
        }
        PrefsProc.flushPref();                                                      // Make all preference changes permanent
    }//GEN-LAST:event_btnPreferencesDebugActionPerformed

    private void textPreferencesLHIActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_textPreferencesLHIActionPerformed
    {//GEN-HEADEREND:event_textPreferencesLHIActionPerformed
        String userHANDICAPLOWHI = HandicapMain.userName + HandicapMain.HANDICAPLOWHI;
        String lowHIS = textPreferencesLHI.getText();
        PrefsProc.putPref(userHANDICAPLOWHI,lowHIS);                                //  Save low HI
        PrefsProc.flushPref();                                                      // Make all preference changes permanent
        System.out.println("Low HI: " + lowHIS);
    }//GEN-LAST:event_textPreferencesLHIActionPerformed

    private void chkBoxPreferencesWHCActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkBoxPreferencesWHCActionPerformed
    {//GEN-HEADEREND:event_chkBoxPreferencesWHCActionPerformed
        DisplayScores.refreshScoreTable(HandicapMain.scoreTableName);       // Change state, refresh
    }//GEN-LAST:event_chkBoxPreferencesWHCActionPerformed

/*
 *  Beginning of common methods within Preferrences class
 */
    
/**
 * 
 *  Read Handicap DB looking for player_SCORE_TBL
 *  Add player to player combo box
 * @param connection 
 */
/*
    public static void loadPlayersInDB(Connection connection)
    {
        DatabaseMetaData md;
        ResultSet rs;
        try
        {
            md = connection.getMetaData();
            rs = md.getTables(null, null, "%", null);
            while (rs.next()) 
            {
                comboBoxPreferencesPlayer.addItem(rs.getString(3));
                System.out.println(rs.getString(3));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Preferrences.class.getName()).log(Level.SEVERE, null, ex);
        }   

    }
*/

/*
        My maintained event handlers
*/ 
    
    private void comboBoxPreferencesLookAndFeelActionPerformed(java.awt.event.ActionEvent evt)                                                               
    {                                                                   
        HandicapMain.lookAndFeel = 
                (String) (comboBoxPreferencesLookAndFeel.
                getSelectedItem());                                                 // No
        if (HandicapMain.lookAndFeel.equals("Metal"))                               // Is this metal?
        {
            lblPreferencesTheme.setVisible(true);                                   // Metal, so set themes label visible in Prefs
            comboBoxPreferencesTheme.setVisible(true);                              // Set themes combobox visible
        } 
        else                                                                        // Not metal
        {
            lblPreferencesTheme.setVisible(false);                                  // Set themes label invisible in Prefs
            comboBoxPreferencesTheme.setVisible(false);                             // Set themes combobox not visible
            HandicapMain.initLookAndFeel(HandicapMain.
                    lookAndFeel, HandicapMain.THEME);                               // No theme, so go
        }
        PrefsProc.putPref(HandicapMain.HANDICAPLOOKANDFEEL, 
                HandicapMain.lookAndFeel);                                          // Set L & F in Prefs
        SwingUtilities.updateComponentTreeUI(getParent());                          // Update all components of frame
        DisplayScores.scoreDataChanged = true;                                      // Re-due display scores
        if (HandicapMain.lastCard.equals(HandicapMain.MAINTAINCOURSES))             // Are we changing L&F from course display?
            MaintainCourses.refreshCourseTable(SQLiteConnection.connection,
                    HandicapMain.courseTableName);                                  // Yes, refresh course table
        if (HandicapMain.lastCard.equals(HandicapMain.DISPLAYSCORES))               // Are we changing L&F from score display?
            DisplayScores.refreshScoreTable(HandicapMain.scoreTableName);           // Refresh scores table
        PrefsProc.flushPref();                                                      // Make all preference changes permanent                                  
    }
    
    private void comboBoxPreferencesThemeActionPerformed(java.awt.event.ActionEvent evt)                                                         
    {                                                             
        String theme = (String) (comboBoxPreferencesTheme.getSelectedItem());       // No, get theme selection
        PrefsProc.putPref(HandicapMain.HANDICAPTHEME, theme);                       // Save in preferences
        PrefsProc.flushPref();                                                      // Make all preference changes permanent
        HandicapMain.initLookAndFeel(HandicapMain.lookAndFeel, theme);              // Set theme
        SwingUtilities.updateComponentTreeUI(getParent());                          // Update all components of frame
    }

    private void comboBoxPreferencesPlayerActionPerformed(java.awt.event.ActionEvent evt)                                                          
    {                                                              
            
        if (!comboBoxPreferencesPlayer.hasFocus())                                  // Loading combo?
            return;                                                                 // Yes, do nothing
        HandicapMain.userName = (String) comboBoxPreferencesPlayer.
                getSelectedItem();                                                  // Table name from combo
        PrefsProc.putPref(HandicapMain.HANDICAPUSERNAME, 
                HandicapMain.userName);                                             // Save username
        HandicapMain.scoreTableName = HandicapMain.userName + "_SCORE_TBL";         // scoreTable Name "userName_SCORE_TBL"
        PrefsProc.putPref(HandicapMain.HANDICAPSCORETABLENAME, 
                HandicapMain.scoreTableName);                                       // Save the new in preference
        HandicapMain.setFrameTitle("Handicap - " + HandicapMain.userName);          // Set screen title
        PrefsProc.flushPref();                                                      // Make all preference changes permanent
            
        String lowHI = PrefsProc.getPref(HandicapMain.userName + 
                HandicapMain.HANDICAPLOWHI, HandicapMain.NOHI);                     // Get low HI
        if (!lowHI.equals(HandicapMain.NOHI))                                       // Get one?
        {
            Preferrences.textPreferencesLHI.setText(lowHI);                         // Yes, set it
        }
            
            DisplayScores.scoreDataChanged = true;                                  // Force re-display
            DisplayScores.refreshScoreTable(HandicapMain.scoreTableName);           // Chane player, must redisplay
            MaintainCourses.refreshCourseTable(SQLiteConnection.connection, 
                    HandicapMain.courseTableName);                                  // Fill initial course table
//            HandicapMain.returnStack.push(HandicapMain.MAINMENU);                   // For main menu
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPreferencesDebug;
    public static javax.swing.JCheckBox chkBoxPreferencesWHC;
    private javax.swing.JComboBox<String> comboBoxPreferencesLookAndFeel;
    public static javax.swing.JComboBox<String> comboBoxPreferencesPlayer;
    private javax.swing.JComboBox<String> comboBoxPreferencesTheme;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblLookAndFeel;
    private javax.swing.JLabel lblPreferencesTheme;
    private javax.swing.JPanel panelCenter;
    public static javax.swing.JTextField textPreferencesLHI;
    // End of variables declaration//GEN-END:variables
}
