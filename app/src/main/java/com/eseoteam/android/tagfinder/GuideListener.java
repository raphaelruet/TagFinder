package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.AngleChangedEvent;

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
}
