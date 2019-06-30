package com.mybusinessapp.example.madhu.mybusinessapp2.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Users.db";

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION =5;

    protected final String TAG = "DBHelper";


     public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }
    // TODO ORDER_CAT table to be renamed to CATEGORY
    //TODO payment_hist table to be renamed to PAYMENT
    //TODO close db and cursor in all dao methods.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS CLIENT (CLI_ID INTEGER PRIMARY KEY AUTOINCREMENT,REG_DATE TEXT, UPDATE_DATE, CLI_NAME TEXT,CLI_PHONE1 TEXT,CLI_PHONE2 TEXT, CLI_EMAIL TEXT, CLI_ADDR TEXT)");
        db.execSQL("create table IF NOT EXISTS ORDERS (ORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,CRE_DATE TEXT, UPDATE_DATE, ORD_TITLE TEXT, ORD_DESC TEXT,ORD_AMT REAL,AMT_PAID REAL,CLI_ID INTEGER, CAT_ID INTEGER)");
        db.execSQL("create table IF NOT EXISTS PAYMENT_HIST (PAY_ID INTEGER PRIMARY KEY AUTOINCREMENT,CRE_DATE TEXT, UPDATE_DATE, PAY_DESC TEXT,PAY_AMT REAL,ORD_ID INTEGER)");
        db.execSQL("create table IF NOT EXISTS ORDER_CAT (CAT_ID INTEGER PRIMARY KEY AUTOINCREMENT,CAT_NAME TEXT, UPDATE_DATE)");
     }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       // db.execSQL("DROP TABLE IF EXISTS ORDER_CAT");
        onCreate(db);
    }

    public Cursor getClientById(int clientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf(clientId)};
        Cursor res = db.rawQuery("select CLI_ID,REG_DATE,CLI_NAME,CLI_PHONE1,CLI_PHONE2,CLI_EMAIL,CLI_ADDR from CLIENT WHERE CLI_ID = ?",params);
        return res;
    }

    public Cursor getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf( orderId)};
        Cursor res = db.rawQuery("select * from ORDERS WHERE ORD_ID = ? ",params);
        return res;
    }

    public Cursor getCategoryById(int catId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf(catId)};
        Cursor res = db.rawQuery("select * from ORDER_CAT WHERE CAT_ID = ?",params);
        return res;
    }

    public Cursor getOrdersByClientId(int clientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf( clientId)};
        Cursor res = db.rawQuery("select o.ORD_ID,o.ORD_TITLE, o.CRE_DATE from ORDERS o WHERE o.CLI_ID = ? ",params);
        return res;
    }

    public Cursor getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select CAT_ID,CAT_NAME from ORDER_CAT",null);
        return res;
    }

    /**** PAYMENT ***/
// (PAY_ID INTEGER PRIMARY KEY AUTOINCREMENT,CRE_DATE TEXT, PAY_DESC TEXT,PAY_AMT REAL,ORD_ID INTEGER
    public int insertPayment(int orderId, float amt,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ORD_ID",orderId);
        contentValues.put("PAY_AMT",amt);
        contentValues.put("PAY_DESC",description);
        contentValues.put("CRE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        long result = db.insert("PAYMENT_HIST", null, contentValues);
        if (result == -1) {
            return -1;
        } else {
            Cursor cursor = db.rawQuery("select max(PAY_ID) from PAYMENT_HIST",null);
            int id = -1;
            if(cursor.getCount()>0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(0);
                }
            }
            return id;
        }
    }

    public boolean deletePaymentByOrderId(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = " ORD_ID = ? ";
        String[] params = new String[]{ String.valueOf( orderId)};
        int result = db.delete("PAYMENT_HIST", whereClause,params);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getPaymentByOrderId(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf( orderId)};
        Cursor res = db.rawQuery("select sum(PAY_AMT) from PAYMENT_HIST WHERE ORD_ID = ? ",params);
        return res;
    }

    public Cursor getPayments() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from PAYMENT_HIST ",null);
        return res;
    }

    public Cursor getOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ORDERS ",null);
        return res;
    }

    public Cursor getAllClients() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CLIENT ",null);
        return res;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ORDER_CAT",null);
        return res;
    }

}


