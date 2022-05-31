package com.example.mezamashi;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import com.example.mezamashi.InputActivity;
import com.example.mezamashi.utils.DatabaseHelper;
import com.example.mezamashi.listcomponent.ListAdapter;
import com.example.mezamashi.listcomponent.ListItem;
import com.example.mezamashi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper = null;
    final static public int NEW_REQ_CODE = 1;
    final static public int EDIT_REQ_CODE = 2;
    RecyclerView rv = null;
    RecyclerView.Adapter adapter = null;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アラームのデータを取得
        ArrayList<ListItem> data = this.loadAlarms();

        // RecyclerViewに設定
        this.setRV(data);

        // フローティングアクションボタンの設定
        FloatingActionButton fbt = findViewById(R.id.fbtn);
        fbt.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent i = new Intent(MainActivity.this, InputActivity.class);
                        i.putExtra(getString(R.string.request_code),NEW_REQ_CODE);
                        startActivityForResult(i,NEW_REQ_CODE);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // リクエストコードと結果コードをチェック
        if(requestCode == RESULT_CANCELED){
            // 何もしない
        }else if((requestCode == NEW_REQ_CODE || requestCode == EDIT_REQ_CODE) && resultCode == RESULT_OK){
            ArrayList<ListItem> dataAlarms = this.loadAlarms();
            this.updateRV(dataAlarms);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<ListItem> loadAlarms(){
        helper = DatabaseHelper.getInstance(this);

        ArrayList<ListItem> data = new ArrayList<>();

        try(SQLiteDatabase db = helper.getReadableDatabase()) {

            String[] cols ={"alarmid","name","alarttime"};

            Cursor cs = db.query("alarms",cols,null,null,
                    null,null,"alarmid",null);
            boolean eol = cs.moveToFirst();
            while (eol){
                ListItem item = new ListItem();
                item.setAlarmID(cs.getInt(0));
                item.setAlarmName(cs.getString(1));
                item.setTime(cs.getString(2));
                data.add(item);
                eol = cs.moveToNext();
            }
        }
        return data;
    }

    private void setRV(ArrayList<ListItem> data){
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new ListAdapter(data);
        rv.setAdapter(adapter);
    }

    private void updateRV(ArrayList<ListItem> data){
        adapter = new ListAdapter(data);
        rv.setAdapter(adapter);
        // rv.swapAdapter(adapter,false);
    }
}