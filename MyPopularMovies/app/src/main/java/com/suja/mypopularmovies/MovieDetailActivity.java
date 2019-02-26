package com.suja.mypopularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.suja.mypopularmovies.data.FavouriteDB;
import com.suja.mypopularmovies.data.MovieDBHelper;
import com.suja.mypopularmovies.data.MovieDBSearch;
import com.suja.popularmovies.R;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickListener {


    private static final String TAG = MovieDetailActivity.class.getSimpleName();


    private TextView tvMovieId;

    @BindView(R.id.detail_date)
    TextView tvMovieDate;
    @BindView(R.id.detail_overview)
    TextView tvMovieOverview;
    @BindView(R.id.detail_vote)
    TextView tvMovieVote;
    @BindView(R.id.detail_title)
    TextView tvMovieTitle;
    @BindView(R.id.detail_poster)
    ImageView ivMoviePoster;
    @BindView(R.id.detail_trailer)
    TextView tvTrailer;
    @BindView(R.id.detail_review)
    TextView tvReview;
    @BindView(R.id.hint_fav)
    TextView tvHintFav;
    private ArrayList<Trailer> trailerArrayList;

    private RecyclerView rvTrailerList;
    private RecyclerView rvReviewList;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private MovieDBHelper movieDBHelper;
    private Movie myMovie;
    private String BASE_URL = "http://image.tmdb.org/t/p/";
    private String SIZE = "w185//";
    private static String isFavourite = "No";
    private static boolean mAlreadyLoaded = false;
    private SharedPreferences sharedPreferences;
    private static ArrayList<Movie> movieArrayList;
    private static Movie[] movies;
    private static String subtitle;
    private static String display;
    private Trailer[] trailers=null;
    private Review[] reviews=null;
    private final static String TRAILER="TRAILERS";
    private final static String REVIEW="REVIEWS";

    private final static String DELETE="DELETE";
    private final static String INSERT="INSERT";
    private final static String SEARCH="SEARCH";
    private final static String FAVOURITE_SUBTITLE="Favourite";
    private final static String IS_FAV="Yes";
    private final static String NOT_FAV="No";
    private final static String PREF_SUBTITLE="title";
    private final static String PREF_MOVIE_INFO="movie_info";
    private final static String MY_PREFERENCE="myPrefs";
    private final static String MOVIE_INTENT="MOVIE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

      //  -----------------------------------getting movie list from sharedprefernce-------//
        sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREF_MOVIE_INFO, null);
        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        movieArrayList = gson.fromJson(json, type);
        if (movieArrayList != null) {
            Log.i("--moviedetailclass--","movie");
           movies=movieArrayList.toArray(new Movie[movieArrayList.size()]);}
           subtitle=sharedPreferences.getString(PREF_SUBTITLE,null);
        //  -----------------------------------getting movie list from sharedprefernce-------//

        Intent movieDetailIntent = getIntent();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (movieDetailIntent.hasExtra(MOVIE_INTENT)) {

            myMovie = movieDetailIntent.getParcelableExtra(MOVIE_INTENT);
            String posterPath = BASE_URL + SIZE + myMovie.getImagePath();
            //Loading Image from URL
            Picasso.with(MovieDetailActivity.this)
                    .load(posterPath)
                    .resize(400, 400)                        // optional
                    .into(ivMoviePoster);

            tvMovieTitle.setText(myMovie.getOriginalTitle());
            tvMovieOverview.setText(myMovie.getOverview());
            tvMovieVote.setText(myMovie.getVoteAverage() + getString(R.string.out_of_ten));
            tvMovieDate.setText(myMovie.getReleaseDate());

        //populate trailer recyclerview
            rvTrailerList = findViewById(R.id.trailer_list);
            //populate Trailers
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvTrailerList.setHasFixedSize(true);
            rvTrailerList.setLayoutManager(layoutManager);
            trailerAdapter = new TrailerAdapter(this);
            rvTrailerList.setAdapter(trailerAdapter);

            //populate Reviews recyclerview

            rvReviewList = findViewById(R.id.review_list);
            LinearLayoutManager layoutManager1
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvReviewList.setHasFixedSize(true);
            rvReviewList.setLayoutManager(layoutManager1);
            reviewAdapter = new ReviewAdapter();
            rvReviewList.setAdapter(reviewAdapter);

            //show trailer,review according to menu selection ,on rotation set from savedinstance
            if(savedInstanceState==null) {
                tvTrailer.setVisibility(View.VISIBLE);
                rvTrailerList.setVisibility(View.VISIBLE);
                rvReviewList.setVisibility(View.GONE);
                tvReview.setVisibility(View.GONE);
                new MovieDBSearch(this, new FetchMovieTrailers()).execute(TRAILER + myMovie.getMovieId());
            }

            final Button favButton = findViewById(R.id.favourite);
            setFavouriteButton(favButton,myMovie.getFavourite());
            isFavourite=myMovie.getFavourite();
            favButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //toggle favourite heart according to user clicks
                    if (isFavourite.equalsIgnoreCase(NOT_FAV)) {

                        favButton.setBackgroundResource(R.drawable.fav_filled);
                        tvHintFav.setVisibility(View.INVISIBLE);
                        isFavourite = IS_FAV;
                        myMovie.setFavourite(isFavourite);
                        Log.i("inside onclick insert", isFavourite);

                    } else if (isFavourite.equalsIgnoreCase(IS_FAV)) {

                        favButton.setBackgroundResource(R.drawable.fav_blank);
                        tvHintFav.setVisibility(View.VISIBLE);
                        isFavourite = NOT_FAV;
                        myMovie.setFavourite(isFavourite);
                        Log.i("inside onclick delete", isFavourite);
                    }
                }
            });

        } else
        {showReviewError();
        showTrailerError();}


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.detail_menu, menu);

        return true;

    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("stop", "MYonStop is called");
        if(subtitle!=null && subtitle.equalsIgnoreCase(FAVOURITE_SUBTITLE))
            deleteUnfavourite();
            saveMyFavourite();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if(subtitle!=null && subtitle.equalsIgnoreCase(FAVOURITE_SUBTITLE))
                    deleteUnfavourite();
                saveMyFavourite();
               startActivity(new Intent(this,MainActivity.class));

                return true;
            }
            case R.id.review_menu:
               showReview();
                return true;
            case R.id.trailer_menu:
                showTrailer();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

   private void  showTrailer(){
        if(trailers==null)
            new MovieDBSearch(this, new FetchMovieTrailers()).execute(TRAILER + myMovie.getMovieId());
        tvTrailer.setVisibility(View.VISIBLE);
        rvTrailerList.setVisibility(View.VISIBLE);
        rvReviewList.setVisibility(View.GONE);
        tvReview.setVisibility(View.GONE);
        display="TRAILER_DISPLAY";
    }
    private void  showReview(){
        if(reviews==null)
            new MovieDBSearch(this, new FetchMovieReviews()).execute(REVIEW + myMovie.getMovieId());
        tvTrailer.setVisibility(View.GONE);
        rvTrailerList.setVisibility(View.GONE);
        rvReviewList.setVisibility(View.VISIBLE);
        tvReview.setVisibility(View.VISIBLE);
        display="REVIEW_DISPLAY";
    }
