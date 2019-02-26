package com.suja.bakingapp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suja.bakingapp.R;
import com.suja.bakingapp.data.Ingredients;
import com.suja.bakingapp.data.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suja Manu on 4/5/2018.
 *
 */

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientAdapterViewHolder> {
    // Keeps track of the context and list of images to display
    private Context mContext;
    private Ingredients[] ingredients;


    public IngredientRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public IngredientRecyclerViewAdapter.IngredientAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_item_layout,parent, false);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientRecyclerViewAdapter.IngredientAdapterViewHolder holder, int position) {
        holder.ingredientName.setText(ingredients[position].getIngredientName());
        holder.quantity.setText(ingredients[position].getQuantity());
        holder.measurement.setText(ingredients[position].getMeasure());
        Log.i("step name",ingredients[position].getIngredientName());
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (null == ingredients) return 0;
        Log.i("steps count", String.valueOf(ingredients.length));
        return ingredients.length;
    }
    public void setIngredients(Ingredients[] ingredients)
    {
        this.ingredients=ingredients;
        notifyDataSetChanged();
    }


    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder
           {

        private TextView ingredientName;
        private TextView quantity;
        private TextView measurement;
        public IngredientAdapterViewHolder(View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.ingredient_name);
            quantity=itemView.findViewById(R.id.ingredient_qty);
            measurement=itemView.findViewById(R.id.ingredient_measurement);

        }


    }
}
