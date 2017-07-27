package com.stare.out.adityaiot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class IncommingCall extends BroadcastReceiver{
    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent){

        mcontext = context;

        try{
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context, "Phone Is Ringing", Toast.LENGTH_LONG).show();
                new PingUrl().execute("http://192.168.43.42/LED=ON");

            }

            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(context, "Call Recieved", Toast.LENGTH_LONG).show();
                new PingUrl().execute("http://192.168.43.42/LED=OFF");
            }

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context, "Phone Is Idle", Toast.LENGTH_LONG).show();
                new PingUrl().execute("http://192.168.43.42/LED=OFF");
            }
        }
        catch(Exception e){e.printStackTrace();}
    }

    public class PingUrl extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
                urlc.connect();

                if (urlc.getResponseCode() == 200) {
                    //Toast.makeText(mcontext, "URl pinged", Toast.LENGTH_SHORT).show();
                    Log.d("-----", "URl pinged");
                }else{
                    //Toast.makeText(mcontext, "URl ping failed", Toast.LENGTH_SHORT).show();
                    Log.d("-----", "URl ping failed");
                }

                urlc.disconnect();

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