/*   if detailactivity is called from favourite screen delete movies which are not favourite
* else update current movie list with faourite changed to "yes"*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(subtitle!=null && subtitle.equalsIgnoreCase(FAVOURITE_SUBTITLE))
            deleteUnfavourite();
            saveMyFavourite();
       // startActivity(new Intent(this,MainActivity.class));

    }
//save the selected favourite movies in local db and shared preference list and delete unfavourite from
    // local db and sharedprefernce
    private void saveMyFavourite() {
        if (myMovie.getFavourite().equalsIgnoreCase(IS_FAV)) {
            new FavouriteDB(this, myMovie).execute(INSERT);//insert if fav button
            if (movieArrayList != null)
                for (int j = 0; j < movieArrayList.size(); j++) {
                    Movie obj = movieArrayList.get(j);

                    if (obj.getMovieId().equals(myMovie.getMovieId())) {
                        //found, delete.
                        movieArrayList.remove(j);
                        Log.i("removed -----", obj.getOriginalTitle());
                        movieArrayList.add(j,myMovie);
                        Log.i("added back -----", obj.getOriginalTitle());
                        break;
                    }

                }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movieArrayList);
        editor.putString(PREF_MOVIE_INFO, json);
        editor.commit();


    }

     private void deleteUnfavourite() {
     if (myMovie.getFavourite().equalsIgnoreCase(NOT_FAV)) {
             new FavouriteDB(this, myMovie).execute(DELETE);//delete if unfav and prev screen fav
             if (movieArrayList != null)
                 for (int j = 0; j < movieArrayList.size(); j++) {
                     Movie obj = movieArrayList.get(j);

                     if (obj.getMovieId().equals(myMovie.getMovieId())) {
                         //found, delete.
                         movieArrayList.remove(j);
                         Log.i("removed -----", obj.getOriginalTitle());
                         break;
                     }

                 }
         }

         SharedPreferences.Editor editor = sharedPreferences.edit();
         Gson gson = new Gson();
         String json = gson.toJson(movieArrayList);
         editor.putString(PREF_MOVIE_INFO, json);
         editor.commit();


 }
    private void showTrailerError() {
       tvTrailer.setVisibility(View.VISIBLE);
        tvTrailer.setText(getResources().getString(R.string.error));
        rvTrailerList.setVisibility(View.GONE);
        tvReview.setVisibility(View.GONE);
        rvReviewList.setVisibility(View.GONE);
        display="TRAILER_ERROR";


    }
    private void showReviewError() {
tvReview.setVisibility(View.VISIBLE);
        tvReview.setText(R.string.error);
        rvReviewList.setVisibility(View.GONE);
        tvTrailer.setVisibility(View.GONE);
        rvTrailerList.setVisibility(View.GONE);
        display="REVIEW_ERROR";
    }

    private void setFavouriteButton(Button favButton, String mFavourite){

        if (mFavourite.equalsIgnoreCase(NOT_FAV)) {
            favButton.setBackgroundResource(R.drawable.fav_blank);
            tvHintFav.setVisibility(View.VISIBLE);
            Log.i("favourite", mFavourite);
        } else {
            favButton.setBackgroundResource(R.drawable.fav_filled);
            tvHintFav.setVisibility(View.INVISIBLE);
            Log.i("favourite", mFavourite);
        }

    }

    @Override
    public void onClickOpenYoutube(String trailerId, String trailerName) {
        Context context = this;
        watchYoutubeVideo(this, trailerId);
        Toast.makeText(context, "Selected "+trailerName, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void shareTrailer(String string) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Trailer");
        String sAux = "\nLet me recommend you this trailer\n\n";
        sAux = sAux + "http://www.youtube.com/watch?v="+ string;
        sharingIntent.putExtra(Intent.EXTRA_TEXT,sAux);
       // sharingIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("http://www.youtube.com/watch?v=" + string));
        startActivity(Intent.createChooser(sharingIntent,"choose one"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("DISPLAY",display);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String selector=savedInstanceState.getString("DISPLAY");
        Log.i("display",selector);
       switch (selector){
        case "REVIEW_DISPLAY":showReview();break;
           case "TRAILER_DISPLAY":showTrailer();break;
           case "REVIEW_ERROR":showReview();break;
           case "TRAILER_ERROR":showTrailer();break;
           default:showTrailer();


    }}

    private class FetchMovieTrailers implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            if (result != null)
                try {
                    Log.i("resultfromdetail", result);
                     trailers = MovieJSONUtil.getTrailersFromJSON(result);
                 //   trailerArrayList = new ArrayList<Trailer>(Arrays.asList(trailers));


                    trailerAdapter.setTrailers(trailers);
                    if(trailers.length==0)
                        showTrailerError();
                    else showTrailer();


                } catch (JSONException e) {
                    e.printStackTrace();
                  //  trailerArrayList = null;
                    showTrailerError();
                }
            else {
                //trailerArrayList = null;
                showTrailerError();
            }

        }

        @Override
        public void onFavouriteDBComplete(Movie[] movies) {

        }


    }

    private class FetchMovieReviews implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            if (result != null){
                try {
                    Log.i("resultfromdetail", result);
                    reviews = MovieJSONUtil.getReviewsFromJSON(result);
                    // trailerArrayList = new ArrayList<Trailer>(Arrays.asList(reviews));

                    // Log.i("review",reviews[0].toString());
                    reviewAdapter.setReviews(reviews);
                    if(reviews.length==0)
                        showReviewError();
                    else showReview();


                } catch (JSONException e) {
                    e.printStackTrace();

                    showReviewError();
                }}
            else {

                showReviewError();
            }

        }


        @Override
        public void onFavouriteDBComplete(Movie[] movies) {

        }


    }


    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }






}
