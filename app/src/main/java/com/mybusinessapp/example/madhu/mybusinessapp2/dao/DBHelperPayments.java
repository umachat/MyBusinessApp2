package com.mybusinessapp.example.madhu.mybusinessapp2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.util.Date;

public class DBHelperPayments extends DBHelper {
    public DBHelperPayments(Context context) {
        super(context);
    }



    public boolean deletePayment(int paymentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = " PAY_ID = ? ";
        String[] params = new String[]{ String.valueOf( paymentId)};
        int result = db.delete("PAYMENT_HIST", whereClause,params);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public int updatePayment(float amt, String desc,int paymentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PAY_DESC",desc);
        contentValues.put("PAY_AMT",amt);
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        String whereClause = " PAY_ID = ? ";
        String[] params = new String[]{ String.valueOf( paymentId)};
        int result =  db.update("PAYMENT_HIST",contentValues,whereClause,params);
        return result;
    }

    public Cursor getPaymentById(int paymentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf( paymentId)};
        Cursor res = db.rawQuery("select * from PAYMENT_HIST WHERE PAY_ID = ? ",params);
        return res;
    }

    public Cursor getPaymentByOrderId(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf( orderId)};
        Cursor res = db.rawQuery("select * from PAYMENT_HIST p WHERE p.ORD_ID = ? ",params);
        return res;
    }



}
