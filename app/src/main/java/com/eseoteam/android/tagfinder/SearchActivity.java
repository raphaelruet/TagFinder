package com.eseoteam.android.tagfinder;

import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Manage the research process
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class SearchActivity extends ActionBarActivity implements GuideListener{

    // Attributes //

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
     * The sensor manager for the Search activity
     */
    private SensorManager sensorManager;

    private Guide guide;

    /**
     * The PieChart used in the activity_search view
     */
    PieChart pieChart;

    private Binder binder;

    // Methods //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        //Ajout des listeners sur les boutons
        setListeners();

        // Récupération du PieChart du layout
        pieChart = (PieChart) findViewById(R.id.pieChart);

        //Création du Binder
        this.binder = new Binder();

        //Création du guide
        this.guide = new Guide(this.binder);

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
        Button calibrateButton = (Button)findViewById(R.id.calibrationButton);
        calibrateButton.setOnClickListener(this.calibrateButtonListener);
    }

    /**
     * Sets a listener on the calibrateButton
     */
    private View.OnClickListener calibrateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            guide.goToCalibration();
            Button calibrationButton = (Button)findViewById(R.id.calibrationButton);
            calibrationButton.setVisibility(View.GONE);
            TextView holdStillText = (TextView)findViewById(R.id.holdStillText);
            holdStillText.setVisibility(View.VISIBLE);
        }
    };

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
     * Called when activity goes to pause state.
     * Unregister the sensors
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Called when the activity is resumed
     * Register the sensors
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Asks the user to do the calibration
     */
    @Override
    public void notifyCalibrationAsked() {

        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
                pieChart.setAngles(0,360);
                pieChart.setPieChartColor(getResources().getColor(R.color.main_theme_green));
                Button calibrationButton = (Button)findViewById(R.id.calibrationButton);
                calibrationButton.setVisibility(View.VISIBLE);
            }
        };
        this.runOnUiThread(action);
    }

    @Override
    public void notifyScanAsked() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.guide.stopGuide();
    }
}
