package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperPayments;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;

public class FragmentViewPayment extends Fragment {

    public static final String TAG = "FragmentViewPayment";

    private TextView lblClientName,lblPaymentDate,lblPaymentId,desc,paymentAmt;
    private AlertDialog.Builder  builder;

    private DBHelperPayments mydb;
    private int paymentId;
    private int orderId;
    private int clientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_view_payment, container, false);
        mydb = new DBHelperPayments(getContext());

        lblClientName = view.findViewById(R.id.lbl_client_name_v_pay);
        lblPaymentDate = view.findViewById(R.id.lbl_pay_date_v_pay);
        lblPaymentId = view.findViewById(R.id.lbl_pay_id_v);
        desc = view.findViewById(R.id.lbl_desc_v_pay);
        paymentAmt = view.findViewById(R.id.lbl_pay_amount_v);

        addConfirmDialog();

        registerBtnListeners( view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("View PaymentEntity Details");

        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString("paymentId")) {

            String strPaymentId =bundle.getString("paymentId");
            paymentId = Integer.parseInt(strPaymentId);
            Log.d(TAG, "paymentId " + paymentId);

            String strOrderId =bundle.getString("orderId");
            orderId = Integer.parseInt(strOrderId);
            Log.d(TAG, "orderId " + orderId);

            Cursor cursor = mydb.getOrderById( orderId);
            OrderEntity order = DBUtil.populateOrder(cursor);

            clientId = order.getClientId();
            Cursor res = mydb.getClientById( clientId);
            ClientEntity cli = DBUtil.populateClient(res);
            lblClientName.setText(cli.getName());

            Cursor cursor2 = mydb.getPaymentById(paymentId);
            PaymentEntity payment = DBUtil.populatePayment(cursor2);


            paymentAmt.setText(UIUtil.getIndianRupee(payment.getAmount()));
            lblPaymentId.setText("PaymentEntity ID : "+String.valueOf(paymentId));
            desc.setText(payment.getPaymentDesc());
            lblPaymentDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(payment.getPaymentDate()));


        }
    }

    private void registerBtnListeners(View view) {
        //buttons
        Button submitBtn = (Button) view.findViewById(R.id.btn_edit_pay);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.editPayments(orderId,paymentId,getFragmentManager());
                    }
                }
        );

        Button deleteBtn = (Button) view.findViewById(R.id.btn_delete_pay);
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        builder.show();
                    }
                }
        );

        Button orderBtn = (Button) view.findViewById(R.id.btn_view_ord_pay);
        orderBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.viewOrder(orderId,getFragmentManager());
                    }
                }
        );
    }


    private void addConfirmDialog() {
        //confirm dialog for delete
        builder = UIUtil.getConfirmDialog(getContext(),"You are about to delete data for this payment. proceed ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = mydb.deletePayment(paymentId);
                if(isDeleted) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", String.valueOf(orderId));
                    FragmentViewPayments mfragment = new FragmentViewPayments();
                    UIUtil.refreshFragment(mfragment,bundle, getFragmentManager());
                } else {
                    Toast.makeText(getContext(),"Error deleting data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }

}
