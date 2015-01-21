package com.eseoteam.android.tagfinder;

/**
 * Created on 19/01/2015.
 *
 * @author Raphael RUET.
 * @version 0.1.
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

    public int getAngle() {
        return this.angle;
    }

}
