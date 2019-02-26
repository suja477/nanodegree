package com.suja.shoppingcalculator.controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.suja.shoppingcalculator.model.ShoppingItem;
import com.suja.shoppingcalculator.model.ShoppingItemContentProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Suja Manu on 11/26/2018.
 */

public class Utility {
    public static String itemJSONString;
    public static ShoppingItem[] shoppingItems;

    public static ShoppingItem[] loadJSONFromAsset(Context context) {

        try {
            InputStream is = context.getAssets().open("initialValues.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            itemJSONString = new String(buffer, "UTF-8");
            // SharedPreferences prefs = context.getSharedPreferences("packageName", MODE_PRIVATE);
            // SharedPreferences.Editor editor = prefs.edit();
            // editor.putString("JSON")

            Gson gson = new Gson();
            shoppingItems = gson.fromJson(itemJSONString, ShoppingItem[].class);

            Log.i("got item from json",shoppingItems[1].getName());
            for (int i=0;i<shoppingItems.length;i++){

                Log.i("output",shoppingItems[i].getName()+" "+shoppingItems[i].getPrice()+" "+shoppingItems[i].getDate());

                //here we are directly accessing insert method of async task.
            new ShoppingItemLoader(context).insertShoppingItem(shoppingItems[i]);
            }




        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return shoppingItems;

    }


}
