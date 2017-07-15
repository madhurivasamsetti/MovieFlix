package com.example.vasam.movieflix;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.example.vasam.movieflix.data.MovieContract.MovieEntry;

/**
 * Created by vasam on 7/14/2017.
 */

public class CustomFavoritesLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private CustomFavoritesAdapter mFavoriteAdapter;
    private ProgressBar mLoadingIndicator;

    public CustomFavoritesLoader(Context context,CustomFavoritesAdapter mFavoriteAdapter,
                                 ProgressBar mLoadingIndicator) {
        this.context = context;
        this.mFavoriteAdapter = mFavoriteAdapter;
        this.mLoadingIndicator = mLoadingIndicator;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MovieEntry._ID, MovieEntry.MOVIE_ID, MovieEntry.MOVIE_TITLE
                , MovieEntry.MOVIE_IMAGE_PATH, MovieEntry.MOVIE_DATE, MovieEntry.MOVIE_RATING,
                MovieEntry.MOVIE_DURATION, MovieEntry.MOVIE_OVERVIEW, MovieEntry.MOVIE_TRAILER_KEY
                , MovieEntry.MOVIE_REVIEWS};

        return new CursorLoader(context,MovieEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mFavoriteAdapter.setFavoriteData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteAdapter.setFavoriteData(null);

    }
}
