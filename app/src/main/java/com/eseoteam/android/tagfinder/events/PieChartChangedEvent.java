package com.eseoteam.android.tagfinder.events;

/**
 * Created on 24/01/2015.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class PieChartChangedEvent {

    /**
     * The start angle of the PieChart
     */
    private int startAngle;

    /**
     * The stop angle of the PieChart
     */
    private int stopAngle;

    /**
     * Constructor of the event
     * @param startAngle the start angle of the PieChart
     * @param stopAngle the stop angle of the PieChart
     */
    public PieChartChangedEvent(int startAngle, int stopAngle) {
        this.startAngle = startAngle;
        this.stopAngle = stopAngle;
    }

    /**
     * Accessor to the start angle
     * @return the start angle
     */
    public int getStartAngle() {
        return this.startAngle;
    }

    /**
     * Accessor to the stop angle
     * @return the stop angle
     */
    public int getStopAngle() {
        return this.stopAngle;
    }

}
