/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap;

import javax.swing.SwingUtilities;

/**
 *
 * @author peterream
 */
public final class HandicapLauncher
{
    private HandicapLauncher()
    {
        
    }
    
/**
 * 
 * @param args the command line arguments
 */
    public static void main(String args[]) 
    {
        /* Create and display the form */
        SwingUtilities.invokeLater(() -> 
        {
            new HandicapMain().start(args);
        });
    }
}
