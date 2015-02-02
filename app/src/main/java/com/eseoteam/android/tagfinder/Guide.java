package com.eseoteam.android.tagfinder;

import android.util.Log;

import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.DirectionChangedEvent;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

import java.util.ArrayList;

/**
 * Created on 21/01/15.
 *
 * @author Raphael RUET.
 * @version 0.1.
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

    private static  final int START = 0;

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

    // Accessor //

    // Methods //

    public void run() {
        int angles[] = new int[2];
        int tempAngles[] = new int[2];
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
                    this.mathematician.addData(this.currentCompassAngle,
                            this.wantedTag.getRssi());
                    updatePieChart(-this.currentCompassAngle, 0);
                    if (this.currentCompassAngle > 355){
                        angles = this.mathematician.bestZoneSelection();
                        Log.e("Fin PREMIER scan", "start" + angles[START] + "stop" + angles[STOP]);
                        if ( angles[START] != -1 && angles[STOP] != -1 ) {
                            notifyUserGuidingStart();
                            this.mathematician.initMatrix();
                            this.mathematician.clearZoneList();
                            this.setState(State.GUIDE);
                        } else {
                            notifyUserScanFailed();
                            this.setState(State.IDLE);
                        }
                    }
                    break;
                case GUIDE:
                    updatePieChart(angles[START]-this.currentCompassAngle,
                                angles[STOP]-this.currentCompassAngle);
                    this.mathematician.addData(this.currentCompassAngle, this.wantedTag.getRssi());
                    if(this.directionChanged) {
                        tempAngles = this.mathematician.bestZoneSelection();
                        if ( tempAngles[START] != -1 && tempAngles[STOP] != -1 ) {
                            angles = tempAngles;
                        }
                        this.mathematician.initMatrix();
                        this.mathematician.clearZoneList();
                        this.directionChanged = false;
                        Log.e("Guide","On a calculé une nouvelle zone" + angles[0] + " end " + angles[1]);
                    }
                    /*
                    if(direction == 0) {
                        if (this.currentCompassAngle == angles[START]) {
                            direction = 1;
                            Log.e("Guide", " Gauche à droite" );
                        }
                        if (this.currentCompassAngle == angles[STOP]) {
                            direction = 2;
                            Log.e("Guide", " Droite à gauche" );
                        }
                    }
                    if (this.currentCompassAngle >= angles[START]
                            && this.currentCompassAngle <= angles[STOP]) {
                        this.mathematician.addData(this.currentCompassAngle,
                                this.wantedTag.getRssi());
                    }
                    if (direction == 1 && this.currentCompassAngle == angles[STOP]) {

                        angles = this.mathematician.bestZoneSelection();
                        direction = 0;
                        Log.e("Fin scan", " direction 1 start" + angles[START] + "stop" + angles[STOP]);
                    }
                    if (direction == 2 && this.currentCompassAngle == angles[START]) {
                        angles = this.mathematician.bestZoneSelection();
                        direction = 0;
                        Log.e("Fin scan", " direction 2 start" + angles[START] + "stop" + angles[STOP]);
                    }*/

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

    public State getSate(){
        return this.currentState;
    }

    public void notifyUserScanFailed() {
        for (GuideListener listener : this.listeners) {
            listener.notifyUserScanFailed();
        }
    }

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

    @Override
    public void notifySpeedTooHigh() {
        //Nothing to be done here.
    }

    @Override
    public void notifySpeedOK() {
        //Nothing to be done here.
    }

    @Override
    public void notifyDirectionChanged(DirectionChangedEvent event) {
        if(this.currentState == State.GUIDE) {
            this.directionChanged = true;
            Log.e("Guide", "Direction changed !!!");
        }
    }

}
