package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.reports.FileUtil;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Exports DB in excel format.
 */
public class FragmentExport extends Fragment {

    private Button writeExcelButton;
    private FileUtil excelUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_export, container, false);
        excelUtil = new FileUtil();
        writeExcelButton = (Button) view.findViewById(R.id.btn_export);
        writeExcelButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        writeExcel(v);
                    }
                }
        );


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Export");
    }

    public void writeExcel(View v)
    {
        if(!checkPermission()) {
           // Toast.makeText(v.getContext(),"permission  available!", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(v.getContext(),"Processing your request! Please wait", Toast.LENGTH_SHORT).show();
                excelUtil.saveExcelFile(v.getContext());
                Toast.makeText(v.getContext(), "Successfully exported file!",Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(v.getContext(), "There was an error exporting file!",Toast.LENGTH_SHORT).show();
            }
        } else {
           // Toast.makeText(v.getContext(),"permission not available!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).requestPermission();
            if( ((MainActivity)getActivity()).getPermissionGranted()) {
                try {
                    Toast.makeText(v.getContext(),"Processing your request! Please wait", Toast.LENGTH_SHORT).show();
                    excelUtil.saveExcelFile(v.getContext());
                    Toast.makeText(v.getContext(), "Successfully exported file!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(v.getContext(), "There was an error exporting file!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }


}
