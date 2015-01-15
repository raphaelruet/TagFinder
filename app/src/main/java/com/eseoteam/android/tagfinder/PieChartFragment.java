package com.eseoteam.android.tagfinder;

/**
 *
 * Created on 14/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PieChartFragment extends Fragment {

    // Attributes //

    /**
     * The PieChart of the PieChartFragment
     */
    PieChart pieChart;

    // Constructors //

    /**
     * Constructor for the PieChartFragment
     */
    public PieChartFragment() {
        super();
    }

    // Accessor //

    /**
     * Accessor to set the angles of the PieChart of this PieChartFragment
     * @param angles the angles Array to set the angles on the PieChart
     */
    public void setPieChartAngles(int angles[]) {
        this.pieChart.setAngles(angles);
    }

    // Methods //

    /**
     * Creates the PieChart with the proper context
     * @param context the context
     */
    public void init(Context context){
        pieChart = new PieChart(context);
    }

    /**
     * Return the view with the PieChart created
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the Bundle
     * @return the view with the PieChart
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflation
        View pieChartView = inflater.inflate(
                R.layout.fragment_pie_chart,
                container, false);

        LinearLayout linear=(LinearLayout) pieChartView.findViewById(R.id.piechartLayout);
        linear.addView(this.pieChart);
        return pieChartView;

    }
}



