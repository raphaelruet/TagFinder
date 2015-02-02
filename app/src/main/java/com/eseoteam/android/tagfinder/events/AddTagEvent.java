package com.eseoteam.android.tagfinder.events;

/**
 * Created on 15/01/2015.
 * The add tag event
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class AddTagEvent {

    /**
     * The id to change.
     */
    private String id;

    /**
     * Constructor of a frameChangedEvent which set the changedFrame.
     * @param idOfTagToAdd The id of the tag to add.
     */
    public AddTagEvent(String idOfTagToAdd) {
        this.id = idOfTagToAdd;
    }

    /**
     * Gives the id of the tag to add.
     * @return The id of the tag to add.
     */
    public String getId() {
        return this.id;
    }

}
