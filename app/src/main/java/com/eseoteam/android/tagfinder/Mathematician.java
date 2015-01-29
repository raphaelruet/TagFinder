package com.eseoteam.android.tagfinder;

import java.util.ArrayList;

/**
 * Created on 22/01/15.
 *
 * Algorithms to determine the right direction depending on the RSSI level and angle
 * @author Charline LEROUGE, Pierre TOUZE.
 * @version 0.2.
 */
public class Mathematician {

    /**
     * Number of degrees in a circle.
     */
    private static final int ANGLE = 360;

    /**
     * Number of rows in the matrix containing the data.
     */
    private static final int ROW = 3;

    /**
     * The minimum value of rssi.
     */
    private static final int MIN_RSSI = -80;

    /**
     * The matrix containing the rssi, readCount in function of the angle.
     */
    private int[][] angleTable;

    /**
     * List of the different zone where a tag has been detected.
     */
    private ArrayList<Zone> zoneList;

    /**
     * Constructor of the matrix containing information
     * Three rows.
     */
    public Mathematician() {
        int angle ;
        angleTable = new int[ROW][ANGLE];

        for (angle = 0; angle <ANGLE; angle ++){
            this.angleTable[0][angle] = angle ;
            this.angleTable[1][angle] = MIN_RSSI;
            this.angleTable[2][angle] = 0;
        }
        zoneList  = new ArrayList<>();
    }

    /**
     * Calculation of the average RSSI for each angle
     * @param angle first row
     * @param rawRssi second row
     * @param nbPassage third row
     */
    public void addData (int angle, int rawRssi, int nbPassage){
        /*
        int rssi;
        int processedRssi = 0;

        if(nbPassage == 0){
            rssi = rawRssi;
            nbPassage = 1;
        }else{
            processedRssi = ( (processedRssi * (nbPassage - 1 ) + rawRssi) /nbPassage );
            rssi = processedRssi ;
            nbPassage ++;
        }*/
        this.angleTable[1][angle] = rawRssi ;
        this.angleTable[2][angle] = nbPassage;
    }

    /**
     * Gives the rssi of the searched tag.
     * @param angle The angle corresponding to the desired rssi.
     * @return The rssi at the specified angle.
     */
    public int getRssi(int angle){
       return this.angleTable[1][angle];
    }

    /**
     * Gives the number of time a rssi has been received at a sepcific angle.
     * @param angle The angle corresponding to the desired nbPassage.
     * @return The number of passage.
     */
    public int getNbPassage(int angle){
        return this.angleTable[2][angle];
    }

    /**
     * Calculation of the RSSI values median
     * Change values depending on the median
     */
    public void medianAlgorithm(){
        int angle;
        int maxRSSI = maxRSSI();
        int minRSSI = minRSSI();

        int median = (int)Math.floor((maxRSSI + minRSSI) / 2);
        for(angle = 0; angle < ANGLE; angle++) {
            if (angleTable[1][angle] > median){
                angleTable[1][angle] = angleTable[1][angle] - median;
            }else{
                angleTable[1][angle] = 0;
            }
        }
    }

    /**
     * Calculation of the maximum RSSI value.
     */
    private int maxRSSI(){
        int angle;
        int maxRSSI = -80;

        for(angle = 0; angle < ANGLE; angle++){
            if (angleTable[1][angle]> maxRSSI){
                maxRSSI = angleTable[1][angle];
            }
        }
        return maxRSSI;
    }

    /**
     * Calculation of the minimum RSSI value.
     */
    private int minRSSI(){

        int angle;
        int minRSSI = 0;

        for(angle = 0; angle < ANGLE; angle++){
            if (angleTable[1][angle]< minRSSI && angleTable[1][angle] != MIN_RSSI){
                minRSSI = angleTable[1][angle];
            }
        }
        return minRSSI;
    }

    /**
     * Split values into possible zones.
     * Select best zone.
     * @return tab with angles values of the searched tag.
     */
    public int[] bestZoneSelection(){
        this.medianAlgorithm();
        int [] angleDirection = new int [2];
        
        splitInZone();
        int indexZone = tagZoneSelection();

        //Put angleStart and angleStop in a tab
        angleDirection[0] = zoneList.get(indexZone).getAngleStart();
        angleDirection[1] = zoneList.get(indexZone).getAngleStop();
        return angleDirection;
    }


    /**
     * Add Zone in ArrayList when RSSI value is positive.
     */
    public void splitInZone(){

        int currentAngle;
        int angleStart = 0 ;
        int angleStop = 0;

        // Determine angleStart and angleStop of each zone
        for (currentAngle = 0; currentAngle < ANGLE; currentAngle++) {
            if (currentAngle == 0) {
                if (angleTable[1][currentAngle] != 0) {
                    angleStart = currentAngle;
                }
            } else if (currentAngle == ANGLE - 1) {
                 if(angleTable[1][currentAngle] != 0) {
                     angleStop = currentAngle;
                 }
            } else {
                if ((angleTable[1][currentAngle] != 0) && (angleTable[1][currentAngle - 1] == 0)) {
                    angleStart = currentAngle;
                }
                if ((angleTable[1][currentAngle - 1] != 0) && (angleTable[1][currentAngle] == 0)) {
                    angleStop = currentAngle-1;
                }
            }
            // Create zone and put them in an ArrayList
            if(angleStart!= angleStop && angleStop!=0) {

                Zone zone = new Zone(angleStart, angleStop, angleStop - angleStart) ;
                zoneList.add(zone);

                angleStart = 0;
                angleStop = 0;
            }
        }
    }

    /**
     * Determination of the zone with the biggest area.
     */
    public int tagZoneSelection(){
        int sumRssi =0;
        int biggestArea =0;
        int indexZone =0;

        // Calculate area of each zone to return the biggest
        for (int indexOfZone=0; indexOfZone<zoneList.size(); indexOfZone++) {
            for(int j=zoneList.get(indexOfZone).getAngleStart(); 
                j<=zoneList.get(indexOfZone).getAngleStop();j++ ){
                sumRssi += angleTable[1][j];
            }
            if (sumRssi*zoneList.get(indexOfZone).getAngleSize() > biggestArea){
                biggestArea = sumRssi*zoneList.get(indexOfZone).getAngleSize();
                indexZone = indexOfZone;
            }
            sumRssi = 0;
        }
        return indexZone;
    }
}
