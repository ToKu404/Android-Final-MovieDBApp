package com.example.final_task_mobile.adapters.onclick;

import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;

public interface OnItemClickListener {
    void onItemClick(FavoriteMovie favoriteMovie);
    void onItemClick(FavoriteTv favoriteTv);
    void onItemClick(Movie movie);
    void onItemClick(TvShow tv);
}
