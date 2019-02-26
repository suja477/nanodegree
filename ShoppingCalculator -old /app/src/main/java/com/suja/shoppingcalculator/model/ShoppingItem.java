package com.suja.shoppingcalculator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Suja Manu on 11/26/2018.
 */

public class ShoppingItem  implements Parcelable {
    String name;

    String price ="";
    String weight ="";
    String date="";
    String url="";

    public ShoppingItem(String name, String price, String weight, String date, String url) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.date = date;
        this.url = url;
    }

    public ShoppingItem() {
    }

    public ShoppingItem(Parcel in) {

        this.name = in.readString();
        this.price = in.readString();
        this.weight = in.readString();
        this.date=in.readString();
        this.url=in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(weight);
        parcel.writeString(date);
        parcel.writeString(url);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ShoppingItem createFromParcel(Parcel in) {
            return new ShoppingItem(in);
        }

        public ShoppingItem[] newArray(int size) {
            return new ShoppingItem[size];
        }
    };
}
