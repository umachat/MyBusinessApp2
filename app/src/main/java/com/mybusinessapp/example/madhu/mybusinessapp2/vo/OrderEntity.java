package com.mybusinessapp.example.madhu.mybusinessapp2.vo;

import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;

import java.util.Date;

public class OrderEntity extends AbstractEntity {

    private int orderId;
    private int clientId;
    private int catId;
    private float amt;
    private float paid;
    private String title;
    private String desc;
    private Date createDate;
    private String catName;
    private String cientName;

    public OrderEntity() {
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCientName() {
        return cientName;
    }

    public void setCientName(String cientName) {
        this.cientName = cientName;
    }
}
