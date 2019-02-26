package com.suja.mypopularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suja.mypopularmovies.data.FavouriteDB;
import com.suja.mypopularmovies.data.MovieDBSearch;
import com.suja.popularmovies.R;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /*
     * This tag will be used for logging. It is best practice to use the class's name using
     * getSimpleName as that will greatly help to identify the location from which your logs are
     * being posted.
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView errorMsg;

    private GridView gvMainActivity;
    private static ArrayList<Movie> movieArrayList;
    private static Movie[] movies;
    private boolean mAlreadyLoaded =false;
    private boolean online;
    private android.support.v7.app.ActionBar actionBar;
    private SharedPreferences sharedPreferences;
    private final String POPULAR="most_popular";
    private final String TOP="top_rated";
    private final String SEARCH="search";
    private final static String SUBTITLE="title";
    private final static String MOVIE_INFO="movie_info";
    private final static String MY_PREFERENCE="myPrefs";
    private final static String MOVIE_INTENT="MOVIE";
    private final static String DISC_MOVIE="Discover Movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting sharedpreference
        sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MOVIE_INFO, null);
        Type type = new TypeToken<ArrayList<Movie>>(){}.getType();
        movieArrayList = gson.fromJson(json, type);
        String subtitle = sharedPreferences.getString(SUBTITLE, DISC_MOVIE);


        setContentView(R.layout.main_recycler_view);
        errorMsg = findViewById(R.id.error_msg);
        errorMsg.setVisibility(View.INVISIBLE);
        actionBar = getSupportActionBar();
        actionBar.setSubtitle(subtitle);

        //savedInstanceState  is not Loaded  at 1st

            if (movieArrayList != null ) {

                Log.i("----not first time----", "movie");
                //if data is not null show grid view
                movies = movieArrayList.toArray(new Movie[movieArrayList.size()]);
                showGridView();
            } else {
                actionBar.setSubtitle(DISC_MOVIE);
                Log.i("----first time----", "movie");
                //query db for data
                new MovieDBSearch(this, new FetchMyDataTaskCompleteListener()).execute("");//Url Call


            }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        URL SearchUrl;

        switch (item.getItemId()) {

            case R.id.rated:

            {

                new MovieDBSearch(this, new FetchMyDataTaskCompleteListener()).execute(TOP);
                actionBar.setSubtitle(R.string.top_rated);
                return true;
            }

            case R.id.popular:

            {

                new MovieDBSearch(this, new FetchMyDataTaskCompleteListener()).execute(POPULAR);
                actionBar.setSubtitle(R.string.most_popular);
                return true;
            }
            case R.id.favourite: {
                Log.i("going favourite", "fav");

                new FavouriteDB(this, new FetchMyDataTaskCompleteListener()).execute(SEARCH);
                actionBar.setSubtitle(R.string.favourite);
                return true;
            }
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener<String> {


        @Override
        public void onTaskComplete(String result) {
            if (result != null)
                try {
                    movies = MovieJSONUtil.getMovieStringFromJSON(result);
                    movieArrayList = new ArrayList<Movie>(Arrays.asList(movies));
                    showGridView();


                } catch (JSONException e) {
                    e.printStackTrace();
                    movieArrayList = null;
                    showErrorMessage();
                }
            else {
                movieArrayList = null;
                showErrorMessage();
            }
        }

        @Override
        public void onFavouriteDBComplete(Movie[] moviesResult) {
            if (moviesResult != null) {
                movies = moviesResult;
                movieArrayList = new ArrayList<Movie>(Arrays.asList(movies));
                showGridView();
            } else {
                showErrorMessage();
            }
        }

    }


    public void showGridView() {

        RecyclerView rvMovieView = findViewById(R.id.movie_list);

        GridAutofitLayoutManager gridLayoutManager = new GridAutofitLayoutManager(getApplicationContext(),
                500);
        // rvMovieView.setHasFixedSize(false);
        rvMovieView.setLayoutManager(gridLayoutManager);
        MainRecyclerViewAdapter rvMainAdapter = new MainRecyclerViewAdapter();
        rvMovieView.setAdapter(rvMainAdapter);
        rvMainAdapter.setMovies(movies);
        Log.i("movies count", String.valueOf(movies.length));


        rvMovieView.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.INVISIBLE);

        //---------save the selected movies in shardpreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movieArrayList);
        editor.putString(MOVIE_INFO, json);
        editor.putString(SUBTITLE, actionBar.getSubtitle().toString());
        editor.commit();

    }

    public void showErrorMessage() {

        RecyclerView rvMovieView = findViewById(R.id.movie_list);
        //show specific message ,no network,no result

        rvMovieView.setVisibility(View.INVISIBLE);
        errorMsg.setVisibility(View.VISIBLE);
        online = NetworkUtil.online;
        if (online)
            errorMsg.setText(getResources().getText(R.string.error));
        else
            errorMsg.setText(getResources().getText(R.string.no_network));
    }


  @Override
    public void onBackPressed(){
        SharedPreferences.Editor editor =sharedPreferences.edit();
      editor.putString(MOVIE_INFO, null);
      editor.putString(SUBTITLE, null);
      editor.clear().commit();

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private class VarColumnGridLayoutManager extends GridLayoutManager {

        private int minItemWidth;

        public VarColumnGridLayoutManager(Context context, int minItemWidth) {
            super(context, 1);
            this.minItemWidth = minItemWidth;
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler,
                                     RecyclerView.State state) {
            updateSpanCount();
            super.onLayoutChildren(recycler, state);
        }

        private void updateSpanCount() {
            int spanCount = getWidth() / minItemWidth;
            if (spanCount < 1) {
                spanCount = 1;
            }
            this.setSpanCount(spanCount);
        }
    }

    public static class GridAutofitLayoutManager extends GridLayoutManager {
        private int mColumnWidth;
        private boolean mColumnWidthChanged = true;

        public GridAutofitLayoutManager(Context context, int columnWidth) {
            super(context, 1);
            setColumnWidth(checkedColumnWidth(context, columnWidth));
        }

        public GridAutofitLayoutManager(Context context, int columnWidth, int orientation, boolean reverseLayout) { /* Initially set spanCount to 1, will be changed automatically later. */
            super(context, 1, orientation, reverseLayout);
            setColumnWidth(checkedColumnWidth(context, columnWidth));
        }

        private int checkedColumnWidth(Context context, int columnWidth) {
            if (columnWidth <= 0) { /* Set default columnWidth value (48dp here).
            It is better to move this constant to static constant on top,
            but we need context to convert it to dp, so can't really do so. */
                columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        48, context.getResources().getDisplayMetrics());
            }
            return columnWidth;
        }

        public void setColumnWidth(int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
                mColumnWidth = newColumnWidth;
                mColumnWidthChanged = true;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (mColumnWidthChanged && mColumnWidth > 0) {
                int totalSpace;
                if (getOrientation() == VERTICAL) {
                    totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / mColumnWidth);
                setSpanCount(spanCount);
                mColumnWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }
    }


}
