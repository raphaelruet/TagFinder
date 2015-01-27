package com.eseoteam.android.tagfinder;

import com.eseoteam.android.tagfinder.events.AddTagEvent;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.FrameBindedEvent;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Binder is in charge of interpreting incoming data from the client and linking it with angles.
 * Created on 14/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Binder implements CompassListener {
    //Attributes//
    /**
     * The number of times a tag must have been counted to be selected as the right tag to add.
     */
    private static final int REQUIRED_READ_COUNT = 5;

    /**
     * The frame received from the communication.
     */
    private String frame;

    /**
     * List of listeners on the binder's events
     */
    private ArrayList<BinderListener> listeners;

    /**
     * List of tags which contains info from received frames.
     */
    private Hashtable<String,Tag> tags;

    /**
     * Different modes depending on the current "state" of the application.
     */
    public static enum Mode{CHECK_CONNECTION, ADD_TAG, SEARCH, DEFAULT}

    /**
     * The mode used by the binder to know which action he has to perform depending on the context.
     */
    private Mode operatingMode;

    /**
     * Used to know whether the notify has been performed or not.
     */
    private boolean notifyPerformed;

    private String tagTofind;

    //Constructor//
    /**
     * Constructor of a new Binder which initialize attributes.
     */
    Binder(Mode mode) {
        this.frame = null;
        this.operatingMode = mode;
        this.listeners = new ArrayList<>();
        this.tags = new Hashtable<>();
        this.notifyPerformed = false;
    }

    /**
     * Constructor of a new Binder which initialize attributes.
     */
    Binder(Mode mode, String id) {
        this.frame = null;
        this.tagTofind = id;
        this.operatingMode = mode;
        this.listeners = new ArrayList<>();
        this.tags = new Hashtable<>();
        this.notifyPerformed = false;
    }

    //Methods//
    /**
     * Change the current frame by a new one.
     * @param frame The received frame.
     */
    public void changeFrame(String frame) {
        this.frame = frame;
        this.updateTagTable();
        this.performActionDependingOnMode();
    }

    /**
     *  Switches between the different operating modes.
     */
    private void performActionDependingOnMode() {
        switch (this.operatingMode) {
            case CHECK_CONNECTION:
                this.onCheckConnectionMode();
                break;
            case ADD_TAG:
                this.onAddTagMode();
                break;
            case SEARCH:
                this.onSearchMode();
                break;
            default:
                Log.e("Binder","Error incorrect binder operating mode");
                break;
        }
    }

    /**
     * Notify that a frame has been received.
     * It means that the connection is ok from a tag to the application.
     */
    private void onCheckConnectionMode() {
        if(!this.notifyPerformed) {
            this.signalFrameReceived();
            this.notifyPerformed = true;
        }
    }

    /**
     * Select the best tag from the hashtable as the one to be added.
     */
    private void onAddTagMode() {
        final String bestId = this.getIdWithMaxRssi();
        if (this.tags.get(bestId).getReadCount() >= REQUIRED_READ_COUNT) {
            this.signalTagToAddFound(bestId);
        }
    }

    /**
     * Select the tag with the highest rssi.
     * @return The id of the selected tag.
     */
    private String getIdWithMaxRssi() {
        int maxRssi = -80;
        String selectedId = null;
        for (Map.Entry<String, Tag> entry: this.tags.entrySet()) {
            final String currentId = entry.getKey();
            final Tag currentTag = entry.getValue();
            if (currentTag.getRssi() > maxRssi){
                maxRssi = currentTag.getRssi();
                selectedId = currentId;
            }
        }
        return selectedId;
    }

    /**
     * What to do when binder is in search mode.
     */
    private void onSearchMode() {

    }

    /**
     * Put the retrieved data in a tag in the arrayList
     */
    private void updateTagTable() {
        String[] stringStock = this.frame.split(";");

        //If the Id is already existing.
        if(tags.containsKey(stringStock[0])) {
            //Update current tag
            final String date = tags.get(stringStock[0]).getDate();
            final String time = tags.get(stringStock[0]).getTime();
            final int readCount = tags.get(stringStock[0]).getReadCount();
            tags.put(stringStock[0], new Tag(Integer.parseInt(stringStock[1]),
                    Integer.parseInt(stringStock[2]),
                    readCount + 1,
                    date,
                    time
            ));
        }
        else {
            //Insert it in the Hashtable
            this.tags.put(stringStock[0]/*Id*/,
                    new Tag(Integer.parseInt(stringStock[1]),/*RSSI*/
                    Integer.parseInt(stringStock[2]),/*Phase*/
                    1, /*Readcount*/
                    stringStock[3],/*Date*/
                    stringStock[4]/*Time*/));
        }
    }

    /**
     * Notify that a frame has been received.
     */
    private void signalFrameReceived() {
        for (BinderListener listener: this.listeners) {
            listener.notifyFrameReceived();
        }
    }

    /**
     * Notify that the tag to add has been selected.
     * @param id Id of the tag to add.
     */
    private void signalTagToAddFound(String id) {
        for (BinderListener listener: this.listeners) {
            listener.notifyTagToAddFound(new AddTagEvent(id));
        }
    }

    private void signalFrameBinded(int angle) {
        for (BinderListener listener: this.listeners) {
            listener.notifyFrameBinded(new FrameBindedEvent(
                    this.tags.get(this.tagTofind).getRssi(),
                    this.tags.get(this.tagTofind).getPhase(),
                    this.tags.get(this.tagTofind).getReadCount(),
                    angle));
        }
    }

    /**
     * Add a listener to the list.
     * @param listener The listener to add.
     */
    public void addListener(BinderListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove a listener from the list.
     * @param listener The listener to remove.
     */
    public void removeListener(BinderListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        this.signalFrameBinded(event.getAngle());
    }

    @Override
    public void notifyAngleStabilized() {
        for (BinderListener listener: this.listeners) {
            listener.notifyAngleStabilized();
        }
    }


}
