package com.eseoteam.android.tagfinder;

import android.util.Log;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;
import java.util.ArrayList;

/**
 * Created on 21/01/15.
 * Contains the state machine and all the calls to the other classes
 * @author Raphael RUET.
 * @version 0.9.
 */
public class Guide extends Thread implements CompassListener, CruiseControlListener {

    // Attributes //

    /**
     * States of the Guide's state machine.
     */
    public static enum State {
        IDLE,
        ASK_FOR_CALIBRATION,
        SCAN,
        GUIDE,
        DEBUG
    }

    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = Guide.class.getSimpleName();

    /**
     * The index of the start angle in the angle table given by the mathematician
     */
    private static  final int START = 0;

    /**
     * The index of the stop angle in the angle table given by the mathematician
     */
    private static final int STOP = 1;

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
     * The mathematician used to compute the data
     */
    private Mathematician mathematician;

    /**
     * The wanted tag
     */
    private Tag wantedTag;

    /**
     * Boolean on the direction changed event
     * True : The direction has changed
     * False : The direction has not changed
     */
    private boolean directionChanged;

    // Constructors //

    /**
     * Guide constructor
     */
    public Guide(Binder binder) {
        this.binder = binder;
        this.listeners = new ArrayList<>();
        this.currentState = State.ASK_FOR_CALIBRATION;
        this.directionChanged = false;
        this.currentCompassAngle = 0;
        this.wantedTag = new Tag(-80,0,0,"","");
        this.mathematician = new Mathematician();
    }

    // Methods //

    /**
     * The full state-machine of the searching application
     */
    public void run() {
        int angles[] = new int[2];
        int tempAngles[];
        while (!(this.stop)){
            switch (currentState){

                case IDLE: // Wait and do nothing
                    break;

                case ASK_FOR_CALIBRATION: // Asks for the user to calibrate and goes to IDLE
                    askForCalibration();
                    this.currentState = State.IDLE;
                    break;

                case SCAN: // Gives the RSSI and Angles to the mathematician and make him do the job

                    // Add data with one angle and one RSSI level
                    this.mathematician.addData(this.currentCompassAngle,
                            this.wantedTag.getRssi());

                    // Refresh the PieChart
                    updatePieChart(-this.currentCompassAngle, 0);

                    // If the scan is finished
                    if (this.currentCompassAngle > 355){
                        // Get the best zone
                        angles = this.mathematician.bestZoneSelection();

                        // If a tag have been found
                        if ( angles[START] != -1 && angles[STOP] != -1 ) {
                            notifyUserGuidingStart();
                            //Clear old data
                            this.mathematician.initMatrix();
                            this.mathematician.clearZoneList();
                            this.setState(State.GUIDE);
                        } else { // If no tag found
                            notifyUserScanFailed();
                            this.setState(State.IDLE);
                        }
                    }
                    break;

                case GUIDE: // Guides the user with the data from mathematician
                    // Refresh the PieChart with the angles of the captation of the tag
                    updatePieChart(angles[START]-this.currentCompassAngle,
                                angles[STOP]-this.currentCompassAngle);
                    // Add the real time data
                    this.mathematician.addData(this.currentCompassAngle, this.wantedTag.getRssi());
                    // If the direction change
                    if(this.directionChanged) {
                        //Get the new zone from the mathematician
                        tempAngles = this.mathematician.bestZoneSelection();
                        if ( tempAngles[START] != -1 && tempAngles[STOP] != -1 ) {
                            angles = tempAngles;
                        }
                        // Clear old data
                        this.mathematician.initMatrix();
                        this.mathematician.clearZoneList();
                        this.directionChanged = false;
                    }
                    break;

                case DEBUG: // Makes the pieChart to show the current angle of the compass
                            // Also allow the developers to go directly to GUIDE state
                    updatePieChart(currentCompassAngle, currentCompassAngle+5);
                    break;

                default: // Default case
                    break;
            }
        }
    }

    /**
     * Asks the SearchActivity to update the pieChart with the given angles.
     * @param angleStart Angle of the beginning of the pieChart.
     * @param angleStop Angle of the end of the pieChart.
     */
    private void updatePieChart(int angleStart, int angleStop){
        PieChartChangedEvent pieChartChangedEvent = new PieChartChangedEvent(angleStart, angleStop);
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
     * Allows the other classes to change the state of the guide's stateMachine
     * @param state the state to set
     */
    public void setState(State state) {
        this.currentState =state;
    }

    /**
     * Asks the SearchActivity to inform the user of the calibration process
     */
    private void askForCalibration() {
        this.updatePieChart(0,360);
        for (GuideListener listener : this.listeners) {
            Log.e(LOG_TAG,"User as been asked to calibrate");
            listener.notifyCalibrationAsked();
        }
        this.setState(State.IDLE);
    }

    /**
     * Asks the SearchActivity to inform the user of the scanning process
     */
    public void askForScan() {
        for (GuideListener listener : this.listeners) {
            listener.notifyScanAsked();
        }
    }

    /**
     * Gives the state in the state machine of the Guide
     * @return the state in the state machine
     */
    public State getSate(){
        return this.currentState;
    }

    /**
     * Notifies the user that the scan has detected no tag
     */
    public void notifyUserScanFailed() {
        for (GuideListener listener : this.listeners) {
            listener.notifyUserScanFailed();
        }
    }

    /**
     * Notifies the user that the Guiding has begun
     */
    public void notifyUserGuidingStart() {
        for (GuideListener listener : this.listeners) {
            listener.notifyUserGuidingStart();
        }
    }

    /**
     * Modifies the currentCompassAngle when it has changed
     */
    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        this.currentCompassAngle = event.getAngle();
        if(this.binder.getWantedTag() != null) {
            this.wantedTag.setRssi(this.binder.getWantedTag().getRssi());
            this.wantedTag.setPhase(this.binder.getWantedTag().getPhase());
            this.wantedTag.setReadCount(this.binder.getWantedTag().getReadCount());
            this.binder.acknowledgeFrameTaken();
        }
    }

    /**
     * Inform the guide that the user changed scanning direction
     */
    @Override
    public void notifyDirectionChanged() {
        if(this.currentState == State.GUIDE) {
            this.directionChanged = true;
        }
    }

    /**
     * Nothing to do here
     */
    @Override
    public void notifySpeedTooHigh() {
        //Nothing to be done here.
    }

    /**
     * Nothing to do here
     */
    @Override
    public void notifySpeedOK() {
        //Nothing to be done here.
    }
}
