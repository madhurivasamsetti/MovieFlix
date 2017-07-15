package com.example.vasam.movieflix.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vasam on 7/12/2017.
 */

public final class MovieContract {
    private MovieContract() {

    }

    public static class MovieEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.vasam.movieflix";

        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_TABLENAME = "movies";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TABLENAME);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLENAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLENAME;

        public static final String TABLE_NAME = "movies";
        public static final String _ID = BaseColumns._ID;
        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_IMAGE_PATH = "imagePath";
        public static final String MOVIE_DATE = "date";
        public static final String MOVIE_RATING = "ratings";
        public static final String MOVIE_DURATION = "duration";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_TRAILER_KEY = "movieKey";
        public static final String MOVIE_REVIEWS = "reviews";
    }

}
