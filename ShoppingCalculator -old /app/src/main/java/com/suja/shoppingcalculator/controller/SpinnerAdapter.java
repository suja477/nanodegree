package com.suja.shoppingcalculator.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suja.shoppingcalculator.R;
import com.suja.shoppingcalculator.model.ShoppingItem;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ShoppingItem> {
    int groupid;
    Activity context;
    ArrayList<ShoppingItem> list;
    LayoutInflater inflater;
    String uri;
    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ShoppingItem>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
        this.context=context;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.spinner_img);
        if(list.get(position).getUrl()!="")
            if(list.get(position).getUrl()!=null)
            {
                uri = "@drawable/"+ list.get(position).getUrl();

           int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            Log.i("inside spinner",uri.toString());
             if(imageResource!=0)
                Picasso.with(context)
                        .load(imageResource)
                        .resize(100, 100).centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(imageView);
            }

        TextView textView=(TextView)itemView.findViewById(R.id.spinner_txt);
        textView.setText(list.get(position).getName());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }
}
