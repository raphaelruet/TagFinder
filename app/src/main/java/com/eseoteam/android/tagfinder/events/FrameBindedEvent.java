package com.eseoteam.android.tagfinder.events;

/**
 * Created on 25/01/2015.
 *
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class FrameBindedEvent {

    private int rssi;

    private int phase;

    private int angle;

    public FrameBindedEvent(int rssi, int phase, int angle) {
        this.rssi = rssi;
        this.phase = phase;
        this.angle = angle;
    }

    public int getRssi(){
        return this.rssi;
    }

    public int getPhase(){
        return this.phase;
    }

    public int getAngle(){
        return this.angle;
    }
}
