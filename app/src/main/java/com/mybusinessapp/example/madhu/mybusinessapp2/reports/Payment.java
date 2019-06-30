package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

/**
 * The payment VO used in excel report.
 */
public class Payment extends ReportVO {

    private String payment_Id;
    private String order_Id;
    private String payment_Date;
    private String payment_Description;
    private String payment_Amount;

    public String getPayment_Id() {
        return payment_Id;
    }

    public void setPayment_Id(String payment_Id) {
        this.payment_Id = payment_Id;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getPayment_Date() {
        return payment_Date;
    }

    public void setPayment_Date(String payment_Date) {
        this.payment_Date = payment_Date;
    }

    public String getPayment_Description() {
        return payment_Description;
    }

    public void setPayment_Description(String payment_Description) {
        this.payment_Description = payment_Description;
    }

    public String getPayment_Amount() {
        return payment_Amount;
    }

    public void setPayment_Amount(String payment_Amount) {
        this.payment_Amount = payment_Amount;
    }
}
