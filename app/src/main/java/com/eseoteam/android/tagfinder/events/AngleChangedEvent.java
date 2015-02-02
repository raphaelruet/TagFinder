package com.eseoteam.android.tagfinder.events;

/**
 * Created on 19/01/2015.
 * The angle changed event
 * @author Raphael RUET.
 * @version 0.2.
 */
public class AngleChangedEvent {

    /**
     * The frame to change.
     */
    private int angle;

    /**
     * Constructor of a frameChangedEvent which set the changedFrame.
     * @param changedAngle The changed frame.
     */
    public AngleChangedEvent(int changedAngle) {
        this.angle = changedAngle;
    }

    /**
     * Allow to get the angle
     * @return the angle
     */
    public int getAngle() {
        return this.angle;
    }

}
