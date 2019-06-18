package com.mdurokov.peoplecounter;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity{

    int counter = 0;
    TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enables Always-on
        setAmbientEnabled();
        tvCounter = findViewById(R.id.tvCounter);
        tvCounter.setText(String.valueOf(counter));
    }

    public void reset(View view){
        counter = 0;
        tvCounter.setText(Integer.toString(counter));
    }

    public void increment(View view){
        tvCounter.setText(Integer.toString(++counter));
    }


}
