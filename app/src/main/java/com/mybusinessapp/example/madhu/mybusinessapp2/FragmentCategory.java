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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mybusinessapp.example.madhu.mybusinessapp2.dao.DBHelperCategory;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.MessageConstants;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.NavigationUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.UIUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.util.DBUtil;
import com.mybusinessapp.example.madhu.mybusinessapp2.vo.CategoryEntity;

import java.util.ArrayList;

/**
 * Add/update delete categories
 */
public class FragmentCategory extends Fragment {

    private static final String TAG = "FragmentCategory";

    private final String DELETE_CATEGORY_MSG = "You are about to delete this category. proceed ?";
    private final String DELETE_CAT_LINKED_DATA_MSG = "You are about to delete this category. proceed ?";
    private final String DELETE_CAT_LINKED_ORDER_MSG = "This category has linked orders! Please delete linked orders or change their category.";
    private final String SELECT_CATEGORY_MSG = "Select a category to delete!";

    private AlertDialog.Builder builder,builder2,builder3;
    private EditText catName;
    private Button btnReset;
    private TextView msg;
    private Spinner spClient;

    private int selectedCategory;
    private DBHelperCategory mydb;
    private ArrayList<CategoryEntity> categories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        final View view = inflater.inflate(R.layout.fragment_category, container, false);

        catName = view.findViewById(R.id.txt_create_cat);
        msg = view.findViewById(R.id.txt_msg_cat);
        btnReset = view.findViewById(R.id.btn_reset);
        mydb = new DBHelperCategory(getContext());

        populateCategoryDropdown(view);

        addConfirmDialogs();

        regsterBtnListeners(  view);

        return view;
    }

    private void populateCategoryDropdown(View view) {
        Cursor res = mydb.getCategories();
        categories = DBUtil.populateOrderCategory(res);

        //spinner
        ArrayAdapter<CategoryEntity> adapter = new ArrayAdapter<CategoryEntity>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spClient = view.findViewById(R.id.sp_order_category);
        spClient.setAdapter(adapter);
        spClient.setEmptyView(msg);

        spClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategory = categories.get(position).getId();
                    catName.setText(categories.get(position).getName());
                    //Toast.makeText(getContext(),"selectedCategory "+selectedCategory, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: ");
            }
        });
    }

    private void addConfirmDialogs() {
        builder = UIUtil.getConfirmDialog(getContext(),DELETE_CATEGORY_MSG);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor res = mydb.getOrdersByCategory(selectedCategory);
                if(res.getCount() > 0) {
                    builder2.show();
                } else {
                    deleteCategory();
                }
            }
        });


        builder2 = UIUtil.getConfirmDialog(getContext(),DELETE_CAT_LINKED_DATA_MSG);
        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory();
            }
        });

        builder3 = UIUtil.getConfirmDialog(getContext(),DELETE_CAT_LINKED_ORDER_MSG);
        builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void regsterBtnListeners(final View view) {
        Button submitBtn = (Button) view.findViewById(R.id.btn_cat_save);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        submitSave(view);
                    }
                }
        );
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.btn_cat_delete);
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        delete(view);
                    }
                }
        );
        btnReset.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        resetValues();
                    }
                }
        );
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Project Categories");

    }

    private boolean isDuplicate(String catName) {
        for(CategoryEntity cat : categories) {
            if(cat.getName().trim().equalsIgnoreCase(catName)) {
                return true;
            }
        }
        return false;
    }

    public void submitSave(View view) {
        String strName = catName.getText().toString();
        strName = strName.trim();
        if(!UIUtil.isBlank(strName)) {
            Toast.makeText(getContext(), MessageConstants.BLANK_CATEGORY_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedCategory==0 && isDuplicate(strName)) {
            Toast.makeText(getContext(),MessageConstants.CATEGORY_EXISTS_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        if(categories.size()>= AppConstants.MAXIMUM_CATEGORIES_ALLOWED) {
            Toast.makeText(getContext(),MessageConstants.MAXIMUM_CATEGORIES_MSG, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean updated = false;
        if(selectedCategory>0) {
            //update
            updated = mydb.updateCategory(strName,selectedCategory);
        } else {
            //insert
            updated = mydb.addCategory(strName);
        }
        if(!updated) {
            Toast.makeText(getContext(),MessageConstants.SAVE_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
        } else {
            resetValues();
            Toast.makeText(getContext(),MessageConstants.SAVE_ERROR_MSG, Toast.LENGTH_SHORT).show();
        }

        NavigationUtil.createCategories(getFragmentManager());
    }

    public void delete(View view) {
        Log.d(TAG, "selectedCategory "+selectedCategory);
        Log.d(TAG, "catName.getText().toString() "+catName.getText().toString());
        if(selectedCategory>0 && UIUtil.isBlank(catName.getText().toString())) {
            builder.show();
        } else  {
            Toast.makeText(getContext(),SELECT_CATEGORY_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCategory() {
        int isDeleted = mydb.deleteCategory(selectedCategory);
        if(isDeleted == 1) {
            Toast.makeText(getContext(),MessageConstants.DELETE_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
            NavigationUtil.createCategories(getFragmentManager());
        } else if(isDeleted == -1) {
            Toast.makeText(getContext(),MessageConstants.DELETE_ERROR_MSG, Toast.LENGTH_SHORT).show();
        }else if(isDeleted == 0) {
            builder3.show();
        }
    }


    private void resetValues() {
        spClient.setSelection(0);
        selectedCategory = 0;
        catName.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        mydb.close();
    }
}
