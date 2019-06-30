package com.mybusinessapp.example.madhu.mybusinessapp2.util;

import android.database.Cursor;
import android.util.Log;


import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.MenuItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper class that populates vo with data fetched from database.
 */
public class DBUtil {

    private static final String TAG = "DBUtil";

    public static List<AbstractEntity> populateOrders(Cursor cursor) {

        List<AbstractEntity> data = new ArrayList<AbstractEntity>();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
               int orderId = cursor.getInt(0);
               String orderTitle = cursor.getString(1);
               String dtStr = cursor.getString(2);
               OrderEntity pojo = new OrderEntity();
               pojo.setOrderId(orderId);
               pojo.setTitle(orderTitle);
                Date orderDate = null;
                try {
                    orderDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    Log.e(TAG,"Error parsing date");
                }
                pojo.setCreateDate(orderDate);
                data.add(pojo);
            }
        }

        return data;
    }

    public static List<AbstractEntity> populatePayments(Cursor cursor) {

        List<AbstractEntity> data = new ArrayList<AbstractEntity>();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                PaymentEntity pojo = new PaymentEntity();
                pojo.setPaymentId(cursor.getInt(0));
                String dtStr = cursor.getString(1);
                pojo.setPaymentDesc(cursor.getString(2));
                pojo.setAmount(cursor.getFloat(3));
                pojo.setOrderId(cursor.getInt(4));
                Date paymentDate = null;
                try {
                    paymentDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    Log.e(TAG,"Error parsing date");
                }
                pojo.setPaymentDate(paymentDate);
                data.add(pojo);
            }
        }

        return data;
    }

    public static ClientEntity populateClient(Cursor cursor) {
        ClientEntity cli = new ClientEntity();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                cli.setId(cursor.getString(0));
                cli.setRegisDate(cursor.getString(1));
                cli.setName(cursor.getString(2));
                cli.setPhone1(cursor.getString(3));
                cli.setPhone2( cursor.getString(4));
                cli.setEmail(cursor.getString(5));
                cli.setAddress( cursor.getString(6));
            }
        }
        return cli;
    }

    public static OrderEntity populateOrder(Cursor cursor) {
        OrderEntity order = new OrderEntity();
        //CRE_DATE TEXT, ORD_TITLE TEXT, ORD_DESC TEXT,ORD_AMT REAL,AMT_PAID REAL,CLI_ID INTEGER, CAT_ID INTEGER
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                order.setOrderId(cursor.getInt(0));
                String dtStr = cursor.getString(1);
                order.setTitle(cursor.getString(2));
                order.setDesc(cursor.getString(3));
                order.setAmt(cursor.getFloat(4));
                order.setPaid(cursor.getFloat(5));
                order.setClientId(cursor.getInt(6));
                order.setCatId(cursor.getInt(7));
                Date orderDate = null;
                try {
                    orderDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    Log.e(TAG,"Error parsing date");
                }
                order.setCreateDate(orderDate);
            }
        }
        return order;
    }

    public static PaymentEntity populatePayment(Cursor cursor) {
        PaymentEntity payment = new PaymentEntity();
        //CRE_DATE TEXT, PAY_DESC TEXT,PAY_AMT REAL,ORD_ID
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                payment.setPaymentId(cursor.getInt(0));
                String dtStr = cursor.getString(1);
                payment.setPaymentDesc(cursor.getString(2));
                payment.setAmount(cursor.getFloat(3));
                payment.setOrderId(cursor.getInt(4));
                Date paymentDate = null;
                try {
                    paymentDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    Log.e(TAG,"Error parsing date");
                }
                payment.setPaymentDate(paymentDate);
            }
        }
        return payment;
    }


    public static CategoryEntity populateCategory(Cursor cursor) {
        CategoryEntity cat = null;
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                cat = new CategoryEntity(Integer.parseInt(cursor.getString(0)),cursor.getString(1));

            }
        }
        return cat;
    }

    public static List<MenuItem> populateMenuItem(Cursor cursor) {
        List<MenuItem> cat = new ArrayList<>();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                cat.add( new MenuItem(Integer.parseInt(cursor.getString(0)),cursor.getString(1)));

            }
        }
        return cat;
    }

    public static float getAmount(Cursor cursor) {
       float amt = 0;
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
               amt = cursor.getFloat(0);
            }
        }
        return amt;
    }

    public static ArrayList<CategoryEntity> populateClients(Cursor cursor) {
        ArrayList<CategoryEntity> clients = new ArrayList<CategoryEntity>();
        CategoryEntity cat = new CategoryEntity(-1,"Please select client");
        clients.add(cat);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                CategoryEntity cli = new CategoryEntity(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
                clients.add(cli);
            }
        }
        return clients;
    }

    public static ArrayList<CategoryEntity> populateOrderCategory(Cursor cursor) {
        ArrayList<CategoryEntity> categories = new ArrayList<CategoryEntity>();
        CategoryEntity cat = new CategoryEntity(-1,"Please select category");
        categories.add(cat);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                CategoryEntity cli = new CategoryEntity(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
                categories.add(cli);
            }
        }
        return categories;
    }
}
