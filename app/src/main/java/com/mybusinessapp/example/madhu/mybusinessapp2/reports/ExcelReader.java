package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

import android.content.Context;
import android.util.Log;

import com.mybusinessapp.example.madhu.mybusinessapp2.exception.ValidationException;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelper;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class accepts report vo objects, converts them into DB entity objects and passes onto helper class for persist.
 */
public class ExcelReader {

    private static final String TAG = "ExcelReader";
    private DBHelper mydb;
    private ReportValidator validator;
    private List<String> clientIdList = new ArrayList<>();
    private List<Integer> orderIdList = new ArrayList<>();
    private List<Integer> paymentIdList = new ArrayList<>();
    private List<Integer> categoryIdList = new ArrayList<>();

     /**
     *This method accepts a list of report vo object and converts it into a list of db object.
     */
    private  List<CategoryEntity> getCategories(List<Object> list) throws ValidationException {

        //Maximum allowed categories validation
        if(list.size() > AppConstants.MAXIMUM_CATEGORIES_ALLOWED) {
            Log.e(TAG,MessageConstants.MAXIMUM_CATEGORIES_MSG);
            throw new ValidationException(MessageConstants.MAXIMUM_CATEGORIES_MSG);
        }

        List<String> categoryNames = new ArrayList<>();
        List<CategoryEntity> categories = new ArrayList<>();
        if(list.size()>0) {
            Iterator<Object> itr = list.iterator();
            while (itr.hasNext()) {
               Category item = (Category) itr.next();
               CategoryEntity category = null;
               try {
                   category  = validator.isValidEntity(item);
               } catch (ValidationException e) {
                   Log.e(TAG,e.getMessage());
                   throw e;
               }

                if(categoryIdList.contains(category.getId())) {
                    String errorMessage = String.format(MessageConstants.INVALID_DATA_MSG,"Category");
                    Log.e(TAG,errorMessage);
                    throw new ValidationException(errorMessage);
                }

               //Duplicate categories validation
               if(categoryNames.contains(category.getName())) {
                   Log.e(TAG,MessageConstants.CATEGORY_EXISTS_MSG);
                   throw new ValidationException(MessageConstants.CATEGORY_EXISTS_MSG);
               }

                categories.add(category);
                categoryNames.add(category.getName());
                categoryIdList.add(category.getId());
            }
        }
        return categories;
    }


    /**
     *This method accepts a list of report vo object and converts it into a list of db object.
     */
    private  List<PaymentEntity> getPayments(List<Object> list) throws ValidationException {
        List<PaymentEntity> payments = new ArrayList<>();
        if(list.size()>0) {
            Iterator<Object> itr = list.iterator();
            while (itr.hasNext()) {
                Payment item = (Payment) itr.next();
                PaymentEntity payment = null;
                try{
                    payment = validator.isValidEntity(item);
                } catch (ValidationException e) {
                    Log.e(TAG,e.getMessage());
                    throw e;
                }

                if(paymentIdList.contains(payment.getPaymentId())
                        || !orderIdList.contains(payment.getOrderId())) {
                    String errorMessage = String.format(MessageConstants.INVALID_DATA_MSG,"Payment");
                    Log.e(TAG,errorMessage);
                    throw new ValidationException(errorMessage);
                }

                payments.add(payment);
                paymentIdList.add(payment.getPaymentId());
            }
        }
        return payments;
    }

    /**
     *This method accepts a list of report vo object and converts it into a list of db object.
     */
    public  List<ClientEntity> getClients(List<Object> list) throws ValidationException {

        List<ClientEntity> clients = new ArrayList<>();
        List<String> primaryPhones = new ArrayList<>();
        if(list.size()>0) {
            Iterator<Object> itr = list.iterator();
            while (itr.hasNext()) {
                Client item = (Client) itr.next();
                ClientEntity client = null;
                try {
                    client = validator.isValidEntity(item);
                } catch (ValidationException e) {
                    Log.e(TAG,e.getMessage());
                    throw e;
                }

                if(clientIdList.contains(client.getId())) {
                    String errorMessage = String.format(MessageConstants.INVALID_DATA_MSG,"Client");
                    Log.e(TAG,errorMessage);
                    throw new ValidationException(errorMessage);
                }

                if( primaryPhones.contains(client.getPhone1())) {
                    Log.e(TAG,MessageConstants.DUPLICATE_CLIENT_MSG);
                    throw new ValidationException(MessageConstants.DUPLICATE_CLIENT_MSG);
                }
                clients.add(client);
                primaryPhones.add(client.getPhone1());
                clientIdList.add(client.getId());
            }
        }
        return clients;
    }

    /**
     *This method accepts a list of report vo object and converts it into a list of db object.
     */
    public  List<OrderEntity> getOrders(List<Object> list) throws ValidationException {
        List<OrderEntity> orders = new ArrayList<>();
        if(list.size()>0) {
            Iterator<Object> itr = list.iterator();
            while (itr.hasNext()) {
                Order item = (Order) itr.next();
                OrderEntity order = null;
                try {
                    order = validator.isValidEntity(item);
                } catch (ValidationException e) {
                    Log.e(TAG,e.getMessage());
                    throw e;
                }

                if(orderIdList.contains(order.getOrderId())
                        || !clientIdList.contains(order.getClientId())
                        || !categoryIdList.contains(order.getCatId())) {
                    String errorMessage = String.format(MessageConstants.INVALID_DATA_MSG,"Order");
                    Log.e(TAG,errorMessage);
                    throw new ValidationException(errorMessage);
                }

                orders.add(order);
                orderIdList.add(order.getOrderId());
            }
        }

        return orders;
    }


    private void writeToDB(List<CategoryEntity> categories, List<ClientEntity> clients, List<OrderEntity> orders, List<PaymentEntity> payments) {
        //TODO
        //use transaction management and rollback if exception
        //this operation will update data if already exists
        //for data not already there it will do insert
        //it will not compare modified date time, as such a feature will encourage data disrepancy.
        //either set A or set B, not an unpredictable mix.
    }

    public void testReadFromExcelInMultiSheets(Context context) throws Exception {

       ExcelRead reader = new ExcelRead();
       reader.startReading();

       mydb = new DBHelper(context);
       validator = new ReportValidator(mydb);

       List<ClientEntity> clients = null;
       List<CategoryEntity> categories = null;
       List<OrderEntity> orders = null;
       List<PaymentEntity> payments = null;

        try {
            clients = getClients(reader.getClients());
            categories = getCategories(reader.getCategories());
            orders = getOrders(reader.getOrders());
            payments = getPayments(reader.getPayments());
        } catch (ValidationException e) {
            Log.e(TAG,e.getMessage());
            //throw message and in fragment display this to user
        }

//        try {
//            writeToDB(categories, clients, orders, payments);
//        }catch (SQLDataException e) {
//            //Log
//            //display message in ui that saving of data failed
//        }
    }

//    public static void main(String[] args) {
//        try {
//            new ExcelUtils().testWriteToExcelInMultiSheets();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
