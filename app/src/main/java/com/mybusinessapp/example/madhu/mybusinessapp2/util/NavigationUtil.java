package com.mybusinessapp.example.madhu.mybusinessapp2.util;

import android.support.v4.app.Fragment;
import  android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentCategory;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentCreateClient;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentCreateOrder;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentCreatePayment;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentSearchClient;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewClient;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewOrder;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewOrders;
import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewPayments;


public class NavigationUtil {

    private static final String TAG = "NavigationUtil";

    /************ CLIENT **************/

    public static void viewClient(int clientId, FragmentManager mgr) {
        FragmentViewClient mfragment = new FragmentViewClient();
        UIUtil.refreshFragment( mfragment, "clientId",String.valueOf(clientId), mgr);
    }

    public static void editClient(int clientId, FragmentManager mgr) {
        FragmentCreateClient mfragment = new FragmentCreateClient();
        UIUtil.refreshFragment( mfragment, "clientId",String.valueOf(clientId), mgr);
    }

    public static void searchClients(FragmentManager mgr) {
        Fragment fragment = new FragmentSearchClient();
        UIUtil.refreshFragment(fragment,mgr);
    }

    /************ ORDER **************/

    public static void viewOrders(int clientId, FragmentManager mgr) {
        FragmentViewOrders mfragment = new FragmentViewOrders();
        UIUtil.refreshFragment( mfragment, "clientId",String.valueOf(clientId), mgr);
    }

    public static void viewOrder(int orderId, FragmentManager mgr) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", String.valueOf(orderId));
        FragmentViewOrder mfragment = new FragmentViewOrder();
        UIUtil.refreshFragment(mfragment,bundle,mgr);
    }

    public static void createOrder(int clientId, FragmentManager mgr) {
        Bundle bundle = new Bundle();
        bundle.putString("clientId", String.valueOf(clientId));
        FragmentCreateOrder mfragment = new FragmentCreateOrder();
        UIUtil.refreshFragment(mfragment,bundle, mgr);
    }

    public static void editOrder(int clientId,int orderId,FragmentManager mgr) {
        Bundle bundle = new Bundle();
        bundle.putString("clientId", String.valueOf(clientId));
        bundle.putString("orderId", String.valueOf(orderId));
        FragmentCreateOrder mfragment = new FragmentCreateOrder();
        UIUtil.refreshFragment(mfragment,bundle, mgr);
    }


    /************ PAYMENT **************/

    public  static void createPayments(int orderId, FragmentManager mgr) {
        FragmentCreatePayment mfragment = new FragmentCreatePayment();
        UIUtil.refreshFragment( mfragment, "orderId",String.valueOf(orderId), mgr);
    }

    public  static void editPayments(int orderId,int paymentId, FragmentManager mgr) {
        Bundle bundle = new Bundle();
        bundle.putString("paymentId", String.valueOf(paymentId));
        bundle.putString("orderId", String.valueOf(orderId));
        FragmentCreatePayment mfragment = new FragmentCreatePayment();
        UIUtil.refreshFragment(mfragment,bundle, mgr);
    }

    public static void viewPayments(int orderId, FragmentManager mgr) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", String.valueOf(orderId));
        FragmentViewPayments mfragment = new FragmentViewPayments();
        UIUtil.refreshFragment(mfragment,bundle, mgr);
    }


    /************ ORDER CATEGORY **************/

    public static void createCategories(FragmentManager mgr) {
        Fragment fragment = new FragmentCategory();
        UIUtil.refreshFragment(fragment,mgr);
    }


}
