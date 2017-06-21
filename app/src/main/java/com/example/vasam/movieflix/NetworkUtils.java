package com.example.vasam.movieflix;

import android.net.Uri;
import android.text.TextUtils;
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

import static com.example.vasam.movieflix.MainActivity.LOG_TAG;

/**
 * Created by vasam on 6/18/2017.
 */

public class NetworkUtils {

    /* These utilities are used to communicate with the movie database
    */

    private static final String TAG_LOG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String API_KEY = "";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";
    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE = "en-US";
    private static final String INCLUDEADULT_PARAM = "include_adult";
    private static final String HAS_ADULT_MOVIE = "false";
    private static final String INCLUDEVIDEO_PARAM = "include_video";
    private static final String HAS_VIDEO = "true";
    private static final String IMAGE_SIZE = "w500";
    private static final String GREATER_RELEASE_DATE_PARAM = "release_date.gte";
    private static final String GREATER_RELEASE_DATE = "2016-01-01";
    private static final String LEAST_RELEASE_DATE_PARAM = "release_date.lte";
    private static final String LEAST_RELEASE_DATE = "2017-06-01";
    private static final String GREATER_VOTE_AVERAGE_PARAM = "vote_average.gte";
    private static final String GREATER_VOTE_AVERAGE = "7";

    private NetworkUtils() {
    }

    /**
     * this method is used to build an image url
     * before making a call to the movie database
     *
     * @param imagePath movie Image Thumbnail path
     * @return complete Url to fetch image from server
     */
    public static String buildImageUrl(String imagePath) {
        Uri buildUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .build();
        Uri uri = Uri.withAppendedPath(buildUri, imagePath);
        return uri.toString();
    }

    /**
     * this method is used to construct a complete url used for fetching list of movies
     * based on user's option from database.
     *
     * @param userValue menu option id when user selects any menu options
     * @return url used to retrieve list of movies.
     */
    public static URL buildUrl(int userValue) {

        Uri pathUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(GREATER_RELEASE_DATE_PARAM, GREATER_RELEASE_DATE)
                .appendQueryParameter(LEAST_RELEASE_DATE_PARAM, LEAST_RELEASE_DATE)
                .appendQueryParameter(INCLUDEADULT_PARAM, HAS_ADULT_MOVIE)
                .appendQueryParameter(INCLUDEVIDEO_PARAM, HAS_VIDEO).build();
        URL updatedUrl = null;

        switch (userValue) {
            case R.id.action_rate:
                try {
                    updatedUrl = new URL((pathUri.buildUpon().
                            appendQueryParameter(GREATER_VOTE_AVERAGE_PARAM, GREATER_VOTE_AVERAGE).build()).toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_popular:
            default:
                try {
                    updatedUrl = new URL(pathUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return updatedUrl;
    }

    /**
     * this method is used to make httpconnection using the url,
     * read the stream and return the result i.e jasonresponse
     *
     * @param url used to contact the movie database server
     * @return modified JASONResponse (into user readable format) which received from the server.
     * @throws IOException
     */
    public static String getJSONResponseFromHTTPConnection(URL url) throws IOException {
        HttpURLConnection urlConnection;
        InputStream inputStream = null;
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        urlConnection = (HttpURLConnection) url.openConnection();
        try {
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    jsonResponse = scanner.next();
                } else {
                    return null;
                }
            } else {
                Log.v(TAG_LOG, "error code :" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "network connection error", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * this method parses the JsonResponse that was received
     * @param jsonResponse received from getJSONResponseFromHTTPConnection method
     * @return String array containing all the required movie details.
     * @throws JSONException
     */
    public static String[] getMovieDataFromJSONResponse(String jsonResponse) throws JSONException {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
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
            parsedMovieData[i] = image_path + "=" + movie_title + "=" + user_rating + "=" + overview + "=" + release_date;
        }

        return parsedMovieData;
    }

}

