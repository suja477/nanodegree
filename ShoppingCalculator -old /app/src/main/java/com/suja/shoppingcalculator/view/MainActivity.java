package com.suja.shoppingcalculator.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.suja.shoppingcalculator.R;
import com.suja.shoppingcalculator.controller.ItemRecyclerViewAdapter;
import com.suja.shoppingcalculator.controller.OnItemClick;
import com.suja.shoppingcalculator.controller.Utility;
import com.suja.shoppingcalculator.model.ShoppingItem;
import com.suja.shoppingcalculator.model.ShoppingItemContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClick {
    RecyclerView mRecyclerView;
    ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private  List<ShoppingItem> mSelectedShoppinItems=new ArrayList<ShoppingItem>();
    public static  final int PICK_VEG=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        // read from json and load to db
        ShoppingItem[] shoppingItems=Utility.loadJSONFromAsset(this);


        mRecyclerView = findViewById(R.id.item_recycler_view);
        itemRecyclerViewAdapter=new ItemRecyclerViewAdapter(this,this);
        mRecyclerView.setAdapter(itemRecyclerViewAdapter);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);

        itemRecyclerViewAdapter.setItems( shoppingItems);
        mRecyclerView.setAdapter(itemRecyclerViewAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        Button btn1 = (Button) findViewById(R.id.next_button);
       // Button btn2 = (Button) findViewById(R.id.button3);


        int viewId = v.getId() ;

        if(viewId == R.id.next_button) {
            Intent intent = new Intent(this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("selectedlist", (ArrayList<? extends Parcelable>) mSelectedShoppinItems);
            intent.putExtra("selected", bundle);
            // intent.putExtra("steps",mRecipes[getAdapterPosition()].getSteps());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(List<ShoppingItem> value) {
        mSelectedShoppinItems=value;
    }

}

