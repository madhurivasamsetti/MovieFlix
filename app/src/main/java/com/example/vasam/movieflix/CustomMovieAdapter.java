package com.example.vasam.movieflix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vasam on 6/18/2017.
 */

public class CustomMovieAdapter extends RecyclerView.Adapter<CustomMovieAdapter.MovieViewHolder> {
    private final CustomMovieAdapterOnClickHandler mClickHandler;
    private List<Movie> mMovieData;


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
        Movie movie_objects = mMovieData.get(position);
        String movieImage_path = movie_objects.getmMovieImagePath();
        String url = NetworkUtils.buildImageUrl(movieImage_path);
        Picasso.with(holder.context).load(url).placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) {
            return 0;
        }
        return mMovieData.size();
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    /**
     * declaring an clickHandler interface which can be invoked by the viewHolder
     * in this class when an item in the adapter gets clicked.
     */
    public interface CustomMovieAdapterOnClickHandler {
        void onClickItem(Movie movie_item);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final Context context;
        ImageView mMovieImageView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movieObject = mMovieData.get(position);
            mClickHandler.onClickItem(movieObject);
        }
    }
}
