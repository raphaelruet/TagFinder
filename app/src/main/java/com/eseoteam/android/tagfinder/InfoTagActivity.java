package com.eseoteam.android.tagfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created on 15/01/15.
 * Manage the InfoTag Activity
 * @author Charline LEROUGE.
 * @version 0.1.
 */


public class InfoTagActivity extends ActionBarActivity {

    //Fields of the textView
    TextView textTagName;
    TextView textTagId;
    TextView textTagData;

    //Fields in the database
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";
    static SQLiteDatabase db = null;
    int tag_id;

    private long idInDatabase;

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * Actions to perform when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tag);
        getSupportActionBar().hide();

        //Sets the listener
        setListeners();

        //String tagName = this.getIntent().getStringExtra(TAG_NAME);
        this.databaseHelper = new DatabaseHelper(this);

        //Find the fields to fill with information
        textTagName = (TextView) findViewById(R.id.tagName);
        textTagId = (TextView) findViewById(R.id.tagId);
        textTagData = (TextView) findViewById(R.id.tagInfo);

        //Find clicked tag in database
        this.idInDatabase = getIntent().getLongExtra("tag_id_in_db", -1);
        Cursor cursor = databaseHelper.getOneTag(idInDatabase);

        // Fill the empty field with right tag information from database
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            textTagName.append(name);
            String id = cursor.getString(cursor.getColumnIndex(TAG_MID));
            textTagId.append(id);
            String info = cursor.getString(cursor.getColumnIndex(TAG_DATA));
            textTagData.append(info);
        }
    }

    /**
     * Calls the differents listener setters
     */
    private void setListeners() {
        //searchButton
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this.searchButtonListener);

        //searchButton
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this.deleteButtonListener);
    }

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO Demarrer la recherche pour le tag avec l'id passé
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }
    };

    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseHelper.deleteOneTag(idInDatabase);
            finish();
        }
    };


}


