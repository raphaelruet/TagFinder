package com.eseoteam.android.tagfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class InfoTagActivity extends ActionBarActivity {
    TextView textTagName;
    TextView textTagId;
    TextView textTagData;
    String tagId;
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";
    static SQLiteDatabase db = null;
    long tag_id;

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tag);
        getSupportActionBar().hide();

        //Sets the listener
        setListeners();

        //String tagName = this.getIntent().getStringExtra(TAG_NAME);
        databaseHelper = new DatabaseHelper(this);


        textTagName = (TextView) findViewById(R.id.tagName);
        textTagId = (TextView) findViewById(R.id.tagId);
        textTagData = (TextView) findViewById(R.id.tagInfo);

        Intent intent = getIntent();
        Cursor cursor = databaseHelper.getAllData();

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("tag_name"));
            textTagName.append(name);
            String id = cursor.getString(cursor.getColumnIndex("tag_mid"));
            textTagId.append(id);
            String info = cursor.getString(cursor.getColumnIndex("tag_data"));
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
            //TODO Supprimer le tag tag avec l'id passé
            //TODO Fermer l'activité
        }
    };
}

        /*textTagName.setText("");
        textTagId.setText("");
        textTagData.setText("");
        //move cursor to first position
        cursor.moveToFirst();
        //we use data using column index
        String name =cursor.getString(cursor.getColumnIndex("tag_name"));
        String id =cursor.getString(cursor.getColumnIndex("tag_mid"));
        String info =cursor.getString(cursor.getColumnIndex("tag_data"));
        //display on text view
        textTagName.append(name);
        textTagId.append(id);
        textTagData.append(info);*/

        /*String tagName = this.getIntent().getStringExtra(TAG_NAME);
        textTagName = (TextView) findViewById(R.id.tagName);

        databaseHelper = new DatabaseHelper(this);
        cursor = databaseHelper.getAllData();

        if (tagName != null) {
            cursor.moveToPosition(2);
            textTagName.setText(cursor.getString(cursor.getColumnIndex("tag_name")));

    }

}*/
