package com.eseoteam.android.tagfinder;

/**
 * Listener on the Binder class events.
 * Created on 19/01/2015.
 * @author Raphael RUET.
 * @version 0.1.
 */
public interface CompassListener {

    /**
     * Notify that a new frame has been received.
     * @param event Event of a frame change.
     */
    public void notifyAngleChange(AngleChangedEvent event);
}
