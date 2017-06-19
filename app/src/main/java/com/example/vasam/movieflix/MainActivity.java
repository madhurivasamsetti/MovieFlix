package com.example.vasam.movieflix;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();
    private TextView mDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayTextView = (TextView) findViewById(R.id.tv_response);
        loadMovieData();
    }

    private void loadMovieData() {
        URL url = NetworkUtils.buildUrl();
        new FetchMovieDataAsyncTask().execute(url);
    }

    private class FetchMovieDataAsyncTask extends AsyncTask<URL, Void, String[]> {
        @Override
        protected String[] doInBackground(URL... params) {

            if (params.length == 0) {
                return null;
            }
            URL url = params[0];
            String jsonResponse = null;
            try {
                jsonResponse = NetworkUtils.getJSONResponseFromHTTPConnection(url);
                String[] movieData = NetworkUtils.getMovieDataFromJSONResponse(jsonResponse);
                return movieData;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            if (movieData != null) {
                for (String movieItem : movieData) {
                    mDisplayTextView.append(movieItem + "\n\n\n");
                }
            }
        }
    }
}
