package com.example.vasam.movieflix;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by vasam on 6/18/2017.
 */

public class NetworkUtils {
    /* These utilities are used to communicate with the moviedb
    */

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String API_KEY = "8f54780bddbe846b6331c33af3188a96";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";
    //private static final String IMAGESIZE_PARAM = "";
    private static final String FILE_PATH = "";
    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE = "en-US";
    private static final String SORT_PARAM = "sort_by";
    private static final String SORT = "popularity.desc";
    private static final String INCLUDEADULT_PARAM = "include_adult";
    private static final String HAS_ADULT_MOVIE = "false";
    private static final String INCLUDEVIDEO_PARAM = "include_video";
    private static final String HAS_VIDEO = "true";
    /*
    w92", "w154", "w185", "w342", "w500", "w780", or "original"
     */
    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg


    private NetworkUtils() {
    }

    public static String buildUrl(String imagePath) {
        Uri buildUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath("w500")
                .build();
        Uri uri = Uri.withAppendedPath(buildUri,imagePath);
        Log.i("NetworkUtils.class","uri: "+uri);
        return uri.toString();

    }

    public static URL buildUrl() {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(SORT_PARAM, SORT)
                .appendQueryParameter(INCLUDEADULT_PARAM, HAS_ADULT_MOVIE)
                .appendQueryParameter(INCLUDEVIDEO_PARAM, HAS_VIDEO).build();
        Log.i("NetworkUtils.class", "uri: " + buildUri);
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getJSONResponseFromHTTPConnection(URL url) throws IOException {
        HttpURLConnection urlConnection;
        InputStream inputStream;

        urlConnection = (HttpURLConnection) url.openConnection();
        try {
            inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String[] getMovieDataFromJSONResponse(String jsonResponse) throws JSONException {

        JSONObject root = new JSONObject(jsonResponse);
        JSONArray results = root.getJSONArray("results");
        String[] parsedMovieData = new String[results.length()];
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            String movie_title = object.getString("original_title");
            String image_path = object.getString("poster_path");
            int user_rating = object.getInt("vote_average");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            parsedMovieData[i] = image_path + "-" + movie_title + "-" + user_rating + "-" + overview + "-" + release_date + "-";
        }

        return parsedMovieData;
    }

}

