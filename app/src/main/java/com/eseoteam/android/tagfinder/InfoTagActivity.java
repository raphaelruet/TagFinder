package com.eseoteam.android.tagfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    private DatabaseHelper databaseHelper;
    long tag_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tag);
        getSupportActionBar().hide();


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
