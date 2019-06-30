package com.mybusinessapp.example.madhu.mybusinessapp2.vo;

import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;

import java.util.Date;

/**
 * PaymentEntity vo.
 */
public class PaymentEntity extends AbstractEntity {
    private int paymentId;
    private int orderId;
    private Date paymentDate;
    private float amount;
    private String paymentDesc;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }
}
