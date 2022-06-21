package com.example.raspberrypiled;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class RasberryPiLedHttpGetTask extends AsyncTask<Integer, Void, Void> {
    private Activity parentActivity;
    private ProgressDialog dialog = null;

    private final String DEFAULTURL = "http://192.---.--.---/~pi/ledtest.php?";
    private String uri = null;

    public RasberryPiLedHttpGetTask(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    protected Void doInBackground(Integer... arg0) {
        uri = DEFAULTURL + "num=" + arg0[0].toString() + "&stat=" + arg0[1].toString();
        Log.d("RasPiLED", uri);
        exec_get();
        return null;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(parentActivity);
        dialog.setMessage("通信中...");
        dialog.show();

    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
    }

    private String exec_get() {
        HttpURLConnection http = null;
        InputStream in = null;
        String src = new String();
        try {
            URL url = new URL(uri);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            in = http.getInputStream();

            byte[] line = new byte[1024];
            int size;
            while (true) {
                size = in.read(line);
                if (size <= 0) break;
                src += new String(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (http != null) http.disconnect();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return src;
    }
}
