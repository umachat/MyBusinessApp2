package com.mybusinessapp.example.madhu.mybusinessapp2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewPayment;
import com.mybusinessapp.example.madhu.mybusinessapp2.R;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.PaymentEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Helper class used in view orders.
 */
public class ExpandableListAdapterPayments extends ExpandableListAdapter {

    private Context context;
    /**
     * @param context       context
     * @param listDataChild child menu data
     */
    public ExpandableListAdapterPayments(Context context, HashMap<String, List<AbstractEntity>> listDataChild) {
        super(context, listDataChild);
        this.context = context;
    }


    /**
     * Render child menu.
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PaymentEntity data = (PaymentEntity) getChild(groupPosition,childPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.payment_grid,null);
        }

        TextView desc = (TextView) convertView.findViewById(R.id.lbl_pay_desc);
        desc.setText(data.getPaymentDesc());
        TextView orderDate = (TextView) convertView.findViewById(R.id.lbl_pay_date);
        //amt.getPaint().setUnderlineText(true);
        orderDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(data.getPaymentDate()));
        TextView amt = (TextView) convertView.findViewById(R.id.lbl_pay_amt);
        amt.setText(UIUtil.getIndianRupee(data.getAmount()));

        amt.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("paymentId", String.valueOf(data.getPaymentId()));
                        bundle.putString("orderId", String.valueOf(data.getOrderId()));
                        Fragment mfragment = new FragmentViewPayment();
                        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        UIUtil.refreshFragment(mfragment, bundle, manager);
                    }
                }
        );
        return convertView;
    }
}
