package com.example.vasam.movieflix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements CustomMovieAdapter.CustomMovieAdapterOnClickHandler {

    public static final String LOG_TAG = MainActivity.class.getName();
    private TextView mErrorTextView;
    private ProgressBar mLoadingIndicator;
    private CustomMovieAdapter mAdapter;
    /*
    * checking whether user mobile has internet connection or not.
    * if no internet connection then an error message will be displayed on main screen
    * else loadMovieData method is called.
    */
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mErrorTextView = (TextView) findViewById(R.id.error_textView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mAdapter = new CustomMovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        checkInternetConnection();

    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mErrorTextView.setVisibility(View.VISIBLE);
            mErrorTextView.setText(R.string.error_connection);
        } else {
            loadMovieData(0);
        }
    }

    /**
     * this method takes user specified value and tries to build url accordingly
     * once it calls buildUrl method in NetworkUtils class and gets url
     * then it passes that url to FetchMovieDataAsyncTask method.
     *
     * @param userValue userSelected menu option's id
     */
    private void loadMovieData(int userValue) {
        URL url = NetworkUtils.buildUrl(userValue);
        new FetchMovieDataAsyncTask().execute(url);
    }

    /**
     * this method transfers from current activity to detailActivity via intent
     * sets this movie_item which received from MovieViewHolder to intent.
     *
     * @param movie_item complete details of single movie.
     */
    @Override
    public void onClickItem(String movie_item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movie_item);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
            case R.id.action_popular:
                if (isConnected) {
                    loadMovieData(menuItem_id);
                }
                break;
            case R.id.action_rate:
                if (isConnected) {
                    loadMovieData(menuItem_id);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchMovieDataAsyncTask extends AsyncTask<URL, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {

            if (params.length == 0) {
                return null;
            }
            URL url = params[0];
            String jsonResponse;
            String movieData[];
            try {
                jsonResponse = NetworkUtils.getJSONResponseFromHTTPConnection(url);
                movieData = NetworkUtils.getMovieDataFromJSONResponse(jsonResponse);
                return movieData;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            if (movieData != null) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mAdapter.setMovieData(movieData);
            }
        }
    }

}
