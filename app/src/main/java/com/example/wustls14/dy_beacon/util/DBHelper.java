package com.example.wustls14.dy_beacon.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "DY_Beacon_DB";
    public static String TABLE_NAME = "registered_Info_Table";
    public static int DATABASE_VERSION = 1;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

        try {
            String DROP_SQL = "drop table if exists " + TABLE_NAME;
            db.execSQL(DROP_SQL);
        } catch(Exception ex) {
            U.getInstance().log("Exception in DROP_SQL");
        }

        String CREATE_SQL = "create table " + TABLE_NAME + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " beaconName text, "
                + " srlNo integer, "
                + " distance_position integer, "
                + " distance text)";

        try {
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            U.getInstance().log("CREATE_SQL 에서 오류 발생");
        }
        U.getInstance().log("inserting records 에서 오류 발생");
    }

    public boolean insertRecord2(String beaconName, int srlNo, int distance_number, String distance){
        try {
            db.execSQL( "insert into " + TABLE_NAME + "(beaconName, srlNo, distance_position, distance) values ('" + beaconName + "', "+ srlNo +", "+ distance_number +", '" + distance +"');" );
            return true;
        } catch(Exception ex) {
            U.getInstance().log("insert SQL 에서 오류 발생");
            return false;
        }
    }

    public boolean insertRecord(String beaconName, int srlNo, int distance_number, String distance){
        try {
            db.execSQL( "insert into " + TABLE_NAME + "(beaconName, srlNo, distance_position, distance) values ('" + beaconName + "', "+ srlNo +", "+ distance_number +", '" + distance +"');" );
            return true;
        } catch(Exception ex) {
            U.getInstance().log("Exception in insert SQL");
            return false;
        }
    }

    // 수정시 데이터를 다시 넣는 메소드 --- 작동 안함
    public boolean update(String after_beaconName, int srlNo, String former_srlNo){

        try {
            String strSQL = "UPDATE "+ TABLE_NAME+ " SET beaconName = '"+after_beaconName+"', srlNo = " + srlNo + " WHERE srlNo = "+ former_srlNo;
            db.execSQL(strSQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public void onOpen(SQLiteDatabase db) { U.getInstance().log(DATABASE_NAME + "의 데이터베이스가 열렸다.");}

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        U.getInstance().log("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
    }
}
