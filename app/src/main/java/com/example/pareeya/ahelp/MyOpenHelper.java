package com.example.pareeya.ahelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by masterUNG on 9/3/2016 AD.
 */
public class MyOpenHelper extends SQLiteOpenHelper{

    //Explicit
    public static final String database_name = "aHelp.db";
    private static final int database_version = 1;

    private static final String create_user_table = "create table userTABLE (" +
            "_id integer primary key, " +
            "Name text, " +
            "MyPhone text, " +
            "Password text);";

    private static final String create_phone_table = "create table phoneTABLE(" +
            "_id integer primary key," +
            "idCall text, " +
            "MyPhone text," +
            "Action text);";

    private String firstString = "insert into phoneTABLE VALUES(null, '', '');";





    public MyOpenHelper(Context context) {
        super(context, database_name, null, database_version);
    }   // Constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_user_table);
        sqLiteDatabase.execSQL(create_phone_table);

//        for (int i=0; i<5; i+=1) {
//            sqLiteDatabase.execSQL(firstString);
//
//        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}   // Main Class
