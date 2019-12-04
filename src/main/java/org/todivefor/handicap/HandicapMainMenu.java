/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap;

import java.util.Date;
import javax.swing.SwingUtilities;

/**
 *
 * @author peterream
 */
public class HandicapMainMenu extends javax.swing.JPanel
{

    /**
     * Creates new form HadicapMainMenu
     */
    public HandicapMainMenu()
    {
        initComponents();
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

        panelNorth = new javax.swing.JPanel();
        btnAddScores = new javax.swing.JButton();
        btnDisplayScores = new javax.swing.JButton();
        btnEditCourses = new javax.swing.JButton();
        panelPicture = new javax.swing.JPanel();
        lblOcc = new javax.swing.JLabel();
        panelSouth = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        btnAddScores.setText("Add Scores");
        btnAddScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddScoresActionPerformed(evt);
            }
        });
        panelNorth.add(btnAddScores);

        btnDisplayScores.setText("Display Scores");
        btnDisplayScores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDisplayScoresActionPerformed(evt);
            }
        });
        panelNorth.add(btnDisplayScores);

        btnEditCourses.setText("Edit Courses");
        btnEditCourses.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditCoursesActionPerformed(evt);
            }
        });
        panelNorth.add(btnEditCourses);

        add(panelNorth, java.awt.BorderLayout.PAGE_START);

        lblOcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/todivefor/handicap/images/OCC-9.jpg"))); // NOI18N

        javax.swing.GroupLayout panelPictureLayout = new javax.swing.GroupLayout(panelPicture);
        panelPicture.setLayout(panelPictureLayout);
        panelPictureLayout.setHorizontalGroup(
            panelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPictureLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblOcc)
                .addContainerGap())
        );
        panelPictureLayout.setVerticalGroup(
            panelPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPictureLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblOcc)
                .addGap(262, 262, 262))
        );

        add(panelPicture, java.awt.BorderLayout.CENTER);

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExitActionPerformed(evt);
            }
        });
        panelSouth.add(btnExit);

        add(panelSouth, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExitActionPerformed
    {//GEN-HEADEREND:event_btnExitActionPerformed
        System.exit(0);                                         // Exit application
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnDisplayScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDisplayScoresActionPerformed
    {//GEN-HEADEREND:event_btnDisplayScoresActionPerformed
        HandicapMain.lastCard = HandicapMain.DISPLAYSCORES;     // Where we are
        HandicapMain.returnStack.push(HandicapMain.MAINMENU);   // push MAINMENU onto returnStack
        DisplayScores.scoreEditingAllowed = true;               // score table display, can edit
        if (DisplayScores.scoreDataChanged)                     // Need to rebuild display?
        {
            DisplayScores.tournament = false;                               // Set not tournament score display
            DisplayScores.refreshScoreTable(HandicapMain.scoreTableName);   // Refresh scores table
            DisplayScores.scoreDataChanged = false;                         // No need to display until next change
        }
        HandicapMain.setFrameTitle("Handicap Display Scores - " +  HandicapMain.userName);
/*    
        int lineNum = Thread.currentThread().getStackTrace()[1].
                getLineNumber();                                        // *****debug*****
        hMain.printReturnStack(this.getClass().getName() +
                " " + lineNum);                                         // *****debug*****        
*/
        HandicapMain.cards.show(getParent(), HandicapMain.DISPLAYSCORES);   // Show DisplayScores
    }//GEN-LAST:event_btnDisplayScoresActionPerformed

    private void btnAddScoresActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddScoresActionPerformed
    {//GEN-HEADEREND:event_btnAddScoresActionPerformed
        HandicapMain.lastCard = HandicapMain.ADDSCORES;                 // Where we are
        HandicapMain.returnStack.push(HandicapMain.MAINMENU);           // push MAINMENU onto returnStack
        addScore();                                                     // Make text fields select all
//        AddScores.textFieldScore.requestFocusInWindow();                // Request focus for
        HandicapMain.setFrameTitle("Handicap Add Scores - " +  HandicapMain.userName);
        HandicapMain.cards.show(getParent(), HandicapMain.ADDSCORES);   // Go to AddScores
    }//GEN-LAST:event_btnAddScoresActionPerformed

    private void btnEditCoursesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditCoursesActionPerformed
    {//GEN-HEADEREND:event_btnEditCoursesActionPerformed
        HandicapMain.lastCard = HandicapMain.MAINTAINCOURSES;               // Where we are
        HandicapMain.returnStack.push(HandicapMain.MAINMENU);               // push MAINTAINCOURSES onto returnStack      
        HandicapMain.setFrameTitle("Handicap Maintain Courses - " +  HandicapMain.userName);
        HandicapMain.cards.show(getParent(), HandicapMain.MAINTAINCOURSES); // MAINTAINCOURSEs card
    }//GEN-LAST:event_btnEditCoursesActionPerformed

/*
 *       Beginning of common methods within DisplayScores class
 */
    
    public static void addScore()
    {
        AddScores.textFieldScore.setText(null);                     // Clear out
        AddScores.getCourse((String) AddScores.comboBoxCourse.
                getItemAt(0));                                      // First entry in course combobox

        AddScores.btnAddScoreDelete.setVisible(false);              // Make delete button invisible
        Date now = new Date();                                      // Get todays date
        AddScores.dateChooserAddScoresDate.setDate(now);            // Set todays date in date chooser
        AddScores.textFieldScore.requestFocusInWindow();            // set focus to score
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAddScores;
    public static javax.swing.JButton btnDisplayScores;
    public static javax.swing.JButton btnEditCourses;
    private javax.swing.JButton btnExit;
    private javax.swing.JLabel lblOcc;
    private javax.swing.JPanel panelNorth;
    private javax.swing.JPanel panelPicture;
    private javax.swing.JPanel panelSouth;
    // End of variables declaration//GEN-END:variables
}
