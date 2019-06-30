package com.mybusinessapp.example.madhu.mybusinessapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.adapters.ListViewAdapter;
import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperClients;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.MenuItem;

import java.util.ArrayList;

public class FragmentSearchClient extends Fragment implements SearchView.OnQueryTextListener{

    public static final String TAG = "FragmentSearchClient";

    private ListViewAdapter adapter;

    // Declare Variables
    private ListView list;
    private SearchView editsearch;
    private TextView msg;

    private ArrayList<MenuItem> clients = new ArrayList<>();
    private DBHelperClients mydb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_client, container, false);

        mydb = new DBHelperClients(getContext());
        Cursor cursor = mydb.getClients();
        clients.addAll(DBUtil.populateMenuItem(cursor));
        // Toast.makeText(getContext(),"SEARCHING 1 "+clients.size(), Toast.LENGTH_SHORT).show();
        list = (ListView) view.findViewById(R.id.listview);
        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(getContext(), clients);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        msg = view.findViewById(R.id.txt_msg_srch);
        //error message if empty
        list.setEmptyView(msg);
        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) view.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int clientId = clients.get(position).getId();
               // Toast.makeText(getContext(),"selected client "+clientId, Toast.LENGTH_SHORT).show();
                NavigationUtil.viewClient(clientId,getFragmentManager());
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search ClientEntity");


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
       // Toast.makeText(getContext(),"SEARCHING 2", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }

}
