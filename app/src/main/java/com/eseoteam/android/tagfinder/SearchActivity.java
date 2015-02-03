package com.eseoteam.android.tagfinder;

import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.eseoteam.android.tagfinder.communication.Communication;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;

/**
 * Manage the Research screen
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.8
 */
public class SearchActivity extends ActionBarActivity implements GuideListener, CruiseControlListener{

    // Attributes //

    private Communication communication;
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
     * The Guide
     */
    private Guide guide;

    /**
     * The cruise control
     */
    private CruiseControl cruiseControl;

    /**
     * The Binder
     */
    private Binder binder;

    /**
     * The PieChart
     */
    private PieChart pieChart;

    /**
     * The compass
     */
    private Compass compass;

    /**
     * The id of the searched tag
     */
    private String tag_id;

    /**
     * The X position of the toasts from the top of the screen
     */
    private int toastYPosition;

    /**
     * DEBUG MODE
     * Should be set as 'false' for the custommer version
     */
    private static final boolean DEBUG = false;

    /**
     * The 'Slow down' message shown when the speed is too high
     */
    private TextView slowDownMessage;

    /**
     * The animation of the slowDownMessage
     */
    private Animation anim;

    // Methods //

    /**
     * The onCreate Method
     * @param savedInstanceState the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        // Initialization
        this.init();

        //Get tag information
        getTagInformation();

        //Check if the tag exists and if the id isn't null
        if (this.tag_id != null){
            //Gets back the sensorManager
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            //Compass creation
            this.compass = new Compass(sensorManager);
            //CruiseControl creation
            this.cruiseControl = new CruiseControl(sensorManager);
            //Binder creation
            this.binder = new Binder(Binder.Mode.SEARCH, this.tag_id);
            this.initializeCommunication();
            //Guide creation
            this.guide = new Guide(this.binder);
        }else{
            this.finish();
        }
    }

    /**
     * Does all the initialization process
     */
    private void init() {
        // Gets the display dimensions
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        toastYPosition = (displayMetrics.heightPixels/2);

        // Gets back the 'slow down' message from the view
        this.slowDownMessage = (TextView)findViewById(R.id.speedText);
        // Creates the animation on the 'slow down' message
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        //Adds all the listeners
        setListeners();

        // Gets back the pieChart from the view
        this.pieChart = (PieChart)findViewById(R.id.pieChart);
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

        if (DEBUG){
            //calibrateButton
            Button guideButton = (Button)findViewById(R.id.guideButton);
            guideButton.setVisibility(View.VISIBLE);
            guideButton.setOnClickListener(this.guideButtonListener);
        }
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
            guide.setState(Guide.State.SCAN);
            guide.askForScan();
            compass.calibrateCompass();
        }
    };

    /**
     * Sets a listener on the guideButton
     */
    private View.OnClickListener guideButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            guide.setState(Guide.State.GUIDE);
        }
    };

    /**
     * Gets the tag informations from the database
     */
    private void getTagInformation(){

        // Helper pour la base de données
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        //Récupération des TextView du layout
        textTagName = (TextView) findViewById(R.id.tagName);
        textTagId = (TextView) findViewById(R.id.tagId);
        textTimestamp = (TextView) findViewById(R.id.tagDate);
        textTagData = (TextView) findViewById(R.id.tagInfo);

        //Recherche du tag choisi dan sla BDD
        long idInDatabase = getIntent().getLongExtra("tag_id_in_db", -1);
        Cursor cursor = databaseHelper.getOneTag(idInDatabase);

        // Remplissage des textView du layout avec les infos récupérées
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            textTagName.append(name);
            this.tag_id = cursor.getString(cursor.getColumnIndex(TAG_MID));
            textTagId.append(this.tag_id);
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_STAMP));
            textTimestamp.append(date);
            String info = cursor.getString(cursor.getColumnIndex(TAG_DATA));
            textTagData.append(info);
        }
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
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        R.string.calibration_information,
                        Toast.LENGTH_SHORT
                );
                toast.setGravity(Gravity.TOP,0,toastYPosition);
                toast.show();
            }
        };
        this.runOnUiThread(action);
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
                Toast toastCalibration = Toast.makeText(
                        getApplicationContext(),
                        R.string.calibration_finished,
                        Toast.LENGTH_SHORT
                );
                toastCalibration.setGravity(Gravity.TOP,0,toastYPosition);
                toastCalibration.show();
                Toast toastScan = Toast.makeText(
                        getApplicationContext(),
                        R.string.scan_information,
                        Toast.LENGTH_LONG
                );
                toastScan.setGravity(Gravity.TOP,0,toastYPosition);
                toastScan.show();
            }
        };
        this.runOnUiThread(action);
    }

    /**
     * Notifies the User that the scan has failed
     */
    @Override
    public void notifyUserScanFailed() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                Toast toastCalibration = Toast.makeText(
                        getApplicationContext(),
                        R.string.scan_failed,
                        Toast.LENGTH_LONG
                );
                toastCalibration.setGravity(Gravity.TOP,0,toastYPosition);
                toastCalibration.show();
            }
        };
        this.runOnUiThread(action);
        this.finish();
    }

    /**
     * Notifies the user that the guiding has begun
     */
    @Override
    public void notifyUserGuidingStart() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                Toast toastScan = Toast.makeText(
                        getApplicationContext(),
                        R.string.guiding_information,
                        Toast.LENGTH_LONG
                );
                toastScan.setGravity(Gravity.TOP,0,toastYPosition);
                toastScan.show();
            }
        };
        this.runOnUiThread(action);
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
                pieChart.setAngles(event.getStartAngle(), event.getStopAngle());
                if (DEBUG){
                    TextView angleText = (TextView)findViewById(R.id.angleText);
                    angleText.setText("Start : " +event.getStartAngle()
                            +" , Stop : " +event.getStopAngle());
                }
            }
        };
        this.runOnUiThread(action);
    }

    /**
     * Re-register the sensors
     * Re-add all the listeners
     */
    @Override
    protected void onResume(){
        super.onResume();
        this.guide.addGuideListener(this);
        this.compass.addCompassListener(this.guide);
        this.compass.registerSensors();
        this.cruiseControl.registerSensors();
        this.cruiseControl.addCruiseControlListener(this);
        this.cruiseControl.addCruiseControlListener(this.guide);
        this.guide.start();
    }

    /**
     * Unregister the sensors
     * Remove all the listeners
     */
    @Override
    protected void onPause(){
        super.onPause();
        this.compass.unregisterSensors();
        this.compass.removeCompassListener(this.guide);
        this.cruiseControl.unregisterSensors();
        this.cruiseControl.removeCruiseControlListener(this);
        this.cruiseControl.removeCruiseControlListener(this.guide);
        this.guide.stopGuide();
        try {
            this.guide.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.finish();
    }

    /**
     * Unregister the sensors
     * Remove all the listeners
     * Stops the Guide
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.communication !=null) {
            this.communication.closeConnection();
            try {
                this.communication.join();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG,"Join interrupted");
            }
        }
        this.guide.removeGuideListener(this);
    }

    /**
     * Starts the communication by connecting the socket to the device ip and the specified port.
     */
    private void initializeCommunication() {
        String wifiAddress = Communication.getWifiIpAddress(getApplicationContext());
        this.communication = new Communication(wifiAddress, Communication.CONNECTION_PORT, binder);
        this.communication.start();
    }

    /**
     * Sets the slowDownMessage to visible and make it blink
     * when the speed is too high
     */
    @Override
    public void notifySpeedTooHigh() {
        if (( this.guide.getSate() == Guide.State.SCAN
                || this.guide.getSate() == Guide.State.GUIDE )
                && this.slowDownMessage.getVisibility() == View.INVISIBLE){
            this.slowDownMessage.setVisibility(View.VISIBLE);
            slowDownMessage.startAnimation(anim);
        }
    }

    /**
     * Sets the slow down message to invisible when the speed is correct
     */
    @Override
    public void notifySpeedOK() {
        anim.cancel();
        anim.reset();
        slowDownMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * Nothing to do here
     */
    @Override
    public void notifyDirectionChanged() {
        // Nothing to do here
    }
}
