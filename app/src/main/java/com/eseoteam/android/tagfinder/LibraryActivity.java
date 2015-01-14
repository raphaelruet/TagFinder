package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class LibraryActivity extends Activity {

    private CustomCursorAdapter customAdapter;
    private DatabaseHelper databaseHelper;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    private ListView listView;

    private static final String TAG = LibraryActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        databaseHelper = new DatabaseHelper(this);

        listView = (ListView) findViewById(R.id.listview_tag);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked on item: " + position);
            }
        });

        // Database query can be a time consuming task ..
        // so its safe to call database query in another thread
        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomCursorAdapter(LibraryActivity.this, databaseHelper.getAllData());
                listView.setAdapter(customAdapter);
            }
        });
    }

    public void onClickEnterData(View btnAdd) {

        startActivityForResult(new Intent(this, AddTagActivity.class), ENTER_DATA_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

            databaseHelper.insertData(data.getExtras().getString("tag_name"),
                    data.getExtras().getString("tag_id"),
                    data.getExtras().getString("tag_data"));

            customAdapter.changeCursor(databaseHelper.getAllData());
        }
    }
}






   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);
        getSupportActionBar().hide();

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.libraryActivity, new TagListFragment())
                    .commit();
        }*/

        /*findViewById(R.id.quitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddTagActivity.class));
            }
        });

    }*/
