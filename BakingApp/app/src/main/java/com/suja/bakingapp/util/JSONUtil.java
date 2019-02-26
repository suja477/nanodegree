package com.suja.bakingapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.suja.bakingapp.BuildConfig;
import com.suja.bakingapp.data.Ingredients;
import com.suja.bakingapp.data.Recipe;
import com.suja.bakingapp.data.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import timber.log.Timber;

import static com.suja.bakingapp.util.NetworkUtil.readFromFile;
import static com.suja.bakingapp.util.NetworkUtil.writeToFile;


/**
 * Created by Suja Manu on 4/3/2018.
 */

public class JSONUtil {
    private static final String bakingURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static String TAG = "com.suja.bakingapp.util";
    private static String recipeJSONString;

    private Context mContext;

    public JSONUtil(Context context) {
        mContext = context;

    }

    private static void loadJSONFromAsset(Context context) {
        recipeJSONString = readFromFile(context);



    }


    public static Recipe[] getAllRecipeNames(Context context) {

        try {
            loadJSONFromAsset(context);
            if (recipeJSONString == null) return null;

            JSONArray recipeArray = new JSONArray(recipeJSONString);

            Recipe[] recipeNames = new Recipe[recipeArray.length()];
            for (int i = 0; i < recipeArray.length(); i++) {
                Recipe recipeItem = new Recipe();
                JSONObject recipeJSONObject = recipeArray.getJSONObject(i);
                recipeItem.setRecipeName(recipeJSONObject.getString("name"));
                recipeItem.setImageRecipe(recipeJSONObject.getString("image"));
                recipeItem.setServings(recipeJSONObject.getString("servings"));
                recipeNames[i]=recipeItem;
            }
            return recipeNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static Recipe getRecipeByName(String recipeName, Context mContext) {
        try {
            loadJSONFromAsset(mContext);
            if (recipeJSONString == null) return null;

            JSONArray recipeArray = new JSONArray(recipeJSONString);


            for (int i = 0; i < recipeArray.length(); i++) {
                Recipe recipeItem = new Recipe();
                JSONObject recipeJSONObject = recipeArray.getJSONObject(i);
                recipeItem.setRecipeName(recipeJSONObject.getString("name"));

                if (recipeName.equalsIgnoreCase(recipeItem.getRecipeName())) {
                    Log.i("selected recipe found", recipeItem.getRecipeName());
                    JSONArray ingredientArray = recipeJSONObject.getJSONArray("ingredients");
                    Ingredients[] ingredients = new Ingredients[ingredientArray.length()];
                    Log.i(TAG, "recipe---- " + recipeItem.getRecipeName());
                    for (int j = 0; j < ingredientArray.length(); j++) {
                        Ingredients ingredientItem = new Ingredients();
                        JSONObject ingredientJSON = ingredientArray.getJSONObject(j);
                        ingredientItem.setIngredientName(ingredientJSON.getString("ingredient"));
                        ingredientItem.setMeasure(ingredientJSON.getString("measure"));
                        ingredientItem.setQuantity(ingredientJSON.getString("quantity"));
                        ingredients[j] = (ingredientItem);
                        Log.i(TAG, "ingredient " + ingredientItem.getIngredientName());

                    }
                    recipeItem.setIngredients(ingredients);

                    JSONArray stepArray = recipeJSONObject.getJSONArray("steps");
                    Steps[] steps = new Steps[stepArray.length()];
                    for (int j = 0; j < stepArray.length(); j++) {
                        Steps stepItem = new Steps();
                        JSONObject stepJSON = stepArray.getJSONObject(j);
                        stepItem.setStepDescription(stepJSON.getString("description"));
                        stepItem.setStepId(stepJSON.getString("id"));
                        stepItem.setStepUrl(stepJSON.getString("videoURL"));
                        stepItem.setShortDescription(stepJSON.getString("shortDescription"));
                        stepItem.setThumbnailUrl(stepJSON.getString("thumbnailURL"));
                        steps[j] = stepItem;
                        Log.i(TAG, "step end" + stepItem.getStepDescription());

                    }
                    recipeItem.setSteps(steps);
                    return recipeItem;//check if forloop is going again after return ---remove this comment in the end
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Ingredients[] getIngredientByRecipe(Context mContext, String recipeName) {
        try {
            loadJSONFromAsset(mContext);
            if (recipeJSONString == null) return null;
            JSONArray recipeArray = new JSONArray(recipeJSONString);


            for (int i = 0; i < recipeArray.length(); i++) {
                Recipe recipeItem = new Recipe();
                JSONObject recipeJSONObject = recipeArray.getJSONObject(i);
                recipeItem.setRecipeName(recipeJSONObject.getString("name"));

                if (recipeName.equalsIgnoreCase(recipeItem.getRecipeName())) {
                    Log.i("selected recipe found", recipeItem.getRecipeName());
                    JSONArray ingredientArray = recipeJSONObject.getJSONArray("ingredients");
                    Ingredients[] ingredients = new Ingredients[ingredientArray.length()];
                    Log.i(TAG, "recipe---- " + recipeItem.getRecipeName());
                    recipeItem.setImageRecipe(recipeJSONObject.getString("image"));
                    recipeItem.setServings(recipeJSONObject.getString("servings"));
                    for (int j = 0; j < ingredientArray.length(); j++) {
                        Ingredients ingredientItem = new Ingredients();
                        JSONObject ingredientJSON = ingredientArray.getJSONObject(j);
                        ingredientItem.setIngredientName(ingredientJSON.getString("ingredient"));
                        ingredientItem.setMeasure(ingredientJSON.getString("measure"));
                        ingredientItem.setQuantity(ingredientJSON.getString("quantity"));
                        ingredients[j] = (ingredientItem);
                        Log.i(TAG, "ingredient " + ingredientItem.getIngredientName());

                    }
                    return ingredients;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Recipe[] getAllRecipeNames(String recipeJSONString) {
        try {

            if (recipeJSONString == null) return null;

            JSONArray recipeArray = new JSONArray(recipeJSONString);

            Recipe[] recipeNames = new Recipe[recipeArray.length()];
            for (int i = 0; i < recipeArray.length(); i++) {
                Recipe recipeItem = new Recipe();
                JSONObject recipeJSONObject = recipeArray.getJSONObject(i);
                recipeItem.setRecipeName(recipeJSONObject.getString("name"));
                recipeItem.setImageRecipe(recipeJSONObject.getString("image"));
                recipeItem.setServings(recipeJSONObject.getString("servings"));
                recipeNames[i]=recipeItem;
            }
            return recipeNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
