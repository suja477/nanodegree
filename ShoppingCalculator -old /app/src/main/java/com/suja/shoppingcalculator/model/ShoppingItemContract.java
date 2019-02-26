package com.suja.shoppingcalculator.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Suja Manu on 11/26/2018.
 */

public class ShoppingItemContract {
    /* TODO (veg1) Add content provider constants to the Contract
 Clients need to know how to access the task data, and it's your job to provide
 these content URI's for the path to that data:
    veg1) Content authority,
    2) Base content URI,
    3) Path(s) to the tasks directory
    4) Content URI for data in the TaskEntry class
  */
    public static final String AUTHORITY="com.suja.shoppingcalculator";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
    public static final String PATH_SHOPPING="shopping";


    public static final class ShoppingItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOPPING).build();


        public static final String TABLE_NAME = "shoppingItemTable";
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_DATE="date";


    }
}
