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

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperOrders;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;

public class FragmentViewOrder extends Fragment {

    public static final String TAG = "FragmentViewOrder";

    private TextView lblClientName,lblOrderDate,lblOrderBalance,title,desc,orderAmt,amtPaid,lblOrderCategory;
    private AlertDialog.Builder  builder;

    private DBHelperOrders mydb;
    private int orderId;
    private int clientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        mydb = new DBHelperOrders(getContext());

        lblClientName = view.findViewById(R.id.lbl_client_name_v);
        lblOrderDate = view.findViewById(R.id.lbl_order_date_v);
        lblOrderBalance = view.findViewById(R.id.lbl_order_balance_v);
        title = view.findViewById(R.id.lbl_title_v);
        desc = view.findViewById(R.id.lbl_desc_v);
        orderAmt = view.findViewById(R.id.lbl_order_amount_v);
        amtPaid = view.findViewById(R.id.lbl_order_advance_v);
        lblOrderCategory = view.findViewById(R.id.lbl_order_category_v);

        regsterBtnListeners(view);

        addConfirmDalogs();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("View OrderEntity Details");

        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString("orderId")) {

           String strOrderId =bundle.getString("orderId");
            orderId = Integer.parseInt(strOrderId);
            Log.d(TAG, "orderId " + orderId);

            Cursor cursor = mydb.getOrderById( orderId);
            OrderEntity order = DBUtil.populateOrder(cursor);

            CategoryEntity cat = DBUtil.populateCategory(mydb.getCategoryById(order.getCatId()));
            if (cat != null) {
                lblOrderCategory.setText(cat.getName());
            }
            orderAmt.setText(UIUtil.getIndianRupee(order.getAmt()));
            amtPaid.setText(UIUtil.getIndianRupee(order.getPaid()));
            lblOrderBalance.setText(UIUtil.getIndianRupee(order.getAmt() - order.getPaid()));
            title.setText(order.getTitle());
            desc.setText(order.getDesc());
            lblOrderDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(order.getCreateDate()));

            clientId = order.getClientId();
            Cursor res = mydb.getClientById( clientId);
            ClientEntity cli = DBUtil.populateClient(res);
            lblClientName.setText(cli.getName());
        }
    }

    private void regsterBtnListeners(View view) {
        //links
        lblClientName.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.viewClient(clientId,getFragmentManager());
                    }
                }
        );

        //buttons
        Button submitBtn = (Button) view.findViewById(R.id.btn_edit);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.editOrder(clientId,orderId,getFragmentManager());
                    }
                }
        );

        Button cancelBtn = (Button) view.findViewById(R.id.btn_delete);
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        builder.show();
                    }
                }
        );

        Button paymentBtn = (Button) view.findViewById(R.id.btn_payment);
        paymentBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.viewPayments(orderId,getFragmentManager());
                    }
                }
        );

        Button ctrpaymentBtn = (Button) view.findViewById(R.id.btn_create_payment);
        ctrpaymentBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.createPayments(orderId,getFragmentManager());
                    }
                }
        );
    }

    private void addConfirmDalogs() {
        //confirm dialog for delete
        builder = UIUtil.getConfirmDialog(getContext(),"You are about to delete data for this order. proceed ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = mydb.deleteOrderById(orderId);
                if(isDeleted) {
                    NavigationUtil.viewOrders(clientId,getFragmentManager());
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
