package com.mybusinessapp.example.madhu.mybusinessapp2.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mybusinessapp.example.madhu.mybusinessapp2.adapters.ExpandableListAdapter;
import com.mybusinessapp.example.madhu.mybusinessapp2.R;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * All UI helper methods here.
 */
public class UIUtil {

    private static final String TAG = "UIUtil";


    public static void refreshFragment(Fragment mfragment, String paramName,String paramValue, FragmentManager mgr) {
        FragmentTransaction transection = mgr.beginTransaction();
        //using Bundle to send data
        Bundle bundle = new Bundle();
        bundle.putString(paramName, paramValue);
        mfragment.setArguments(bundle); //data being send to SecondFragment
        transection.replace(R.id.content_frame, mfragment);
        transection.commit();
    }

    public static void refreshFragment(Fragment mfragment, Bundle bundle, FragmentManager mgr) {
        FragmentTransaction transection = mgr.beginTransaction();
        //using Bundle to send data
        mfragment.setArguments(bundle); //data being send to SecondFragment
        transection.replace(R.id.content_frame, mfragment);
        transection.commit();
    }

    public static void refreshFragment(Fragment mfragment, FragmentManager mgr) {
        FragmentTransaction transection = mgr.beginTransaction();
        Bundle bundle = new Bundle();
        mfragment.setArguments(bundle); //data being send to SecondFragment
        transection.replace(R.id.content_frame, mfragment);
        transection.commit();
    }




    /*
     * Scrollable expandable list
     */
    public static void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static AlertDialog.Builder getConfirmDialog(Context context,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm dialog!");
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        return builder;
    }

    public static boolean isBlank(String str) {
        if(null!=str && str.trim().length()>0) {
            return true;
        }
        return false;
    }

    public static String getIndianRupee(float value) {
//        Locale locale = Locale.getDefault();
//        String language = locale.getLanguage();
//        String country = locale.getCountry();
//        Format format = NumberFormat.getCurrencyInstance(new Locale(language, country));
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }


}
