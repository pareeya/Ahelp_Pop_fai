package com.example.pareeya.ahelp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by masterUNG on 11/26/2016 AD.
 */


public class CheckInternet extends AsyncTask<Void, Void, String>{


    //Explicit
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public CheckInternet(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();

            if ((networkInfo != null) && (networkInfo.isConnected())) {
                return "true";
            } else {
                return "false";
            }

        } catch (Exception e) {
            Log.d("26novV2", "e doIn ==> " + e.toString());
            return null;
        }



    }
}   // Main Class
