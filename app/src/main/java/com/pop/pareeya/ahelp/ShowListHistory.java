package com.pop.pareeya.ahelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShowListHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_history);

        String idUserLogin = getIntent().getStringExtra("Login");
        String tag = "3MarchV2";
        Log.d(tag, "id ของคนที่ Login อยู่==>" + idUserLogin);

        try {

            String[] urlstrings = new String[]{
                    "http://swiftcodingthai.com/fai/getDataWhereUser.php",
                    "http://swiftcodingthai.com/fai/getDataWhereID.php"};

            GetHistoryWhereID getHistoryWhereID = new GetHistoryWhereID(ShowListHistory.this);
            getHistoryWhereID.execute(idUserLogin, urlstrings[0]);

            String strJSON1 = getHistoryWhereID.get();
            Log.d(tag, "strJASON1==>" + strJSON1);

            JSONArray jsonArray = new JSONArray(strJSON1);
            String[] Call_IDString = new String[jsonArray.length()];
            String[] Call_DataTime = new String[jsonArray.length()];
            String[] nameCall_IDString = new String[jsonArray.length()];
            String[] showListStrings = new String[jsonArray.length()];

            for (int i=0;i<jsonArray.length();i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Call_IDString[i] = jsonObject.getString("Call_ID");
                Call_DataTime[i] = jsonObject.getString("Call_DataTime");
                nameCall_IDString[i] = findNameCall(Call_IDString[i],urlstrings[1]);

                Log.d(tag, "Name==>" + nameCall_IDString[i]);
                Log.d(tag, "DateTime==>" + Call_DataTime[i]);

                showListStrings[i] = nameCall_IDString[i] + "\n" + Call_DataTime[i];


            }//for

            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(ShowListHistory.this,
                    android.R.layout.simple_list_item_1, showListStrings);
            ListView listView = (ListView) findViewById(R.id.livShowListHistory);
            listView.setAdapter(stringArrayAdapter);

        } catch (Exception e) {
            Log.d(tag, "e==>" + e.toString());
        }

    }//Main Method

    private String findNameCall(String call_idString,String urlPHP) {

        String result = null;

        try {

            GetHistoryWhereID getHistoryWhereID = new GetHistoryWhereID(ShowListHistory.this);
            getHistoryWhereID.execute(call_idString, urlPHP);
            String strJSON = getHistoryWhereID.get();

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            result = jsonObject.getString("Name");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return result;

        }


    }
}//Main Class
