package com.suja.mypopularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Suja Manu on 3/8/2018.
 */

public class MovieContract  {

    /* TODO (1) Add content provider constants to the Contract
   Clients need to know how to access the task data, and it's your job to provide
   these content URI's for the path to that data:
      1) Content authority,
      2) Base content URI,
      3) Path(s) to the tasks directory
      4) Content URI for data in the TaskEntry class
    */
    public static final String AUTHORITY="com.suja.mypopularmovies";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES="movies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_OVERVIEW="overview";
        public static final String COLUMN_MOVIE_VOTE="vote";
        public static final String COLUMN_MOVIE_IMAGE_PATH ="imagePath";
        public static final String COLUMN_MOVIE_FAVURITE="favourite";

    }

}
