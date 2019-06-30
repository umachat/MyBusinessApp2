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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class FragmentCreateOrder extends Fragment {

    private static final String TAG = "FragmentCreateOrder";

    private DBHelperOrders mydb;

    private TextView lblClientName,lblOrderDate,lblOrderBalance;
    private EditText title,desc,orderAmt,amtPaid;
    private Button btnSave,btnReset;
    private Spinner spClient;

    private int clientId;
    private int selectedCategory;
    private Date date;
    private int orderId;
    private ArrayList<CategoryEntity> categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        final View view = inflater.inflate(R.layout.fragment_create_order, container, false);
        mydb = new DBHelperOrders(getContext());

        populateCategoryDropdown( view);

        lblClientName = view.findViewById(R.id.lbl_client_name);
        lblOrderDate = view.findViewById(R.id.lbl_order_date);
        lblOrderBalance = view.findViewById(R.id.lbl_order_balance);
        title = view.findViewById(R.id.txt_title);
        desc = view.findViewById(R.id.txt_desc);
        orderAmt = view.findViewById(R.id.txt_order_amount);
        amtPaid = view.findViewById(R.id.txt_order_advance);
        btnSave = view.findViewById(R.id.btn_order_save);
        btnReset = view.findViewById(R.id.btn_order_reset);

        //add btn listeners
        save();
        reset();

        return view;
    }

    private void populateCategoryDropdown(View view) {
        Cursor res = mydb.getCategories();
        categories = DBUtil.populateOrderCategory(res);
        //spinner
        ArrayAdapter<CategoryEntity> adapter = new ArrayAdapter<CategoryEntity>(getContext(), android.R.layout.simple_spinner_item, categories);
        spClient = view.findViewById(R.id.sp_order_category);
        spClient.setAdapter(adapter);

        spClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategory = categories.get(position).getId();
                   // Toast.makeText(getContext(),"selectedCategory "+selectedCategory, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: ");
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString("clientId")) {
            String strClientId = bundle.getString("clientId");
            clientId = Integer.parseInt(strClientId);
            Log.d(TAG, "clientId " + clientId);

            Cursor cursor = mydb.getClientById(clientId);
            ClientEntity cli = DBUtil.populateClient(cursor);
            lblClientName.setText(cli.getName());

            if (null != bundle.getString("orderId")) {
                getActivity().setTitle("Edit OrderEntity");

                String strOrderId = bundle.getString("orderId");
                orderId = Integer.parseInt(strOrderId);
                Log.d(TAG, "orderId " + orderId);

                Cursor res = mydb.getOrderById(orderId);
                OrderEntity order = DBUtil.populateOrder(res);
                populateValues( order);
                //disable advance
                amtPaid.setFocusable(false);
                amtPaid.setEnabled(false);

            } else {
                getActivity().setTitle("Create New OrderEntity");
                lblOrderDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(new Date()));
                lblOrderBalance.setText(UIUtil.getIndianRupee(0F));
            }
        }

    }

    public void save() {
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String strTitle = title.getText().toString();
                        String strDesc = desc.getText().toString();
                        String strAmount = orderAmt.getText().toString();
                        String strAdvance = amtPaid.getText().toString();

                        if(selectedCategory==0) {
                            Toast.makeText(getContext(),"Please select OrderEntity CategoryEntity!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(null==strTitle || strTitle.trim().length()==0) {
                            Toast.makeText(getContext(),"OrderEntity Title can not be blank!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(null==strDesc || strDesc.trim().length()==0) {
                            Toast.makeText(getContext(),"OrderEntity Description can not be blank!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(null==strAmount || strAmount.trim().length()==0) {
                            Toast.makeText(getContext(),"OrderEntity Amount can not be blank!", Toast.LENGTH_SHORT).show();
                            return;
                        } else  if(!Pattern.matches(AppConstants.NUMERIC_REGEX, strAmount)) {
                            Toast.makeText(getContext(),"OrderEntity Amount should be numeric!", Toast.LENGTH_SHORT).show();
                            return;
                        }else  if(!Pattern.matches(AppConstants.NUMERIC_REGEX, strAdvance)) {
                            Toast.makeText(getContext(),"Advance Paid should be numeric!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //if advance is blank then make it 0.
                        if(strAdvance==null || strAdvance.trim().isEmpty()) {
                            strAdvance = "0";
                        }

                        Float amount = Float.parseFloat(strAmount);
                        Float advance = Float.parseFloat(strAdvance);

                        if(advance > amount) {
                            Toast.makeText(getContext(),"Advance paid can not be greater than total order amount!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int createdId = -1;

                        if(orderId>0) {
                            Cursor res = mydb.getPaymentByOrderId(orderId);

                            //if total payments made crosses initial amount
                            float totalOrderPayment = DBUtil.getAmount(res);
                            amount = amount > totalOrderPayment ? amount : totalOrderPayment;

                            createdId =  mydb.updateOrder(amount, advance, strTitle, strDesc, orderId,selectedCategory);
                        } else{
                            createdId = mydb.insertOrder(clientId, selectedCategory,amount,advance,strTitle,strDesc);
                            orderId = createdId;
                        }

                        if(createdId > 0) {
                            Toast.makeText(getContext(),"Successfully saved data!", Toast.LENGTH_SHORT).show();
                            NavigationUtil.viewOrders(clientId,getFragmentManager());
                        } else {
                            Toast.makeText(getContext(),"Error inserting data!", Toast.LENGTH_SHORT).show();
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

    private void resetValues() {
        spClient.setSelection(0);
        selectedCategory = 0;
        orderAmt.setText("0.0");
        amtPaid.setText("0.0");
        lblOrderBalance.setText(UIUtil.getIndianRupee(0F));
        title.setText("");
        desc.setText("");
    }

    private void populateValues(OrderEntity order) {
        CategoryEntity cat = DBUtil.populateCategory(mydb.getCategoryById(order.getCatId()));
        if (cat != null) {
            spClient.setSelection(categories.indexOf(cat));
        }
        orderAmt.setText(String.valueOf(order.getAmt()));
        amtPaid.setText(String.valueOf(order.getPaid()));
        lblOrderBalance.setText(UIUtil.getIndianRupee(order.getAmt() - order.getPaid()));
        title.setText(order.getTitle());
        desc.setText(order.getDesc());
        lblOrderDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(order.getCreateDate()));
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }
}
