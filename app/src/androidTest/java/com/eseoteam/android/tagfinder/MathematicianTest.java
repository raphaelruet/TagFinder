package com.eseoteam.android.tagfinder;

import android.test.InstrumentationTestCase;

/**
 * Created on 25/01/15.
 * Class test of Mathematician
 * @author Charline LEROUGE, Raphael RUET.
 * @version 0.2.
 */
public class MathematicianTest extends InstrumentationTestCase {

    private static final int ANGLE = 360;

    private static final int ROW = 3;

    /**
     * Test the method addData
     */
    public void testAddData(){

        int i;

        int table[][] = new int[ROW][ANGLE];

        //Initialisation of the table
        for (i = 0; i <ANGLE; i ++){
            table[0][i] = i ;
            table[1][i] = 0 ;
            table[2][i] = 0;
        }

        final Mathematician math = new Mathematician();

        int angle = 36;
        int rawRssi = 52;

        math.addData(angle, rawRssi);
        math.addData(36,48);
        table[0][1] = 36;
        table[1][1] = 50;
        table[2][1] = 2;

        // Comparison between values add to mathematician and known value in the table
        assertEquals("addDataTest - Test1 - equals",table[1][1],math.getRssi(36) );
        assertEquals("addDataTest - Test2 - equals", table[2][1], math.getNbPassage(36));

    }

    public void testFilterRssi() {
        final Mathematician math = new Mathematician();
        math.addData(10, -50);
        math.addData(11,-80);
        math.addData(12,-80);
        math.addData(13,-30);
        math.addData(14,-80);
        math.addData(15,-80);
        math.addData(16,-80);
        math.addData(17,-80);
        math.addData(18,-50);

        math.filterRssi();
        assertEquals("addDataWithSmoothTest - Test5 - equals",-40,math.getRssi(11));
        assertEquals("addDataWithSmoothTest - Test5 - equals",-40,math.getRssi(12));
        assertEquals("addDataWithSmoothTest - Test5 - equals",-80,math.getRssi(17));


    }

    /**
     * Test the method medianAlgorithm
     */
    public void testMedianAlgorithm(){
        int angle;
        final Mathematician math = new Mathematician();

        //Fill the matrix with different RSSI values
        for (angle = 0; angle <20; angle ++){
            math.addData(angle,-80);
        }
        for(angle=20; angle<50; angle++){
            math.addData(angle,-60);
        }
        for(angle=50; angle<100; angle++){
            math.addData(angle,-80);
        }
        for(angle=100; angle<150; angle++){
            math.addData(angle,-20);
        }
        for(angle=150; angle<200; angle++){
            math.addData(angle,-80);
        }
        for(angle=200; angle<280; angle++){
            math.addData(angle,-30);
        }
        for(angle=280; angle<360; angle++){
            math.addData(angle,-80);
        }

        math.medianAlgorithm();

        assertEquals("testMedianAlgorithm - Test1 - equals",0,math.getRssi(30) );
        assertEquals("testMedianAlgorithm - Test2 - equals",20,math.getRssi(120));

    }

    /**
     * Test the method bestZoneSelection
     */
    public void testBestZoneSelection(){

        int angle;
        final Mathematician math = new Mathematician();

        //Fill the matrix with different RSSI values
        for (angle = 0; angle <20; angle ++){
            math.addData(angle,-80);
        }
        for(angle=20; angle<50; angle++){
            math.addData(angle,-60);
        }
        for(angle=50; angle<100; angle++){
            math.addData(angle,-80);
        }
        for(angle=100; angle<150; angle++){
            math.addData(angle,-20);
        }
        for(angle=150; angle<200; angle++){
            math.addData(angle,-80);
        }
        for(angle=200; angle<201; angle++){
            math.addData(angle,-30);
        }
        for(angle=201; angle<203; angle++){
            math.addData(angle,-80);
        }
        for(angle=203; angle<280; angle++){
            math.addData(angle,-30);
        }
        for(angle=280; angle<360; angle++){
            math.addData(angle,-80);
        }



        int[] finalTab = math.bestZoneSelection();

        assertEquals("testBestZoneSelection - Test1 - equals",200,finalTab[0]);
        assertEquals("testBestZoneSelection - Test2 - equals",279,finalTab[1]);

    }


}
