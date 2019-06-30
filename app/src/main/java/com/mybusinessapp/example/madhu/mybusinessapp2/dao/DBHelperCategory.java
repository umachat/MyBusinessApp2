package com.mybusinessapp.example.madhu.mybusinessapp2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.util.Date;

public class DBHelperCategory extends DBHelper {
    public DBHelperCategory(Context context) {
        super(context);
    }




    public Cursor getCategoryByName(String catName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ catName};
        Cursor res = db.rawQuery("select * from ORDER_CAT WHERE CAT_NAME = ?",params);
        return res;
    }



    public boolean addCategory(String catName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = getCategoryByName( catName);
        if(res.getCount()>0) {
            Log.d(TAG, "duplicate category!!");
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("CAT_NAME", catName);
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        long result = db.insert("ORDER_CAT", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateCategory(String catName,int catId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CAT_NAME", catName);
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        String whereClause = " CAT_ID = ? ";
        String[] params = new String[]{ String.valueOf(catId)};
        long result =  db.update("ORDER_CAT",contentValues,whereClause,params);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public int deleteCategory(int catId) {
        // return if category is bound to an order and display user message to first delete related orders
        Cursor res = getOrdersByCategory( catId);
        if(res.getCount() > 0) {
            return 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = " CAT_ID = ? ";
        String[] params = new String[]{ String.valueOf( catId)};
        int result = db.delete("ORDER_CAT", whereClause,params);
        if (result == -1) {
            return -1;
        } else {
            return 1;
        }
    }

    public Cursor getOrdersByCategory(int catId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ String.valueOf(catId)};
        Cursor res = db.rawQuery("select CAT_ID from ORDERS WHERE CAT_ID = ?",params);
        return res;
    }
}
