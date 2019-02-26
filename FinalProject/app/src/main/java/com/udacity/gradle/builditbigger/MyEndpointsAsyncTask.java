package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.suja.displaylibrary.MainDisplayActivity;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by Suja Manu on 7/30/2018.
 */
public class MyEndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> implements AsyncTaskCompleteListener{
    private static MyApi myApiService = null;
    private Context context;
    private ProgressBar dialog;
    public AsyncTaskCompleteListener<String> listener;

    /** progress dialog to show user that the backup is processing. */
    /**
     * application context.
     */
    public MyEndpointsAsyncTask(Context context, AsyncTaskCompleteListener listener) {
        this.context = context;

        this.listener=listener;
    }

    public MyEndpointsAsyncTask(ProgressBar dialog) {
        this.listener=this;
        this.dialog=dialog;
    }

    public MyEndpointsAsyncTask() {

    }

    @Override
    protected void onPreExecute() {
        // dialog=ProgressDialog.show(this.context.getApplicationContext(),"","Please Wait",false);
        if(dialog!=null)
        dialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            return myApiService.sayHi(name).execute().getJoke();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onTaskComplete(result);

    }




    @Override
    public void onTaskComplete(String result) {
        if(dialog!=null)
            dialog.setVisibility(View.GONE);
        // dialog.dismiss();
        Intent intent = new Intent(this.context, MainDisplayActivity.class);
        intent.putExtra(String.valueOf(R.string.JOKE_INTENT_KEY), result);
        this.context.startActivity(intent);
    }
}