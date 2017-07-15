package com.example.vasam.movieflix;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vasam.movieflix.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by vasam on 7/14/2017.
 */

public class CustomFavoritesAdapter extends RecyclerView.Adapter<CustomFavoritesAdapter.FavoritesViewHolder> {
    private Cursor mCursorData;

    private final CustomFavoritesAdapterOnClickHandler mClickHandler;

    public interface CustomFavoritesAdapterOnClickHandler {
        void onClickItem(Movie movie_item);
    }

    public CustomFavoritesAdapter(CustomFavoritesAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        mCursorData.moveToPosition(position);
        String movieImage_path = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_IMAGE_PATH));
        String url = NetworkUtils.buildImageUrl(movieImage_path);
        Picasso.with(holder.context).load(url).placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursorData == null) {
            return 0;
        } else return mCursorData.getCount();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieImageView;
        final Context context;

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movieObject = getMovieObject(position);
            mClickHandler.onClickItem(movieObject);
        }
    }

    public Movie getMovieObject(int position) {
        mCursorData.moveToPosition(position);
        int movie_id = mCursorData.getInt(mCursorData.getColumnIndex(MovieEntry.MOVIE_ID));
        String movie_title = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_TITLE));
        String imagePath = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_IMAGE_PATH));
        String date = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_DATE));
        int rating = mCursorData.getInt(mCursorData.getColumnIndex(MovieEntry.MOVIE_RATING));
        int duration = mCursorData.getInt(mCursorData.getColumnIndex(MovieEntry.MOVIE_DURATION));
        String overview = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_OVERVIEW));
        String videoKey = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_TRAILER_KEY));
        String reviews = mCursorData.getString(mCursorData.getColumnIndex(MovieEntry.MOVIE_REVIEWS));
        String reviewArray[] = reviews.split("=");
        Movie movie = new Movie(movie_id, movie_title, imagePath, date, rating, overview, videoKey, reviewArray, duration);
        return movie;
    }

    public void setFavoriteData(Cursor cursorData) {
        if (mCursorData != null) {
            mCursorData.close();
        }
        mCursorData = cursorData;
        if (cursorData != null) {
            this.notifyDataSetChanged();
        }
    }
}
