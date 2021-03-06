package com.eseoteam.android.tagfinder;

/**
 * Created on 19/01/15.
 * DatabaseOpenHelper class will actually be used to perform database related operation
 * @author Charline LEROUGE.
 * @version 0.1.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    /**
     * Database configuration : name & version
     * If configuration of the onUpgrade then change DATABASE_NAME
     */
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "mydatabase.db";

    /**
     * Table configuration: columns of the database
     */
    public static final String TABLE_NAME = "person_table";         // Table name
    private static final String TAG_ID = "_id";     // a column named "_id" is required for cursor
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";
    private static final String COLUMN_TIME_STAMP = "timestamp";

    /**
     * Class constructor
     * @param aContext the current context
     */
    public DatabaseOpenHelper(Context aContext) {
        super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creation of the database
     * @param sqLiteDatabase the database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + TAG_ID + " INTEGER PRIMARY KEY, " +
                TAG_NAME + " TEXT, " + TAG_MID + " TEXT, " + TAG_DATA + " TEXT, " +
                COLUMN_TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        sqLiteDatabase.execSQL(buildSQL);
    }

    /**
     * Database schema upgrade code
     * @param sqLiteDatabase our database
     * @param oldVersion before the upgrade
     * @param newVersion after the upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // drop previous table
        sqLiteDatabase.execSQL(buildSQL);
        // create the table from the beginning
        onCreate(sqLiteDatabase);
    }
}
