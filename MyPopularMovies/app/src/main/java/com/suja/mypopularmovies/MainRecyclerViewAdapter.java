package com.suja.mypopularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.suja.popularmovies.R;

/**
 * Created by Suja Manu on 3/14/2018.
 * This class is recycler view adapter for mainactivity
 */

public class MainRecyclerViewAdapter extends  RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>  {
    private static LayoutInflater inflater = null;
    private Movie myMovie;
    private String movieId;
    private ImageView moviePoster;
    private  String BASE_URL= "http://image.tmdb.org/t/p/" ;
    private String SIZE="w185//";
    private Movie[] movies;
    private Context context ;

    @Override
    public MainRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
              // infalte the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewAdapter.MyViewHolder holder, final int position) {
        myMovie = new Movie();
        myMovie = movies[position];
        context = holder.moviePoster.getContext();
        moviePoster = holder.moviePoster;
        String posterPath = BASE_URL+SIZE+myMovie.getImagePath();
        Log.i("imagepath", posterPath);

        //Loading Image from URL using Picasso and set it in image view
        Picasso.with(context)
                .load(posterPath)
                .resize(400, 400)                        // optional
                .into(moviePoster);
        moviePoster.setTag(myMovie.getOriginalTitle());


        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("selected movie",moviePoster.getTag().toString());
                Intent childActivityIntent = new Intent(context, MovieDetailActivity.class);
                //load details of movie in intent to show in next activity screen
                childActivityIntent.putExtra("MOVIE",movies[position]);
                context.startActivity(childActivityIntent);

            }
            });

    }
    public void setMovies(Movie[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public  final ImageView moviePoster;

        public MyViewHolder(View view) {
            super(view);
            moviePoster = view.findViewById(R.id.movie_poster);


        }


    }
}
