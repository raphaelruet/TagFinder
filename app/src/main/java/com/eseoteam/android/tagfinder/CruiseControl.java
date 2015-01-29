package com.eseoteam.android.tagfinder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Pierre TOUZE & Raphael RUET
 * 20/01/2014
 */
public class CruiseControl implements SensorEventListener{

    // Attributes //
    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = CruiseControl.class.getSimpleName();

    /**
     * The sensor manager used by the Compass
     */
    private SensorManager sensorManager;

    /**
     * The listeners on the Compass
     */
    private ArrayList<CruiseControlListener> listeners;

    /**
     * The index of the averageSpeed
     */
    private int index;

    /**
     * Arry of speeds to get an average speed
     */
    private double[] speed;

    /**
     * The round value
     * The higher ROUND_VALUE is, the higher the inertia is.
     */
    private static final int ROUND_VALUE = 10;

    /**
     * The rotation speed limit in rad/s
     */
    private static final double SPEED_LIMIT = 0.2;

    // Constructor //

    /**
     * Constructor for the Compass
     */
    public CruiseControl(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.listeners = new ArrayList<>();
        this.index = 0;
        this.speed = new double[ROUND_VALUE];
        for (int i = 0 ; i < ROUND_VALUE ; i++){
            this.speed[i] = 0;
        }
        Log.i(LOG_TAG, "Fin du constructeur");
    }

    // Accessor //

    /**
     * Gets the sensors data when they change
     * @param event the event of the sensors
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        computeAverageSpeed(event.values[2]);
    }

    /**
     * Computes the average speed of the device
     * @param newValue the new speed Value to add
     */
    private void computeAverageSpeed(float newValue) {
        if (newValue > 2*SPEED_LIMIT){
            newValue = (float)(2*SPEED_LIMIT);
        }
        float averageSpeed = 0;
        if (this.index < ROUND_VALUE-1){
            this.speed[index] = newValue;
            this.index ++;
        }else{
            this.index = 0;
        }
        for (int i = 0 ; i < ROUND_VALUE ; i++){
            averageSpeed += this.speed[i];
        }
        averageSpeed /= ROUND_VALUE;

        //If the absolute value of speed is to high, it is signaled
        if ( Math.abs(averageSpeed) > SPEED_LIMIT){
            this.signalSpeedTooHigh();
        }else{
            this.signalSpeedOk();
        }
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalSpeedTooHigh(){
        for (CruiseControlListener listener : this.listeners) {
            listener.notifySpeedTooHigh();
        }
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalSpeedOk(){
        for (CruiseControlListener listener : this.listeners) {
            listener.notifySpeedOK();
        }
    }

    /**
     * Unregister the sensors
     */
    public void unregisterSensors(){
        this.sensorManager.unregisterListener(this);
    }

    /**
     * Register the sensors
     */
    public void registerSensors(){
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Adds a listener on the compass
     * @param listener the listener to add
     */
    public void addCruiseControlListener(CruiseControlListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener of the compass
     * @param listener the listener to remove
     */
    public void removeCruiseControlListener(CruiseControlListener listener)
    {
        this.listeners.remove(listener);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to be done here
    }
}
