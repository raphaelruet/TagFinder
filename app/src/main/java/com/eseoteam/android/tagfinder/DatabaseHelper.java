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
import android.util.Log;

public class DatabaseHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();



    /**
     * Table configuration: columns of the database
     */
    public static final String TABLE_NAME = "person_table";         // Table name
    private static final String TAG_ID = "_id";     // a column named "_id" is required for cursor
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;


    /**
     * Wrapper class: DatabaseHelper will perform database operations
     * @param aContext is the actual context
     */

    public DatabaseHelper(Context aContext) {

        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();

    }

    /**
     * Allows to insert data such as the tag name, id, information
     */
    public void insertData (String aTagName, String aTagId, String aTagData) {

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

        Log.d(TAG, "getAllData SQL: " + buildSQL);

        return database.rawQuery(buildSQL, null);
    }

    /**
     * To get the data from one particular item of the SQLite database
     * @param id of the item
     * @return cursor
     */

    public Cursor getOneTag(int id) {

        String buildSQL = "SELECT * FROM " + TABLE_NAME + " WHERE _id = ?";

        Log.d(TAG, "getOneTag SQL: " + buildSQL);

        return database.rawQuery(buildSQL, new String[] { String.valueOf(id) });

        // 1. get reference to readable DB
       /* SQLiteDatabase db = openHelper.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME, // a. table
                        COLUMNS,  // b. column names
                        " _id = ?", // c. selections
                        new String[] { String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        Log.d("getBook("+id+")", "getOneTag SQL :");

        // 5. return book
        return cursor;*/
    }

    public void deleteOneTag(long id){
        String string =String.valueOf(id);
        database.execSQL("DELETE FROM TABLE_NAME WHERE _id = '" + string + "'");
    }

}