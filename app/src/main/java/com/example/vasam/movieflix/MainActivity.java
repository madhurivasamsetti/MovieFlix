package com.example.vasam.movieflix;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vasam.movieflix.preferences.SettingsActivity;
import com.example.vasam.movieflix.utils.ApplicationUtils;
import com.facebook.stetho.Stetho;

import static com.example.vasam.movieflix.utils.ApplicationUtils.checkInternetConnection;
import static com.example.vasam.movieflix.utils.ApplicationUtils.updateUrl;

public class MainActivity extends AppCompatActivity implements
        CustomMovieAdapter.CustomMovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener
        , CustomFavoritesAdapter.CustomFavoritesAdapterOnClickHandler {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int CURSOR_LOADER_ID = 2;
    private static final int LOADER_ID = 1;
    public TextView mErrorTextView;
    private Context context;
    private ProgressBar mLoadingIndicator;
    private CustomMovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CustomFavoritesAdapter mFavoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mErrorTextView = (TextView) findViewById(R.id.error_textView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        context = getApplicationContext();
        mFavoriteAdapter = new CustomFavoritesAdapter(this);
        mAdapter = new CustomMovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, ApplicationUtils.calculateNoOfColumns(this));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        startLoader();

    }

    private void showInternetStatus() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(R.string.error_connection);
    }

    public void startLoader() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String preferred = sharedPreferences.getString(getString(R.string.pref_main_key),
                getString(R.string.pref_priority_value));
        if (preferred.equals(getString(R.string.pref_favorite_value))) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.INVISIBLE);
            mRecyclerView.setAdapter(mFavoriteAdapter);
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null,
                    new CustomFavoritesLoader(context, mFavoriteAdapter, mLoadingIndicator));
        } else {
            if (checkInternetConnection(this)) {
                mErrorTextView.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                String updateUrl = updateUrl(preferred);
                mRecyclerView.setAdapter(mAdapter);
                getLoaderManager().initLoader(LOADER_ID, null, new
                        FetchMoviesLoader(this, updateUrl, mAdapter, mLoadingIndicator));
            } else {
                showInternetStatus();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem_id = item.getItemId();
        switch (menuItem_id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClickItem(Movie movie_item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movie_item);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_main_key))) {
            String sortValue = sharedPreferences.getString(key, "");
            if (sortValue.equals(getString(R.string.pref_favorite_value))) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mErrorTextView.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mFavoriteAdapter);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, new CustomFavoritesLoader(context, mFavoriteAdapter, mLoadingIndicator));
            } else {
                if (checkInternetConnection(this)) {

                    mErrorTextView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mRecyclerView.setAdapter(mAdapter);
                    String updateUrl = updateUrl(sortValue);
                    getLoaderManager().restartLoader(LOADER_ID, null,
                            new FetchMoviesLoader(this, updateUrl, mAdapter, mLoadingIndicator));
                } else {
                    showInternetStatus();
                }
            }
        }
    }
}



