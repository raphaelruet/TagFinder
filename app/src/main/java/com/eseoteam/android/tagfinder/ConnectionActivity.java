package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eseoteam.android.tagfinder.communication.Communication;
import com.eseoteam.android.tagfinder.events.AddTagEvent;


/**
 * Manage the Connection screen
 * Created on 12/01/2014.
 * @author Raphael RUET, Pierre TOUZE
 * @version 0.3
 */
public class ConnectionActivity extends Activity implements BinderListener{

    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = ConnectionActivity.class.getSimpleName();

    /**
     * The request code chen user goes to the Wifi settings.
    * Must be >=0 to be returned.
    */
    private static final int REQUEST_ENABLE_WIFI = 1;

    /**
     * The default connection port of the socket.
     */
    private static final int CONNECTION_PORT = 12345;

    /**
     * Ip address of the device.
     */
    private String wifiAddress;

    /**
     * Communication with the UDP client
     */
    private Communication communication;

    /**
     * Binder to retrieve incoming tag data and link them with angles.
     */
    private Binder binder;

    /**
     * The onCreate method
     * @param savedInstanceState the state of the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        //Set the skip checking connection button.
        Button skipButton = (Button)findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this.skipButtonListener);

    }

    /**
     * Called when activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        //If the wifi is disabled, ask the user to enable it.
        if (!this.isWifiEnabled()) {
            this.displayWifiAlertDialog();
        }
        else {
            this.initializeCommunication();
        }
    }

    /**
     * Called when finish() is used.
     */
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
            this.binder.removeListener(this);
        }

    }

    /**
     * Called when the "physical" back button is pressed
     */
    @Override
    public void onBackPressed() {
        //Do nothing when back button is pressed.
        //i.e. disable the back button.
    }

    /**
     * Displays an AlertDialog asking to enable wifi or not.
     */
    private void displayWifiAlertDialog() {
        //Creates alertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.wifi_dialog_title);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.wifi_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.wifi_dialog_yes,wifiYesListener)
                .setNegativeButton(R.string.wifi_dialog_no,wifiNoListener);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Called when the user come back after the wifi settings.
     * @param requestCode The request code given when the settings when first called.
     *                    if the code is the same it means that everything went right
     * @param resultCode The result code fro the activity.
     * @param data The intent from the activity that can contain data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the wifi is disabled, ask the user to enable it.
        switch (requestCode) {
            case REQUEST_ENABLE_WIFI:
                Log.i(LOG_TAG, "Everything is ok");
                break;
            default:
                Toast.makeText(getApplicationContext(),
                        R.string.result_error,Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }

    }

    /**
     * Checks if the Wifi is enabled
     * @return True if the Wifi is enabled else false.
     */
    private boolean isWifiEnabled() {
        //Retrieve the network state.
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }
    /**
     * Listener on the skipButton.
     * Switches to the libraryActivity when pressed.
     */
    private View.OnClickListener skipButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Go to the library activity.
            startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
            finish();
        }
    };

    /**
     * Listener on the "Yes" button of the wifi AlertDialog
     */
    private DialogInterface.OnClickListener wifiYesListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQUEST_ENABLE_WIFI);

        }
    };

    /**
     * Listener on the "No" button of the wifi AlertDialog.
     * User doesn't want to enable wifi.
     * Assumed he just wants to consult tag info
     */
    private DialogInterface.OnClickListener wifiNoListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //Close the dialog.
            dialog.cancel();
            //Go to the library activity.
            startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
            finish();
        }
    };

    /**
     * Starts the communication by connecting the socket to the device ip and the specified port.
     */
    private void initializeCommunication() {
            this.wifiAddress = Communication.getWifiIpAddress(getApplicationContext());
            Log.e(LOG_TAG, "Wifi Address:" + wifiAddress);

            this.binder = new Binder(Binder.Mode.CHECK_CONNECTION);
            this.binder.addListener(this);
            this.communication = new Communication(this.wifiAddress, CONNECTION_PORT, binder);
            this.communication.start();
    }

    /**
     * Notifies that a frame has been received
     */
    @Override
    public void notifyFrameReceived() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                //Notify user he's connected.
                Toast.makeText(getApplicationContext(),"Connected with IP: " + wifiAddress
                        + "\nOn port : " + CONNECTION_PORT,Toast.LENGTH_LONG).show();
                //Go to library screen
                startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
            }
        };
        this.runOnUiThread(action);
        //Close the connectionActivity;
        this.finish();
    }

    /**
     * Nothing to be done here
     * @param event Event containing the id of th tag to add.
     */
    @Override
    public void notifyTagToAddFound(AddTagEvent event) {
        //Nothing to be done here.
    }
}
