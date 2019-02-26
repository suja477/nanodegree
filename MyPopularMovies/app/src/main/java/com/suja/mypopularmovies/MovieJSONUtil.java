package com.suja.mypopularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2/10/2018.
 * This class will parse JSON message and gives movie data.
 */

public final class MovieJSONUtil {


    private static final String TAG = MovieJSONUtil.class.getSimpleName();
    public static Movie[] getMovieStringFromJSON(String movieJSONString)
            throws JSONException {

        try {
            JSONObject movieJSON = new JSONObject(movieJSONString);
            JSONArray movieArray = movieJSON.getJSONArray("results");

            Movie[] movies = new Movie[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {
                Movie movieItem = new Movie();
                JSONObject movieJSONObject = movieArray.getJSONObject(i);

                movieItem.setOriginalTitle(movieJSONObject.getString("title"));
                movieItem.setImagePath(movieJSONObject.getString("poster_path"));
                movieItem.setMovieId(movieJSONObject.getString("id"));
                movieItem.setOverview(movieJSONObject.getString("overview"));
                movieItem.setVoteAverage(movieJSONObject.getString("vote_average"));
                movieItem.setReleaseDate(movieJSONObject.getString("release_date"));
                Log.i("moviename",movieItem.getOriginalTitle());
                movies[i]=movieItem;
            }
            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Trailer[] getTrailersFromJSON(String movieJSONString)
            throws JSONException {

        try {
            Log.i("trailername",movieJSONString);
            JSONObject trailerJSON = new JSONObject(movieJSONString);
            JSONArray trailerArray = trailerJSON.getJSONArray("results");

            Trailer[] trailers = new Trailer[trailerArray.length()];
            for (int i = 0; i < trailerArray.length(); i++) {
                Trailer trailer = new Trailer();
                JSONObject tailerJSONObject = trailerArray.getJSONObject(i);

                trailer.setTrailerName(tailerJSONObject.getString("name"));
                trailer.setTrailerId(tailerJSONObject.getString("key"));

                Log.i("trailername",trailer.getTrailerName());
                trailers[i]=trailer;
            }
            return trailers;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Review[] getReviewsFromJSON(String movieJSONString)
            throws JSONException {

        try {
            Log.i("reviews",movieJSONString);
            JSONObject reviewJSON = new JSONObject(movieJSONString);
            JSONArray reviewArray = reviewJSON.getJSONArray("results");

            Review[] reviews = new Review[reviewArray.length()];
            for (int i = 0; i < reviewArray.length(); i++) {
                Review review = new Review();
                JSONObject tailerJSONObject = reviewArray.getJSONObject(i);

                review.setReviewerName(tailerJSONObject.getString("author"));
                review.setReviewContent(tailerJSONObject.getString("content"));

                Log.i("review",review.getReviewerName());
                reviews[i]=review;
            }
            return reviews;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
