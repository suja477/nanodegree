package com.suja.shoppingcalculator.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suja.shoppingcalculator.R;
import com.suja.shoppingcalculator.model.ShoppingItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Suja Manu on 11/27/2018.
 */

public class DetailItemRecyclerViewAdapter
        extends RecyclerView.Adapter<DetailItemRecyclerViewAdapter.ItemRecyclerViewHolder> {

    private final Context mContext;
    private static ShoppingItem shoppingItem;
    String uri;
    private List<ShoppingItem> mShoppintItems;
    private static String TAG="com.suja.shoppingCalculator";
    public float price;
    public int quantity;
    public ArrayList<Float> costs =new ArrayList<Float>();
    public TextView total;

    public DetailItemRecyclerViewAdapter(Context context) {
        this.mContext=context;}
    public DetailItemRecyclerViewAdapter(Context context,TextView textView) {
        this.mContext=context;
    this.total=textView;
    }

    @Override
    public ItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_item_list,parent, false);
        return new ItemRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemRecyclerViewHolder holder, final int position) {
         holder.itemName.setText((mShoppintItems.get(position).getName()));
       //  holder.itemPrice.setText("0");
        // holder.itemQty.setText("0");
        // int sum=(Integer.parseInt(holder.itemPrice.getText().toString())
        //         *Integer.parseInt(holder.itemQty.getText().toString()));
       // Log.i("sum ",String.valueOf(sum));
        // holder.itemSum.setText(String.valueOf(sum));
            holder.itemPrice.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View view, boolean b) {
                   TextView v=view.findViewById(R.id.item_detail_price);
                   mShoppintItems.get(position).setPrice(v.getText().toString());
                }
            });
        holder.itemQty.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                TextView v=view.findViewById(R.id.item_detail_weight);
                mShoppintItems.get(position).setWeight(v.getText().toString());
                Float sum =0F;
                try {
                    sum=Float.parseFloat(mShoppintItems.get(position).getPrice()) *
                            Float.parseFloat(mShoppintItems.get(position).getWeight());

                }catch (Exception e){
                    e.printStackTrace();

                }
                costs.add(sum);
                holder.itemSum.setText(sum.toString());
                //find the total bill and display at the end textview accessed using constructor
                float totalvalue = 0;
                for (float i : costs)
                    totalvalue += i;
                total.setText(String.valueOf(totalvalue));

            }
        });

    }

    @Override
    public int getItemCount() {
        if(mShoppintItems!=null)
            return mShoppintItems.size();
        else return 0;
    }
    public void setItems(List<ShoppingItem> items)
    {
        mShoppintItems=items;
        notifyDataSetChanged();
    }
    public class ItemRecyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView itemName;
        TextView itemPrice;
        TextView itemQty;
        TextView itemSum;

        // ImageView itemImage;
        public ItemRecyclerViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_detail_name);
            itemPrice = itemView.findViewById(R.id.item_detail_price);
            itemQty = itemView.findViewById(R.id.item_detail_weight);
            itemSum = itemView.findViewById(R.id.item_detail_sum);
            //itemImage=itemView.findViewById(R.id.item_image);
        }

    }
}
