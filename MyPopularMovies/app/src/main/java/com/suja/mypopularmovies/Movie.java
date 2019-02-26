package com.suja.mypopularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suja Manu on 2/11/2018.
 * This class will store movie data
 */

public class Movie implements Parcelable {
private String originalTitle;
private String imagePath;
private  String overview;
private  String releaseDate;
private  String movieId;
private  String voteAverage;
private  String favourite="No";


    public Movie(String originalTitle, String imagePath,
                 String url, String overview, String releaseDate,
                 String getId, String voteAverage,String favourite) {
        this.originalTitle = originalTitle;
        this.imagePath = imagePath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = getId;
        this.voteAverage = voteAverage;
        this.favourite=favourite;

    }

    public Movie(){}



    private Movie(Parcel in){
        originalTitle = in.readString();
        imagePath = in.readString();
        overview=in.readString();
        releaseDate=in.readString();
        voteAverage=in.readString();
        movieId=in.readString();
        favourite=in.readString();

    }
    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public void setMovieId(String getId) {
        this.movieId = getId;
    }

    public String getMovieId() {
        return movieId;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }


    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getImagePath() { return this.imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }



    public void setImagePath(String imagePath) {


        this.imagePath = imagePath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(imagePath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(movieId);
        parcel.writeString(favourite);

    }
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
