package com.eseoteam.android.tagfinder;

/**
 * Describes a RFID tag with all the attributes received from a real tag.
 * Created on 21/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Tag {

    //Attributes
    /**
     * Current RSSI of the tag
     */
    private int rssi;

    /**
     * Current phase of the tag
     */
    private int phase;

    /**
     * Number of times the tag has been read.
     */
    private int readCount;

    /**
     * The date when the tag was first read.
     */
    private String date;

    /**
     * The time when the tag was first read.
     */
    private String time;

    //Constructor
    /**
     * Constructor of a tag.
     * @param rssi Rssi of the tag
     * @param phase Phase of the tag.
     * @param readCount Number of times the tag has been read.
     * @param date Date when the tag was read.
     * @param time Time when the time was read.
     */
    public Tag(int rssi, int phase, int readCount, String date, String time) {
        this.rssi = rssi;
        this.phase = phase;
        this.readCount = readCount;
        this.date = date;
        this.time = time;
    }

    //Accessor

    /**
     * Gives the rssi of the tag.
     * @return The rssi of the tag.
     */
    public int getRssi() {
        return this.rssi;
    }

    /**
     * Gives the phase of the tag.
     * @return The current phase of the tag.
     */
    public int getPhase(){
        return this.phase;
    }

    /**
     * Gives the number of times the tag has been read.
     * @return The number of times the tag has been read.
     */
    public int getReadCount() {
        return this.readCount;
    }

    /**
     * Gives the date when the tag was first read.
     * @return The date when the tag was first read.
     */
    public String getDate() {
        return  this.date;
    }

    /**
     * Give the time when the tag was first read.
     * @return The time when the tag was first read.
     */
    public String getTime() {
        return this.time;
    }
}
