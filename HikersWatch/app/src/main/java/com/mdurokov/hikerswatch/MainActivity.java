package com.mdurokov.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInformation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                updateLocationInformation(lastLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    private void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void updateLocationInformation(Location location) {
        TextView textViewLatitude = findViewById(R.id.textViewLatitude);
        TextView textViewLongitude = findViewById(R.id.textViewLongitude);
        TextView textViewAccuracy = findViewById(R.id.textViewAccuracy);
        TextView textViewAddress = findViewById(R.id.textViewAddress);
        TextView textViewAltitude = findViewById(R.id.textViewAltitude);

        textViewLatitude.setText("Latitude: " + Double.toString(location.getLatitude()));
        textViewLongitude.setText("Longitude: " + Double.toString(location.getLongitude()));
        textViewAccuracy.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        textViewAltitude.setText("Altitude: " + Double.toString(location.getAltitude()));

        String address = "Could not find address :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses != null && addresses.size() > 0) {
                address = "Address: \n";

                if(addresses.get(0).getThoroughfare() != null){
                    address += addresses.get(0).getThoroughfare() + "\n";
                }
                if(addresses.get(0).getLocality() != null){
                    address += addresses.get(0).getLocality() + " ";
                }
                if(addresses.get(0).getPostalCode() != null){
                    address += addresses.get(0).getPostalCode() + " ";
                }
                if(addresses.get(0).getAdminArea() != null){
                    address += addresses.get(0).getAdminArea() + "\n";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        textViewAddress.setText(address);

    }
}
