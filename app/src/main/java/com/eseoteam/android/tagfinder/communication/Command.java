package com.eseoteam.android.tagfinder.communication;

/**
 * Command interface used to be implemented by specifics commands.
 * Created by Pierre on 07/12/2014.
 * @version 0.2
 */
public interface Command {

    //Method
    /**
     * Generic method to perform an action on the data.
     * @param data The data to perform an action on.
     */
    public void execute(String data);
}
