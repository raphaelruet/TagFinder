package com.eseoteam.android.tagfinder;

import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage the research process
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class SearchActivity extends ActionBarActivity implements GuideListener{

    // Attributes //

    /**
     * Header to print a log message
     */
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    /**
     * Fields of the textView
     */
    TextView textTagName;
    TextView textTagId;
    TextView textTimestamp;
    TextView textTagData;

    /**
     * Fields of the database
     */
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String COLUMN_TIME_STAMP = "timestamp";
    private static final String TAG_DATA = "tag_data";

    /**
     * Id in the database
     */
    private long idInDatabase;

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * The Guide
     */
    private Guide guide;

    /**
     * The Binder
     */
    private Binder binder;

    /**
     * The PieChart
     */
    private PieChart pieChart;

    // Methods //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        //Ajout des listeners sur les boutons
        setListeners();

        Compass compass = new Compass((SensorManager) getSystemService(SENSOR_SERVICE));

        //Récupération du pieChart du layout
        this.pieChart = (PieChart)findViewById(R.id.pieChart);

        //Création du Binder
        this.binder = new Binder(Binder.Mode.SEARCH, compass);

        //Création du guide
        this.guide = new Guide(this.binder,this);
        this.guide.start();

        // Helper pour la base de données
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
     * Sets the different listener
     */
    private void setListeners() {
        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);

        //calibrateButton
        PieChart pieChart = (PieChart)findViewById(R.id.pieChart);
        pieChart.setOnClickListener(this.calibrateButtonListener);
    }

    /**
     * Sets a listener on the backButton
     */
    private View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * Sets a listener on the calibrateButton
     */
    private View.OnClickListener calibrateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pieChart.setClickable(false);
            guide.setState(Guide.State.CALIBRATION);
        }
    };

    /**
     * Asks the user to do the calibration
     */
    @Override
    public void notifyCalibrationAsked() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                Toast.makeText(
                        getApplicationContext(),
                        "Please keep the phone still and horizontal, then tap the blue radar",
                        Toast.LENGTH_LONG
                ).show();
            }
        };
        this.runOnUiThread(action);
        Log.e(LOG_TAG,"User as been asked to calibrate");
    }

    /**
     * Asks the user to scan the environment
     */
    @Override
    public void notifyScanAsked() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                Toast.makeText(
                        getApplicationContext(),
                        "Calibration complete",
                        Toast.LENGTH_SHORT
                ).show();
                Toast.makeText(
                        getApplicationContext(),
                        "Keep the phone horizontal and slowly make a 360 degree turn",
                        Toast.LENGTH_LONG
                ).show();
            }
        };
        this.runOnUiThread(action);
        Log.e(LOG_TAG,"User as been asked to scan");
    }

    /**
     * Asks the SearchActivity to refresh the PieChart with the given values
     * @param event the event containing the new angles.
     */
    @Override
    public void notifyPieChartChanged(final PieChartChangedEvent event) {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                pieChart.setAngles(event.getAngles()[0], event.getAngles()[1]);
            }
        };
        this.runOnUiThread(action);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.guide.stopGuide();
    }
}
