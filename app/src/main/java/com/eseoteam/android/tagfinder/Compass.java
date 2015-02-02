package com.eseoteam.android.tagfinder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.eseoteam.android.tagfinder.events.AngleChangedEvent;

import java.util.ArrayList;

/**
 * Created by Pierre TOUZE & Raphael RUET
 * 20/01/2014
 */
public class Compass implements SensorEventListener{

    // Attributes //
    /**
     * The sensor manager used by the Compass
     */
    private SensorManager sensorManager;

    /**
     * The current angle of the phone
     */
    private float currentAngle;

    /**
     * The current angle of the phone
     */
    private float compassOffset;

    /**
     * The listeners on the Compass
     */
    private ArrayList<CompassListener> listeners;

    /**
     * The acknoledgment of the beginning of the scan
     */
    private boolean startScanAcknoledgment;

    // Constructor //

    /**
     * Constructor for the Compass
     */
    public Compass(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.listeners = new ArrayList<>();
        this.currentAngle = 0;
        this.compassOffset = 0;
        this.startScanAcknoledgment = false;
        Log.i("Compass:Compass", "Fin du constructeur");
    }

    // Accessor //

    /**
     * Gets the sensors data when they change
     * @param event the event of the sensors
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentAngle,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(0);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        currentAngle = degree;
        if (this.currentAngle < this.compassOffset){
            this.currentAngle = 359 + this.currentAngle;
        }
        this.currentAngle -= this.compassOffset;
        signalAngleChanged();
    }

    /**
     * Allow to calibrate the compass
     */
    public void calibrateCompass(){
        this.compassOffset = this.currentAngle;
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalAngleChanged(){

        if (!this.startScanAcknoledgment){
            if (this.currentAngle > 300 || this.currentAngle < 5){
                this.currentAngle = 5;
            }
            if (90 < this.currentAngle && this.currentAngle < 100){
                this.startScanAcknoledgment = true;
            }
        }
        for (CompassListener listener : this.listeners) {
            listener.notifyAngleChanged(new AngleChangedEvent((int)this.currentAngle));
        }
    }

    /**
     * Not used here
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to be done here
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
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Adds a listener on the compass
     * @param listener the listener to add
     */
    public void addCompassListener(CompassListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener of the compass
     * @param listener the listener to remove
     */
    public void removeCompassListener(CompassListener listener)
    {
        this.listeners.remove(listener);
    }


}
