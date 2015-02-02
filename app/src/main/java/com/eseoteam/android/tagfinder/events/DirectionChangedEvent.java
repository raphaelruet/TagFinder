package com.eseoteam.android.tagfinder.events;

/**
 * Created on 19/01/2015.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class DirectionChangedEvent {

    /**
     * The frame to change.
     */
    private boolean clockwise;

    public DirectionChangedEvent(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public boolean getClockwiseValue() {
        return this.clockwise;
    }

}
