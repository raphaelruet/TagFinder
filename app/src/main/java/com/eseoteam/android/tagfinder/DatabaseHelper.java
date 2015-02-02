package com.eseoteam.android.tagfinder;

/**
 * Created on 13/01/15.
 * DatabaseHelper gives methods to manipulate items in the SQLite database
 * @author Charline LEROUGE.
 * @version 0.1.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

    /**
     * Table configuration: columns of the database
     */
    public static final String TABLE_NAME = "person_table";         // Table name
    private static final String TAG_ID = "_id";     // a column named "_id" is required for cursor
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";

    private SQLiteDatabase database;

    /**
     * Wrapper class: DatabaseHelper will perform database operations
     * @param aContext is the actual context
     */

    public DatabaseHelper(Context aContext) {

        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();

    }

    /**
     * Allows to insert data such as the tag name, id, information
     */
    public void insertData (String aTagName, String aTagId, String aTagData ) {


        //We are using ContentValues to avoid sql format errors
        ContentValues contentValues = new ContentValues();

        contentValues.put(TAG_NAME, aTagName);
        contentValues.put(TAG_MID, aTagId);
        contentValues.put(TAG_DATA, aTagData);

        database.insert(TABLE_NAME, null, contentValues);
    }


    /**
     * To get all the data from the table
     * @return Cursor
     */
    public Cursor getAllData () {

        String buildSQL = "SELECT * FROM " + TABLE_NAME;

        return database.rawQuery(buildSQL, null);
    }

    /**
     * To get the data from one particular item of the SQLite database
     * @param id of the item
     * @return cursor
     */

    public Cursor getOneTag(long id) {

        String buildSQL = "SELECT * FROM " + TABLE_NAME + " WHERE _id = ?";

        return database.rawQuery(buildSQL, new String[] { String.valueOf(id) });

    }

    /**
     * Delete one tag and his information from the database
     * @param id of the tag
     */
    public void deleteOneTag(long id){

        String whereClause = TAG_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        database.delete(TABLE_NAME, whereClause, whereArgs);

    }
}