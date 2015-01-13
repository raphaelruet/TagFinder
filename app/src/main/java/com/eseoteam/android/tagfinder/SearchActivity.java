package com.eseoteam.android.tagfinder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;



public class SearchActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        /*RadarView radarView= new RadarView(this);
        radarView.
        addContentView(radarView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
        */




    }
}
