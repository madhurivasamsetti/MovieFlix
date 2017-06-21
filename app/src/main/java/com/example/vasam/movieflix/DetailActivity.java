package com.example.vasam.movieflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView mMovieTitleView;
    private ImageView mMovieImageView;
    private TextView mMovieOverview;
    private TextView mMovieRatingView;
    private TextView mMovieReleaseDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleView = (TextView) findViewById(R.id.movie_title);
        mMovieImageView = (ImageView) findViewById(R.id.detail_image);
        mMovieRatingView = (TextView) findViewById(R.id.user_rating);
        mMovieReleaseDateView = (TextView) findViewById(R.id.release_date);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieItem = intent.getStringExtra(Intent.EXTRA_TEXT);
            loadDetailLayout(movieItem);
        }

    }

    /**
     * this methods is responsible for populating the views in activity_detail.xml
     * layout.
     * @param data movie details received through intent
     */
    private void loadDetailLayout(String data) {
        String parts[] = data.split("=");
        String movieImage_path = parts[0];
        String url = NetworkUtils.buildImageUrl(movieImage_path);
        Picasso.with(this).load(url).into(mMovieImageView);
        mMovieTitleView.setText(parts[1]);
        mMovieRatingView.setText(parts[2]);
        mMovieOverview.setText(parts[3]);
        mMovieReleaseDateView.setText(parts[4]);


    }
}
