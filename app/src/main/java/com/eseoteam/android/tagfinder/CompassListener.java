package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.AngleChangedEvent;

/**
 * Listener on the Binder class events.
 * Created on 19/01/2015.
 * @author Raphael RUET.
 * @version 0.1.
 */
public interface CompassListener {

    /**
     * Notify that a new angle has been received.
     * @param event Event of a angle change.
     */
    public void notifyAngleChanged(AngleChangedEvent event);


}
