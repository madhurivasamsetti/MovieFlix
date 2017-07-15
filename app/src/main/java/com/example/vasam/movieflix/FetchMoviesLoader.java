package com.example.vasam.movieflix;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by vasam on 7/14/2017.
 */

public class FetchMoviesLoader implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private String mUrl;
    private Context context;
    private CustomMovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;

    public FetchMoviesLoader(Context context, String url, CustomMovieAdapter adapter, ProgressBar loadingIndicator) {
        this.context = context;
        mUrl = url;
        mAdapter = adapter;
        mLoadingIndicator = loadingIndicator;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieDataAsyncTaskLoader(context, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setMovieData(null);

    }
}
