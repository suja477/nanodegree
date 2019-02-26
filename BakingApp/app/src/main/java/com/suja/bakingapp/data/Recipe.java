package com.suja.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Suja Manu on 4/3/2018.
 */

public class Recipe implements Serializable{

    String recipeId;
    String servings;

    public Recipe() {
    }


    String imageRecipe;
    String recipeName;
    Ingredients[] ingredients;
    Steps[] steps;



    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setImageRecipe(String imageRecipe) {
        this.imageRecipe = imageRecipe;
    }



    public String getServings() {
        return servings;
    }

    public String getImageRecipe() {
        return imageRecipe;
    }
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients[] ingredients) {
        this.ingredients = ingredients;
    }

    public Steps[] getSteps() {
        return steps;
    }

    public void setSteps(Steps[] steps) {
        this.steps = steps;
    }

}
