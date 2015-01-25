package com.eseoteam.android.tagfinder;

/**
 * Created on 22/01/15.
 *
 * @author Charline LEROUGE.
 * @version 0.1.
 */
public class Zone {

    public static int angleStart;
    public static int angleStop;
    public static int angleSize;

    public Zone (int angleStart, int angleStop, int angleSize){
        this.angleStart = angleStart;
        this.angleStop = angleStop;
        this.angleSize = angleSize;

    }

    public int getAngleStart(){
        return angleStart;
    }

    public int getAngleStop(){
        return angleStop;
    }

    public int getAngleSize(){
        return angleSize;
    }
}
