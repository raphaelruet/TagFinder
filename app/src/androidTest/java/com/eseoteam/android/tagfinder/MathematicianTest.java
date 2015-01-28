package com.eseoteam.android.tagfinder;

import android.test.InstrumentationTestCase;

import com.eseoteam.android.tagfinder.Mathematician;

/**
 * Created on 25/01/15.
 * Class test of Mathematician
 * @author Charline LEROUGE.
 * @version 0.1.
 */
public class MathematicianTest extends InstrumentationTestCase {


    private int[][] table;

    private static final int ANGLE = 360;

    private static final int ROW = 3;

    /**
     * Test the method addData
     */
    public void testAddData(){

        int i = 0;
        table = new int[3][360];

        table = new int[ROW][ANGLE];

        //Initialisation of the table
        for (i = 0; i <ANGLE; i ++){
            this.table[0][i] = i ;
            this.table[1][i] = 0 ;
            this.table[2][i] = 0;
        }

        final Mathematician math = new Mathematician();

        int angle = 36;
        int rawRssi = 52;
        int nbPassage = 1;

        math.addData(angle, rawRssi, nbPassage);

        table[0][1] = 36;
        table[1][1] = 52;
        table[2][1] = 2;

        // Comparison between values add to mathematician and known value in the table
        assertEquals("addDataTest - Test1 - equals",table[1][1],math.getRssi(36) );
        assertEquals("addDataTest - Test2 - equals", table[2][1], math.getNbPassage(36));

    }

    /**
     * Test the method medianAlgorithm
     */
    public void testMedianAlgorithm(){
        int angle;
        final Mathematician math = new Mathematician();

        //Fill the matrix with different RSSI values
        for (angle = 0; angle <20; angle ++){
            math.addData(angle,-80,1);
        }
        for(angle=20; angle<50; angle++){
            math.addData(angle,-60,1);
        }
        for(angle=50; angle<100; angle++){
            math.addData(angle,-80,1);
        }
        for(angle=100; angle<150; angle++){
            math.addData(angle,-20,1);
        }
        for(angle=150; angle<200; angle++){
            math.addData(angle,-80,1);
        }
        for(angle=200; angle<280; angle++){
            math.addData(angle,-30,1);
        }
        for(angle=280; angle<360; angle++){
            math.addData(angle,-80,1);
        }

        math.medianAlgorithm();

        assertEquals("testMedianAlgorithm - Test1 - equals",0,math.getRssi(30) );
        assertEquals("testMedianAlgorithm - Test2 - equals",30,math.getRssi(120));

    }

    /**
     * Test the method bestZoneSelection
     */
    public void testBestZoneSelection(){

        int angle = 0;
        final Mathematician math = new Mathematician();

        //Fill the matrix with different RSSI values
        for (angle = 0; angle <20; angle ++){
            math.addData(angle,0,1);
        }
        for(angle=20; angle<50; angle++){
            math.addData(angle,0,1);
        }
        for(angle=50; angle<100; angle++){
            math.addData(angle,0,1);
        }
        for(angle=100; angle<150; angle++){
            math.addData(angle,30,1);
        }
        for(angle=150; angle<200; angle++){
            math.addData(angle,0,1);
        }
        for(angle=200; angle<280; angle++){
            math.addData(angle,20,1);
        }
        for(angle=280; angle<360; angle++){
            math.addData(angle,0,1);
        }

        int[] finalTab = math.bestZoneSelection();

        assertEquals("testBestZoneSelection - Test1 - equals",200,finalTab[0]);
        assertEquals("testBestZoneSelection - Test2 - equals",279,finalTab[1]);

    }


}
