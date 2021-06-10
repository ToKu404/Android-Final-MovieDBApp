package com.example.final_task_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.db.table.FavoriteMovie;
import com.example.final_task_mobile.networks.Const;

import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.ViewHolder> {
    private List<FavoriteMovie> movieList;

    public FavMovieAdapter(List<FavoriteMovie> movieList){
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindItemView(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        FavoriteMovie movie;
        ImageView ivPoster;
        TextView tvTitle;
       RatingBar rbFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_favorite);
            tvTitle = itemView.findViewById(R.id.tv_title_fav);
            rbFavorite = itemView.findViewById(R.id.rb_favorite);
        }
        void onBindItemView(FavoriteMovie movie) {
            this.movie = movie;
            Glide.with(itemView.getContext()).load(Const.IMG_URL_200 + movie.getImgPath()).into(ivPoster);
            tvTitle.setText(movie.getTitle());

            rbFavorite.setRating(movie.getRate());
        }

    }

}
