package com.example.pareeya.ahelp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    //Explicit การประกาศตัวแปร
    private MyManage myManage;
    private EditText nameEditText, MyPhoneEditText,
            PasswordEditText, rePasswordEdiText;
    private String nameString, MyPhoneString,
            passwordString, rePasswordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // image animation
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setBackgroundResource(R.drawable.anima);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText7);
        MyPhoneEditText = (EditText) findViewById(R.id.editText8);
        PasswordEditText = (EditText) findViewById(R.id.editText9);
        rePasswordEdiText = (EditText) findViewById(R.id.editText10);

        //Check Internet
        try {

            CheckInternet checkInternet = new CheckInternet(MainActivity.this);
            checkInternet.execute();
            if (Boolean.parseBoolean(checkInternet.get())) {
                Toast.makeText(MainActivity.this, " ", Toast.LENGTH_SHORT);



            } else {
                Toast.makeText(MainActivity.this, "กรุณาตรวจสอบ Internet", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        myManage = new MyManage(this);

        myCheck();



    }//Main Method

    private void myCheck() {

        //Check SQLite ทำการเข้าหน้าล็อคอินแค่ครั้งเดียว

        Log.d("4SepV1", "Man==>");

        if (checkSQLite()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

    }

    private boolean checkSQLite() {

        boolean result = false;

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE", null);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            result = true;  // Have Data
        }
        Log.d("4SepV1", "Result==>" + result);
        return result;


    }

    public void clickSaveData(View view) {

        nameString = nameEditText.getText().toString().trim();
        MyPhoneString = MyPhoneEditText.getText().toString().trim();
        passwordString = PasswordEditText.getText().toString().trim();
        rePasswordString = rePasswordEdiText.getText().toString().trim();

        Log.d("loctV1", "Pass ==>" + passwordString);
        Log.d("loctV1", "RePass ==>" + rePasswordString);


        //Check Space เช็คข้อมูล

        if (checkSpace()) {
            //Have Space ถ้ามีความว่างเปล่า
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "กรอกข้อมูลไม่ครบ", "กรุณากรอกข้อมูลใหม่ คะ");
        } else if (MyPhoneString.length() != 10) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "Phone False", "กรุณากรอก หมายเลขโทรศัพท์ เพียงแค่ 10 หลัก");
        } else if (passwordString.length() != 4) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "Password False", "กรุณากรอก Password ให้มีแค่ 4 หลัก");
        } else if (!passwordString.equals(rePasswordString)) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "Password False", "กรุณากรอก Password ให้เหมือนกัน");
        } else {
            confirmData();
        }


    }//clickSaveData

    //การตรวจสอบข้อมูลชื่อ-เบอร์โทร
    private void confirmData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("โปรดตรวจสอบข้อมูล");
        builder.setMessage("ชื่อ-นามสกุล = " + nameString + "\n" +
                "เบอร์โทรศัพท์ = " + MyPhoneString + "\n" +
                "Password = " + passwordString);
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //UPload user to mySQL server
                UploadToMySQL uploadToMySQL = new UploadToMySQL(MainActivity.this);
                uploadToMySQL.execute();

                //add user SQLITE
                SaveSQLite();
                dialog.dismiss();

            }
        });
        builder.show();
    }//confirmData

    private class UploadToMySQL extends AsyncTask<Void, Void, String> {

        //Explicit
        private Context context;
        private static final String urlPHP = "http://swiftcodingthai.com/fai/add_user_fai.php";

        public UploadToMySQL(Context context) {
            this.context = context;
        }//constructor เมธทอสหลัก


        @Override
        protected String doInBackground(Void... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Name", nameString)
                        .add("Phone", MyPhoneString)
                        .add("Password", passwordString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlPHP).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("1octV2", "e doInBack ==> " + e.toString());
                return null;
            }


        }//doInBankทำงานอยู่เบื้องหลังในการต่อ server การเชื่อมต่อเน็ต

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("locV2", "Result ==>" + s);

        }//onPost ทำงานหลังจาก

    }//upload Class

    private void SaveSQLite() {

        myManage.addValueToSQLite(nameString, MyPhoneString, passwordString);
        startActivity(new Intent(MainActivity.this, ContentActivity.class));
        finish();

    }

    private boolean checkSpace() {
        return nameString.equals("") ||
                MyPhoneString.equals("") ||
                passwordString.equals("") ||
                rePasswordString.equals("");


    }

}//Main Class