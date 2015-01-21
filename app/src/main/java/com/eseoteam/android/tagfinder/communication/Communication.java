package com.eseoteam.android.tagfinder.communication;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.eseoteam.android.tagfinder.Binder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Manage the communication service.
 * Created on 08/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Communication extends Thread {

    //Attributes//
    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = "Communication";

    /**
     * UDP connection.
     */
    private Connection connection;

    /**
     * Command to perform actions on receipt.
     */
    private Command command;

    //Constructors//

    /**
     * Constructor of a UDP cmmunication
     * @param ip Ip of the device
     * @param port The port to which the socket is bound.
     * @param binder Binder which will receive the frames.
     */
    public Communication(String ip, int port, Binder binder){
        try {
            this.connection = new Connection(ip, port);
        } catch (UnknownHostException e) {
            Log.e(LOG_TAG,"Unknown host");
        } catch (SocketException e) {
            Log.e(LOG_TAG,"Error creating connection");
        }
        this.command = new ChangeFrameCommand(binder);
    }

    //Methods//
    /**
     * Main routine of the thread.
     * Wait for messages and interpret frames on receive.
     */
    public void run(){
        Log.i(LOG_TAG,"Run Starts");
        String receivedMessage = null;
        try {
            while (true) {
                receivedMessage  = this.connection.read();
                if (receivedMessage != null) {
                    this.command.execute(receivedMessage);
                    //interpretMessage(this.receivedMessage);
                    Log.i(LOG_TAG, "Message received" + receivedMessage);
                }
                else {
                    break;
                }
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG,"Error while reading frame");
        }
        finally {
            Log.i(LOG_TAG, "Closing communication");
            this.closeConnection();
        }
    }

    /**
     * Close the connection.
     */
    public void  closeConnection(){
        this.connection.closeConnection();
    }

    /**
     * Interpret the received message keeping only the relevant values.
     * @param frame Received message.
     */
    public void interpretMessage(String frame){
        final String[] stringStock = frame.split(";");
        try{
            this.command.execute(stringStock[7] + "," + stringStock[8]);
        } catch(NumberFormatException e){
            Log.e(LOG_TAG,"Number Format exception");
        }
    }

    /**
     * Retrieve the IP address of the device on the current Wifi network.
     * @param context Application context to get access to the WifiManager
     * @return IP address as a string.
     */
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
            Log.e(LOG_TAG, "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }
}