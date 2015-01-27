package com.eseoteam.android.tagfinder;

import android.util.Log;

import com.eseoteam.android.tagfinder.events.AddTagEvent;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.FrameBindedEvent;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

import java.util.ArrayList;

/**
 * Created on 21/01/15.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class Guide extends Thread implements CompassListener, BinderListener{

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

    private Mathematician mathematician;

    private int currentRssi;

    private int currentPhase;

    private int currentReadCount;

    // Constructors //

    /**
     * Guide constructor
     */

    public Guide(Binder binder) {
        this.binder = binder;
        this.listeners = new ArrayList<>();
        this.currentState = State.ASK_FOR_CALIBRATION;

        this.currentCompassAngle = 0;
        this.currentRssi = 0;
        this.currentPhase = 0;
        this.currentReadCount = 0;
        this.mathematician = new Mathematician();
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
                case SCAN:
                    //this.mathematician.addData(this.currentCompassAngle,
                    //        this.currentRssi,this.currentReadCount);
                    updatePieChart(-this.currentCompassAngle, 0);
                    if (this.currentCompassAngle > 355){
                        notifyUserScanFinished();
                        this.setState(State.GUIDE);
                    }
                    break;
                case GUIDE:
                    //int angles[] = this.mathematician.bestZoneSelection();
                    //updatePieChart(angles[0], angles[1]);
                    updatePieChart(-this.currentCompassAngle, 30-currentCompassAngle);
                    //this.stopGuide();
                    break;
                case DEBUG:
                    updatePieChart(currentCompassAngle, currentCompassAngle+5);
                    break;
                default:
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
     * Modifies the currentCompassAngle when it has changed
     */
    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        this.previousCompassAngle = this.currentCompassAngle;
        this.currentCompassAngle = event.getAngle();
    }

    @Override
    public void notifyAngleStabilized() {
        //Nothing to be done here.
    }


    public void notifyUserScanFinished() {
        for (GuideListener listener : this.listeners) {
            listener.notifyUserScanFinished();
        }
    }

    @Override
    public void notifyTagToAddFound(AddTagEvent event) {
        //Nothing to be done here.
    }

    @Override
    public void notifyFrameBinded(FrameBindedEvent event) {
        this.currentRssi = event.getRssi();
        this.currentPhase = event.getPhase();
        this.currentReadCount = event.getReadCount();
        this.currentCompassAngle = event.getAngle();
    }

    @Override
    public void notifyFrameReceived() {
        //Nothing to be done here.
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
            Log.e(LOG_TAG,"User as been asked to calibrate");
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
