package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperClients;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;

import java.util.ArrayList;
//CURRENTLY NOT USED
public class FragmentMain extends Fragment {

    private static final String TAG = "FragmentMain";
    private Button viewClientBtn,registerBtn,createOrderBtn,viewOrdersBtn;

    private int selectedClientId = -1;
    private DBHelperClients mydb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        mydb = new DBHelperClients(getContext());
        Cursor res = mydb.getClients();
        final  ArrayList<CategoryEntity>  clients = DBUtil.populateClients(res);
        //auto complete
        int layoutItemId = android.R.layout.simple_list_item_1;
        final ArrayAdapter<CategoryEntity> adapter = new ArrayAdapter<>(getContext(), layoutItemId, clients);

        AutoCompleteTextView autocompleteView =
                view.findViewById(R.id.autocompleteView);
        autocompleteView.setAdapter(adapter);


        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof CategoryEntity){
                    CategoryEntity cat=(CategoryEntity) item;
                    selectedClientId = cat.getId();
                  //  Toast.makeText(view.getContext(), String.valueOf(selectedClientId),Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewClientBtn =  (Button) view.findViewById(R.id.btn_view_client);
        registerBtn =  (Button) view.findViewById(R.id.btn_register);
        createOrderBtn =  (Button) view.findViewById(R.id.btn_create_orders);
        viewOrdersBtn =  (Button) view.findViewById(R.id.btn_view_orders);

        //listeners
        registerBtnListeners( view);

        return view;
    }

    private void registerBtnListeners(final View view) {
        viewClientBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        viewClient(view);
                    }
                }
        );

        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        register(view);
                    }
                }
        );

        createOrderBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        createOrder(view);
                    }
                }
        );

        viewOrdersBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        viewOrders(view);
                    }
                }
        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Search");
    }

    public void viewClient(View view) {
        Log.d(TAG, " selectedClientId "+selectedClientId);
        //navigate
        if(selectedClientId==-1) {
            Toast.makeText(getContext(), "Please select a client", Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentViewClient mfragment = new FragmentViewClient();
        UIUtil.refreshFragment(mfragment, "clientId",String.valueOf(selectedClientId), getFragmentManager());
    }

    public void viewOrders(View view) {
        Log.d(TAG, " selectedClientId "+selectedClientId);
        //navigate
        if(selectedClientId==-1) {
            Toast.makeText(getContext(), "Please select a client", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentViewOrders mfragment = new FragmentViewOrders();
        UIUtil.refreshFragment(mfragment, "clientId",String.valueOf(selectedClientId), getFragmentManager());
    }

    public void register(View view) {
        FragmentCreateClient mfragment = new FragmentCreateClient();
        UIUtil.refreshFragment(mfragment, getFragmentManager());
    }

    public void createOrder(View view) {
        Log.d(TAG, " selectedClientId "+selectedClientId);
        //navigate
        if(selectedClientId==-1) {
            Toast.makeText(getContext(), "Please select a client", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentCreateOrder mfragment = new FragmentCreateOrder();
        UIUtil.refreshFragment(mfragment, "clientId",String.valueOf(selectedClientId), getFragmentManager());

    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }
}
