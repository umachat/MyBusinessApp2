package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

/**
 * The order VO used in excel report.
 */
public class Order extends ReportVO {

    private String order_Id;
    private String client_Id;
    private String category_Id;
    private String order_Creation_Date;
    private String order_Title;
    private String order_Description;
    private String order_Amount;
    private String advance_Paid;


    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getClient_Id() {
        return client_Id;
    }

    public void setClient_Id(String client_Id) {
        this.client_Id = client_Id;
    }

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public String getOrder_Creation_Date() {
        return order_Creation_Date;
    }

    public void setOrder_Creation_Date(String order_Creation_Date) {
        this.order_Creation_Date = order_Creation_Date;
    }

    public String getOrder_Title() {
        return order_Title;
    }

    public void setOrder_Title(String order_Title) {
        this.order_Title = order_Title;
    }

    public String getOrder_Description() {
        return order_Description;
    }

    public void setOrder_Description(String order_Description) {
        this.order_Description = order_Description;
    }

    public String getOrder_Amount() {
        return order_Amount;
    }

    public void setOrder_Amount(String order_Amount) {
        this.order_Amount = order_Amount;
    }

    public String getAdvance_Paid() {
        return advance_Paid;
    }

    public void setAdvance_Paid(String advance_Paid) {
        this.advance_Paid = advance_Paid;
    }
}
