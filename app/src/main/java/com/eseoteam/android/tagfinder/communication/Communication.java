package com.eseoteam.android.tagfinder.communication;

import android.util.Log;

import com.eseoteam.android.tagfinder.Binder;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Manage the communication service.
 * Created on 08/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public class Communication extends Thread {
    //Attributes//
    /**
     * UDP connection.
     */
    private Connection connection;

    /**
     * The received message on the socket.
     */
    private String receivedMessage = null;

    /**
     * Connection status.
     */
    private boolean connected;

    /**
     * Command to perform actions on receipt.
     */
    private Command command;

    //Constructors//
    public Communication(DatagramSocket socket, Binder binder){

        this.connection = new Connection(socket);
        this.connected = true;
        this.command = new ChangeFrameCommand(binder);
    }

    //Accessor//

    /**
     * Gives the received message on the socket.
     * @return The received message as a String.
     */
    /*
    public String getReceivedMessage(){
        return this.receivedMessage;
    }*/

    //Methods//
    public void run(){
        while(this.connected){
            try {
                this.receivedMessage = this.connection.read();
            } catch (IOException e) {
                //TODO
                //Log.e("Communication:run","Error while reading frame");
            }
            if(this.receivedMessage != null){
                this.command.execute(this.receivedMessage);
                //interpretMessage(this.receivedMessage);
                Log.d("Communication", "Message received" + this.receivedMessage);
            }
        }
        this.closeConnection();
    }

    /**
     * Set the connection status to false.
     */
    /*
    public void disconnect() {
        this.connected = false;
    }*/

    /**
     * Close the connection.
     */
    private void  closeConnection(){
        this.connection.closeConnection();
    }

    public void interpretMessage(String frame){
        final String[] stringStock = frame.split(";");
        try{
            this.command.execute(stringStock[7]+","+stringStock[8]);
        } catch(NumberFormatException e){
            Log.e("Communication","Number Format exception");
        }
    }
}
