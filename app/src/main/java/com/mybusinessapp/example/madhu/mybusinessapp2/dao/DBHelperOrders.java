package com.mybusinessapp.example.madhu.mybusinessapp2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.util.Date;

public class DBHelperOrders extends DBHelper {
    public DBHelperOrders(Context context) {
        super(context);
    }

//ORD_ID ,CRE_DATE TEXT, ORD_TITLE TEXT, ORD_DESC TEXT,ORD_AMT REAL,AMT_PAID REAL,CLI_ID INTEGER, CAT_ID INTEGER
    public int insertOrder(int clientId, int catId, float amt, float paid, String title, String desc) {
        int orderId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("CAT_ID", catId);
            contentValues.put("CLI_ID", clientId);
            contentValues.put("AMT_PAID", paid);
            contentValues.put("ORD_AMT", amt);
            contentValues.put("ORD_DESC", desc);
            contentValues.put("ORD_TITLE", title);
            contentValues.put("CRE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
            contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
            long result = db.insert("ORDERS", null, contentValues);
            if (result == -1) {
                orderId = -1;
            } else {
                Cursor cursor = db.rawQuery("select max(ORD_ID) from ORDERS", null);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        orderId = cursor.getInt(0);
                    }
                }
                if (paid > 0) {
                    insertPayment(orderId, paid, "Advance paid");
                }
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }

        return orderId;
    }


    public int updateOrder(float amt, float paid, String title, String desc,int orderId,int catId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ORD_TITLE", title);
        contentValues.put("ORD_DESC",desc);
        contentValues.put("ORD_AMT",amt);
        contentValues.put("AMT_PAID",paid);
        contentValues.put("CAT_ID",String.valueOf(catId));
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        String whereClause = " ORD_ID = ? ";
        String[] params = new String[]{ String.valueOf( orderId)};
        int result =  db.update("ORDERS",contentValues,whereClause,params);
        return result;
    }

    public boolean deleteOrderById(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean deleted = false;
        try {
            db.beginTransaction();
            // delete linked payments
            deletePaymentByOrderId(orderId);

            String whereClause = " ORD_ID = ? ";
            String[] params = new String[]{String.valueOf(orderId)};
            int result = db.delete("ORDERS", whereClause, params);
            db.setTransactionSuccessful();
            if (result == -1) {
                deleted = false;
            } else {
                deleted = true;
            }
        } finally {
            db.endTransaction();
        }
        return deleted;
    }




}
