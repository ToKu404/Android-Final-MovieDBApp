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
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.networks.Const;

import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.ViewHolder> {
    //attribute
    private List<FavoriteMovie> movieList;
    private OnItemClickListener clickListener;



    public FavMovieAdapter(List<FavoriteMovie> movieList){
        this.movieList = movieList;
    }
    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //movie favorite object
        FavoriteMovie movie;

        //attribute
        ImageView ivPoster;
        TextView tvTitle;
        RatingBar rbFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivPoster = itemView.findViewById(R.id.iv_poster_fav);
            tvTitle = itemView.findViewById(R.id.tv_title_fav);
            rbFavorite = itemView.findViewById(R.id.rb_rate_fav);
        }
        void onBindItemView(FavoriteMovie movie) {
            this.movie = movie;
            Glide.with(itemView.getContext()).load(Const.IMG_URL_200 + movie.getImgPath()).into(ivPoster);
            tvTitle.setText(movie.getTitle());

            rbFavorite.setRating(movie.getRate());
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(movie);
        }
    }

}
