package com.eseoteam.android.tagfinder.communication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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
     * Connection socket.
     */
    private DatagramSocket socket=null;

    //Constructor//

    public Connection(DatagramSocket socket){
        this.socket = socket;
        try{
            this.socket.setSoTimeout(1000);
        }catch(SocketException e){
            Log.e("Connection:Connection","Problem setting timeout");
        }
    }

    //Accessors//

    /**
     * Donne la socket de la connexion
     * @return la socket de la connexion
     *//*
    public DatagramSocket getSocket(){
        return this.socket;
    }*/

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
            Log.e("Connection:read","Error while receiving a packet");
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
