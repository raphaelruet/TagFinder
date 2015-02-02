package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.DirectionChangedEvent;

/**
 * Listener on the Binder class events.
 * Created on 29/01/2015.
 * @author Raphael RUET.
 * @version 0.1.
 */
public interface CruiseControlListener {

    /**
     * Notify that the rotation speed is too high
     */
    public void notifySpeedTooHigh();

    /**
     * Notify that the rotation speed is correct.
     */
    public void notifySpeedOK();

    /**
     * Notify that the speed has changed
     */
    public void notifyDirectionChanged(DirectionChangedEvent event);

}
