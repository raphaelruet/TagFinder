package com.eseoteam.android.tagfinder;

/**
 * Created on 15/01/2015.
 *
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class FrameChangedEvent {

    /**
     * The frame to change.
     */
    private String frame;

    /**
     * Constructor of a frameChangedEvent which set the changedFrame.
     * @param changedFrame The changed frame.
     */
    public FrameChangedEvent(String changedFrame) {
        this.frame = changedFrame;
    }

    public String getFrame() {
        return this.frame;
    }

}
