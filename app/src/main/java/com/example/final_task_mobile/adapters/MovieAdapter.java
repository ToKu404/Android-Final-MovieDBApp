package com.example.final_task_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.networks.Const;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.GridViewHolder> {
    private List<Movie> movieList;
    private OnItemClick onItemClick;

    public MovieAdapter(List<Movie> movieList, OnItemClick onItemClick){
        this.movieList = movieList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_recycler, parent, false);
        return new GridViewHolder(v, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(Const.IMG_URL_200 + movieList.get(position).getImgUrl()).into(holder.ivPoster);
        holder.tvTitle.setText(movieList.get(position).getTitle());
        holder.tvVoteAverage.setText(movieList.get(position).getVoteAverage());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClick onItemClick;
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvVoteAverage;

        public GridViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivPoster = itemView.findViewById(R.id.iv_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvVoteAverage = itemView.findViewById(R.id.tv_movie_vote);
            this.onItemClick = onItemClick;
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(getAdapterPosition());

        }
    }
    public interface OnItemClick {
        void onClick(int pos);
    }
}
