package com.suja.mypopularmovies.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.suja.mypopularmovies.AsyncTaskCompleteListener;
import com.suja.mypopularmovies.NetworkUtil;

import java.net.URL;

/**
 * Created by Suja Manu on 3/6/2018.
 */

public class MovieDBSearch extends AsyncTask<String, String, String> {

    private boolean mAlreadyLoaded;
    private boolean online;
    private Context context;
    public AsyncTaskCompleteListener<String> listener;


    public MovieDBSearch(Context ctx,AsyncTaskCompleteListener listener){
        this.context = ctx;
        this.listener = listener;

    }

    @Override
    protected String doInBackground(String... params) {
        String movieResults=null;

            URL searchUrl = NetworkUtil.buildUrl(params[0]);
            online = NetworkUtil.isOnline();
            if (online) {
                movieResults = NetworkUtil.getMovieData(searchUrl);
                //Log.i("resultfromDBsearch", movieResults);
            }



        return movieResults;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        listener.onTaskComplete(result);
       // listener.onReviewsDBComplete(result);



    }
}