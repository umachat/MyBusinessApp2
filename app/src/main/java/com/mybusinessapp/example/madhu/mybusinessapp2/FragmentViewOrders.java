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

import com.mybusinessapp.example.madhu.mybusinessapp2.adapters.ExpandableListAdapterOrders;
import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperOrders;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;

import java.util.HashMap;
import java.util.List;

/**
 * View list of clients and orders
 */
public class FragmentViewOrders extends Fragment {

    private static final String TAG = "FragmentViewOrders";

    private ExpandableListView listView;
    private ExpandableListAdapterOrders adapter;
    private TextView msg;
    private TextView txt;
    private TextView clientName;

    private DBHelperOrders mydb;
    private int clientId;
    private String cliName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view =  inflater.inflate(R.layout.fragment_view_orders, container, false);

        msg = view.findViewById(R.id.txt_msg);
        msg.setText("No orders found for this client!");
        txt = view.findViewById(R.id.lbl_txt);
        clientName = view.findViewById(R.id.lbl_client_name);
        listView =  view.findViewById(R.id.lvexp);

        //error message if empty
        listView.setEmptyView(msg);

        mydb = new DBHelperOrders(getContext());

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("View Orders");

        Bundle bundle = getArguments();
        if(bundle!=null && null!=bundle.getString("clientId")) {

            String strClientId =bundle.getString("clientId");
            clientId = Integer.parseInt(strClientId);

            Log.d(TAG, "clientId " + clientId);

            if (0 != clientId) {
                Cursor res = mydb.getClientById(clientId);
                ClientEntity cli = DBUtil.populateClient(res);
                cliName = cli.getName();
                Log.d(TAG, "client name is " + cli.getName());

                txt.setText("Orders for ");
                //show client name
                txt.setText(cliName);
                //grid
                setGrid(view);
            }

        }
    }

    private HashMap<String, List<AbstractEntity>> getData() {
       Cursor cursor = mydb.getOrdersByClientId(clientId);
       List<AbstractEntity> orders = DBUtil.populateOrders( cursor);
       HashMap<String, List<AbstractEntity>> data = new HashMap<>();
       data.put(cliName,orders);
       return data;
    }

    private void setGrid(View view) {

        HashMap<String, List<AbstractEntity>> data = getData();

        if(data.isEmpty()) {
            Log.d(TAG, "no orders found for this client " + clientId);
            return;
        }

        adapter = new ExpandableListAdapterOrders(view.getContext(), data);
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
