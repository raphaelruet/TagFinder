package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.AddTagEvent;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.FrameBindedEvent;

/**
 * Listener on the Binder class events.
 * Created on 15/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public interface BinderListener {

    public void notifyAngleChanged(AngleChangedEvent event);

    public void notifyAngleStabilized();

    /**
     * Notify that the tag to add has been found.
     * @param event Event containing the id of th tag to add.
     */
    public void notifyTagToAddFound(AddTagEvent event);

    public void notifyFrameBinded(FrameBindedEvent event);

    /**
     * Notify that a frame has been received.
     * Used to check the connection.
     */
    public void notifyFrameReceived();
}
