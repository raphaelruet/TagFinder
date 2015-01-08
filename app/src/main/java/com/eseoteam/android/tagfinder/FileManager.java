package com.eseoteam.android.tagfinder;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Read/Write an internal file.
 * Created on 08/01/2015.
 * @author Pierre TOUZE.
 * @version 0.1.
 */
public final class FileManager {

    /**
     * Encoding format to write data into file.
     */
    private static final String ENCODING = "UTF-8";

    private FileManager() {
    }

    /**
     * Read the content of a specific file.
     * @param context Activity's context.
     * @param fileName Name of the file to write in.
     * @return The content of the file as a String.
     * @throws IOException
     */
    public static final String readFromFile(Context context, String fileName) throws IOException {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.e("FileManager:readFromFile", "Can't read");
            throw e;
        }

        return ret;
    }

    /**
     * Write data into a specified file.
     * @param context Activity's context.
     * @param fileName Name of the file to write in.
     * @param data Data to write.
     */
    public static final void writeToFile(Context context, String fileName, String data) throws IOException {
        //Add an "\n" to add a new line
        String messageToSend = data + "\n";
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE),ENCODING);
            outputStreamWriter.write(messageToSend);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("FileManager:writeToFile", "Can't write");
            throw e;
        }
    }


}
