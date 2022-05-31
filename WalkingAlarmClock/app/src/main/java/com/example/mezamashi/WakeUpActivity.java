package com.example.mezamashi;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.mezamashi.R;
import com.example.mezamashi.service.SoundService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

// 参考 https://github.com/hiroaki-dev/AlarmSample/blob/master/app/src/main/java/me/hiroaki/alarmsample/PlaySoundActivity.java
// 参考　https://github.com/mei28/KPK/blob/master/Mezamashi/app/src/
// 参考　https://github.com/TsubasaSato/Pedometer

public class WakeUpActivity extends AppCompatActivity implements SensorEventListener {

    Button stopBtn;
    boolean walked = false;

    private int mStepCount;
    private int STEP_MAX_COUNT = 10;

    boolean first = true;
    boolean up = false;
    float d0, d = 0f;
    //Filter 0<a<1
    float a = 0.65f;

    private TextView mComment, mLeftCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_wake_up);
        Toolbar toolbar = findViewById(R.id.toolbarWakeUp);
        setSupportActionBar(toolbar);

        startService(new Intent(this, SoundService.class));

        stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(WakeUpActivity.this, SoundService.class));
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_UI);

        mLeftCount = (TextView) findViewById(R.id.left_count);
        mComment = (TextView) findViewById(R.id.comment);

        if (walked == false) {
            stopBtn.setEnabled(false);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (first) {
            first = false;
            up = true;
            d0 = a * gForce;
        } else {
            //ローパスフィルタリング 時系列の細かいデータを平滑化
            d = a * gForce + (1 - a) * d0;
            if (up && d < d0) {
                up = false;
                mStepCount++;
            } else if (!up && d > d0) {
                up = true;
                d0 = d;
            }
        }

//        Log.d("mezamashi", String.valueOf(mStepCount));
        if (mStepCount > STEP_MAX_COUNT && walked == false) {
            walked = true;
            stopBtn.setEnabled(true);
        }
        float progress = (float) ((float) mStepCount / (float) STEP_MAX_COUNT);
        Log.d("step", String.valueOf(progress));
        mLeftCount.setText("Walk" + String.valueOf(Math.max(STEP_MAX_COUNT - mStepCount, 0)) + "steps！");
        if (mStepCount == 0) {
            mComment.setText(String.valueOf(STEP_MAX_COUNT) + " steps and stop the alarm clock!");
        } else if (progress >= 1.0) mComment.setText("Let's do our best for the day！！");
        else if (progress > 0.8) mComment.setText("Is it about to wake up？");
        else if (progress > 0.5) mComment.setText("Finally half, let's do our best to wake up feelings!");
        else if (progress > 0.3) mComment.setText("Let's walk more！！");
        else mComment.setText("Let's walk to stop the alarm！");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
