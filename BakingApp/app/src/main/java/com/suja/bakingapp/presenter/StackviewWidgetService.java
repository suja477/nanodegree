package com.suja.bakingapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.suja.bakingapp.BakingAppWidget;
import com.suja.bakingapp.R;
import com.suja.bakingapp.data.Ingredients;
import com.suja.bakingapp.data.Recipe;
import com.suja.bakingapp.util.JSONUtil;

import org.json.JSONException;

import java.util.ArrayList;

import timber.log.Timber;

public class StackviewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private Recipe[] mRecipes;


    public StackRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {
        Timber.plant(new Timber.DebugTree());
        try {

            mRecipes = JSONUtil.getAllRecipeNames(mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSetChanged() {


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (null == mRecipes) return 0;
        Log.i("recipe count", String.valueOf(mRecipes.length));
        return mRecipes.length;    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe);
        rv.setTextViewText(R.id.widget_title, mRecipes[i].getRecipeName());

        Ingredients[] ingredients = JSONUtil.getIngredientByRecipe(mContext, mRecipes[i].getRecipeName());
        try {


            for (Ingredients ingredient : ingredients) {
                Timber.d("Adding ingredient" + ingredient.getIngredientName());
                RemoteViews ingredientView = new RemoteViews(mContext.getPackageName(),
                        R.layout.widget_ingredient_item);
                String text=ingredient.getIngredientName()+"  "+ingredient.getQuantity()+"  "+ingredient.getMeasure();
                ingredientView.setTextViewText(R.id.widget_ingredient_name,text);
                rv.addView(R.id.widget_ingredient_list, ingredientView);

            }
        } catch (Exception e) {
            RemoteViews ingredientView = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_ingredient_item);
            ingredientView.setTextViewText(R.id.widget_ingredient_name, "No Ingredient found");
            rv.addView(R.id.widget_ingredient_list, ingredientView);
        }

        Bundle extras = new Bundle();
        extras.putInt(BakingAppWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        // interim.
        try {

            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the remote views object.
        return rv;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
