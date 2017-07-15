package com.example.vasam.movieflix.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;

/**
 * Created by vasam on 7/5/2017.
 */

public class ApplicationUtils {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpwidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        return (int) (dpwidth / scalingFactor);
    }
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
    public static String updateUrl(String sortValue)
    {
        String BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=" +
                "&language=en-US&region=US&include_adult=false&release_date.gte=2016-01-01";
        final String SORT = "sort_by";
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter(SORT, sortValue);
        return builder.toString();
    }

}
