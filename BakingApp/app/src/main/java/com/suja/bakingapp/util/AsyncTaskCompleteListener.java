package com.suja.bakingapp.util;

import com.suja.bakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * Created by Suja Manu on 3/6/2018.
 * This c
 * lisneter class handles result from network call for movies and result from local db call for favourite movies
 */

public interface AsyncTaskCompleteListener<T>
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onTaskComplete(Recipe[] result);

   // public void onReviewsDBComplete(String reviews);
}