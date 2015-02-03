package com.eseoteam.android.tagfinder;

/**
 * Describes an area where a tag has been detected.
 * Created on 22/01/15.
 * @author Charline LEROUGE.
 * @version 0.1.
 */
public class Zone {

    /**
     * Angles that correspond with the beginning of the zone.
     */
    private int angleStart;

    /**
     * Angles that correspond with the end of the zone.
     */
    private int angleStop;

    /**
     * Width of the zone.
     */
    private int angleSize;

    /**
     * Constructor of a zone.
     * @param angleStart Beginning of the zone.
     * @param angleStop End of the zone.
     * @param angleSize Width of the zone.
     */
    public Zone (int angleStart, int angleStop, int angleSize){
        this.angleStart = angleStart;
        this.angleStop = angleStop;
        this.angleSize = angleSize;
    }

    /**
     * Gives the beginning of the zone.
     * @return The angle of the beginning of the zone.
     */
    public int getAngleStart(){
        return angleStart;
    }

    /**
     * Gives the end of the zone.
     * @return The angle of the end of the zone.
     */
    public int getAngleStop(){
        return angleStop;
    }

    /**
     * Gives the width of the zone.
     * @return The width of the zone.
     */
    public int getAngleSize(){
        return angleSize;
    }
}
