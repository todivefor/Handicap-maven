/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap.process;

/**
 *
 * @author peterream
 * 
 * This class contains 2 elements, the calculated GHIN differential and
 * the JTable row number contained in the DisplayScores() class.
 */
public class DiffRow implements Comparable<DiffRow>
{
    public double differential;
    public int tableRowNumber;
 
    /**
     * This is the constructor for the DiffRow class
     * @param diff - differential
     * @param row - JTable row number
     */
    public DiffRow(double diff, int row)
    {
        this.differential = diff;
        this.tableRowNumber = row;
    }

    /**
     * This method is a bubble sort of the passed array with differential as
     * key in ascending order.
     * @param diffRows  array of DiffRow objects
     */
    public static void sort(DiffRow[] diffRows)
    {
        boolean swapped = true;                                                     // Stopper
        while (swapped)                                                             // Run until no longer swapping
        {
            swapped = false;                                                        // No swap
            for (int n = 0; n < diffRows.length - 1; n++)                            // Loop thru 1 less than length
            {
                if (diffRows[n].differential > diffRows[n + 1].differential)          // Nth diff > n+1th diff?
                {
                    swap(diffRows, n);                                               // Yes - swap it
                    swapped = true;                                                 // Keep going				
                }
            }
        }
    }

    /**
     * This method swaps the Nth and Nth + 1 array elements
     * @param diffRow  Array on which we are working
     * @param first  Nth element
     */
    static void swap(DiffRow[] diffRow, int first)
    {
        DiffRow tempDiffRow = diffRow[first + 1];
        diffRow[first + 1] = diffRow[first];
        diffRow[first] = tempDiffRow;
    }

    /**
     * This method is for the Comparable interface, and therefore the Arrays.sort.
     * @param o  DiffRow object
     * @return 
     */
    @Override
    public int compareTo(DiffRow o)
    {
        int lastCmp = 0;
        if (this.differential < o.differential)
            lastCmp = -1;
        if (this.differential == o.differential)
            lastCmp = 0;
        if (this.differential > o.differential)
            lastCmp = 1;
        return lastCmp;
    }

    /**
     * This method makes sense of the DiffRow object
     * @return 
     */
    @Override
    public String toString()
    {
        return "Differential: " + this.differential + " Table row: " +
                this.tableRowNumber;
    }
}

