package com.example.raspberrypiled;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CompoundButton ledToggleButton1;
    private CompoundButton ledToggleButton2;
    private CompoundButton ledToggleButton3;
    private CompoundButton ledToggleButton4;
    private CompoundButton ledToggleButton5;

    private int ledNum;

    final int LED1 = 0;
    final int LED2 = 1;
    final int LED3 = 2;
    final int LED4 = 3;
    final int LED5 = 4;
    private int stat = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ledToggleButton1 = (CompoundButton) findViewById(R.id.toggle_button_led_1);
        ledToggleButton2 = (CompoundButton) findViewById(R.id.toggle_button_led_2);
        ledToggleButton3 = (CompoundButton) findViewById(R.id.toggle_button_led_3);
        ledToggleButton4 = (CompoundButton) findViewById(R.id.toggle_button_led_4);
        ledToggleButton5 = (CompoundButton) findViewById(R.id.toggle_button_led_5);

        ledToggleButton1.setOnCheckedChangeListener(this);
        ledToggleButton2.setOnCheckedChangeListener(this);
        ledToggleButton3.setOnCheckedChangeListener(this);
        ledToggleButton4.setOnCheckedChangeListener(this);
        ledToggleButton5.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.toggle_button_led_1:
                ledNum = LED1;
                break;
            case R.id.toggle_button_led_2:
                ledNum = LED2;
                break;
            case R.id.toggle_button_led_3:
                ledNum = LED3;
                break;
            case R.id.toggle_button_led_4:
                ledNum = LED4;
                break;
            case R.id.toggle_button_led_5:
                ledNum = LED5;
                break;
        }
        if (isChecked == true) {
            stat = 1;
        } else {
            stat = 0;
        }
        RasberryPiLedHttpGetTask task = new RasberryPiLedHttpGetTask(this);
        task.execute(ledNum,stat);
    }
}