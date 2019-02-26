package com.suja.shoppingcalculator.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.suja.shoppingcalculator.model.ShoppingItem;
import com.suja.shoppingcalculator.model.ShoppingItemContract;

/**
 * Created by Suja Manu on 11/26/2018.
 */

public class ShoppingItemLoader  extends AsyncTask<String, Void, ShoppingItem[]> {
    private final Context context;
    private ShoppingItem item;
    public AsyncTaskCompleteListener listener;
    ShoppingItem[] shoppingItems;

    public ShoppingItemLoader(Context ctx,AsyncTaskCompleteListener listener) {
        this.context = ctx;
          this.listener = listener;
        // this.item=item;

    }

    public ShoppingItemLoader(Context context) {
        this.context=context;
    }

    @Override
    protected ShoppingItem[] doInBackground(String... params) {
        if (params[0].equalsIgnoreCase("select")) {
             shoppingItems =  selectAllItems();
        }
        return shoppingItems;
    }

    @Override
    protected void onPostExecute(ShoppingItem[] items) {
        super.onPostExecute(items);
        listener.onTaskComplete(items);
    }

    public void insertShoppingItem(ShoppingItem myItem) {
        Log.i("inside insert", "insert");
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();


        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(ShoppingItemContract.ShoppingItemEntry.COLUMN_ITEM, myItem.getName());
        contentValues.put(ShoppingItemContract.ShoppingItemEntry.COLUMN_PRICE, myItem.getPrice());
        contentValues.put(ShoppingItemContract.ShoppingItemEntry.COLUMN_QUANTITY, myItem.getWeight());
        if (myItem.getDate() != null)
            contentValues.put(ShoppingItemContract.ShoppingItemEntry.COLUMN_DATE, myItem.getDate().toString());


        // Insert the content values via a ContentResolver

        Uri uri = context.getContentResolver().insert(ShoppingItemContract.ShoppingItemEntry.CONTENT_URI, contentValues);


    }

    private void deleteItem(ShoppingItem myItem) {

        // Create new empty ContentValues object

        Uri uri = ShoppingItemContract.ShoppingItemEntry.CONTENT_URI.buildUpon().appendPath(myItem.getName()).build();

        int rows = context.getContentResolver().delete(uri, null, null);


    }

    private ShoppingItem[] selectAllItems() {
        Uri uri = ShoppingItemContract.ShoppingItemEntry.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        ShoppingItem[] shoppingItems = new ShoppingItem[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            Log.i("cursor values",cursor.getColumnIndex(ShoppingItemContract.ShoppingItemEntry.COLUMN_ITEM)+"");
           int nameIndex = cursor.getColumnIndex(ShoppingItemContract.ShoppingItemEntry.COLUMN_ITEM);
            int priceIndex = cursor.getColumnIndex(ShoppingItemContract.ShoppingItemEntry.COLUMN_PRICE);
            int qtyIndex = cursor.getColumnIndex(ShoppingItemContract.ShoppingItemEntry.COLUMN_QUANTITY);
            int dateIndex = cursor.getColumnIndex(ShoppingItemContract.ShoppingItemEntry.COLUMN_DATE);
            cursor.moveToPosition(i); // get to the right location in the cursor
            ShoppingItem item = new ShoppingItem();
            Log.i("curosr check",cursor.getString(nameIndex));
            if(cursor.getString(nameIndex)!=null)item.setName(cursor.getString(nameIndex));
            if(null!=cursor.getString(priceIndex))item.setPrice(cursor.getString(priceIndex));
            if(null!=cursor.getString(qtyIndex))item.setWeight(cursor.getString(qtyIndex));
            if(null!=cursor.getString(dateIndex))item.setDate(cursor.getString(dateIndex));

            shoppingItems[i]=item;
        }
        return shoppingItems;
    }
}
