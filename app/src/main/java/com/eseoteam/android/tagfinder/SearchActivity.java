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
import com.eseoteam.android.tagfinder.events.DirectionChangedEvent;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;


/**
 * Manage the research process
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class SearchActivity extends ActionBarActivity implements GuideListener, CruiseControlListener{

    // Attributes //

    /**
     * The default connection port of the socket.
     */
    private static final int CONNECTION_PORT = 12345;

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

        // Récupération des dimensions de l 'écran afin de régler la position des toasts
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        toastYPosition = (displayMetrics.heightPixels/2);

        this.slowDownMessage = (TextView)findViewById(R.id.speedText);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);


        //Ajout des listeners sur les boutons
        setListeners();

        // Récupération du pieChart du Layout
        this.pieChart = (PieChart)findViewById(R.id.pieChart);

        //Récupération des informations du tag
        getTagInformation();

        //On vérifie que l'id du tag existe, sinon on finish l'activity
        if (this.tag_id != null){
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            this.compass = new Compass(sensorManager);
            this.cruiseControl = new CruiseControl(sensorManager);

            //compass.registerSensors();

            //Création du Binder
            this.binder = new Binder(Binder.Mode.SEARCH,this.tag_id);
            // compass.addCompassListener(this.binder);
            this.initializeCommunication();
            //Création du guide
            this.guide = new Guide(this.binder);
        }else{
            this.finish();
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
            Log.e(LOG_TAG, "On est passé");
        }
    };

    /**
     * Sets a listener on the calibrateButton
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
        // Ajout du listener de la SearchActivity sur le Guide
        this.guide.addGuideListener(this);
        Log.i(LOG_TAG, "onResume : Le listener de la SearchActivity sur le Guide a bien été ajouté");
        // Ajout du listener du guide sur le compass
        this.compass.addCompassListener(this.guide);
        Log.i(LOG_TAG, "onResume : Le listener du Guide sur le Compass a bien été ajouté");
        // Enregistrement des capteurs du compass
        this.compass.registerSensors();
        this.cruiseControl.registerSensors();
        this.cruiseControl.addCruiseControlListener(this);
        Log.i(LOG_TAG, "onResume : Les capteurs du compass sont bien enregistrés");
        //Démarrage du guide
        this.guide.start();
    }


    /**
     * Unregister the sensors
     * Remove all the listeners
     */
    @Override
    protected void onPause(){
        super.onPause();
        // Désenregistrement des capteurs du compass
        this.compass.unregisterSensors();
        // Suppression du listener du guide sur le compass
        this.compass.removeCompassListener(this.guide);
        this.cruiseControl.unregisterSensors();
        this.cruiseControl.removeCruiseControlListener(this);
        Log.i(LOG_TAG,"onPause : Le listener du Guide sur le Compass a bien été supprimé");
        // Arrêt du guide
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


        Log.i(LOG_TAG,"onDestroy : Les capteurs du compass sont bien désenregistrés");
        // Suppression du listener de la SearchActivity sur le Guide
        this.guide.removeGuideListener(this);
        Log.i(LOG_TAG,"onDestroy : Le listener de la SearchActivity sur le Guide a bien été supprimé");

    }

    /**
     * Starts the communication by connecting the socket to the device ip and the specified port.
     */
    private void initializeCommunication() {
        String wifiAddress = Communication.getWifiIpAddress(getApplicationContext());
        Log.e(LOG_TAG, "Wifi Address:" + wifiAddress);
        this.communication = new Communication(wifiAddress, CONNECTION_PORT, binder);
        this.communication.start();
    }

    /**
     * Sets the slowDownMessage to visible and make it blink
     * when the speed is too high
     */
    @Override
    public void notifySpeedTooHigh() {
        if (this.guide.getSate() == Guide.State.SCAN &&
                this.slowDownMessage.getVisibility() == View.INVISIBLE){
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
     * Not used
     * @param event that we don't care about
     */
    @Override
    public void notifySpeedChanged(DirectionChangedEvent event) {
        //Nothing to be done here.
    }
}
