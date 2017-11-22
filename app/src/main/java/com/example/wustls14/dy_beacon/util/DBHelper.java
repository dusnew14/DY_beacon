package com.example.wustls14.dy_beacon.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    String TAG = "TAG";
    private static String DATABASE_NAME = "DY_Beacon_DB";
    private static String TABLE_NAME = "registered_Info_Table";
    private static int DATABASE_VERSION = 1;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        //println("creating table [" + TABLE_NAME + "].");

        try {
            String DROP_SQL = "drop table if exists " + TABLE_NAME;
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }

        String CREATE_SQL = "create table " + TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " phone text)";

        try {
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }

        Log.e(TAG, "inserting records.");
    }

    public boolean insertReord(String beaconName, int srlNo, String distance){
        try {
            // db.execSQL( "insert into " + TABLE_NAME + "(name, age, phone) values ('" + beaconName + "', "+ srlNo +", "+distance+");" );
            db.execSQL( "insert into " + TABLE_NAME + "(name, age, phone) values ('" + beaconName + "', "+ srlNo +", '" + distance +"');" );
            return true;
        } catch(Exception ex) {
            Log.e(TAG, "Exception in insert SQL", ex);
            return false;
        }
    }


    public void onOpen(SQLiteDatabase db) { Log.e(TAG, "opened database [" + DATABASE_NAME + "].");}

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

    }
}
