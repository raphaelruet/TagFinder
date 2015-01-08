package com.eseoteam.android.tagfinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StartActivity extends ActionBarActivity {

    /**
     * SplashScreen duration in millisecond.
     */
    private static final int SPLASH_TIME_OUT = 3000;

    /**
     * Enable the SPLASH_TIME_OUT duration display of the screen.
     */
    private static final boolean ENABLE_TIMER = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        Button btnOne = (Button)findViewById(R.id.buttonOne);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBtnOne = new Intent(getApplicationContext(), ConnectionActivity.class);
                startActivity(intentBtnOne);
            }
        });

        Button btnTwo = (Button)findViewById(R.id.buttonTwo);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBtnTwo = new Intent(getApplicationContext(), LibraryActivity.class);
                startActivity(intentBtnTwo);
            }
        });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
