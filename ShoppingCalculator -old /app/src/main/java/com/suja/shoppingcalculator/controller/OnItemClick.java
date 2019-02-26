package com.suja.shoppingcalculator.controller;

import com.suja.shoppingcalculator.model.ShoppingItem;

import java.util.List;

/**
 * Created by Suja Manu on 11/28/2018.
 */


    public interface OnItemClick {
        void onClick (List<ShoppingItem> value);
    }

