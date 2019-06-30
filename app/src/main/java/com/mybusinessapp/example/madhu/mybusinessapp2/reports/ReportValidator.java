package com.mybusinessapp.example.madhu.mybusinessapp2.reports;


import android.util.Log;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelper;
import com.mybusinessapp.example.madhu.mybusinessapp2.exception.ValidationException;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;


/**
 * This helper class contains methods which do validation of data before converting a report vo into a DB object.
 * Throws validation exception if any chck fails.
 */
public class ReportValidator {

    private static final String TAG = "ReportValidator";
    private DBHelper mydb;


    public ReportValidator(DBHelper mydb) {
        this.mydb = mydb;
    }

    private boolean isNullOrBlank(String str) {
        if(null!=str && str.trim().length()>0) {
            return false;
        }
        return true;
    }


    /**
     * This method does null and empty check for all mandatory fields of a Category entity. It also does entity specific field level validations.
     * @param item
     * @return entity
     */
    public CategoryEntity isValidEntity(Category item) throws ValidationException {

        //mandatory fields check
        if(isNullOrBlank(item.getCategory_Id()) || isNullOrBlank(item.getCategory_Name())) {
            String errorMessage = String.format(MessageConstants.BLANK_RECORD_MSG,"Category");
            throw new ValidationException(errorMessage);
        }

        CategoryEntity category = null;
        try {
            int categoryId = Integer.valueOf(item.getCategory_Id().trim());
            String categoryName = item.getCategory_Name();
            category = ConversionUtil.getCategory(categoryId, categoryName);
        } catch (NumberFormatException e) {
            String errorMessage = String.format(MessageConstants.NUMBER_PARSING_EXCEPTION,"Category");
            throw new ValidationException(errorMessage);
        }
        return category;
    }

    /**
     * This method does null and empty check for all mandatory fields of a Client entity. It also does entity specific field level validations.
     * @param item
     * @return entity
     */
    public ClientEntity isValidEntity(Client item) throws ValidationException {

        //mandatory fields check
        if(isNullOrBlank(item.getClient_Id()) || isNullOrBlank(item.getRegistration_Date())
                || isNullOrBlank(item.getClient_Name()) || isNullOrBlank(item.getPhone1())) {
            String errorMessage = String.format(MessageConstants.BLANK_RECORD_MSG,"Client");
            throw new ValidationException(errorMessage);
        }

        ClientEntity client = null;

        int clientId = Integer.valueOf(item.getClient_Id());
        String dtStr = item.getRegistration_Date();
        String clientName = item.getClient_Name();

        //valid ph number check
        String phone1 = item.getPhone1();
        if(!!Pattern.matches(AppConstants.PHONE_REGEX, phone1)) {
            String errorMessage = MessageConstants.INVALID_PH_MSG;
            throw new ValidationException(errorMessage);
        }

        //if secondary phone and email avilable then check valid pattern.
        String phone2 = item.getPhone2();
        if(!isNullOrBlank(phone2) && !Pattern.matches(AppConstants.PHONE_REGEX, phone2)) {
            String errorMessage = MessageConstants.INVALID_SECONDARY_PH_MSG;
            throw new ValidationException(errorMessage);
        }

        String email = item.getEmail();
        if(!isNullOrBlank(email) && !Pattern.matches(AppConstants.EMAIL_REGEX, email)) {
            String errorMessage = MessageConstants.INVALID_EMAIL_MSG;
            throw new ValidationException(errorMessage);
        }

        String address = item.getAddress();

        //registration date format validity check.
        Date regisDate = null;
        try {
            regisDate = AppConstants.DISPLAY_DATE_FORMATTER.parse(dtStr);
        } catch (ParseException e) {
            String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Client Registration");
            throw new ValidationException(errorMessage);
        }

        client = ConversionUtil.getClient(clientId,clientName,regisDate,phone1,phone2,email,address);

        return client;
    }


    /**
     * This method does null and empty check for all mandatory fields of an Order entity. It also does entity specific field level validations.
     * @param item
     * @return entity
     */
    public OrderEntity isValidEntity(Order item) throws ValidationException {

        //mandatory fields check
        if(isNullOrBlank(item.getOrder_Id()) || isNullOrBlank(item.getOrder_Creation_Date())
                || isNullOrBlank(item.getOrder_Title()) || isNullOrBlank(item.getOrder_Description())
                || isNullOrBlank(item.getOrder_Amount()) || isNullOrBlank(item.getClient_Id())
                || isNullOrBlank(item.getCategory_Id())) {
            String errorMessage = String.format(MessageConstants.BLANK_RECORD_MSG,"Order");
            throw new ValidationException(errorMessage);
        }
        OrderEntity order = null;

        String orderTitle = item.getOrder_Title();
        String orderDescription = item.getOrder_Description();

        //order creation date format validity check.
        Date orderDate = null;
        try {
            String dtStr = item.getOrder_Creation_Date();
            orderDate = AppConstants.DISPLAY_DATE_FORMATTER.parse(dtStr);
        } catch (ParseException e) {
            String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Order Creation");
            throw new ValidationException(errorMessage);
        }


        try {
            int orderId = Integer.valueOf(item.getOrder_Id());
            float orderAmount = Float.valueOf(item.getOrder_Amount());
            float advancePaid = Float.valueOf(item.getAdvance_Paid());
            int clientId = Integer.valueOf(item.getClient_Id());
            int categoryId = Integer.valueOf(item.getCategory_Id());

            order = ConversionUtil.getOrder(orderId,clientId,categoryId,orderDate,orderTitle,orderDescription,orderAmount,advancePaid);
        }catch (NumberFormatException e) {
            String errorMessage = String.format(MessageConstants.NUMBER_PARSING_EXCEPTION,"Order");
            throw new ValidationException(errorMessage);
        }

        return order;
    }


    /**
     * This method does null and empty check for all mandatory fields of a Payment entity. It also does entity specific field level validations.
     * @param item
     * @return entity
     */
    public PaymentEntity isValidEntity(Payment item) throws ValidationException {

        //mandatory fields check
        if(isNullOrBlank(item.getPayment_Id()) || isNullOrBlank(item.getOrder_Id())
                || isNullOrBlank(item.getPayment_Date()) || isNullOrBlank(item.getPayment_Description())
                || isNullOrBlank(item.getPayment_Amount())) {
            String errorMessage = String.format(MessageConstants.BLANK_RECORD_MSG,"Payment");
            throw new ValidationException(errorMessage);
        }

        PaymentEntity payment = null;
        String paymentDescription = item.getPayment_Description();

        //payment date format validity check.
        Date paymentDate = null;
        try {
            String dtStr = item.getPayment_Date();
            paymentDate = AppConstants.DISPLAY_DATE_FORMATTER.parse(dtStr);
        } catch (ParseException e) {
            String errorMessage = String.format(MessageConstants.DATE_PARSING_EXCEPTION,"Payment");
            throw new ValidationException(errorMessage);
        }

        try {
            int paymentId = Integer.valueOf(item.getPayment_Id());
            int orderId = Integer.valueOf(item.getOrder_Id());
            float paymentAmount = Float.valueOf(item.getPayment_Amount());

            payment = ConversionUtil.getPayment(paymentId,orderId,paymentDate,paymentDescription,paymentAmount);
        } catch (NumberFormatException e) {
            String errorMessage = String.format(MessageConstants.NUMBER_PARSING_EXCEPTION,"Payment");
            throw new ValidationException(errorMessage);
        }

        return payment;
    }
}
