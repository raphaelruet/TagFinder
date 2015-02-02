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
     * Header to print a log message
     */
    private static final String LOG_TAG = Compass.class.getSimpleName();

    /**
     * The sensor manager used by the Compass
     */
    private SensorManager sensorManager;

    /**
     * The current angle of the phone
     */
    private float currentCompassAngle;
    
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
    
    private boolean direction = false;

    // Constructor //

    /**
     * Constructor for the Compass
     */
    public Compass(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.listeners = new ArrayList<>();
        this.currentCompassAngle = 0;
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
                currentCompassAngle,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(0);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        currentCompassAngle = degree;
        if (this.currentCompassAngle < this.compassOffset){
            this.currentCompassAngle = 359 + this.currentCompassAngle;
        }
        this.currentCompassAngle -= this.compassOffset;
        signalAngleChanged();
        checkDirection();
    }

    private void checkDirection () {
        if (this.direction){
            if (this.currentCompassAngle > this.currentAngle){
                currentAngle = currentCompassAngle;
            }else if (currentCompassAngle <= currentAngle - 3){
                direction = false;
                notifyDirectionChanged ();
            }
        }else{
            if (this.currentCompassAngle < this.currentAngle){
                currentAngle = currentCompassAngle;
            }else if (currentCompassAngle >= currentAngle +3){
                direction = true;
                notifyDirectionChanged ();
            }
        }

    }

    private void notifyDirectionChanged(){
        for (CompassListener listener : this.listeners) {
            listener.notifyDirectionChanged();
        }
    }

    /**
     * Allow to calibrate the compass
     */
    public void calibrateCompass(){
        this.compassOffset = this.currentCompassAngle;
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalAngleChanged(){

        if (!this.startScanAcknoledgment){
            if (this.currentCompassAngle > 300 || this.currentCompassAngle < 5){
                this.currentCompassAngle = 5;
            }
            if (90 < this.currentCompassAngle && this.currentCompassAngle < 100){
                this.startScanAcknoledgment = true;
            }
        }
        for (CompassListener listener : this.listeners) {
            listener.notifyAngleChanged(new AngleChangedEvent((int)this.currentCompassAngle));
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
