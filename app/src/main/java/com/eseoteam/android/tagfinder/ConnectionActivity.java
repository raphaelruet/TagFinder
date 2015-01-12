package com.eseoteam.android.tagfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ConnectionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().hide();

        Button skipButton = (Button)findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this.skipButtonListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Retrieve the network state.
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //If the wifi is disabled, ask the user to enable it.
        if (!mWifi.isConnected()) {
            this.startWifiAlertDialog();
        }
    }

    /**
     * Displays an AlertDialog asking to enable wifi or not.
     */
    private void startWifiAlertDialog() {
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
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
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
}
