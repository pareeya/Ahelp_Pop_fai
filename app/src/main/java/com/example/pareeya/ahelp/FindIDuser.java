package com.example.pareeya.ahelp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by masterUNG on 12/8/2016 AD.
 */

public class FindIDuser extends AsyncTask<Void, Void, String>{

    //Explicit
    private Context context;
    private String nameString, passwordString;
    private static final String urlPHP = "http://swiftcodingthai.com/fai/get_id_where_name_and_pass.php";

    public FindIDuser(Context context,
                      String nameString,
                      String passwordString) {
        this.context = context;
        this.nameString = nameString;
        this.passwordString = passwordString;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isAdd", "true")
                    .add("Name", nameString)
                    .add("Password", passwordString)
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(urlPHP).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.d("8decV2", "e doin ==> " + e.toString());
            return null;
        }


    }
}   // Main Class
