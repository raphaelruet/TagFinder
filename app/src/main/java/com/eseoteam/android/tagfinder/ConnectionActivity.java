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
import android.widget.Toast;


public class ConnectionActivity extends ActionBarActivity {

    /**
     * The request code chen user goes to the Wifi settings.
     * Must be >=0 to be returned.
     */
    private static final int REQUEST_ENABLE_WIFI = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        //Hide the top action bar.
        getSupportActionBar().hide();
        //Set the skip checking connection button.
        Button skipButton = (Button)findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this.skipButtonListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //If the wifi is disabled, ask the user to enable it.
        if (!this.isWifiEnabled()) {
            this.displayWifiAlertDialog();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the wifi is disabled, ask the user to enable it.
        switch (requestCode) {
            case REQUEST_ENABLE_WIFI:
                if (!this.isWifiEnabled()) {
                    this.displayWifiAlertDialog();
                }
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
}
