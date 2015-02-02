package com.eseoteam.android.tagfinder.communication;

import com.eseoteam.android.tagfinder.Binder;

/**
 * ChangeFrameCommand is in charge of passing the received frames from the communication
 * to the binder.
 * Created by Pierre on 07/12/2014.
 * @version 0.2
 */
public class ChangeFrameCommand implements Command {

    //Attributes
    /**
     * Binder which will decode the frames.
     */
    private Binder binder;

    //Constructor
    /**
     * Constructor of a command to specify the binder which will receive the frames.
     * @param binder The binder which will receive the frames.
     */
    public ChangeFrameCommand(Binder binder){
        this.binder = binder;
    }

    //Method
    /**
     * Do the changeFrame method the gives the frame to the binder.
     * @param data The frame.
     */
    public void execute(String data){
        this.binder.changeFrame(data);
    }
}
