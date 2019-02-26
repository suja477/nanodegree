package com.udacity.gradle.builditbigger;

/**
 * Created by Suja Manu on 3/6/2018.
 * This c
 * lisneter class handles result from network call
 */

public interface AsyncTaskCompleteListener<T>
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onTaskComplete(String result);

}