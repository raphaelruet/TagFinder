package com.eseoteam.android.tagfinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

/**
 * Manage the Start screen
 * Created on 07/01/2014.
 * @author Raphael RUET.
 * @version 0.2
 */
public class StartActivity extends ActionBarActivity {

    // Attributes //

    /**
     * SplashScreen duration in millisecond.
     */
    private static final int SPLASH_TIME_OUT = 3000;

    /**
     * Enable the SPLASH_TIME_OUT duration display of the screen.
     */
    private static final boolean ENABLE_TIMER = true;


    // Methods //

    /**
     * The onCreate method
     * @param savedInstanceState the state of the savedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        if(ENABLE_TIMER) {
            //Switch to the connectionActivity after SPLASH_TIME_OUT milliseconds.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), ConnectionActivity.class);
                    startActivity(intent);
                    // Close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ConnectionActivity.class);
            startActivity(intent);
            // Close this activity
            finish();
        }
    }
}
