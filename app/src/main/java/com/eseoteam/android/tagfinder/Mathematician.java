package com.eseoteam.android.tagfinder;

import java.util.ArrayList;

/**
 * Created on 22/01/15.
 *
 * Algorithms to determine the right direction depending on the RSSI level and angle
 *
 * @author Charline LEROUGE.
 * @version 0.1.
 */
public class Mathematician {

    private static final int ANGLE = 360;
    private static final int ROW = 3;

    private int[][] angleTable;

    private ArrayList<Zone> zoneList = new ArrayList<>();

    /**
     * Constructor of the matrix containing information
     * Three rows.
     */
    public Mathematician() {
        int angle = 0;
        angleTable = new int[ROW][ANGLE];

        for (angle = 0; angle <ANGLE; angle ++){
            this.angleTable[0][angle] = angle ;
            this.angleTable[1][angle] = 0 ;
            this.angleTable[2][angle] = 0;
        }
    }

    /**
     * Calculation of the average RSSI for each angle
     * @param angle first row
     * @param rawRssi second row
     * @param nbPassage third row
     */
    public void addData (int angle, int rawRssi, int nbPassage){

        int rssi;
        int processedRssi = 0;

        if(nbPassage == 0){
            rssi = rawRssi;
            nbPassage = 1;
        }else{
            processedRssi = ( (processedRssi * (nbPassage - 1 ) + rawRssi) /nbPassage );
            rssi = processedRssi ;
            nbPassage ++;
        }

        this.angleTable[1][angle] = rssi ;
        this.angleTable[2][angle] = nbPassage;
    }

    public int getRssi(int angle){
       return this.angleTable[1][angle];
    }

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

        int median = ((maxRSSI + minRSSI) / 2);
        for(angle = 0; angle < ANGLE; angle++) {
            if (angleTable[1][angle] > median){
                angleTable[1][angle] = angleTable[1][angle] - median;
            }else{
                angleTable[1][angle] = 0;
            }
        }
    }

    /**
     * Calculation of the maximum RSSI value
     */
    public int maxRSSI(){
        int angle;
        int maxRSSI = 0;

        for(angle = 0; angle < ANGLE; angle++){
            if (angleTable[1][angle]> maxRSSI){
                maxRSSI = angleTable[1][angle];
            }
        }
        return maxRSSI;
    }

    /**
     * Calculation of the minimum RSSI value
     */
    public int minRSSI(){

        int angle;
        int minRSSI = angleTable[1][0];

        for(angle = 1; angle < ANGLE; angle++){
            if (angleTable[1][angle]< angleTable[1][angle-1]){
                minRSSI = angleTable[1][angle];
            }
        }
        return minRSSI;

    }

    /**
     * Split values into possible zones.
     * Select best zone.
     * @return tab with angles values of the searched tag
     */
    public int[] bestZoneSelection(){

        int [] angleDirection = new int [2];
        
        splitInZone();
        
        int indexZone = tagZoneSelection();

        //Put angleStart and angleStop in a tab
        angleDirection[0] = zoneList.get(indexZone).getAngleStart();
        angleDirection[1] = zoneList.get(indexZone).getAngleStop();

        return angleDirection;
    }


    /**
     * Add Zone in ArrayList when RSSI value is positive
     */
    public void splitInZone(){

        int currentAngle;
        int angleStart = 0 ;
        int angleStop = 0;
        int angleSize = 0;

        // Determine angleStart and angleStop of each zone
        for (currentAngle = 0; currentAngle < ANGLE; currentAngle++){
            if (currentAngle == 0 && angleTable[1][currentAngle] != 0) {
                angleStart = currentAngle;
            }
            if ((angleTable[1][currentAngle] != 0) && (angleTable[1][currentAngle-1]==0)){
                angleStart = currentAngle;
            }
            if((angleTable[1][currentAngle] != 0) && (angleTable[1][currentAngle+1]==0)) {
                angleStop = currentAngle;
            }
            if(currentAngle == ANGLE-1 && angleTable[1][currentAngle] != 0) {
                angleStop = currentAngle;
            }

            // Create zone anf put them in an ArrayList
            if(angleStart!= 0 && angleStop!=0) {
                Zone zone = new Zone(angleStart, angleStop, angleSize) ;

                zone.angleStart = angleStart;
                zone.angleStop = angleStop;
                zone.angleSize = angleStop - angleStart;
                zoneList.add(zone);

                angleStart = 0;
                angleStop = 0;
            }
        }

    }


    /**
     * Determination of the zone with the biggest area
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
        }
        return indexZone;
    }

}
