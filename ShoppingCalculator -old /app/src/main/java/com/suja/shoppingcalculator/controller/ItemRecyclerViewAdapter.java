package com.suja.shoppingcalculator.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suja.shoppingcalculator.R;
import com.suja.shoppingcalculator.model.ShoppingItem;
import com.suja.shoppingcalculator.view.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suja Manu on 11/27/2018.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemRecyclerViewHolder> {

    private final Context mContext;
    private static ShoppingItem shoppingItem;
    String uri;
    public boolean iscolor = true;
    private ShoppingItem[] mShoppintItems;
    public List<ShoppingItem> mSelectedShoppingItems =new ArrayList<ShoppingItem>();
    public int count=0;
    private static String TAG="com.suja.shoppingCalculator";
    private OnItemClick mcallBack;

    public ItemRecyclerViewAdapter(Context context,OnItemClick mCallback) {
        this.mContext=context;
    this.mcallBack=mCallback;
    }

    @Override
    public ItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list,parent, false);
        return new ItemRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemRecyclerViewHolder holder, final int position) {
       // holder.itemName.setText((mShoppintItems[position]).getName());
        //Loading Image from URL using Picasso and set it in image view
        if((mShoppintItems[position].getUrl())!="")
             uri = "@drawable/"+mShoppintItems[position].getUrl();
        // where myresource (without the extension) is the file

        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        holder.itemImage.setImageResource(imageResource);
        holder.itemImage.setContentDescription(mShoppintItems[position].getName());
      /*  Picasso.with(mContext)
                .load("http://pngimg.com/uploads/cabbage/cabbage_PNG8825.png")
                .resize(400, 400)                        // optional
                .into(holder.itemImage);*/
        Log.i("item name",mShoppintItems[position].getName());
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("selected item",mShoppintItems[position].getName());
               //put all the clicked items in a list and when next button is pressed call detailactivity
                //with list bundle in intent
                //toggle favourite heart according to user clicks


                    if(!mSelectedShoppingItems.contains(mShoppintItems[position])) {
                        mSelectedShoppingItems.add(mShoppintItems[position]);
                    holder.itemImage.setBackgroundColor(Color.GRAY);

                    Log.i("inside  select", "");
                    iscolor=false;

                } else{
                    holder.itemImage.setBackgroundColor(Color.WHITE);
                mSelectedShoppingItems.remove(mShoppintItems[position]);
                    Log.i("inside not select", "");
                    iscolor=true;
                }
                mcallBack.onClick(mSelectedShoppingItems);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mShoppintItems!=null)
        return mShoppintItems.length;
        else return 0;
    }
    public void setItems(ShoppingItem[] items)
    {
        mShoppintItems=items;
        notifyDataSetChanged();
    }
    public class ItemRecyclerViewHolder extends RecyclerView.ViewHolder
           {

       // TextView itemName;
        ImageView itemImage;
        public ItemRecyclerViewHolder(View itemView) {
            super(itemView);
           // itemName=itemView.findViewById(R.id.item_name);
            itemImage=itemView.findViewById(R.id.item_image);
        }

    }
}
