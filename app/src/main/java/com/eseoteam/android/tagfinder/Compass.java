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
public class Compass implements SensorEventListener{

    // Attributes //
    /**
     * The sensor manager used by the Compass
     */
    private SensorManager sensorManager;

    /**
     * The accelerometer sensor of the Compass
     */
    private Sensor accelerometer;

    /**
     * The magnetometer sensor of the Compass
     */
    private Sensor magnetometer;

    /**
     * The data of the different captors
     */
    private float[] orientation;

    /**
     * The current angle of the phone
     */
    private int currentAngle = 0;

    /**
     * The previous angle of the phone
     */
    private int previousAngle = 0;

    /**
     * TODO
     */
    float[] mGravity;

    /**
     * TODO
     */
    float[] mGeomagnetic;

    /**
     * The listeners on the Compass
     */
    private ArrayList<CompassListener> listeners;


    // Constructor //

    /**
     * Constructor for the Compass
     * @param sensorManager
     */
    public Compass(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.orientation = new float[3];
        this.listeners = new ArrayList<>();
        Log.e("Compass:Compass","Fin du constructeur");
    }

    // Accessor //

    public int getAzimutInDegrees(){
        return (int)convertFromRadiansToDegrees(this.orientation[0]);
    }

    // Methods //

    /**
     * Gets the sensors data when they change
     * @param event the event of the sensors
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {
                SensorManager.getOrientation(R, this.orientation);
                System.out.println("X: "+getAzimutInDegrees());
                this.currentAngle = getAzimutInDegrees();
                signalAngleChanged();
            }
        }
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalAngleChanged(){
        for (CompassListener listener : this.listeners) {
            listener.notifyAngleChange(new AngleChangedEvent(this.currentAngle));
        }
    }

    /**
     * Not used here
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to be done here
    }

    /**
     * Unregister the sensors
     */
    public void unregisterSensors(){
        this.sensorManager.unregisterListener(this, this.accelerometer);
        this.sensorManager.unregisterListener(this, this.magnetometer);
    }

    /**
     * Register the sensors
     */
    public void registerSensors(){
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Converts the radian angle into degree angle
     * @param angleToConvert
     * @return
     */
    public double convertFromRadiansToDegrees(float angleToConvert){
        return angleToConvert*180/Math.PI;
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