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
     * Notify the SearchActivity that the scan is finished
     */
    public void notifyUserScanFinished();

    /**
     * Notify the SearchActivity that the scan is finished
     */
    public void notifyScanFailed();

    /**
     * Notify that the PieChart angles has changed
     * @param event the angles
     */
    public void notifyPieChartChanged(PieChartChangedEvent event);
}
