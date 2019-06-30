package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

/**
 * The client VO used in excel report.
 */
public class Client extends ReportVO {

    private String client_Id;
    private String client_Name;
    private String registration_Date;
    private String phone1;
    private String phone2;
    private String email;
    private String address;


    public String getClient_Id() {
        return client_Id;
    }

    public void setClient_Id(String client_Id) {
        this.client_Id = client_Id;
    }

    public String getClient_Name() {
        return client_Name;
    }

    public void setClient_Name(String client_Name) {
        this.client_Name = client_Name;
    }

    public String getRegistration_Date() {
        return registration_Date;
    }

    public void setRegistration_Date(String registration_Date) {
        this.registration_Date = registration_Date;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
