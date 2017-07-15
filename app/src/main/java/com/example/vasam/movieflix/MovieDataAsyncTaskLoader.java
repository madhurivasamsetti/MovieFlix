package com.example.vasam.movieflix;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

/**
 * Created by vasam on 7/7/2017.
 */

public class MovieDataAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

private String mUrl;

    public MovieDataAsyncTaskLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

        Log.d("TASK LOADER","in loadBackground ");
        if(mUrl == null)
        {
            return null;
        }
        List<Movie> movieData = null;
        try {
            movieData = NetworkUtils.fetchMovieData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      return movieData;
    }
}
