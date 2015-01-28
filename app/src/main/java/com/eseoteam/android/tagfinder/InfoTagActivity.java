package com.eseoteam.android.tagfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created on 15/01/15.
 * Manage the InfoTag Activity
 * @author Charline LEROUGE.
 * @version 0.2.
 */


public class InfoTagActivity extends ActionBarActivity {

    /**
     * Fields of the textView
     */
    TextView textTagName;
    TextView textTagId;
    TextView textTagData;
    TextView textTimestamp;

    /**
     * Fields in the database
     */
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String TAG_DATA = "tag_data";
    private static final String COLUMN_TIME_STAMP = "timestamp";

    /**
     * The X position of the toasts from the top of the screen
     */
    private int toastYPosition;

    /**
     * Id in the database
      */
    private long idInDatabase;

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * Actions to perform when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tag);
        getSupportActionBar().hide();

        // Récupération des dimensions de l 'écran afin de régler la position des toasts
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        toastYPosition = (displayMetrics.heightPixels/2);

        //Sets the listener
        setListeners();

        //String tagName = this.getIntent().getStringExtra(TAG_NAME);
        this.databaseHelper = new DatabaseHelper(this);

        //Find the fields to fill with information
        textTagName = (TextView) findViewById(R.id.tagName);
        textTagId = (TextView) findViewById(R.id.tagId);
        textTimestamp = (TextView) findViewById(R.id.tagDate);
        textTagData = (TextView) findViewById(R.id.tagInfo);



        //Find clicked tag in database
        this.idInDatabase = getIntent().getLongExtra("tag_id_in_db", -1);
        Cursor cursor = databaseHelper.getOneTag(idInDatabase);

        // Fill the empty field with right tag information from database
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            textTagName.append(name);
            String id = cursor.getString(cursor.getColumnIndex(TAG_MID));
            textTagId.append(id);
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_STAMP));
            textTimestamp.append(date);
            String info = cursor.getString(cursor.getColumnIndex(TAG_DATA));
            textTagData.append(info);

        }
    }

    /**
     * Calls the differents listener setters
     */
    private void setListeners() {
        //searchButton
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this.searchButtonListener);

        //searchButton
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this.deleteButtonListener);

        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);
    }

    /**
     * Sets the listener on the searchButton to access to the SearchActivity
     */

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        if(isWifiEnabled()) {
            //Go to the search activity.
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("tag_id_in_db",idInDatabase);
            startActivity(intent);        }
        else {
            //Notify user that the wifi isn't enabled.
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.toast_no_wifi2, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,toastYPosition);
            toast.show();
        }
        }
    };

    /**
     * Sets the listener on the deleteButton to delete a tag from the database
     */

    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayDeleteAlertDialog();
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
     * Displays an AlertDialog asking to enable wifi or not.
     */
    private void displayDeleteAlertDialog() {
        //Creates alertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.delete_dialog_title);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.delete_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.delete_dialog_yes,deleteYesListener)
                .setNegativeButton(R.string.delete_dialog_no, deleteNoListener);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Listener on the "Yes" button of the delete AlertDialog
     */
    private DialogInterface.OnClickListener deleteYesListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            databaseHelper.deleteOneTag(idInDatabase);
            finish();

        }
    };

    /**
     * Listener on the "No" button of the delete AlertDialog.
     * User finally doesn't want to delete tag.
     */
    private DialogInterface.OnClickListener deleteNoListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //Close the dialog.
            dialog.cancel();
        }
    };

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

}
