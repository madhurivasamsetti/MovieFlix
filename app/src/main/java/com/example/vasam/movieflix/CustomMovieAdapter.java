package com.example.vasam.movieflix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by vasam on 6/18/2017.
 */

public class CustomMovieAdapter extends RecyclerView.Adapter<CustomMovieAdapter.MovieViewHolder> {
    private String[] mMovieData;
    private final CustomMovieAdapterOnClickHandler mClickHandler;

    /**
     * declaring an clickHandler interface which can be invoked by the viewHolder
     * in this class when an item in the adapter gets clicked.
     */
    public interface CustomMovieAdapterOnClickHandler {
        void onClickItem(String movie_item);
    }

    public CustomMovieAdapter(CustomMovieAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        String movieItem = mMovieData[position];
        String part[] = movieItem.split("=");
        String movieImage_path = part[0];
        String url = NetworkUtils.buildImageUrl(movieImage_path);
        Picasso.with(holder.context).load(url).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) {
            return 0;
        }
        return mMovieData.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieImageView;
        final Context context;

        private MovieViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String movieDataItem = mMovieData[position];
            mClickHandler.onClickItem(movieDataItem);
        }
    }

    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

}
