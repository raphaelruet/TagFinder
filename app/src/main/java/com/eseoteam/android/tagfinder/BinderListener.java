package com.eseoteam.android.tagfinder;

/**
 * Listener on the Binder class events.
 * Created on 15/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public interface BinderListener {

    /**
     * Notify that a new frame has been received.
     * @param event Event of a frame change.
     */
    public void notifyFrameChange(FrameChangedEvent event);
}
