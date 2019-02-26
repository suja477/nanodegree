package com.suja.shoppingcalculator.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.suja.shoppingcalculator.R;
import com.suja.shoppingcalculator.controller.DetailItemRecyclerViewAdapter;
import com.suja.shoppingcalculator.controller.ItemRecyclerViewAdapter;
import com.suja.shoppingcalculator.controller.SpinnerAdapter;
import com.suja.shoppingcalculator.controller.Utility;
import com.suja.shoppingcalculator.model.ShoppingItem;
import com.suja.shoppingcalculator.model.ShoppingItemContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
RecyclerView mRecyclerView;
DetailItemRecyclerViewAdapter detailItemRecyclerViewAdapter;
public ArrayList<ShoppingItem> arrayList;
    List<ShoppingItem> selectedShoppingItem;
    public SpinnerAdapter adapter;
    TextView sumValue;
public static  final int PICK_VEG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mRecyclerView = findViewById(R.id.item_recycler_view_detail);
        sumValue=findViewById(R.id.total_value);
        detailItemRecyclerViewAdapter=new DetailItemRecyclerViewAdapter(this,sumValue);
        mRecyclerView.setAdapter(detailItemRecyclerViewAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        selectedShoppingItem =new ArrayList<ShoppingItem>();
        detailItemRecyclerViewAdapter.setItems( selectedShoppingItem);
        mRecyclerView.setAdapter(detailItemRecyclerViewAdapter);

        ShoppingItem[] list=Utility.loadJSONFromAsset(this);

        arrayList = new ArrayList<ShoppingItem>(Arrays.asList(list));
         adapter=new SpinnerAdapter(this,
                R.layout.spinner_layout,R.id.spinner_txt, arrayList);




    }
    public void onClick(View w) {
        new AlertDialog.Builder(this)
                .setTitle("Add vegetable")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        ShoppingItem selectedItem =arrayList.get(position);
                        Log.i("selected spinner item",arrayList.get(position).getName());
                        boolean present=false;
                        for (ShoppingItem shoppingItem : selectedShoppingItem) {
                            if(selectedItem.getName().equalsIgnoreCase(shoppingItem.getName()))
                                present=true;
                        }

                        if(!present)//add item to selected list
                            selectedShoppingItem.add(arrayList.get(position));
                        detailItemRecyclerViewAdapter.setItems( selectedShoppingItem);


                        dialog.dismiss();
                    }
                }).create().show();
    }

}
