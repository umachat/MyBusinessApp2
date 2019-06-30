package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;

import java.util.Date;

/**
 * This helper class contains methods which accept one report vo and converts to a DB objct. And vice versa.
 */
public class ConversionUtil {

    /**
     *This method accepts a single report vo object and converts it into a single db object.
     */
    public static CategoryEntity getCategory(int id, String name) {
        CategoryEntity c = new CategoryEntity(id,name);
        return c;
    }

    /**
     *This method accepts a single report vo object and converts it into a single db object.
     */
    public static PaymentEntity getPayment(int paymentId, int orderId, Date paymentDate, String paymentDescription, float paymentAmount) {
        PaymentEntity p = new PaymentEntity();
        p.setPaymentId(paymentId);
        p.setOrderId(orderId);
        p.setAmount(paymentAmount);
        p.setPaymentDesc(paymentDescription);
        p.setPaymentDate(paymentDate);
        return p;
    }

    /**
     *This method accepts a single report vo object and converts it into a single db object.
     */
    public static OrderEntity getOrder(int orderId, int clientId, int categoryId, Date orderCreationDate, String orderTitle, String orderDescription, float orderAmount, float advancePaid) {
        OrderEntity o = new OrderEntity();
        o.setOrderId(orderId);
        o.setCatId(categoryId);
        o.setClientId(clientId);
        o.setTitle(orderTitle);
        o.setDesc(orderDescription);
        o.setAmt(orderAmount);
        o.setPaid(advancePaid);
        o.setCreateDate(orderCreationDate);
        return o;
    }

    /**
     *This method accepts a single report vo object and converts it into a single db object.
     */
    public static ClientEntity getClient(int id, String name, Date regisDate, String phone1, String phone2, String email, String address) {
        ClientEntity c = new ClientEntity();
        c.setId(String.valueOf(id));
        c.setName(name);
        c.setRegisDate(AppConstants.DB_DATE_FORMAT.format(regisDate));
        c.setPhone1(phone1);
        c.setPhone2(phone2);
        c.setEmail(email);
        c.setAddress(address);
        return c;
    }

    /**
     *This method accepts a single Client db object and converts it into a single Client report vo object.
     */
    public static  Client getClientReportVo(int client_Id,String client_Name,Date registration_Date,String phone1,String phone2,String email,String address) {
        Client c = new Client();
        c.setClient_Id(String.valueOf(client_Id));
        c.setClient_Name(client_Name);
        c.setRegistration_Date(ReportConstants.REPORT_DISPLAY_FORMAT.format(registration_Date));
        c.setPhone1(phone1);
        c.setPhone2(phone2);
        c.setEmail(email);
        c.setAddress(address);
        return c;
    }

    /**
     *This method accepts a single Category db object and converts it into a single Category report vo object.
     */
    public static  Category getCategoryReportVo(int category_Id,String category_Name) {
        Category c = new Category();
        c.setCategory_Id(String.valueOf(category_Id));
        c.setCategory_Name(category_Name);
        return c;
    }

    /**
     *This method accepts a single Order db object and converts it into a single Order report vo object.
     */
    public static  Order getOrderReportVo(int order_Id,int client_Id,int category_Id,Date order_creation_date,String order_title,String order_description,float order_amount,float advance_paid) {
        Order o = new Order();
        o.setOrder_Id(String.valueOf(order_Id));
        o.setCategory_Id(String.valueOf(category_Id));
        o.setClient_Id(String.valueOf(client_Id));
        o.setOrder_Title(order_title);
        o.setOrder_Description(order_description);
        o.setOrder_Amount(String.valueOf(order_amount));
        o.setAdvance_Paid(String.valueOf(advance_paid));
        o.setOrder_Creation_Date(ReportConstants.REPORT_DISPLAY_FORMAT.format(order_creation_date));
        return o;
    }

    /**
     *This method accepts a single Payment db object and converts it into a single Payment report vo object.
     */
    public static  Payment getPaymentReportVo(int payment_Id,int order_Id, Date payment_date,String payment_description,float payment_amount) {
        Payment p = new Payment();
        p.setPayment_Id(String.valueOf(payment_Id));
        p.setOrder_Id(String.valueOf(order_Id));
        p.setPayment_Amount(String.valueOf(payment_amount));
        p.setPayment_Description(payment_description);
        p.setPayment_Date(ReportConstants.REPORT_DISPLAY_FORMAT.format(payment_date));
        return p;
    }
}
