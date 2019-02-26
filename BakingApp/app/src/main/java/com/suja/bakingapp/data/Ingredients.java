package com.suja.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Suja Manu on 4/3/2018.
 */

public class Ingredients implements Serializable{
  //  private static final long   serialVersionUID    = -459713774703414626L;
    String quantity;
    String measure;
    String ingredientName;

    public Ingredients() {
    }

    protected Ingredients(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredientName = in.readString();
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }


}
