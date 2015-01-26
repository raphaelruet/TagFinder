package com.eseoteam.android.tagfinder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

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
     * The previous angle of the phone
     */
    private int computedAngle = 0;

    /**
     * The current angle of the phone
     */
    private int currentAngle = 0;

    /**
     * TODO
     */
    private int[] previousAngles = {0,0,0};

    /**
     * TODO
     */
    private float[] mGravity;

    /**
     * TODO
     */
    private float[] mGeomagnetic;

    /**
     *
     */
    private int calibrationOffset = 0;

    /**
     *
     */
    private boolean calibrationDone = false;

    /**
     * The listeners on the Compass
     */
    private ArrayList<CompassListener> listeners;


    // Constructor //

    /**
     * Constructor for the Compass
     * @param sensorManager
     */
    public Compass(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.orientation = new float[3];
        this.listeners = new ArrayList<>();
        Log.e("Compass:Compass", "Fin du constructeur");
    }

    // Accessor //

    public int getAzimutInDegrees(){
        return (int)convertFromRadiansToDegrees(this.orientation[0]);
    }

    // Methods //

    public void calibrateCompass() {
        this.calibrationOffset = this.currentAngle;
    }

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
                this.currentAngle = (int)convertFromRadiansToDegrees(this.orientation[0]) + 180;
                if (this.currentAngle < this.calibrationOffset){
                    this.currentAngle = 359 + this.currentAngle;
                }
                this.currentAngle -= this.calibrationOffset;
                computeAngles();
                signalAngleChanged();
                signalAngleStabilized();

            }
        }
    }

    private void computeAngles(){
        this.computedAngle=0;
        previousAngles[2]=previousAngles[1];
        previousAngles[1]=previousAngles[0];
        previousAngles[0]=this.currentAngle;
        for (int i = 0; i < 3 ; i++){
            this.computedAngle += this.previousAngles[i];
        }
        this.computedAngle = this.computedAngle/3;
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalAngleChanged(){
        for (CompassListener listener : this.listeners) {
            listener.notifyAngleChanged(new AngleChangedEvent(this.computedAngle));
        }
    }

    /**
     * Signals via listeners when the angle has changed
     */
    private void signalAngleStabilized(){
        for (CompassListener listener : this.listeners) {
            listener.notifyAngleStabilized();
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
        this.sensorManager.unregisterListener(this, this.accelerometer);
        this.sensorManager.unregisterListener(this, this.magnetometer);
    }

    /**
     * Register the sensors
     */
    public void registerSensors(){
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Converts the radian angle into degree angle
     * @param angleToConvert the angle to convert
     * @return the angle converted
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
