package com.eseoteam.android.tagfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Manage the tag adding
 * Created on 08/01/2014.
 * @author Raphael RUET, Charline LEROUGE
 * @version 0.1.
 */
public class AddTagActivity extends Activity {

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        databaseHelper = new DatabaseHelper(this);

        //Sets the listener
        setListeners();

    }

    /**
     * Calls the differents listener setters
     */
    private void setListeners() {
        //validButton
        ImageButton validButton = (ImageButton) findViewById(R.id.validButton);
        validButton.setOnClickListener(this.validButtonListener);

        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);

        //scanButton
        Button scanTagButton = (Button) findViewById(R.id.scanTagButton);
        scanTagButton.setOnClickListener(this.scanTagButtonListener);
    }

    /**
     * Listener on the valid.
     * Adds  the activity and the Application
     */
    private View.OnClickListener validButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO Informer l'utilisateur en cas de champs laissés vides
            storeTagIntoDatabase();
            finish();
        }
    };

    /**
     * Used to store tag information into the database
     */
    private void storeTagIntoDatabase () {
        EditText tagNameField = (EditText) findViewById(R.id.tagNameField);
        EditText tagIdField = (EditText) findViewById(R.id.tagIdField);
        EditText tagDataField = (EditText) findViewById(R.id.tagDataField);

        databaseHelper.insertData(
                tagNameField.getText().toString(),
                tagIdField.getText().toString(),
                tagDataField.getText().toString());

    }

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
     * Sets the listener on the scanButton to lauch the connection and scan the tag to add
     */
    private View.OnClickListener scanTagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Button) findViewById(R.id.scanTagButton)).setText(R.string.scanning_tag);
            //TODO Ouvrir la connection et récupérer le tagID et le foutre dans le tagIdField
        }
    };

    /**
     * Closes the connection anyway
     */
    protected void onDestroy() {
        super.onDestroy();
        //TODO Clore la connection si elle est active
    }

}


