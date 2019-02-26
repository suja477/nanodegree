package com.suja.shoppingcalculator.controller;

import com.suja.shoppingcalculator.model.ShoppingItem;

/**
 * Created by Suja Manu on 3/6/2018.
 * This c
 * lisneter class handles result
 */

public interface AsyncTaskCompleteListener<T>
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onTaskComplete(ShoppingItem[] result);


}