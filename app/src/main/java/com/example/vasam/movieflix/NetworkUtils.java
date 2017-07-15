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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.vasam.movieflix.MainActivity.LOG_TAG;

/**
 * Created by vasam on 6/18/2017.
 */

public class NetworkUtils {

    /* These utilities are used to communicate with the movie database
    */

    private static final String TAG_LOG = NetworkUtils.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";
    private static final String IMAGE_SIZE = "w500";


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

    public static URL buildUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            Log.e(LOG_TAG, "exception thrown during url parse", e);
        }

        return url;
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


    public static List<Movie> fetchMovieData(String requestedUrl) throws JSONException {

        URL url = buildUrl(requestedUrl);
        String jsonResponse = null;

        try {
            jsonResponse = getJSONResponseFromHTTPConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geteDataFromJSONResponse(jsonResponse);
    }

    public static List<Object> fetchExtraMovieData(String extraDataUrl) {

        URL updatedUrl = buildUrl(extraDataUrl);
        String extraJsonResponse = null;

        try {
            extraJsonResponse = getJSONResponseFromHTTPConnection(updatedUrl);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return extractFromextraJsonResponse(extraJsonResponse);
    }

    private static List<Object> extractFromextraJsonResponse(String extraJsonResponse) {
        List<Object> extra_data = new ArrayList<Object>();
        if (TextUtils.isEmpty(extraJsonResponse)) {
            return null;
        }
        String videoKey;
        int duration;
        JSONObject root;

        try {
            root = new JSONObject(extraJsonResponse);
            if (!root.isNull("runtime")) {
                duration = root.getInt("runtime");
                Log.d("NetworkUtils.class", "duration: " + duration);
                extra_data.add(0, duration);
            } else {
                extra_data.add(0, 0);
            }
            JSONObject videosObject = root.getJSONObject("videos");
            JSONArray videosArray = videosObject.getJSONArray("results");
            if (videosArray.length() < 1) {
                videoKey = "NoVideoAvailable";
            } else {
                JSONObject videoObject = videosArray.getJSONObject(0);
                videoKey = videoObject.getString("key");
            }
            extra_data.add(1, videoKey);

            JSONObject reviewsObject = root.getJSONObject("reviews");
            JSONArray reviewsArray = reviewsObject.getJSONArray("results");
            if (reviewsArray.length() < 1) {
                String[] reviews = {"No reviews found"};
                extra_data.add(2, reviews);
            } else {
                String[] reviews = new String[reviewsArray.length()];
                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject reviewObject = reviewsArray.getJSONObject(i);
                    reviews[i] = reviewObject.getString("content");
                }
                extra_data.add(2, reviews);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extra_data;
    }


    public static List<Movie> geteDataFromJSONResponse(String jsonResponse) throws JSONException {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray results = root.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            int movie_id = object.getInt("id");
            String movie_title = object.getString("original_title");
            String image_path = object.getString("poster_path");
            String release_date = object.getString("release_date");
            int user_rating = object.getInt("vote_average");
            String overview = object.getString("overview");

            String base_url = "https://api.themoviedb.org/3/movie/" + movie_id
                    + "?api_key=&language=en-US" +
                    "&append_to_response=reviews,videos";

            List<Object> extra_data = fetchExtraMovieData(base_url);
            int duration = (int) extra_data.get(0);
            String video_key = (String) extra_data.get(1);
            String[] reviews = (String[]) extra_data.get(2);
            Movie movie = new Movie(movie_id, movie_title, image_path, release_date, user_rating,
                    overview, video_key, reviews, duration);
            movies.add(movie);
        }
        return movies;
    }
}


