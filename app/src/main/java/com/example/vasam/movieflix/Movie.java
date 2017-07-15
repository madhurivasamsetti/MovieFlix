package com.example.vasam.movieflix;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vasam on 7/10/2017.
 */

public class Movie implements Parcelable {

    private int mMovieId;
    private String mMovieTitle;
    private String mMovieImagePath;
    private String mMovieDate;
    private int mMovieRating;
    private String mMovieOverview;
    private String mMovieKey;
    private String[] mMovieReview;
    private int mMovieDuration;

    public Movie(int mMovieId, String mMovieTitle, String mMovieImagePath, String mMovieDate,
                 int mMovieRating, String mMovieOverview, String mMovieKey, String[] mMovieReview,
                 int mMovieDuration) {
        this.mMovieId = mMovieId;
        this.mMovieTitle = mMovieTitle;
        this.mMovieImagePath = mMovieImagePath;
        this.mMovieDate = mMovieDate;
        this.mMovieRating = mMovieRating;
        this.mMovieOverview = mMovieOverview;
        this.mMovieKey = mMovieKey;
        this.mMovieReview = mMovieReview;
        this.mMovieDuration = mMovieDuration;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mMovieImagePath);
        dest.writeString(mMovieDate);
        dest.writeInt(mMovieRating);
        dest.writeString(mMovieOverview);
        dest.writeString(mMovieKey);
        dest.writeStringArray(mMovieReview);
        dest.writeInt(mMovieDuration);
    }

    private Movie(Parcel in) {
        this.mMovieId = in.readInt();
        this.mMovieTitle = in.readString();
        this.mMovieImagePath = in.readString();
        this.mMovieDate = in.readString();
        this.mMovieRating = in.readInt();
        this.mMovieOverview = in.readString();
        this.mMovieKey = in.readString();
        this.mMovieReview = in.createStringArray();
        this.mMovieDuration = in.readInt();

    }
public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel source) {

        return new Movie(source);
    }

    @Override
    public Movie[] newArray(int size) {
        return new Movie[size];
    }
};
    public int getmMovieId() {
        return mMovieId;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieImagePath() {
        return mMovieImagePath;
    }

    public String getmMovieDate() {
        return mMovieDate;
    }

    public int getmMovieRating() {
        return mMovieRating;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public String getmMovieKey() {
        return mMovieKey;
    }

    public String[] getmMovieReview() {
        return mMovieReview;
    }

    public int getmMovieDuration() {
        return mMovieDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
