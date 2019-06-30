package com.mybusinessapp.example.madhu.mybusinessapp2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mybusinessapp.example.madhu.mybusinessapp2.R;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    private Context mContext;
    private LayoutInflater inflater;
    private List<MenuItem> clients = null;
    private ArrayList<MenuItem> arraylist;

    public ListViewAdapter(Context context, List<MenuItem> clients) {
        mContext = context;
        this.clients = clients;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<MenuItem>();
        this.arraylist.addAll(clients);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return clients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(clients.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        clients.clear();
        if (charText.length() == 0) {
            clients.addAll(arraylist);
        } else {
            for (MenuItem wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    clients.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
