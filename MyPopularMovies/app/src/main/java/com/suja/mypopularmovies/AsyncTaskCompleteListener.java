package com.suja.mypopularmovies;

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
    public void onTaskComplete(T result);
    public void onFavouriteDBComplete(Movie[] movies);
   // public void onReviewsDBComplete(String reviews);
}