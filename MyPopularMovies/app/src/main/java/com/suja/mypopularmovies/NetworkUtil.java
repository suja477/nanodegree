package com.suja.mypopularmovies;

import android.net.Uri;
import android.util.Log;

import com.suja.popularmovies.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * Created by admin on 2/11/2018.
 * This class is for network db operations like
 * search top_rated movies(main activity)
 * most_popular movies(main activity)
 * trailers of a selected movie(MovieDetail activity)
 * reviews of a selected movie(MovieDetail activity)
 */

public class NetworkUtil {


    private static final String TAG = NetworkUtil.class.getSimpleName();
    private  final static String MOVIEDB_SORT_URL = BuildConfig.API_SORT_URL;
    private final static String MOVIEDB_BASE_URL =BuildConfig.API_BASE_URL;
    private final static String PARAM_SORT = "sort_by";
    private final static String sortByMostPopular = "popular";
    private final static String sortByTop = "top_rated";
    private final static String PARAM_QUERY = "query";
    private final static String API_KEY = "api_key";
    private final static String KEY = BuildConfig.API_KEY;
    private final static String PARAM_TRAILER="videos";
    private final static String PARAM_REVIEW="reviews";
    private final static String TRAILER="TRAILERS";
    private final static String REVIEW="REVIEWS";
    private final static String MOST_POPULAR="most_popular";
    private final static String TOP_RATED="top_rated";
    public  static boolean online=true;


    public NetworkUtil() {
    }
/*build URL for network call based on the user selection for mst_popular or top_rated,or trailer or review*/
    public static URL buildUrl(String movieSort) {
        Uri builtUri = null;
        if (movieSort.contains(TRAILER)) {
            builtUri=buildUrlForTrailer(movieSort.substring(TRAILER.length()));
            Log.i("trailer uri",builtUri.toString());

        } else if(movieSort.contains(REVIEW)){
            builtUri=buildUrlForReviews(movieSort.substring(REVIEW.length()));
            Log.i("review uri",builtUri.toString());
        } else{

            switch (movieSort) {
                case MOST_POPULAR:
                    builtUri = Uri.parse(MOVIEDB_SORT_URL).buildUpon()
                            .appendPath(sortByMostPopular)
                            .appendQueryParameter(API_KEY, KEY)
                            .build();
                    Log.i("url", builtUri.toString());
                    break;
                case TOP_RATED:
                    builtUri = Uri.parse(MOVIEDB_SORT_URL).buildUpon()
                            .appendPath(sortByTop)
                            .appendQueryParameter(API_KEY, KEY)
                            .build();
                    Log.i("url", builtUri.toString());
                    break;
                case "":
                    builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                            .appendQueryParameter(API_KEY, KEY)
                            .build();
                    Log.i(TAG, builtUri.toString());
                    break;

            }
        }

            // Log.i("url", builtUri.toString());
            URL url = null;
            try {
                url = new URL(builtUri.toString());
                 Log.i("url", builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return url;

    }
    public static Uri buildUrlForTrailer(String movieId) {
        Uri builtUri = null;
        builtUri = Uri.parse(MOVIEDB_SORT_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(PARAM_TRAILER)
                .appendQueryParameter(API_KEY, KEY)
                .build();
        // Log.i("url", builtUri.toString());
       return builtUri;
    }
    public static Uri buildUrlForReviews(String movieId) {
        Uri builtUri = null;
        builtUri = Uri.parse(MOVIEDB_SORT_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(PARAM_REVIEW)
                .appendQueryParameter(API_KEY, KEY)
                .build();
        // Log.i("url", builtUri.toString());
        return builtUri;
    }


    public static String getMovieData(URL searchUrl) {
        String result = null;
        BufferedReader bReader = null;
        try {
            URL url = searchUrl;



            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            if (urlConnection.getResponseCode() == 200) {
                InputStream iStream = new BufferedInputStream(urlConnection.getInputStream());
                bReader = new BufferedReader(new InputStreamReader(iStream));

                StringBuilder sBuilder = new StringBuilder();
                String line;
                while ((line = bReader.readLine()) != null) {


                    sBuilder.append(line);
                }
                result = sBuilder.toString();
                Log.i("result",result);

                urlConnection.disconnect();


            } else {
                return null;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bReader != null)
                try {
                    bReader.close();
                } catch (IOException exp) {
                    Log.e("Reader close", "", exp);
                }
        }
        return result;
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    public static  boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { online=false;return false; }
    }
}
