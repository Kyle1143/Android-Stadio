package com.example.intentsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {
    private LocationManager mLocationManager;
    private TextView mGpsLatitudeTextview;
    private TextView mGpsLongitudeTextview;
    private TextView mGpsAccuracyTextview;
    private TextView mGpsAltitudeTextview;
    private TextView mWifiLatitudeTextView;
    private TextView mWifiLongitudeTextView;
    private TextView mWifiAccuracyTextview;
    private TextView mWifiAltitudeTextview;
    private static int GPS = 1;
    private static int WIFI = 0;
    private int mLocationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mGpsLatitudeTextview = findViewById(R.id.text_view_gps_latitude_value);
        mGpsLongitudeTextview = findViewById(R.id.text_view_gps_longitude_value);
        mGpsAccuracyTextview = findViewById(R.id.text_view_gps_accuracy_value);
        mGpsAltitudeTextview = findViewById(R.id.text_view_gps_altitude_value);
        mWifiLatitudeTextView = findViewById(R.id.text_view_wifi_latitude_value);
        mWifiLongitudeTextView = findViewById(R.id.text_view_wifi_longitude_value);
        mWifiAccuracyTextview = findViewById(R.id.text_view_wifi_accuracy_value);
        mWifiAltitudeTextview = findViewById(R.id.text_view_wifi_altitude_value);
        Button gpsButton= (Button) findViewById(R.id.button_gps);gpsButton.setOnClickListener(this);
        Button wifiButton= (Button) findViewById(R.id.button_wifi);wifiButton.setOnClickListener(this);
        mLocationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
    }

    protected void onResume(){
        Log.d("PlaceSample","plog onResume");
        super.onResume();
    }

    protected void onPause(){
        Log.d("PlaceSample","plog onPause");
        if(mLocationManager != null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
                return;
            }
            mLocationManager.removeUpdates(this);
        }
        super.onPause();
    }

    public void onLocationChanged(Location location){
        Log.d("PlaceSample","plog onLocationChanged");
        if(mLocationType == GPS){
            mGpsLatitudeTextview.setText(String.valueOf(location.getLatitude()));
            mGpsLongitudeTextview.setText(String.valueOf(location.getLongitude()));
            mGpsAccuracyTextview.setText(String.valueOf(location.getAccuracy()));
            mGpsAltitudeTextview.setText(String.valueOf(location.getAltitude()));
        }
        else if(mLocationType == WIFI){
            mWifiLatitudeTextView.setText(String.valueOf(location.getLatitude()));
            mWifiLongitudeTextView.setText(String.valueOf(location.getLongitude()));
            mWifiAccuracyTextview.setText(String.valueOf(location.getAccuracy()));
            mWifiAltitudeTextview.setText(String.valueOf(location.getAltitude()));
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    public void onStatusChanged(String s, int i, Bundle bundle){
        switch (i){
            case LocationProvider.AVAILABLE:
                Log.v("PlaceSample","AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("PlaceSample","OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("PlaceSample","TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    public void onProviderEnabled(String s){
    }

    public void onProviderDisabled(String s){
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button_gps){
            mLocationType = GPS;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else if (view.getId() == R.id.button_wifi){
            mLocationType = WIFI;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }
}

