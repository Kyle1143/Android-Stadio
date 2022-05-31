package com.example.mezamashi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mezamashi.listcomponent.ListItem;
import com.example.mezamashi.utils.DatabaseHelper;
import com.example.mezamashi.utils.Util;

public class SettingAlarmActivity extends BroadcastReceiver {

    private static final String TAG = SettingAlarmActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        // アラームを再登録
        // 参考 PutExtraは使用できない
        // https://stackoverflow.com/questions/12506391/retrieve-requestcode-from-alarm-broadcastreceiver
        // リクエストコードに紐づくデータを取得
        String requestCode = intent.getData().toString();
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        ListItem item = Util.getAlarmsByID(Integer.parseInt(requestCode), helper);

        // アラームを設定
        Util.setAlarm(context, item);

        Intent startActivityIntent = new Intent(context, WakeUpActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }
}