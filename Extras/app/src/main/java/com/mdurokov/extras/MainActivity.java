package com.mdurokov.extras;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        String[] friends = {"Matt", "Fred", "Dave", "Pete", "Marc", "Mist", "Mole"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friends);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
    }

    public void goPiP(View view){
        enterPictureInPictureMode();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        TextView textView = findViewById(R.id.tvFontChanged);
        Button btnPip= findViewById(R.id.btnPIP);

        if(isInPictureInPictureMode){
            // going to pip
            btnPip.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
            textView.setText("PIP");
        }else{
            // going out of pip
            btnPip.setVisibility(View.VISIBLE);
            textView.setText("Hello World!");
            getSupportActionBar().show();
        }
    }
}
