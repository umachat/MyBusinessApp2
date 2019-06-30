package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperPayments;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;

import java.util.Date;
import java.util.regex.Pattern;

public class FragmentCreatePayment extends Fragment {

    private static final String TAG = "FragmentCreatePayment";

    private final String ORDER_ID = "orderId";
    private final String PAYMENT_ID = "paymentId";
    private final String EDIT_TITLE = "Edit Payment";
    private final String CREATE_TITLE = "Create New Payment";

    private DBHelperPayments mydb;

    private TextView lblClientName,lblPaymentDate,lblOrderId;
    private EditText desc,amt;
    private Button btnSave,btnReset,btnOrder;

    private int paymentId;
    private int orderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_payment, container, false);
        mydb = new DBHelperPayments(getContext());

        lblClientName = view.findViewById(R.id.lbl_client_name_pay);
        lblPaymentDate = view.findViewById(R.id.lbl_cre_pay_date);
        lblOrderId = view.findViewById(R.id.lbl_order_id_pay);
        desc = view.findViewById(R.id.txt_desc_pay);
        amt = view.findViewById(R.id.txt_cre_pay_amount);

        btnSave = view.findViewById(R.id.btn_pay_save);
        btnReset = view.findViewById(R.id.btn_pay_reset);
        btnOrder = view.findViewById(R.id.btn_view_ord_pay2);

        //add btn listeners
        save();
        reset();
        viewOrder();

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString(ORDER_ID)) {
            String strOrderId = bundle.getString(ORDER_ID);
            orderId = Integer.parseInt(strOrderId);
            Log.d(TAG, "orderId " + orderId);

            Cursor cursor = mydb.getOrderById(orderId);
            OrderEntity order = DBUtil.populateOrder(cursor);
            orderId = order.getOrderId();
            lblOrderId.setText("Payments for Order ID : "+String.valueOf(orderId));
            Cursor res = mydb.getClientById(order.getClientId());
            ClientEntity cli = DBUtil.populateClient(res);
            lblClientName.setText(cli.getName());

            if (null != bundle.getString(PAYMENT_ID)) {
                getActivity().setTitle(EDIT_TITLE);

                String strPaymentId = bundle.getString(PAYMENT_ID);
                paymentId = Integer.parseInt(strPaymentId);
                Log.d(TAG, "paymentId " + paymentId);

                Cursor cursor1 = mydb.getPaymentById(paymentId);
                PaymentEntity payment = DBUtil.populatePayment(cursor1);
                populateValues( payment);


            } else {
                getActivity().setTitle(CREATE_TITLE);
                lblPaymentDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(new Date()));

            }
        }

    }

    public void save() {
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String strDesc = desc.getText().toString();
                        String strAmount = amt.getText().toString();

                       if(null==strDesc || strDesc.trim().length()==0) {
                            Toast.makeText(getContext(),MessageConstants.BLANK_PAYMENT_DESC_MSG, Toast.LENGTH_SHORT).show();
                            return;
                        } else if(null==strAmount || strAmount.trim().length()==0) {
                            Toast.makeText(getContext(),MessageConstants.BLANK_PAYMENT_AMT_MSG, Toast.LENGTH_SHORT).show();
                            return;
                        } else  if(!Pattern.matches(AppConstants.NUMERIC_REGEX, strAmount)) {
                            Toast.makeText(getContext(),MessageConstants.NUMERIC_PAYMENT_AMT_MSG, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Float amount = Float.parseFloat(strAmount);

                        int createdId = -1;

                        if(paymentId>0) {
                            createdId =  mydb.updatePayment(amount,  strDesc, paymentId);
                        } else{
                            createdId = mydb.insertPayment(orderId, amount,strDesc);
                            paymentId = createdId;
                        }

                        if(createdId > 0) {
                            Toast.makeText(getContext(), MessageConstants.SAVE_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
                            NavigationUtil.viewPayments(orderId,getFragmentManager());
                        } else {
                            Toast.makeText(getContext(),MessageConstants.SAVE_ERROR_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    public void reset() {
        btnReset.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        resetValues();
                    }
                }
        );
    }

    public void viewOrder() {
        btnOrder.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.viewPayments(orderId,getFragmentManager());
                    }
                }
        );
    }

    private void resetValues() {
        amt.setText("");
        desc.setText("");
    }

    private void populateValues(PaymentEntity payment) {
        amt.setText(String.valueOf(payment.getAmount()));
        desc.setText(payment.getPaymentDesc());
        lblPaymentDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(payment.getPaymentDate()));
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }

}
