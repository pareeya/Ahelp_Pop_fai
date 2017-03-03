package com.pop.pareeya.ahelp;

import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by masterUNG on 3/3/2017 AD.
 */

public class AddHistory extends AsyncTask<Void, Void, String>{

    private Context context;
    private static final String urlPHP = "http://swiftcodingthai.com/fai/addHistory.php";
    private String callIDString, receiveIDString, latString, lngString, callDateTimeString;

    public AddHistory(Context context,
                      String callIDString,
                      String receiveIDString,
                      String latString,
                      String lngString,
                      String callDateTimeString) {
        this.context = context;
        this.callIDString = callIDString;
        this.receiveIDString = receiveIDString;
        this.latString = latString;
        this.lngString = lngString;
        this.callDateTimeString = callDateTimeString;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("Call_ID", callIDString)
                    .add("Receive_ID", receiveIDString)
                    .add("Lat", latString)
                    .add("Lng", lngString)
                    .add("Call_DataTime", callDateTimeString)
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(urlPHP).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}   // Main Class
