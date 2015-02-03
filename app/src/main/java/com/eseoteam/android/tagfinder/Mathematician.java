package com.eseoteam.android.tagfinder;

import java.util.ArrayList;

/**
 * Created on 22/01/15.
 *
 * Algorithms to determine the right direction depending on the RSSI level and angle
 * @author Charline LEROUGE, Pierre TOUZE.
 * @version 0.12.
 */
public class Mathematician {

    /**
     * Number of degrees in a circle.
     */
    private static final int ANGLE = 360;

    /**
     * Number of rows in the matrix containing the data.
     */
    private static final int ROW = 2;

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
     * The maximum number of angles that could conduct to a smoothing of the RSSIs
     */
    private static final int MAX_MISSED_ANGLE = 3;

    /**
     * Constructor of the matrix containing information
     * Three rows.
     */
    public Mathematician() {

        this.angleTable = new int[ROW][ANGLE];
        zoneList  = new ArrayList<>();
        initMatrix();
    }

    /**
     * Initialization of the entire matrix
     */
    public void initMatrix(){
        int angle ;
        for (angle = 0; angle <ANGLE; angle ++){
            this.angleTable[0][angle] = MIN_RSSI;
            this.angleTable[1][angle] = 0;
        }
    }

    /**
     * Clear the zone list  after a passage
     */
    public void clearZoneList() {
        if(this.zoneList.size() != 0) {
            this.zoneList.clear();
        }
    }

    /**
     * Calculation of the average RSSI for each angle
     * @param angle first row
     * @param rawRssi second row
     */
    public void addData (int angle, int rawRssi){

        int rssi;
        int nbPassage = this.angleTable[1][angle];

        if(nbPassage == 0){
            rssi = rawRssi;
        }else{
            rssi = (this.angleTable[0][angle]*nbPassage + rawRssi) / (nbPassage + 1 );
        }
        nbPassage ++;
        this.angleTable[0][angle] = rssi ;
        this.angleTable[1][angle] = nbPassage;
    }

    /**
     * Gives the rssi of the searched tag.
     * @param angle The angle corresponding to the desired rssi.
     * @return The rssi at the specified angle.
     */
    public int getRssi(int angle){
       return this.angleTable[0][angle];
    }

    /**
     * Gives the number of time a rssi has been received at a sepcific angle.
     * @param angle The angle corresponding to the desired nbPassage.
     * @return The number of passage.
     */
    public int getNbPassage(int angle){
        return this.angleTable[1][angle];
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
            if (angleTable[0][angle] > median){
                angleTable[0][angle] = angleTable[0][angle] - median;
            }else{
                angleTable[0][angle] = 0;
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
            if (angleTable[0][angle]> maxRSSI){
                maxRSSI = angleTable[0][angle];
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
            if (angleTable[0][angle]< minRSSI && angleTable[0][angle] != MIN_RSSI){
                minRSSI = angleTable[0][angle];
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
        this.rssiSmoothing();
        this.medianAlgorithm();
        int [] angleDirection = new int [2];
        
        splitInZone();
        int indexZone = tagZoneSelection();

        //Put angleStart and angleStop in a tab
        if(zoneList.size() != 0) {
            angleDirection[0] = zoneList.get(indexZone).getAngleStart();
            angleDirection[1] = zoneList.get(indexZone).getAngleStop();
        }
        else {
            angleDirection[0] = -1;
            angleDirection[1] = -1;
        }
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
                if (angleTable[0][currentAngle] != 0) {
                    angleStart = currentAngle;
                }
            } else if (currentAngle == ANGLE - 1) {
                 if(angleTable[0][currentAngle] != 0) {
                     angleStop = currentAngle;
                 }
            } else {
                if ((angleTable[0][currentAngle] != 0) && (angleTable[0][currentAngle - 1] == 0)) {
                    angleStart = currentAngle;
                }
                if ((angleTable[0][currentAngle - 1] != 0) && (angleTable[0][currentAngle] == 0)) {
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

        // Calculate area of each zone to return the index of the biggest
        for (int indexOfZone=0; indexOfZone<zoneList.size(); indexOfZone++) {
            for(int j=zoneList.get(indexOfZone).getAngleStart(); 
                j<=zoneList.get(indexOfZone).getAngleStop();j++ ){
                sumRssi += angleTable[0][j];
            }
            if (sumRssi*zoneList.get(indexOfZone).getAngleSize() > biggestArea){
                biggestArea = sumRssi*zoneList.get(indexOfZone).getAngleSize();
                indexZone = indexOfZone;
            }
            sumRssi = 0;
        }
        return indexZone;
    }

    /**
     * Smoothing of the RSSI to obtain clear zones
     */
    public void rssiSmoothing() {

        boolean down = false;
        int missedAngles = 0;
        int currentAngle;
        int lastCorrectAngle;
        int rssiUsedAsAverage;

        // Detection of a minimal Rssi value just after a zone
        for (currentAngle = 0 ; currentAngle < ANGLE ; currentAngle ++) {
            if(currentAngle != 0 && this.angleTable[0][currentAngle] == -80
                    && this.angleTable[0][currentAngle - 1] != -80 ) {
                down = true;
            }
            //Count the number of consecutive minimal  RSSI values
            if (this.angleTable[0][currentAngle] <= -80 && down){
                missedAngles ++;
            }

            //Catch the last Rssi value (>-80), calculate average
            // and replace minimal values to complete zone
            if (this.angleTable[0][currentAngle] > -80 && missedAngles != 0){
                lastCorrectAngle = currentAngle - ( missedAngles + 1);
                rssiUsedAsAverage = (this.angleTable[0][currentAngle] +
                        this.angleTable[0][lastCorrectAngle]) / 2;
                for (int i = lastCorrectAngle +1 ; i < currentAngle ; i++ ){
                    this.angleTable[0][i] = rssiUsedAsAverage;
                }
                missedAngles = 0;
                down = false;
            }

            //Verify the number of consecutive minimal values to replace
            if (missedAngles >= MAX_MISSED_ANGLE) {
                missedAngles = 0;
                down = false;
            }
        }
    }
}