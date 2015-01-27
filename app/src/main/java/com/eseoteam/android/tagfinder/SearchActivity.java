package com.eseoteam.android.tagfinder;

import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.eseoteam.android.tagfinder.events.PieChartChangedEvent;


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

    /**
     * The compass
     */
    private Compass compass;

    /**
     * The id of the searched tag
     */
    private String tag_id;

    /**
     * DEBUG MODE
     * Should be set as false for the custommer version
     */
    private static final boolean DEBUG = false;

    // Methods //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        //Ajout des listeners sur les boutons
        setListeners();

        // Récupération du pieChart du Layout
        this.pieChart = (PieChart)findViewById(R.id.pieChart);

        //Récupération des informations du tag
        getTagInformation();

        //On vérifie que l'id du tag existe, sinon on finish l'activity
        if (this.tag_id != null){
            this.compass = new Compass((SensorManager) getSystemService(SENSOR_SERVICE));
            //compass.registerSensors();

            //Création du Binder
            this.binder = new Binder(Binder.Mode.SEARCH,this.tag_id);
            // compass.addCompassListener(this.binder);

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

//        if (DEBUG){
//            //calibrateButton
//            Button guideButton = (Button)findViewById(R.id.guideButton);
//            guideButton.setVisibility(View.VISIBLE);
//            guideButton.setOnClickListener(this.guideButtonListener);
//        }
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
                        "Please keep the phone still and horizontal, then tap the blue radar",
                        Toast.LENGTH_SHORT
                );
                toast.setGravity(Gravity.TOP,0,150);
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
                        "Calibration complete",
                        Toast.LENGTH_SHORT
                );
                toastCalibration.setGravity(Gravity.TOP,0,200);
                toastCalibration.show();
                Toast toastScan = Toast.makeText(
                        getApplicationContext(),
                        "Keep the phone horizontal and slowly make a 360 degrees turn",
                        Toast.LENGTH_LONG
                );
                toastScan.setGravity(Gravity.TOP,0,200);
                toastScan.show();
            }
        };
        this.runOnUiThread(action);
        Log.e(LOG_TAG,"User as been asked to scan");
    }

    @Override
    public void notifyUserScanFinished() {
        final Runnable action = new Runnable() {
            @Override
            public void run()
            {
                Toast toastCalibration = Toast.makeText(
                        getApplicationContext(),
                        "Scan finished",
                        Toast.LENGTH_SHORT
                );
                toastCalibration.setGravity(Gravity.TOP,0,200);
                toastCalibration.show();
                Toast toastScan = Toast.makeText(
                        getApplicationContext(),
                        "Follow given directions and sweep the targetted direction",
                        Toast.LENGTH_LONG
                );
                toastScan.setGravity(Gravity.TOP,0,200);
                toastScan.show();
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
     * Unregister the sensors
     * Remove all the listeners
     */
    @Override
    protected void onPause(){
        super.onPause();
        this.finish();

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
        Log.i(LOG_TAG,"onResume : Le listener de la SearchActivity sur le Guide a bien été ajouté");
        // Ajout du listener du guide sur le compass
        this.compass.addCompassListener(this.guide);
        Log.i(LOG_TAG,"onResume : Le listener du Guide sur le Compass a bien été ajouté");
        // Ajout du listener du binder sur le compass
        this.compass.addCompassListener(this.binder);
        Log.i(LOG_TAG,"onResume : Le listener du Binder sur le Compass a bien été ajouté");
        // Enregistrement des capteurs du compass
        this.compass.registerSensors();
        Log.i(LOG_TAG,"onResume : Les capteurs du compass sont bien enregistrés");
        //Démarrage du guide
        this.guide.start();
    }

    /**
     * Unregister the sensors
     * Remove all the listeners
     * Stops the Guide
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Suppression du listener du guide sur le compass
        this.compass.removeCompassListener(this.guide);
        Log.i(LOG_TAG,"onDestroy : Le listener du Guide sur le Compass a bien été supprimé");
        // Suppression du listener du binder sur le compass
        this.compass.removeCompassListener(this.binder);
        Log.i(LOG_TAG,"onDestroy : Le listener du Binder sur le Compass a bien été supprimé");
        // Désenregistrement des capteurs du compass
        this.compass.unregisterSensors();
        Log.i(LOG_TAG,"onDestroy : Les capteurs du compass sont bien désenregistrés");
        // Suppression du listener de la SearchActivity sur le Guide
        this.guide.removeGuideListener(this);
        Log.i(LOG_TAG,"onDestroy : Le listener de la SearchActivity sur le Guide a bien été supprimé");
        // Arrêt du guide
        this.guide.stopGuide();
        try {
            this.guide.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
