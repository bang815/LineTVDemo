package com.bang.linetvdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkChangedReceiver extends BroadcastReceiver{
    public NetChangeListener listener = MainActivity.listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if ( networkInfo != null && networkInfo.isAvailable()){
            AppConfig.isConnect=true;
            if (listener != null) {
                listener.onChangeListener(true);
            }
        }else{
            AppConfig.isConnect=false;
            AppConfig.check=false;
            if (listener != null) {
                listener.onChangeListener(false);
            }
        }
    }
    public interface NetChangeListener {
        void onChangeListener(Boolean result);
    }
}