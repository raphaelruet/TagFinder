package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.FrameChangedEvent;

import java.util.ArrayList;

/**
 * Created on 21/01/15.
 *
 * @author Raphael RUET.
 * @version 0.1.
 */
public class Guide extends Thread implements BinderListener{



    // Attributes //

    /**
     * States of the Guide's state machine.
     */
    private static enum State {
        IDLE,
        CALIBRATION,
        SCAN,
        GUIDE
    }

    /**
     * The listeners of the Guide
     */
    private ArrayList<GuideListener> listeners;

    /**
     * The current state in the Guide's state machine
     */
    State currentState = State.IDLE;

    /**
     * Shows if the calibration has been done
     */
    private boolean calibrationDone = false;

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

    // Constructors //

    /**
     * Guide constructor
     */
    /*public Guide() {
        this.listeners = new ArrayList<>();
        this.binder = new Binder();
    }*/

    /**
     * Guide constructor
     * @param binder the Binder needed
     */
    public Guide(Binder binder) {
        this.binder = binder;
        this.binder.addListener(this);
        this.listeners = new ArrayList<>();
    }

    public Guide(Binder binder, GuideListener listener) {
        this.binder = binder;
        this.binder.addListener(this);
        this.listeners = new ArrayList<>();
        this.listeners.add(listener);
    }

    // Accessor //

    // Methods //

    public void run() {
        while (!(this.stop)){
            switch (currentState){
                case IDLE:
                    if (!calibrationDone){
                        askForCalibration();
                    }else{
                        askForScan();
                        do {
                        }while (currentState == State.IDLE);
                    }
                    break;
                case CALIBRATION:
                    calibrateCompass();
                    if (calibrationDone){
                        currentState = State.IDLE;
                    }
                    break;

                case SCAN:
                    break;

                case GUIDE:
                    break;

                default:
                    break;

            }
        }
    }

    private void askForCalibration() {
        for (GuideListener listener : this.listeners) {
            listener.notifyCalibrationAsked();
        }
    }

    private void askForScan() {
        for (GuideListener listener : this.listeners) {
            listener.notifyScanAsked();
        }
    }


    /**
     * Sets the angle of the PieChart when the sensors values change
     */
    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        this.currentCompassAngle = event.getAngle();
    }


    /**
     * Calibrates the orientation of the phone to zero
     */
    private void calibrateCompass() {
        //TODO ajout la methode de binder permettant la calibration du compass
        this.calibrationDone = true;
    }

    public void goToCalibration(){
        this.currentState = State.CALIBRATION;
    }

    public void addGuideListener(GuideListener listener) {
        this.listeners.add(listener);
    }

    public void removeGuideListener(GuideListener listener) {
        this.listeners.remove(listener);
    }

    public void stopGuide() {
        this.stop = true;
    }

    @Override
    public void notifyFrameChange(FrameChangedEvent event) {

    }

}
