package com.suja.mypopularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.suja.mypopularmovies.AsyncTaskCompleteListener;
import com.suja.mypopularmovies.Movie;

import static android.content.ContentValues.TAG;

/**
 * Created by Suja Manu on 3/9/2018.
 * This class handles favourite movie database operations,like
 * inserting into local db
 * deleting from local db
 * searching local db for all favourite movies
 * converting result cursor from db to movie objects
 */

public class FavouriteDB extends AsyncTask< String,Void,Movie[]> {
    public AsyncTaskCompleteListener<String> listener;
    private boolean mAlreadyLoaded;
    private boolean online;
    private Context context;
    private  Movie[] movies = null;
    private Movie movie=null;
    private final static String DELETE="DELETE";
    private final static String INSERT="INSERT";
    private final static String SEARCH="SEARCH";


    public FavouriteDB(Context ctx, AsyncTaskCompleteListener listener) {
        this.context = ctx;
        this.listener = listener;

    }

    public FavouriteDB(Context context,Movie movie) {
        this.context = context;
        this.movie=movie;
    }

    @Override
    protected void onPostExecute(Movie[] results) {
        super.onPostExecute(results);
    if(results!=null)listener.onFavouriteDBComplete(results);
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        if (params[0].equalsIgnoreCase(INSERT))
        { if (movie != null)
                   insertFavouriteMovie(movie);}
        else if (params[0].equalsIgnoreCase(DELETE))
        { if (movie != null)
            deleteFavouriteMovie(movie);}
            else if (params[0].equalsIgnoreCase(SEARCH)){
                try {
                    Cursor cursor =
                            context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);
                    movies = convertToMovie(cursor);



                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

        } return movies;
    }

    private Movie[] convertToMovie(Cursor mCursor) {


        movies = new Movie[mCursor.getCount()];
        for (int i = 0; i < mCursor.getCount(); i++) {
            // Indices for the _id, description, and priority columns
            int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH);
            int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int voteIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE);
            int overviewIndex=mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            int favourriteIndex=mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVURITE);
            mCursor.moveToPosition(i); // get to the right location in the cursor

            // Determine the values of the wanted data
            String id = mCursor.getString(idIndex);
            String imagePath = mCursor.getString(imageIndex);
            String title = mCursor.getString(titleIndex);
            String vote=mCursor.getString(voteIndex);
            String overview=mCursor.getString(overviewIndex);
            String favourite=mCursor.getString(favourriteIndex);
            Movie movieItem = new Movie();
            movieItem.setImagePath(imagePath);
            movieItem.setMovieId(id);
            movieItem.setOriginalTitle(title);
            movieItem.setVoteAverage(vote);
            movieItem.setOverview(overview);
            movieItem.setFavourite(favourite);
            movies[i] = movieItem;

        }
        return movies;

    }
    private void insertFavouriteMovie(Movie myMovie) {

        Log.i("inside insert","insert");
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();


        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, myMovie.getMovieId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, myMovie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, myMovie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH, myMovie.getImagePath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, myMovie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE, myMovie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVURITE, "Yes");

        // Insert the content values via a ContentResolver

        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);


    }
    private void deleteFavouriteMovie(Movie myMovie) {

        // Create new empty ContentValues object

        Uri uri=MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(myMovie.getMovieId()).build();

        int rows=context.getContentResolver().delete(uri, null,null);


    }
}
