package com.eseoteam.android.tagfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eseoteam.android.tagfinder.communication.Communication;

import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;


public class ConnectionActivity extends ActionBarActivity implements BinderListener{

    /**
     * The request code chen user goes to the Wifi settings.
     * Must be >=0 to be returned.
     */
    private static final int REQUEST_ENABLE_WIFI = 1;

    /**
     * The default connection port of the socket.
     */
    private static final int CONNECTION_PORT = 12345;

    private String wifiAddress;

    private Communication communication;

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
        else {
            this.initializeCommunication();
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
                else {
                    this.initializeCommunication();
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

    protected String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("ConnectionActivity:getWifiIpAddress", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    /**
     * Starts the communication by connecting the socket to the device ip and the specified port.
     */
    private void initializeCommunication() {
        try {
            this.wifiAddress = this.getWifiIpAddress(getApplicationContext());
            Log.d("ConnectionActivity","Wifi Address:" + wifiAddress);
            DatagramSocket socket = new DatagramSocket(CONNECTION_PORT,
                    InetAddress.getByName(this.wifiAddress));
            Binder binder = new Binder();
            binder.addListener(this);
            this.communication = new Communication(socket,binder);
            this.communication.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch(UnknownHostException uhe){
        Log.e("Main","Bad host");
    }

    }

    @Override
    public void notifyFrameChange(FrameChangedEvent event) {
        final String address = this.wifiAddress;
        final int port = this.CONNECTION_PORT;
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                //Notify user he's connected.
                Toast.makeText(getApplicationContext(),"Connected with IP: " + address
                        + "\nOn port : " + port,Toast.LENGTH_LONG).show();
                //Go to library screen
                startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
            }
        };
        this.runOnUiThread(action);
        //Close the connectionActivity;
        this.finish();
    }
}
