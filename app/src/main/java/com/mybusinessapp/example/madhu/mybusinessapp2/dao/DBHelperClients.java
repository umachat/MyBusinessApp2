package com.mybusinessapp.example.madhu.mybusinessapp2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.util.Date;

public class DBHelperClients extends DBHelper {

    private static final String TABLE_NAME = "CLIENT";

    public DBHelperClients(Context context) {
        super(context);
    }

    public int insertClient(String name, String email, String phone1, String phone2, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CLI_NAME", name);
        contentValues.put("CLI_EMAIL", email);
        contentValues.put("CLI_PHONE1",phone1);
        contentValues.put("CLI_PHONE2",phone2);
        contentValues.put("CLI_ADDR",address);
        contentValues.put("REG_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return -1;
        } else {
            Cursor cursor = getClientByPrimaryPhone( phone1);
            int id = -1;
            if(cursor.getCount()>0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(0);
                }
            }
            return id;
        }
    }

    public int updateClient(String name, String email, String phone1, String phone2, String address,int clientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CLI_NAME", name);
        contentValues.put("CLI_PHONE1",phone1);
        contentValues.put("CLI_PHONE2",phone2);
        contentValues.put("CLI_ADDR",address);
        contentValues.put("CLI_EMAIL",email);
        contentValues.put("UPDATE_DATE", AppConstants.DB_DATE_FORMAT.format(new Date()));
        String whereClause = " CLI_ID = ? ";
        String[] params = new String[]{ String.valueOf( clientId)};
        int result =  db.update("CLIENT",contentValues,whereClause,params);
        return result;
    }


    public boolean deleteDataByClientId(int clientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean deleted = false;
        try {
            db.beginTransaction();
            //delete orders and payments
            deleteOrderByClientId(clientId);

            String whereClause = " CLI_ID = ? ";
            String[] params = new String[]{String.valueOf(clientId)};
            int result = db.delete(TABLE_NAME, whereClause, params);

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


    public boolean deleteOrderByClientId(int clientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // GET orderids for client and delete linked payments
        boolean deleted = false;
        try {
            db.beginTransaction();
            Cursor cursor = getOrdersByClientId(clientId);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int orderId = cursor.getInt(0);
                    deletePaymentByOrderId(orderId);
                }
            }

            //delete order
            String whereClause = " CLI_ID = ? ";
            String[] params = new String[]{String.valueOf(clientId)};
            int result = db.delete("ORDERS", whereClause, params);
            db.setTransactionSuccessful();
            if (result == -1) {
                deleted= false;
            } else {
                deleted= true;
            }
        }finally {
            db.endTransaction();
        }
        return deleted;
    }


    public Cursor getClients() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select CLI_ID,CLI_NAME from CLIENT ",null);
        return res;
    }

    public Cursor getClientByPrimaryPhone(String phone1) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{ phone1};
        Cursor res = db.rawQuery("select CLI_ID from CLIENT WHERE CLI_PHONE1 = ?",params);
        return res;
    }

}
