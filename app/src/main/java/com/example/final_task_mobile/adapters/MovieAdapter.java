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
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.networks.Const;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.GridViewHolder> {
    //attribute adapter
    private final List<Movie> movieList;
    private OnItemClickListener clickListener;

    public MovieAdapter(List<Movie> movieList){
        this.movieList = movieList;
    }
    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card_recycler, parent, false);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.onBindItemView(movieList.get(position));
    }

    public void appendList(List<Movie> listToAppend) {
        movieList.addAll(listToAppend);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //attribute holder
        Movie movie;

        //widget holder
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvVoteAverage;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //set layout for widget
            ivPoster = itemView.findViewById(R.id.iv_main_card_poster);
            tvTitle = itemView.findViewById(R.id.tv_main_card_title);
            tvVoteAverage = itemView.findViewById(R.id.tv_main_card_vote);
        }
        void onBindItemView(Movie movie) {
            //set value
            this.movie = movie;
            Glide.with(itemView.getContext()).load(Const.IMG_URL_200 + movie.getImgUrl()).into(ivPoster);
            tvTitle.setText(movie.getTitle());
            tvVoteAverage.setText(movie.getVoteAverage());
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(movie);
        }
    }

}
