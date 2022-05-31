package com.example.intentsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button orientationActivitySwitchButton = (Button) findViewById(R.id.button_orientation);
        orientationActivitySwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getApplicationContext(), OrientationActivity.class);
                startActivity(intent);}
        });
        Button locationActivitySwitchButton = (Button) findViewById(R.id.button_location);
        locationActivitySwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(getApplicationContext(),LocationActivity.class);startActivity(intent);
            }
        });
    }
}