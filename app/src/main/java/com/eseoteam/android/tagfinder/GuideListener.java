package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

/**
 * Listener on the Binder class events.
 * Created on 19/01/2015.
 * @author Raphael RUET.
 * @version 0.1.
 */
public interface GuideListener {

    /**
     * Notify that a calibration is needed
     */
    public void notifyCalibrationAsked();

    /**
     * Notify that a scan is needed
     */
    public void notifyScanAsked();

    /**
     * Notify the SearchActivity that the scan has failded
     */
    public void notifyUserScanFailed();


    /**
     * Notify the SearchActivity that the Guiding has began.
     */
    public void notifyUserGuidingStart();

    /**
     * Notify that the PieChart angles has changed
     * @param event the angles
     */
    public void notifyPieChartChanged(PieChartChangedEvent event);
}
