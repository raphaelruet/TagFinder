package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eseoteam.android.tagfinder.communication.Communication;
import com.eseoteam.android.tagfinder.events.AddTagEvent;
import com.eseoteam.android.tagfinder.events.AngleChangedEvent;
import com.eseoteam.android.tagfinder.events.FrameBindedEvent;

/**
 * Manage the tag adding
 * Created on 08/01/2014.
 * @author Raphael RUET, Charline LEROUGE, Pierre TOUZE
 * @version 0.3.
 */
public class AddTagActivity extends Activity implements BinderListener {

    /**
     * The default connection port of the socket.
     */
    private static final int CONNECTION_PORT = 12345;

    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = "AddTagActivity";
    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * Communication with the UDP client
     */
    private Communication communication;

    /**
     * Binder to retrieve incoming tag data and link them with angles.
     */
    private Binder binder;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        this.databaseHelper = new DatabaseHelper(this);

        //Sets the button listeners
        setListeners();

    }

    /**
     * Calls the differents listener setters
     */
    private void setListeners() {
        //validButton
        ImageButton validButton = (ImageButton) findViewById(R.id.validButton);
        validButton.setOnClickListener(this.validButtonListener);

        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);

        //scanButton
        Button scanTagButton = (Button) findViewById(R.id.scanTagButton);
        scanTagButton.setOnClickListener(this.scanTagButtonListener);
    }

    /**
     * Listener on the validButton.
     * Adds  the activity and the Application
     */
    private View.OnClickListener validButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Retrieve the three edit text containing tag info.
            EditText tagNameField = (EditText) findViewById(R.id.tagNameField);
            EditText tagIdField = (EditText) findViewById(R.id.tagIdField);
            EditText tagDataField = (EditText) findViewById(R.id.tagDataField);
            //Get the string contained in the editTexts.
            final String tagNameFieldString = tagNameField.getText().toString();
            final String tagIdFieldString = tagIdField.getText().toString();
            final String tagDataFieldString = tagDataField.getText().toString();

            //Checks if tagName or tagId are empty
            //TODO Put the security for empty tag field over.
            if(tagNameFieldString.matches("")/* || tagIdFieldString.matches("")*/) {
                Toast.makeText(getApplicationContext(),
                        R.string.toast_empty_field, Toast.LENGTH_SHORT).show();
            }
            else {
                databaseHelper.insertData(tagNameFieldString,tagIdFieldString,tagDataFieldString);
                finish();
            }
        }
    };

    /**
     * Sets the listener on the backButton to return to the LibraryActivity
     */
    private View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * Sets the listener on the scanButton to lauch the connection and scan the tag to add
     */
    private View.OnClickListener scanTagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(communication == null) {
                ((Button) findViewById(R.id.scanTagButton)).setText(R.string.scanning_tag);
                initializeCommunication();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.communication !=null) {
            this.communication.closeConnection();
            try {
                this.communication.join();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG,"Join interrupted");
            }
        }
        this.binder.removeListener(this);
    }

    /**
     * Starts the communication by connecting the socket to the device ip and the specified port.
     */
    private void initializeCommunication() {
        String wifiAddress = Communication.getWifiIpAddress(getApplicationContext());
        Log.e(LOG_TAG, "Wifi Address:" + wifiAddress);

        this.binder = new Binder(Binder.Mode.ADD_TAG);
        this.binder.addListener(this);
        this.communication = new Communication(wifiAddress, CONNECTION_PORT, binder);
        this.communication.start();
    }

    @Override
    public void notifyAngleChanged(AngleChangedEvent event) {
        //Nothing to be done here
    }

    @Override
    public void notifyAngleStabilized() {
        // Nothing to be done here
    }

    @Override
    public void notifyTagToAddFound(AddTagEvent event) {
        final String id = event.getId();
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                EditText tagIdField = (EditText) findViewById(R.id.tagIdField);
                tagIdField.setText(id);
                ((Button) findViewById(R.id.scanTagButton)).setText(R.string.done_scanning_tag);
            }
        };
        this.runOnUiThread(action);
    }

    @Override
    public void notifyFrameBinded(FrameBindedEvent event) {
        //Nothing to be done here
    }

    @Override
    public void notifyFrameReceived() {
        //Nothing to be done here.
    }
}