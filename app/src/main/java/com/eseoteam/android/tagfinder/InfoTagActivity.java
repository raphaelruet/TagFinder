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
        databaseHelper = new DatabaseHelper(this);

        //Find the fields to fill with information
        textTagName = (TextView) findViewById(R.id.tagName);
        textTagId = (TextView) findViewById(R.id.tagId);
        textTagData = (TextView) findViewById(R.id.tagInfo);

        //Find clicked tag in database
        long id_in_db = getIntent().getLongExtra("tag_id_in_db", -1);
        Cursor cursor = databaseHelper.getOneTag(id_in_db);

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
     * Sets the different listener
     */
    private void setListeners() {
        //searchButton
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this.searchButtonListener);

        //searchButton
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this.deleteButtonListener);

        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);
    }

    /**
     * Sets the listener on the searchButton
     */
    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO Demarrer la recherche pour le tag avec l'id passé
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }
    };

    /**
     * Sets the listener on the deleteButton
     */
    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
       /* Problème: Pas possible d'ajouter un id sur un listener de type View() et non AdapterView
        AdapterView pour un click sur une listView et non sur un bouton
       @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            databaseHelper.deleteOneTag(id);//create remove method in database class
        }*/
        @Override
        public void onClick(View v) {
            //TODO Supprimer le tag tag avec l'id passé
            //TODO Fermer l'activité
            //finish();
        }
    };

    /**
     * Sets the listener on the backButton
     */
    private View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
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
