package com.eseoteam.android.tagfinder.events;

/**
 * Created on 24/01/2015.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class PieChartChangedEvent {

    /**
     * The frame to change.
     */
    private int[] angles = new int[2];


    public PieChartChangedEvent(int[] angles) {
        this.angles = angles;
    }

    public int[] getAngles() {
        return this.angles;
    }

}
