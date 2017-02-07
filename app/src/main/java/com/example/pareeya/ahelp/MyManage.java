package com.example.pareeya.ahelp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 9/3/2016 AD.
 */
public class MyManage {

    //Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static final String table_name = "userTABLE";
    public static final String column_id = "_id";
    public static final String column_Name = "Name";
    public static final String column_MyPhone = "MyPhone";
    public static final String column_Password = "Password";
    public static final String column_idCall = "idCall";

    public static final String table_phone = "phoneTABLE";
    public static final String column_Action = "Action";

    public MyManage(Context context) {

        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();

    }   // Constructor

    //add Phone to SQLite
    public long addPhoneToSQLite(String strIdCall,
                                 String strMyPhone,
                                 String strAction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_idCall, strIdCall);
        contentValues.put(column_MyPhone, strMyPhone);
        contentValues.put(column_Action, strAction);

        return sqLiteDatabase.insert(table_phone, null, contentValues);
    }

    //add value to SQLite
    public long addValueToSQLite(String strName,
                                 String strMyPhone,
                                 String strPassword) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(column_Name, strName);
        contentValues.put(column_MyPhone, strMyPhone);
        contentValues.put(column_Password, strPassword);

        return sqLiteDatabase.insert(table_name, null, contentValues);
    }

}   // Main Class
