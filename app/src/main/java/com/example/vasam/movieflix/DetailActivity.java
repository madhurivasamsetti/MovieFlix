package com.example.vasam.movieflix;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vasam.movieflix.data.MovieContract.MovieEntry;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitleView;
    private ImageView mMovieImageView;
    private TextView mMovieOverview;
    private TextView mMovieRatingView;
    private TextView mMovieReleaseDateView;
    private TextView mMovieDurationView;
    private TextView mMovieReviewsView;
    private ImageView mFavoriteView;
    private Movie movie_object;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMovieTitleView = (TextView) findViewById(R.id.movie_title);
        mMovieImageView = (ImageView) findViewById(R.id.detail_image);
        mMovieRatingView = (TextView) findViewById(R.id.user_rating);
        mMovieReleaseDateView = (TextView) findViewById(R.id.release_date);
        mMovieDurationView = (TextView) findViewById(R.id.duration);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        mMovieReviewsView = (TextView) findViewById(R.id.reviews);
        mFavoriteView = (ImageView) findViewById(R.id.favorite);
        movie_object = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        if (checkPresentMovieInDatabase()) {
            Drawable drawable = getDrawable(R.drawable.ic_action_favorite);
            drawable.setTint(ContextCompat.getColor(context, R.color.checkedState));
            mFavoriteView.setImageResource(R.drawable.ic_action_favorite);
        } else {
            mFavoriteView.setImageResource(R.drawable.ic_action_unfavorite);
        }
        loadDetailView();

        mFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPresentMovieInDatabase()) {

                    Drawable drawable = getDrawable(R.drawable.ic_action_favorite);
                    drawable.setTint(ContextCompat.getColor(context, R.color.checkedState));
                    mFavoriteView.setImageResource(R.drawable.ic_action_favorite);
                    saveMovieToDb();
                } else {
                    mFavoriteView.setImageResource(R.drawable.ic_action_unfavorite);
                    deleteMovieFromDb();
                }
            }
        });
    }

    public boolean checkPresentMovieInDatabase() {
        boolean isPresent = false;
        String[] projection = {MovieEntry.MOVIE_TITLE};
        Uri updatedUri = MovieEntry.CONTENT_URI.buildUpon().
                appendPath(Integer.toString(movie_object.getmMovieId())).build();
        Cursor cursor = getContentResolver().query(updatedUri, projection, null, null, null);

        if (cursor.getCount() != 0) {
            isPresent = true;
        }
        cursor.close();
        return isPresent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDetailView() {
        String url = NetworkUtils.buildImageUrl(movie_object.getmMovieImagePath());
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mMovieImageView);
        if (movie_object.getmMovieDuration() == 0) {
            mMovieDurationView.setText("N/A");
        } else {
            mMovieDurationView.setText(String.valueOf(movie_object.getmMovieDuration()));
            mMovieDurationView.append("min");
        }

        mMovieTitleView.setText(movie_object.getmMovieTitle());
        mMovieRatingView.setText(String.valueOf(movie_object.getmMovieRating()));
        mMovieRatingView.append("/10");
        mMovieOverview.setText(movie_object.getmMovieOverview());
        String date[] = movie_object.getmMovieDate().split("-");
        String year = date[0];
        mMovieReleaseDateView.setText(year);
        String reviews[] = movie_object.getmMovieReview();
        mMovieReviewsView.setText("");
        for (int i = 0; i < reviews.length; i++) {
            mMovieReviewsView.append(reviews[i] + "\n");
        }
    }

    public void playVideo(View view) {
        String movieKey = movie_object.getmMovieKey();
        String url = "https://www.youtube.com/watch?V=";
        Uri uri = Uri.parse(url);

        if (movieKey.equals("NoVideoAvailable")) {
            Toast.makeText(context, "No Video Available", Toast.LENGTH_SHORT).show();
            return;
        } else if (movieKey.contains("https://")) {
            uri = Uri.parse(movie_object.getmMovieKey());
            movieKey = uri.getLastPathSegment();
        }
        Uri videoQuery = Uri.withAppendedPath(uri, movieKey);

        Intent intent = new Intent(Intent.ACTION_VIEW, videoQuery);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void saveMovieToDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.MOVIE_ID, movie_object.getmMovieId());
        contentValues.put(MovieEntry.MOVIE_TITLE, movie_object.getmMovieTitle());
        contentValues.put(MovieEntry.MOVIE_IMAGE_PATH, movie_object.getmMovieImagePath());
        contentValues.put(MovieEntry.MOVIE_DATE, movie_object.getmMovieDate());
        contentValues.put(MovieEntry.MOVIE_RATING, movie_object.getmMovieRating());
        contentValues.put(MovieEntry.MOVIE_DURATION, movie_object.getmMovieDuration());
        contentValues.put(MovieEntry.MOVIE_OVERVIEW, movie_object.getmMovieOverview());
        contentValues.put(MovieEntry.MOVIE_TRAILER_KEY, movie_object.getmMovieKey());

        String[] reviews = movie_object.getmMovieReview();

        StringBuilder appendedReviews = new StringBuilder();
        for (int i = 0; i < reviews.length; i++) {
            appendedReviews.append(reviews[i]);
            if (i != (reviews.length - 1)) {
                appendedReviews.append("=");
            }
        }
        contentValues.put(MovieEntry.MOVIE_REVIEWS, appendedReviews.toString());
        Uri insertedUri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if (insertedUri == null) {
            Toast.makeText(this, "Error while inserting", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "saved to favorites", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteMovieFromDb() {

        Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movie_object.getmMovieId());
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        if (rowsDeleted == 0) {
            Toast.makeText(this, "error while deleting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "removed from favorites", Toast.LENGTH_SHORT).show();
        }

    }
}
