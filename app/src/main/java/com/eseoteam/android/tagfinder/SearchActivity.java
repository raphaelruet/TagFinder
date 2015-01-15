package com.eseoteam.android.tagfinder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Manage the research process
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class SearchActivity extends ActionBarActivity {

    // Attributes //

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

        //Creation of the PieChart of the view
        this.createPieChart();

    }

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
