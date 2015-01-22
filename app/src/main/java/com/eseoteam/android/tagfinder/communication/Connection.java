package com.eseoteam.android.tagfinder.communication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UDP Connection.
 * Created on 08/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Connection {

    //Attributes//
    /**
     * Maximal length (in bytes) of a received frame.
     */
    private final static int FRAME_LENGTH = 128;

    /**
     * Timeout of the socket in milliseconds.
     * If >0 the receive() method will block during this amount of time.
     * If = 0 it means that it's an infinite timeout.
     */
    private final static int SOCKET_TIMEOUT = 0;

    /**
     * Header to print a log message
     */
    private final static String LOG_TAG = "communication.Connection";

    /**
     * Connection socket.
     */
    private DatagramSocket socket;

    //Constructor//

    public Connection(String ip, int port) throws UnknownHostException, SocketException {
        try {
        InetAddress wifiAddress = InetAddress.getByName(ip);
        this.socket = new DatagramSocket(port, wifiAddress);
        this.socket.setSoTimeout(SOCKET_TIMEOUT);
        }
        catch(SocketException e){
            Log.e(LOG_TAG,"Error creating socket");
            throw e;
        }
        catch(UnknownHostException uhe) {
            Log.e(LOG_TAG,"Bad host");
            throw uhe;
        }
        Log.i(LOG_TAG,"Connection Created");
    }

    //Methods//
    /**
     * Read a message on the socket.
     * @return The received message.
     * @throws IOException Input/Output Exception
     */
    public String read() throws IOException {
        String msg;
        try {
            DatagramPacket packet = new DatagramPacket(new byte[FRAME_LENGTH], FRAME_LENGTH);
            this.socket.receive(packet);
            msg = new String(packet.getData(), 0, packet.getLength());
        }catch(IOException e){
            Log.e(LOG_TAG,"Error while receiving a packet");
            throw e;
        }
        return msg;
    }

    /**
     * Close the connection.
     */
    public void closeConnection() {
        this.socket.close();
    }
}
