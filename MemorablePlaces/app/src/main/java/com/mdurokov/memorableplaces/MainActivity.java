package com.mdurokov.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewPlaces;
    static ArrayList<String> places;
    static ArrayList<LatLng> locations;
    static ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewPlaces = findViewById(R.id.listViewPlaces);
        locations = new ArrayList<LatLng>();
        places =  new ArrayList<>();

        places.add("Add a new place... ");
        locations.add(new LatLng(0,0));

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        listViewPlaces.setAdapter(adapter);

        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }

}
