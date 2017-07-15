package com.example.vasam.movieflix.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vasam.movieflix.data.MovieContract.MovieEntry;


/**
 * Created by vasam on 7/12/2017.
 */

public class MovieContentProvider extends ContentProvider {
    private MovieDbHelper movieDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieEntry.CONTENT_AUTHORITY, MovieEntry.PATH_TABLENAME, MOVIES);
        sUriMatcher.addURI(MovieEntry.CONTENT_AUTHORITY, MovieEntry.PATH_TABLENAME + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MOVIES:
                cursor = database.query(MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null,null);
                break;
            case MOVIE_ID:
                selection = MovieEntry.MOVIE_ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown uri"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MOVIES:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("cannot getType unknown uri"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MOVIES:
                long id = database.insert(MovieEntry.TABLE_NAME,null,values);
                if(id==-1)
                {
                    Log.e("ContentProvider", "Failed to insert row for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("cannot insert data unknown uri"+uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match)
        {
            case MOVIES:
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME,selection,selectionArgs);
                if(rowsDeleted!=0)
                {
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;

            case MOVIE_ID:
                selection = MovieEntry.MOVIE_ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=database.delete(MovieEntry.TABLE_NAME,selection,selectionArgs);
                if(rowsDeleted!=0)
                {
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("cannot delete unknown uri "+uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
