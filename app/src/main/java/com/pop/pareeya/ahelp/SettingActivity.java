package com.pop.pareeya.ahelp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    //Explicit
    private TextView phone1TextView, phone2TextView, phone3TextView,
            phone4TextView, phone5TextView;
    private ImageView addPhone1ImageView, addPhone2ImageView,
            addPhone3ImageView, addPhone4ImageView, addPhone5ImageView;
    private ImageView deletePhone1ImageView, deletePhone2ImageView,
            deletePhone3ImageView, deletePhone4ImageView, deletePhone5ImageView;
    private RadioGroup radioGroup;
    private RadioButton phone1RadioButton, phone2RadioButton,
            phone3RadioButton, phone4RadioButton, phone5RadioButton;
    private ListView listView;
    private Button button;
    private String urlJSON = "http://swiftcodingthai.com/fai/get_User_kanyarat.php";
    private String[] nameStrings, phoneStrings, passwordStrings, idStrings;
    private String nameChooseString, phoneChooseString, passwordChooseString, radioButtonChooseString,
            idCallString;
    private ArrayList<String> idCallStringsArrayList, myPhoneStringArrayList, actionStringArrayList;
    private int indexRadioChoose = 0;

    private TextView[] friendTextViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //setup
        idCallStringsArrayList = new ArrayList<String>();
        myPhoneStringArrayList = new ArrayList<String>();

        //Bind Widget
        bindWidget();

        //Create ListView Friend for Choose
        SynUser synUser = new SynUser(SettingActivity.this);
        synUser.execute(urlJSON);

        //Check Radio Button
        checkRadioButton(); //จะทำการ เช็คว่า ของเก่าเลือกเบอร์ไหนสำหรับโทรออก

        //Create ListView Show Friend
        showListFriend();


        //Image Controller
        addPhone1ImageView.setOnClickListener(SettingActivity.this);
        addPhone2ImageView.setOnClickListener(SettingActivity.this);
        addPhone3ImageView.setOnClickListener(SettingActivity.this);
        addPhone4ImageView.setOnClickListener(SettingActivity.this);
        addPhone5ImageView.setOnClickListener(SettingActivity.this);
        deletePhone1ImageView.setOnClickListener(SettingActivity.this);
        deletePhone2ImageView.setOnClickListener(SettingActivity.this);
        deletePhone3ImageView.setOnClickListener(SettingActivity.this);
        deletePhone4ImageView.setOnClickListener(SettingActivity.this);
        deletePhone5ImageView.setOnClickListener(SettingActivity.this);

        //RadioGroup Controller
        radioController();


    }//Main Method

    private void checkRadioButton() {

        String strID = null;

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneTABLE WHERE Action = 1", null);
        if (cursor.getCount() != 0) {

            cursor.moveToFirst();
            strID = cursor.getString(0);
            Log.d("26janV2", "strID ==> " + strID);

            markRadioButton(Integer.parseInt(strID));

        }   // if

    }   // checkRadio

    private void markRadioButton(int intPosition) {

        switch (intPosition) {
            case 1:
                phone1RadioButton.setChecked(true);
                break;
            case 2:
                phone2RadioButton.setChecked(true);
                break;
            case 3:
                phone3RadioButton.setChecked(true);
                break;
            case 4:
                phone4RadioButton.setChecked(true);
                break;
            case 5:
                phone5RadioButton.setChecked(true);
                break;
        }

    }   // markRadioButton

    public void clickCancelSetting(View view) {
        finish();
    }

    private void showListFriend() {

        try {

            friendTextViews = new TextView[5];
            friendTextViews[0] = phone1TextView;
            friendTextViews[1] = phone2TextView;
            friendTextViews[2] = phone3TextView;
            friendTextViews[3] = phone4TextView;
            friendTextViews[4] = phone5TextView;

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneTABLE", null);

            Log.d("22decV1", "cursor.getCount ==> " + cursor.getCount());
            if (cursor != null) {

                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {

                        SynAhelp synAhelp = new SynAhelp(SettingActivity.this,
                                cursor.getString(1));
                        synAhelp.execute();
                        String s = synAhelp.get();
                        Log.d("22decV1", "JSON (" + i + ") ==> " + s);

                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String strName = jsonObject.getString("Name");


                        friendTextViews[i].setText(strName);

                    } catch (Exception e) {
                        Log.d("22decV1", "e Thread ==> " + e.toString());
                    }

                    cursor.moveToNext();
                }   //for
            }   // if

        } catch (Exception e) {
            Log.d("22decV1", "e ==> " + e.toString());
        }

    }   // showListFriend

    private void radioController() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.mradioButton6:
                        indexRadioChoose = 0;
                        break;
                    case R.id.mradioButton7:
                        indexRadioChoose = 1;
                        break;
                    case R.id.mradioButton8:
                        indexRadioChoose = 2;
                        break;
                    case R.id.mradioButton9:
                        indexRadioChoose = 3;
                        break;
                    case R.id.mradioButton10:
                        indexRadioChoose = 4;
                        break;


                } //switch
            }
        });
    }


    @Override
    public void onClick(View view) {

        Log.d("29octV2", "Click ImageAdd");

        switch (view.getId()) {

            //ปุ่มเพิ่ม

            case R.id.imageView6:

                confirmPassword(phone1TextView, nameChooseString,
                        phoneChooseString, passwordChooseString, idCallString);

                break;
            case R.id.imageView7:

                confirmPassword(phone2TextView, nameChooseString,
                        phoneChooseString, passwordChooseString, idCallString);

                break;
            case R.id.imageView8:

                confirmPassword(phone3TextView, nameChooseString,
                        phoneChooseString, passwordChooseString, idCallString);

                break;
            case R.id.imageView9:

                confirmPassword(phone4TextView, nameChooseString,
                        phoneChooseString, passwordChooseString, idCallString);

                break;
            case R.id.imageView10:

                confirmPassword(phone5TextView, nameChooseString,
                        phoneChooseString, passwordChooseString, idCallString);

                break;

            //ปุ่มลบ

            case R.id.imageView11:

                try {
                    phone1TextView.setText("");
                    idCallStringsArrayList.remove(0);
                    myPhoneStringArrayList.remove(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imageView12:
                try {
                    phone2TextView.setText("");
                    idCallStringsArrayList.remove(1);
                    myPhoneStringArrayList.remove(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imageView13:
                try {
                    phone3TextView.setText("");
                    idCallStringsArrayList.remove(2);
                    myPhoneStringArrayList.remove(2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imageView14:
                try {
                    phone4TextView.setText("");
                    idCallStringsArrayList.remove(3);
                    myPhoneStringArrayList.remove(3);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.imageView15:
                try {
                    phone5TextView.setText("");
                    idCallStringsArrayList.remove(4);
                    myPhoneStringArrayList.remove(4);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }   // switch


    }   // onClick

    private void confirmPassword(final TextView phoneTextView,
                                 final String nameChooseString,
                                 final String phoneChooseString,
                                 final String passwordChooseString,
                                 final String idCallString) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("Password " + nameChooseString);
        builder.setMessage("โปรดกรอก Password ของ " + nameChooseString);

        final EditText editText = new EditText(SettingActivity.this);
        builder.setView(editText);

        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String strPassword = editText.getEditableText().toString().trim();

                if (strPassword.equals(passwordChooseString)) {

                    //Password True
                    phoneTextView.setText(nameChooseString);

                    idCallStringsArrayList.add(idCallString);
                    myPhoneStringArrayList.add(phoneChooseString);


                    dialogInterface.dismiss();

                } else {
                    //Password False
                    Toast.makeText(SettingActivity.this,
                            "Password ผิด กรุณากรอกใหม่",
                            Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }   // if

            }   // onClick
        });
        builder.show();


    }   // confirm

    private class SynUser extends AsyncTask<String, Void, String> {

        private Context context;


        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("29octV1", "e doInBack ==> " + e.toString());
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("29octV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                phoneStrings = new String[jsonArray.length()];
                passwordStrings = new String[jsonArray.length()];
                idStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    nameStrings[i] = jsonObject.getString("Name");
                    phoneStrings[i] = jsonObject.getString("Phone");
                    passwordStrings[i] = jsonObject.getString("Password");
                    idStrings[i] = jsonObject.getString("id");


                }   // for

                PhoneAdapter phoneAdapter = new PhoneAdapter(context,
                        nameStrings, phoneStrings);
                listView.setAdapter(phoneAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(context, "คุณเลือก " + nameStrings[i],
                                Toast.LENGTH_SHORT).show();

                        nameChooseString = nameStrings[i];
                        phoneChooseString = phoneStrings[i];
                        passwordChooseString = passwordStrings[i];
                        idCallString = idStrings[i];


                    }   // onItmeClick
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }   // SynUser Class


    private void bindWidget() {
        phone1TextView = (TextView) findViewById(R.id.textView5);
        phone2TextView = (TextView) findViewById(R.id.textView6);
        phone3TextView = (TextView) findViewById(R.id.textView9);
        phone4TextView = (TextView) findViewById(R.id.textView10);
        phone5TextView = (TextView) findViewById(R.id.textView11);


        addPhone1ImageView = (ImageView) findViewById(R.id.imageView6);
        addPhone2ImageView = (ImageView) findViewById(R.id.imageView7);
        addPhone3ImageView = (ImageView) findViewById(R.id.imageView8);
        addPhone4ImageView = (ImageView) findViewById(R.id.imageView9);
        addPhone5ImageView = (ImageView) findViewById(R.id.imageView10);

        deletePhone1ImageView = (ImageView) findViewById(R.id.imageView11);
        deletePhone2ImageView = (ImageView) findViewById(R.id.imageView12);
        deletePhone3ImageView = (ImageView) findViewById(R.id.imageView13);
        deletePhone4ImageView = (ImageView) findViewById(R.id.imageView14);
        deletePhone5ImageView = (ImageView) findViewById(R.id.imageView15);


        phone1RadioButton = (RadioButton) findViewById(R.id.mradioButton6);
        phone2RadioButton = (RadioButton) findViewById(R.id.mradioButton7);
        phone3RadioButton = (RadioButton) findViewById(R.id.mradioButton8);
        phone4RadioButton = (RadioButton) findViewById(R.id.mradioButton9);
        phone5RadioButton = (RadioButton) findViewById(R.id.mradioButton10);

        listView = (ListView) findViewById(R.id.livFriend);
        radioGroup = (RadioGroup) findViewById(R.id.ragPhone);


    }//bindWidget

    public void clickSetting(View view) {

        Log.d("8decV1", "กดยืนยัน");

        if (checkAddPhone()) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(SettingActivity.this, "ยังไม่ได้เลือกเบอร์โทรศัพท์",
                    "กรุณาเลือกเบอร์โทรศัพท์");
        } else if (checkRadio()) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(SettingActivity.this, "ยังไม่ได้เลือกเบอร์โทรศัพท์โทรออก",
                    "กรุณาเลือกเบอร์โทรศัพท์โทรออก");
        } else {
            addPhoneToSQLite();

        }

    }//clickSetting


    private void addPhoneToSQLite() {


        if (idCallStringsArrayList.size() == 0) {

            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(SettingActivity.this, "ไม่มีการแก้ไขข้อมูลใหม่", "กรุณาลบข้อมูลทั้งหมดใหม่ แล้วทำการตั้งค่าผู้ช่วยเหลือใหม่ทั้งหมด ถ้าไม่มีการแก้ไขข้อมูลกรุณากดปุ่ม ยกเลิก");

        } else {

            Log.d("8decV1", "Leangth of arryList ==>" + idCallStringsArrayList.size());
            Log.d("8decV1", "radioChoose==>" + indexRadioChoose);

            Log.d("26janV1", "idCallStringsArrayList ==> " + idCallStringsArrayList.toString());


            try {
                actionStringArrayList = new ArrayList<String>();

                for (int i = 0; i < idCallStringsArrayList.size(); i++) {

                    if (i == indexRadioChoose) {
                        actionStringArrayList.add("1");
                    } else {
                        actionStringArrayList.add("0");
                    }

                }//for


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("8decV1", "actionArrayList==>" + actionStringArrayList);
            Log.d("8DecV1", "idCallArrayList ==>" + idCallStringsArrayList);
            Log.d("8DecV1", "myPhoneArrayList ==>" + myPhoneStringArrayList);

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);


            sqLiteDatabase.delete(MyManage.table_phone, null, null);

            MyManage myManage = new MyManage(SettingActivity.this);
            for (int i = 0; i < idCallStringsArrayList.size(); i++) {

                myManage.addPhoneToSQLite(idCallStringsArrayList.get(i),
                        myPhoneStringArrayList.get(i),
                        actionStringArrayList.get(i));

            }//for

            Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


    }//addPhone

    private boolean checkRadio() {

        boolean result = true; //noncheck
        if (phone1RadioButton.isChecked() ||
                phone2RadioButton.isChecked() ||
                phone3RadioButton.isChecked() ||
                phone4RadioButton.isChecked() ||
                phone5RadioButton.isChecked()) {
            result = false;
        }
        Log.d("8decV1", "phone1 ==>" + phone1RadioButton.isChecked());
        Log.d("8decV1", "phone1 ==>" + phone2RadioButton.isChecked());
        Log.d("8decV1", "phone1 ==>" + phone3RadioButton.isChecked());
        Log.d("8decV1", "phone1 ==>" + phone4RadioButton.isChecked());
        Log.d("8decV1", "phone1 ==>" + phone5RadioButton.isChecked());
        Log.d("8decV1", "radioButtonChoose==>" + radioButtonChooseString);
        Log.d("8decV1", "result checkRadio==>" + result);

        return result;
    }

    private boolean checkAddPhone() {

        boolean result = false;
        if (phone1TextView.getText().toString().equals("") &&
                phone2TextView.getText().toString().equals("") &&
                phone3TextView.getText().toString().equals("") &&
                phone4TextView.getText().toString().equals("") &&
                phone5TextView.getText().toString().equals("")) {

            result = true;
        }
        return result;
    }
}//MAIN Class
