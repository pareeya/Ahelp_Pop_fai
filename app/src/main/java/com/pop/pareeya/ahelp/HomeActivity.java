package com.pop.pareeya.ahelp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity {

    //Explicit
    private Button button, callGreenButton, showListHistoryImageView;
    private ImageView img ;
    private String truePasswordString, userPasswordString,
            idUserString, nameString, idCallString;
    private boolean statusABoolean = true;
    private LocationManager locationManager;
    private Criteria criteria;
    private double lagADouble = 13.859882, lngADouble = 100.481604;
    private String phoneHelpCall; //phone ของคนที่ให้ ความช่วยเหลือ
    private boolean statusCallGreen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        callGreenController();
        setupForLocation();
        createEffectAnimation();

        //Call 1669
        call1669();

        //Img Controller
        imgController();

        //Find Id user
        findIDuser();

        showListHistoryController();

        //My Loop
        myLoop();

    }   // Main Method

    private void showListHistoryController() {

        showListHistoryImageView = (Button) findViewById(R.id.btnShowListHistory);
        showListHistoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CheckInternet checkInternet = new CheckInternet(HomeActivity.this);
                    checkInternet.execute();
                    if (Boolean.parseBoolean(checkInternet.get())) {
                        findIDuser();
                        Intent intent = new Intent(HomeActivity.this, ShowListHistory.class);
                        intent.putExtra("Login", idUserString);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "กรุณาตรวจสอบ Internet",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createEffectAnimation() {
        // image animation
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        img = (ImageView) findViewById(R.id.imageView3);
        img.setBackgroundResource(R.drawable.butalarm);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }

    private void setupForLocation() {
        //setting ขออนุญาติ เปิด Service Get Location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void callGreenController() {
        callGreenButton = (Button) findViewById(R.id.button13);
        callGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusCallGreen = false;
                findPhoneNumberFriend();//โทรหาเบอร์เพื่อนโดยการกดปุ่ม
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            lagADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();

        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            lagADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }
        Log.d("22decV3", "lat==>" + lagADouble);
        Log.d("22decV3", "lng==>" + lngADouble);
    }//onResume

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;
        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);//การค้นหาพิกัดทุกๆ1วินาที,ถ้ามีการเปลี่ยนพิกัด10เมตร ให้ทำการค้นหา
            //การค้นหาพิกัดทุกๆ1วินาที,ถ้ามีการเปลี่ยนพิกัด10เมตร ให้ทำการค้นหา

        }

        return location;
    }

    //location อัตโนมัติ

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            lagADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void findIDuser() {
        //Find idUser
        try {
            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE", null);
            cursor.moveToFirst();
            nameString = cursor.getString(cursor.getColumnIndex(MyManage.column_Name));
            truePasswordString = cursor.getString(cursor.getColumnIndex(MyManage.column_Password));
            Log.d("8decV3", "truePass ==> " + truePasswordString);
            cursor.close();

            FindIDuser findIDuser = new FindIDuser(HomeActivity.this,
                    nameString, truePasswordString);
            findIDuser.execute();
            String strJSON = findIDuser.get();
            Log.d("8decV3", "JSON ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            idUserString = jsonObject.getString("id");

            Log.d("8decV3", "idUser ==> " + idUserString);

//            //Find idCall
//            findIDcall();

        } catch (Exception e) {
            e.printStackTrace();
        }   // try
    }

    //เช็คค่าในMySQL Ahelp ถ้าเป็น0 หรือค่าว่าง จะหยุดทำการแจ้งเตือน
    private void myLoop() {

        //My To do
        try {

            SynAhelp synAhelp = new SynAhelp(HomeActivity.this, idUserString);
            synAhelp.execute();
            String strJSON = synAhelp.get();
            Log.d("8decV3", "JSON ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String strAHlep = jsonObject.getString("Ahelp");

            if (!strAHlep.equals("")) {

                if (Integer.parseInt(strAHlep) != 0) {
                    myNotification();
                }//if2
            }//if1


        } catch (Exception e) {
            Log.d("8decV3", "e myLoop ==> " + e.toString());
        }


        //Delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statusABoolean) {
                    myLoop();
                }
            }
        }, 1000);


    }   // myLoop

    //การ alert ไปหา friend
    private void myNotification() {

        statusABoolean = false;

        Log.d("8decV4", "Noti OK");
        Intent intent = new Intent(HomeActivity.this, NotificationMaps.class);
        intent.putExtra("idUser", idUserString);
        PendingIntent pendingIntent = PendingIntent.getActivity(HomeActivity.this,
                (int) System.currentTimeMillis(), intent, 0);
        Uri uri = RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND);
        Notification.Builder builder = new Notification.Builder(HomeActivity.this);
        builder.setTicker("Ahelp");
        builder.setContentTitle("ข้อความจาก");
        builder.setContentText("เกิดเหตุฉุกเฉินกับ" + "กรุณามาช่วยเหลือที่ตำแหน่ง");
        builder.setSmallIcon(R.drawable.alert);
        builder.setSound(uri);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);


    }//noti

    // คลิก ปุ่มมีภัย เพื่อเรียกเพื่อน onClick เพื่อส่งข้อความเรียกเพื่อน
    private void imgController() {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmPassword(0);

            }
        });

    }

    //หลังจากส่งข้อความจะทำการหน่วงเวลา 10วินาที ไปยังเบอร์ที่เราตั้งค้าไว้ โดยดูจาก FROM phoneTABLE WHERE Action = 1
    private void findPhoneNumberFriend() {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneTABLE WHERE Action = 1", null);
            cursor.moveToFirst();
            Log.d("12janV2", "cursor.getCount ==> " + cursor.getCount());
            phoneHelpCall = cursor.getString(2);
            Log.d("12janV2", "phoneHelpCall ==>" + phoneHelpCall);

            if (statusCallGreen) {
                delayTime();
            } else {
                callPhoneToFriend();
            }

        } catch (Exception e) {
            Log.d("12janV2", "e ==> " + e.toString());
        }


    }   // findPhone

    //หน่วงเวลาโทรออก
    private void delayTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callPhoneToFriend();

            }
        }, 10000);
    }

    private void callPhoneToFriend() {

        try {

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:=" + phoneHelpCall));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }//callPhoneToFriend

    //การยืนยันรหัสผ่านก่อนจะทำการส่งข้อความขอความช่วยเหลือไปหา friend
    private void confirmPassword(final int intdex) {

        //Get Password from SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE", null);
        cursor.moveToFirst();
        nameString = cursor.getString(cursor.getColumnIndex(MyManage.column_Name));
        truePasswordString = cursor.getString(cursor.getColumnIndex(MyManage.column_Password));
        Log.d("8decV2", "truePass ==> " + truePasswordString);
        cursor.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("ยืนยันรหัสผ่าน");
        builder.setMessage("กรุณากรอกรหัสผ่านของคุณ");

        final EditText editText = new EditText(HomeActivity.this);
        builder.setView(editText);

        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userPasswordString = editText.getText().toString().trim();
                if (userPasswordString.equals(truePasswordString)) {
                    //Password True
                    switch (intdex) {
                        case 0:
                            myAHelp();
                            dialogInterface.dismiss();
                            break;
                        case 1:

                            Intent CallIntent = new Intent(Intent.ACTION_CALL);
                            CallIntent.setData(Uri.parse("tel:=1669"));
                            startActivity(CallIntent);

                            dialogInterface.dismiss();
                            break;
                    }

                } else {
                    passwordFalse();
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }   // confirm

    private void myAHelp() {
        callFriend();
        findPhoneNumberFriend();
    }

    private void passwordFalse() {
        MyAlert myAlert = new MyAlert();
        myAlert.myDialog(HomeActivity.this, "รหัสผ่านผิด",
                "กรุณากรอกรหัสผ่านใหม่");
    }

    private void callFriend() {

        //Find idUser
        try {

            //หาข้อมูลของ คนที่กำลัง Login อยู่ โดยต้องการ id ของ User ที่ Login ==> idUserString
            FindIDuser findIDuser = new FindIDuser(HomeActivity.this,
                    nameString, truePasswordString);
            findIDuser.execute();
            String strJSON = findIDuser.get();
            Log.d("8decV2", "JSoN ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            idUserString = jsonObject.getString("id");

            Log.d("8decV2", "idUser ==> " + idUserString);

            //Find idCall
            findIDcall();

        } catch (Exception e) {
            e.printStackTrace();
        }   // try


    }   // callFriend

    private void findIDcall() {
        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneTABLE", null);
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                String strAHeip = idUserString; // หมายถึง id ของ user ที่กดเรียกเพื่อน

                // หมายถึง id ของเพื่อนที่ บันทึกไว้ใน phoneTABLE
                String idUser = cursor.getString(cursor.getColumnIndex(MyManage.column_idCall));

                //ในแต่ละรอบจะส่ง id ของคนกดเรียกเพื่อน และ เพื่อนไป editAhelp

                Log.d("12janV1", "id ของเพื่อนที่ถูกเรียก ==> " + idUser);
                Log.d("12janV1", "my Location Lat ==> " + lagADouble);
                Log.d("12janV1", "my Location Lng ==> " + lngADouble);

                editAhelp(idUser, strAHeip);

                addHistoryToServer(idUser);

                cursor.moveToNext();
            }//for
            idCallString = cursor.getString(cursor.getColumnIndex(MyManage.column_idCall));
            Log.d("8decV2", "idCall ==> " + idCallString);


        } catch (Exception e) {
            Log.d("8decV2", "e find idCall ==> " + e.toString());
        }
    }

    private void addHistoryToServer(String receiveID) {

        String tag = "3MarchV1";
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(calendar.getTime());


        try {



            Log.d(tag, "Call_ID ==> " + idUserString);
            Log.d(tag, "Receive_ID ==> " + receiveID);
            Log.d(tag, "Lat ==> " + Double.toString(lagADouble));
            Log.d(tag, "Lng ==> " + Double.toString(lngADouble));
            Log.d(tag, "Call_DateTime ==> " + currentDateTime);

            AddHistory addHistory = new AddHistory(HomeActivity.this, idUserString, receiveID,
                    Double.toString(lagADouble), Double.toString(lngADouble), currentDateTime);
            addHistory.execute();

            Log.d(tag, "Result ==> " + addHistory.get());

        } catch (Exception e) {
            Log.d(tag, "e addHistory ==> " + e.toString());
        }
    }

    //ระบบทำการยืนยันการส่งข้อมูล
    private void editAhelp(String idUser, String strAHelp) {

        try {

            EditAhelp editAhelp = new EditAhelp(HomeActivity.this,
                    idUser, strAHelp, Double.toString(lagADouble), Double.toString(lngADouble));
            editAhelp.execute("http://swiftcodingthai.com/fai/edit_Ahelp_where_id.php");

            if (Boolean.parseBoolean(editAhelp.get())) {
                Toast.makeText(HomeActivity.this, "ส่งข้อความเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "การส่งข้อความไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("8decV2", "e edit AHelp ==> " + e.toString());
        }
    }

    private void call1669() {
        button = (Button) findViewById(R.id.call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPassword(1);

            }//onClick


        });
    }//call1669

    public void clickHomeGoSetting(View view) {
        try {
            CheckInternet checkInternet = new CheckInternet(HomeActivity.this);
            checkInternet.execute();
            if (Boolean.parseBoolean(checkInternet.get())) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            } else {
                Toast.makeText(HomeActivity.this, "กรุณาตรวจสอบ Internet",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // clickHome

}   // Main Class
