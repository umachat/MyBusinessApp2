package com.mybusinessapp.example.madhu.mybusinessapp2.vo;

/**
 * ClientEntity pojo
 */
public class ClientEntity {

    private String id;
    private String name;
    private String phone1;
    private String phone2;
    private String email;
    private String address;
    private String regisDate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRegisDate() {
        return regisDate;
    }

    public void setRegisDate(String regisDate) {
        this.regisDate = regisDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
