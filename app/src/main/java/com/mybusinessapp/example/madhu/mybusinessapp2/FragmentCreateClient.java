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
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperClients;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;

import java.util.regex.Pattern;

public class FragmentCreateClient extends Fragment {

    private static final String TAG = "FragmentCreateClient";
    private final String EDIT_TITLE = "Edit Client Details";
    private final String CREATE_TITLE = "Register New Client";
    private final String CLIENT_ID = "clientId";

    private EditText txtName,txtEmail,txtPhone1,txtPhone2,txtAddress;

    private DBHelperClients mydb;
    private int clientId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_client, container, false);
        mydb = new DBHelperClients(getContext());
        txtName = (EditText) view.findViewById(R.id.txt_name);
        txtEmail = (EditText) view.findViewById(R.id.txt_email);
        txtPhone1 = (EditText) view.findViewById(R.id.txt_phone1);
        txtPhone2 = (EditText) view.findViewById(R.id.txt_phone2);
        txtAddress = (EditText) view.findViewById(R.id.txt_address);

        regsterBtnListeners(  view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString(CLIENT_ID)) {
            String strClientId = bundle.getString(CLIENT_ID);
            clientId = Integer.parseInt(strClientId);
            Log.d(TAG, "clientId " + clientId);

            Cursor cursor = mydb.getClientById( clientId);
            ClientEntity cli = DBUtil.populateClient(cursor);

            getActivity().setTitle(EDIT_TITLE);

            txtName.setText(cli.getName());
            txtEmail.setText(cli.getEmail());
            txtPhone1.setText(cli.getPhone1());
            txtPhone2.setText(cli.getPhone2());
            txtAddress.setText(cli.getAddress());

        } else {
            getActivity().setTitle(CREATE_TITLE);
        }


    }

    public void submitSave(View view) {

        String strName = txtName.getText().toString();
        String strEmail = txtEmail.getText().toString();
        String strPhone1 = txtPhone1.getText().toString();
        String strPhone2 = txtPhone2.getText().toString();
        String strAddress = txtAddress.getText().toString();

        strName = strName!=null ? strName.trim() : strName;
        strEmail = strEmail!=null ? strEmail.trim() : strEmail;
        strPhone1 = strPhone1!=null ? strPhone1.trim() : strPhone1;
        strPhone2 = strPhone2!=null ? strPhone2.trim() : strPhone2;
        strAddress = strAddress!=null ? strAddress.trim() : strAddress;

        if(!UIUtil.isBlank(strName)) {
            Toast.makeText(getContext(), MessageConstants.BLANK_NAME_MSG, Toast.LENGTH_SHORT).show();
            return;
        }else if(!UIUtil.isBlank(strPhone1)) {
            Toast.makeText(getContext(),MessageConstants.BLANK_PRIMARY_PH_MSG, Toast.LENGTH_SHORT).show();
            return;
        } else if( !Pattern.matches(AppConstants.PHONE_REGEX, strPhone1)) {
            Toast.makeText(getContext(),MessageConstants.INVALID_PH_MSG, Toast.LENGTH_SHORT).show();
            return;
        }  else if(clientId==0 && mydb.getClientByPrimaryPhone(strPhone1).getCount()>0) {
            Toast.makeText(getContext(),MessageConstants.DUPLICATE_CLIENT_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        else if(UIUtil.isBlank(strPhone2) && !Pattern.matches(AppConstants.PHONE_REGEX, strPhone2)) {
            Toast.makeText(getContext(),MessageConstants.INVALID_SECONDARY_PH_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        else if(UIUtil.isBlank(strEmail) && !Pattern.matches(AppConstants.EMAIL_REGEX, strEmail)) {
            Toast.makeText(getContext(),MessageConstants.INVALID_EMAIL_MSG, Toast.LENGTH_SHORT).show();
            return;
        }

        String address = "";
        if(strAddress!=null && strAddress.trim().length()>0) {
            address = strAddress;
        }
        int createdId = -1;

        if(clientId>0) {
            createdId =  mydb.updateClient( strName,  strEmail,  strPhone1,  strPhone2,  address,clientId);
        } else{
            createdId = mydb.insertClient( strName,  strEmail,  strPhone1,  strPhone2,  address) ;
            clientId = createdId;
        }

        if(createdId > 0) {
            Toast.makeText(getContext(),MessageConstants.SAVE_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
            NavigationUtil.viewClient(clientId,getFragmentManager());
        } else {
            Toast.makeText(getContext(),MessageConstants.DELETE_ERROR_MSG, Toast.LENGTH_SHORT).show();
        }

    }

    private void regsterBtnListeners(final View view) {
        Button submitBtn = (Button) view.findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        submitSave(view);
                    }
                }
        );

        Button btnReset = view.findViewById(R.id.btn_client_reset);
        btnReset.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        reset();
                    }
                }
        );
    }

    private void reset() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone1.setText("");
        txtPhone2.setText("");
        txtAddress.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }
}
