package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelper;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This helper class populate report vos with DB data before writting an excel file.
 */
public class ExcelWriter {

    private static final String TAG = "ExcelWriter";

    private DBHelper mydb;

    /**
     * Fetch categories from db and populate a list of report vo.
     * @return
     */
    private  List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = mydb.getAllCategories();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
               int categoryId = cursor.getInt(0);
               String categoryName = cursor.getString(1);
               Category category = ConversionUtil.getCategoryReportVo(categoryId,categoryName);
               categories.add(category);
            }
        }
        return categories;
    }

    /**
     * Fetch payments from db and populate a list of report vo.
     * @return
     */
    private  List<Payment> getPayments() throws ParseException {
        List<Payment> payments = new ArrayList<>();
        Cursor cursor = mydb.getPayments();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                int paymentId = cursor.getInt(0);
                int orderId = cursor.getInt(4);
                String dtStr = cursor.getString(1);
                String paymentDescription = cursor.getString(2);
                float paymentAmount = cursor.getFloat(3);
                Date paymentDate = null;
                try {
                    paymentDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Payment");
                    Log.e(TAG,errorMessage);
                    throw e;
                }
                Payment payment = ConversionUtil.getPaymentReportVo(paymentId,orderId,paymentDate,paymentDescription,paymentAmount);
                payments.add(payment);
            }
        }

        return payments;
    }

    /**
     * Fetch clients from db and populate a list of report vo.
     * @return
     */
    public  List<Client> getClients() throws ParseException {
        List<Client> clients = new ArrayList<Client>();
        Cursor cursor = mydb.getAllClients();
        if(cursor.getCount()>0) {

            while (cursor.moveToNext()) {
                int clientId = cursor.getInt(0);
                String dtStr = cursor.getString(1);
                String clientName = cursor.getString(2);
                String phone1 = cursor.getString(3);
                String phone2 = cursor.getString(4);
                String email = cursor.getString(5);
                String address = cursor.getString(6);
                Date regisDate = null;
                try {
                    regisDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Client Registration");
                    Log.e(TAG,errorMessage);
                    throw e;
                }
                Client client = ConversionUtil.getClientReportVo(clientId,clientName,regisDate,phone1,phone2,email,address);
                clients.add(client);
            }
        }

        return clients;
    }

    /**
     * Fetch orders from db and populate a list of report vo.
     * @return
     */
    public  List<Order> getOrders() throws ParseException {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = mydb.getOrders();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
               int orderId = cursor.getInt(0);
                String dtStr = cursor.getString(1);
                String orderTitle = cursor.getString(2);
                String orderDescription = cursor.getString(3);
                float orderAmount = cursor.getFloat(4);
                float advancePaid = cursor.getFloat(5);
                int clientId = cursor.getInt(6);
                int categoryId = cursor.getInt(7);
                Date orderDate = null;
                try {
                    orderDate = AppConstants.DB_DATE_FORMAT.parse(dtStr);
                } catch (ParseException e) {
                    String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Order");
                    Log.e(TAG,errorMessage);
                    throw e;
                }
                Order order = ConversionUtil.getOrderReportVo(orderId,clientId,categoryId,orderDate,orderTitle,orderDescription,orderAmount,advancePaid);
                orders.add(order);
            }
        }

        return orders;
    }

    public void testWriteToExcelInMultiSheets(Context context,File file) throws Exception {
//       File file = null;
//       file = new File("D:/excel.xlsx");
       ExcelWriteUtils.refresh(file);
       mydb = new DBHelper(context);
       ExcelWriteUtils.writeToExcelInMultiSheets(file, Client.class.getSimpleName(), getClients(),false);
       ExcelWriteUtils.writeToExcelInMultiSheets(file, Category.class.getSimpleName(), getCategories(),true);
       ExcelWriteUtils.writeToExcelInMultiSheets(file, Order.class.getSimpleName(), getOrders(),true);
       ExcelWriteUtils.writeToExcelInMultiSheets(file, Payment.class.getSimpleName(), getPayments(),true);
    }

//    public static void main(String[] args) {
//        try {
//            new ExcelUtils().testWriteToExcelInMultiSheets();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
