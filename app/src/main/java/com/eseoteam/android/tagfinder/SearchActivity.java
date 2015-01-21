package com.eseoteam.android.tagfinder;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Manage the research process
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class SearchActivity extends ActionBarActivity {

    // Attributes //
    //Fields of the textView
    TextView textTagName;
    TextView textTagId;
    TextView textTimestamp;
    TextView textTagData;

    //Fields in the database
    private static final String TAG_NAME = "tag_name";
    private static final String TAG_MID = "tag_mid";
    private static final String COLUMN_TIME_STAMP = "timestamp";
    private static final String TAG_DATA = "tag_data";

    //Id in the database
    private long idInDatabase;

    /**
     * Helper to access the database
     */
    private DatabaseHelper databaseHelper;

    /**
     * The PieChart used in the activity_search view
      */
    PieChartFragment pieChartFragment;

    // Methods //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        setListeners();

        //Creation of the PieChart of the view
        this.createPieChart();
        refreshPieChart(0,0);

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
     * Sets the different listener
     */
    private void setListeners() {
        //backButton
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this.backButtonListener);
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
     * Creates the new PieChart and commit it on the view
     */
    private void createPieChart(){
        this.pieChartFragment = new PieChartFragment();
        this.pieChartFragment.init(getApplicationContext());
        System.out.println("Commit");
        getFragmentManager().beginTransaction()
                .add(R.id.piechartLayout, this.pieChartFragment)
                .commit();
    }

    /**
     * Refreshes the PieChart with the chosen angles
     * @param minAngle the min angle
     * @param maxAngle the max angle
     */
    public void refreshPieChart(int minAngle, int maxAngle){
        this.pieChartFragment.setPieChartAngles(new int[]{minAngle,maxAngle});
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.replace(R.id.piechartLayout, this.pieChartFragment);
        tr.commit();
    }

}
