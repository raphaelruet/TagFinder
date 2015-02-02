package com.eseoteam.android.tagfinder.communication;

import com.eseoteam.android.tagfinder.Binder;

/**
 * Created by Pierre on 07/12/2014.
 * @version 0.1
 */
public class ChangeFrameCommand implements Command {

    private Binder binder;

    public ChangeFrameCommand(Binder binder){
        this.binder = binder;
    }

    public void execute(String data){
        this.binder.changeFrame(data);
    }
}
