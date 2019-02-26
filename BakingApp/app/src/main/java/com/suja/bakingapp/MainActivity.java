package com.suja.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.suja.bakingapp.data.Recipe;
import com.suja.bakingapp.presenter.RecipeRecyclerViewAdapter;
import com.suja.bakingapp.util.AsyncTaskCompleteListener;
import com.suja.bakingapp.util.JSONUtil;
import com.suja.bakingapp.util.NetworkUtil;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity  {
    RecyclerView mRecyclerView;
    private static final String bakingURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @BindView(R.id.error_msg_recipe)
    TextView tvError;
    RecipeRecyclerViewAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.recipe_recycler_view);
        recipeAdapter = new RecipeRecyclerViewAdapter(this);
        tvError = findViewById(R.id.error_msg_recipe);
        tvError.setVisibility(View.INVISIBLE);


        //read from network and write json to local file in first loading
        //else update from local file

        if(savedInstanceState==null)
        { networkConnect();}
        else updateRecycleriew();


        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(recipeAdapter);
        //if configuration changed to landscape or if tablet screen show gridlayout
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                || (findViewById(R.id.activity_main_sw600) != null)) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);

        } else {
            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh) {
            networkConnect();
        }
       return super.onOptionsItemSelected(item);
    }

    public void networkConnect() {
        tvError.setVisibility(View.GONE);//hide error if any previous
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, bakingURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                       updateRecycleriewFirtLoading(response);

                        NetworkUtil.writeToFile(response,getApplicationContext());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
              showError(R.string.net_error);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void updateRecycleriewFirtLoading(String response) {
        try {

            Recipe[] recipes = JSONUtil.getAllRecipeNames(response);
            if (recipes == null) {

                recipeAdapter.setRecipes(null);
                showError(R.string.error_recipe);

            } else {

                recipeAdapter.setRecipes(recipes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            recipeAdapter.setRecipes(null);
           showError(R.string.error_recipe);

        }
    }

    private void showError(int errorMsg) {
        tvError.setText(errorMsg);
        tvError.setVisibility(View.VISIBLE);
    }


    private void updateRecycleriew() {
        try {
            Recipe[] recipes = JSONUtil.getAllRecipeNames(this);
            if (recipes == null) {
                tvError.setText(R.string.error_recipe);
                recipeAdapter.setRecipes(null);
                tvError.setText(R.string.empty_view_text);
            } else
                recipeAdapter.setRecipes(recipes);


        } catch (Exception e) {
            e.printStackTrace();
            recipeAdapter.setRecipes(null);
            tvError.setText(R.string.empty_view_text);

        }
    }
}
