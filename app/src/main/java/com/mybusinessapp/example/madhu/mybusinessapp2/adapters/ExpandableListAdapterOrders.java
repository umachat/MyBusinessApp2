package com.mybusinessapp.example.madhu.mybusinessapp2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mybusinessapp.example.madhu.mybusinessapp2.FragmentViewOrder;
import com.mybusinessapp.example.madhu.mybusinessapp2.R;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.OrderEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Helper class used in view orders.
 */
public class ExpandableListAdapterOrders extends ExpandableListAdapter {

    private Context context;
    /**
     * @param context       context
     * @param listDataChild child menu data
     */
    public ExpandableListAdapterOrders(Context context, HashMap<String, List<AbstractEntity>> listDataChild) {
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
        final OrderEntity data = (OrderEntity) getChild(groupPosition,childPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_grid,null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.lbl_ord_title);
        title.setText(data.getTitle());
        TextView orderDate = (TextView) convertView.findViewById(R.id.lbl_ord_date);
        //amt.getPaint().setUnderlineText(true);
        orderDate.setText(AppConstants.DISPLAY_DATE_FORMATTER.format(data.getCreateDate()));
        title.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        //refresh page
                        Fragment mfragment = new FragmentViewOrder();

                        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        UIUtil.refreshFragment(mfragment, "orderId",String.valueOf(data.getOrderId()), manager);
                    }
                }
        );
        return convertView;
    }
}
