package com.eseoteam.android.tagfinder;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created on 14/01/2015.
 *
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Binder {
    //Attributes//
    /**
     * The frame received from the communication.
     */
    private String frame;

    /**
     * List of listeners on the binder's events
     */
    private ArrayList<BinderListener> listeners;

    //Constructor//

    /**
     * Constructor of a new Binder which initialize attributes.
     */
    Binder() {
        this.frame =null;
        this.listeners = new ArrayList<>();
    }

    //Accessor//

    //Methods//
    public void changeFrame(String frame) {
        this.frame = frame;
        this.signalFrameChange();
        Log.d("Binder:changeFrame","Frame changed !!!");
    }

    public void addListener(BinderListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(BinderListener listener) {
        this.listeners.remove(listener);
    }

    private void signalFrameChange() {
        for (BinderListener listener: this.listeners) {
            listener.notifyFrameChange(new FrameChangedEvent(this.frame));
        }
    }



}
