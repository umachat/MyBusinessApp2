package com.mybusinessapp.example.madhu.mybusinessapp2.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mybusinessapp.example.madhu.mybusinessapp2.R;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Adapter class that dynamically builds required 2 level menu list layout.
 */
public abstract class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> _listDataHeader;
    private HashMap<String, List<AbstractEntity>> _listDataChild;

    /**
     * @param context context
     * @param listDataChild child menu data
     */
    public ExpandableListAdapter(Context context, HashMap<String, List<AbstractEntity>> listDataChild) {
        this.context = context;
        this._listDataHeader = new ArrayList<String>(listDataChild.keySet());
        this._listDataChild = listDataChild;
    }

    /**
     * Number of header menus.
     * @return
     */
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    /**
     * Number of child menu items for a given header menu.
     * @param groupPosition index of header menu
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    /**
     * Get header menu item by index.
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    /**
     * Get child menu item by group and child position.
     * @param groupPosition header menu position
     * @param childPosition child menu position
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    /**
     * Get index of header menu.
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Get index of child menu.
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Render header menu.
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_group,null);
        }

        TextView lblGroupHeader = (TextView) convertView.findViewById(R.id.lbl_list_header);
        lblGroupHeader.setText(groupTitle);
        lblGroupHeader.setTypeface(null, Typeface.BOLD);

        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
