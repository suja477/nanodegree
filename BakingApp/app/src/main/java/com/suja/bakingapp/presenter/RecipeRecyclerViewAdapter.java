package com.suja.bakingapp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suja.bakingapp.DetailActivity;
import com.suja.bakingapp.MainActivity;
import com.suja.bakingapp.R;
import com.suja.bakingapp.data.Recipe;
import com.suja.bakingapp.util.JSONUtil;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Suja Manu on 4/3/2018.
 */

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeAdapterViewHolder> {
    private final Context mContext;
    private static Recipe recipeSelected;

    private Recipe[] mRecipes;
    private static String TAG = "com.suja.bakingapp.presenter.REcipeREcyclerViewAdapter";


    public RecipeRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item_view, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeRecyclerViewAdapter.RecipeAdapterViewHolder holder, final int position) {
        holder.recipeName.setText(mRecipes[position].getRecipeName());
        String imagePath = mRecipes[position].getImageRecipe();
        //Loading Image from URL
        if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                    holder.imageRecipe.setImageResource(R.drawable.recipe_logo);
                }
            });
            builder.build().load(imagePath).into(holder.imageRecipe);
        }
        holder.recipeServing.setText("Servings "+mRecipes[position].getServings());
    }
      /*  if(imagePath!=null&&!imagePath.equalsIgnoreCase(""))
        {
            Picasso.with(mContext)
                .load(imagePath)
                .resize(100, 100)                        // optional
                .into(holder.imageRecipe);}

        Log.i("recipe name", mRecipes[(position)].getRecipeName());


    }*/

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        Log.i("recipe count", String.valueOf(mRecipes.length));
        return mRecipes.length;
    }

    public void setRecipes(Recipe[] recipes) {

        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeServing;
        TextView recipeName;
        ImageView imageRecipe;

        RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_list_item);
            imageRecipe=itemView.findViewById(R.id.recipe_image);
            recipeServing=itemView.findViewById(R.id.serving_tv);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.i(TAG, "on click " + mRecipes[getAdapterPosition()]);
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("recipe", mRecipes[getAdapterPosition()].getRecipeName());
            mContext.startActivity(intent);
            Timber.d("Detail activity called");

        }
    }
}

