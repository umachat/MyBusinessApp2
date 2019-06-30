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

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperClients;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.ClientEntity;

/**
 * View details of a client.
 */
public class FragmentViewClient extends Fragment {

    public static final String TAG = "FragmentViewClient";

    private  TextView txtName,txtPhone1,txtPhone2,txtEmail,txtAddress,txtRegisDate;
    AlertDialog.Builder  builder;

    private DBHelperClients mydb;
    private int clientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        final View view = inflater.inflate(R.layout.fragment_view_client, container, false);
        mydb = new DBHelperClients(getContext());
        txtName = view.findViewById(R.id.txtViewName);
        txtPhone1 = view.findViewById(R.id.txtViewPhone1);
        txtPhone2 = view.findViewById(R.id.txtViewPhone2);
        txtEmail = view.findViewById(R.id.txtViewEmail);
        txtAddress = view.findViewById(R.id.txtViewAddress);
        txtRegisDate = view.findViewById(R.id.txtViewDate);

        registerBtnListeners(view);

        addConfirmDialogs();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("View ClientEntity Details");

        Bundle bundle=getArguments();
        if(bundle!=null && null!=bundle.getString("clientId")) {

           String strClientId =bundle.getString("clientId");
            clientId = Integer.parseInt(strClientId);

            Log.d(TAG, "clientId " + clientId);
            Cursor cursor = mydb.getClientById( clientId);
            ClientEntity cli = DBUtil.populateClient(cursor);

            txtName.setText(cli.getName());
            Log.d(TAG, "name is " + cli.getName());
            txtPhone1.setText(cli.getPhone1());
            txtPhone2.setText(cli.getPhone2());
            txtEmail.setText(cli.getEmail());
            txtAddress.setText(cli.getAddress());
            txtRegisDate.setText(cli.getRegisDate());
        }
    }

    private void registerBtnListeners(View view) {
        //buttons
        Button submitBtn = (Button) view.findViewById(R.id.btn_edit);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.editClient(clientId,getFragmentManager());
                    }
                }
        );

        Button deleteBtn = (Button) view.findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        builder.show();
                    }
                }
        );

        Button orderBtn = (Button) view.findViewById(R.id.btn_view_ord);
        orderBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.viewOrders(clientId,getFragmentManager());
                    }
                }
        );

        Button crtorderBtn = (Button) view.findViewById(R.id.btn_create_ord);
        crtorderBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        NavigationUtil.createOrder(clientId,getFragmentManager());
                    }
                }
        );
    }

    private void addConfirmDialogs() {
        //confirm dialog for delete
        builder = UIUtil.getConfirmDialog(getContext(),"You are about to delete data for this client. proceed ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = mydb.deleteDataByClientId(clientId);
                if(isDeleted) {
                    NavigationUtil.searchClients(getFragmentManager());
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
