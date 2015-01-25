package com.eseoteam.android.tagfinder;

import android.util.Log;

import com.eseoteam.android.tagfinder.events.AddTagEvent;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

import java.util.ArrayList;

/**
 * Created on 21/01/15.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class Guide extends Thread implements CompassListener{

    // Attributes //

    /**
     * States of the Guide's state machine.
     */
    public static enum State {
        IDLE,
        ASK_FOR_CALIBRATION,
        CALIBRATION,
        SCAN,
        GUIDE,
        DEBUG
    }

    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = Guide.class.getSimpleName();

    /**
     * The listeners of the Guide
     */
    private ArrayList<GuideListener> listeners;

    /**
     * The current state in the Guide's state machine
     */
    State currentState;

    /**
     * Stops the thread
     */
    private boolean stop = false;

    /**
     * Guide's Binder
     */
    private Binder binder;

    /**
     * Current compass angle
     */
    private int currentCompassAngle;

    /**
     * Current compass angle
     */
    private int previousCompassAngle;

    // Constructors //

    /**
     * Guide constructor
     */

    public Guide(Binder binder) {
        this.binder = binder;
        this.listeners = new ArrayList<>();
        this.currentState = State.ASK_FOR_CALIBRATION;
    }

    // Accessor //

    // Methods //

    public void run() {
        while (!(this.stop)){
            switch (currentState){
                case IDLE:
                    //Wait and do nothing
                    break;
                case ASK_FOR_CALIBRATION:
                    askForCalibration();
                    this.currentState = State.IDLE;
                    break;
                case CALIBRATION:
                    // Wait for compass to be stabilized
                    break;
                case SCAN:
                    //TODO Informer Mathematician
                    updatePieChart(new int[]{-currentCompassAngle,0});
                    break;
                case GUIDE:
                    break;
                case DEBUG:
                    updatePieChart(new int[]{currentCompassAngle, currentCompassAngle+5});
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Asks the SearchActivity to update the pieChart with the given angles
     * @param angles the angles of the pieChart
     */
    private void updatePieChart(int[] angles){
        PieChartChangedEvent pieChartChangedEvent = new PieChartChangedEvent(angles);
        for (GuideListener listener : this.listeners){
            listener.notifyPieChartChanged(pieChartChangedEvent);
        }
    }

    /**
     * Adds a listener to Guide listeners
     * @param listener the listener to add
     */
    public void addGuideListener(GuideListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener to Guide listeners
     * @param listener the listener to remove
     */
    public void removeGuideListener(GuideListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Stops the Guide
     */
    public void stopGuide() {
        Log.i(LOG_TAG, "Guide has been correctly stopped");
        this.stop = true;
    }

    /**
     * Modifies the currentCompassAngle when it has changed
     */
    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        this.previousCompassAngle = this.currentCompassAngle;
        this.currentCompassAngle = event.getAngle();
    }

    @Override
    public void notifyAngleStabilized() {
        if (this.currentState == State.CALIBRATION){
            this.currentState = State.SCAN;
        }
    }

    /**
     * Allows the other classes to change the state of the guide's stateMachine
     * @param state the state to set
     */
    public void setState(State state){
        this.currentState =state;
    }

    /**
     * Asks the SearchActivity to inform the user of the calibration process
     */
    private void askForCalibration() {
        for (GuideListener listener : this.listeners) {
            listener.notifyCalibrationAsked();
        }
    }

    /**
     * Asks the SearchActivity to inform the user of the scanning process
     */
    private void askForScan() {
        for (GuideListener listener : this.listeners) {
            listener.notifyScanAsked();
        }
    }

}
