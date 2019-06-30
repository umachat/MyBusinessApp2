package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mybusinessapp.example.madhu.mybusinessapp2.adapters.ExpandableListAdapterPayments;
import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperPayments;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;

import java.util.HashMap;
import java.util.List;

/**
 * View list of payments by order ID.
 */
public class FragmentViewPayments extends Fragment {

    private static final String TAG = "FragmentViewPayments";

    private ExpandableListView listView;
    private TextView msg,orderTitle;

    private ExpandableListAdapterPayments adapter;

    private DBHelperPayments mydb;
    private int orderId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view =  inflater.inflate(R.layout.fragment_view_payments, container, false);

        msg = view.findViewById(R.id.txt_msg_pay);
        orderTitle = view.findViewById(R.id.lbl_order_id);
        listView =  view.findViewById(R.id.lvexp_pay);

        //error message if empty
        listView.setEmptyView(msg);

        mydb = new DBHelperPayments(getContext());

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("View Payments");

        Bundle bundle = getArguments();
        if(bundle!=null && null!=bundle.getString("orderId")) {

            String strOrderId =bundle.getString("orderId");
            orderId = Integer.parseInt(strOrderId);

            Log.d(TAG, "orderId " + orderId);

            if (0 != orderId) {

                orderTitle.setText("Payments for OrderEntity ID : "+String.valueOf(orderId));
                orderTitle.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                NavigationUtil.viewOrder(orderId, getFragmentManager());
                            }
                        }
                );
                //grid
                setGrid(view);
            }

        }
    }

    private HashMap<String, List<AbstractEntity>> getData() {
       Cursor cursor = mydb.getPaymentByOrderId(orderId);
       List<AbstractEntity> payments = DBUtil.populatePayments( cursor);
       HashMap<String, List<AbstractEntity>> data = new HashMap<>();
       data.put(String.valueOf(orderId),payments);
       return data;
    }

    private void setGrid(View view) {

        HashMap<String, List<AbstractEntity>> data = getData();

        if(data.isEmpty()) {
            Log.d(TAG, "no payments found for this order " + orderId);
            return;
        }

        adapter = new ExpandableListAdapterPayments(view.getContext(), data);
        listView.setAdapter(adapter);
        //scrollable
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                UIUtil.setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        //expand by default
        for(int i=0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }
}
