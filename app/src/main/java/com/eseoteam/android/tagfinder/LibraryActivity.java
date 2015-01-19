package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Manage the tag library
 * Created on 08/01/2014.
 * @author Raphael RUET, Charline LEROUGE
 * @version 0.1.
 */
public class LibraryActivity extends Activity {

    /**
     * Creates the inflator to inflate each tag in the single_tag layout.
     */
    private CustomCursorAdapter customAdapter;


    /**
     * Helper for the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * The tag list
     */
    private ListView listView;

    /**
     * Gets the activity name
     */
    private static final String TAG = LibraryActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        //Sets the add tag button listener.
        ImageButton addTagButton = (ImageButton)findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(this.addTagButtonListener);

        //Sets the quit button listener.
        ImageButton quitButton = (ImageButton)findViewById(R.id.quitButton);
        quitButton.setOnClickListener(this.quitButtonListener);

        databaseHelper = new DatabaseHelper(this);

        listView = (ListView) findViewById(R.id.listview_tag);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked on item: " + position);
                Log.d("Clicked item id", " "+ id);

                Intent intent = new Intent(LibraryActivity.this, InfoTagActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, id);
                startActivity(intent);
            }
        });

    }

    /**
     * Refreshes the tag list on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomCursorAdapter(LibraryActivity.this,
                        databaseHelper.getAllData());
                listView.setAdapter(customAdapter);
            }
        });
    }

    /**
     * Listener on the addButton.
     * Show the addTag screen when the addTagButton is pressed
     */
    private View.OnClickListener addTagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Go to the addButton activity.
            startActivity(new Intent(getApplicationContext(), AddTagActivity.class));
        }

    };

    /**
     * Listener on the quitButton.
     * Closes the activity and the Application
     */
    private View.OnClickListener quitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Closes the activity
            finish();
            //Quits the app
            System.exit(0);
        }

    };

}
