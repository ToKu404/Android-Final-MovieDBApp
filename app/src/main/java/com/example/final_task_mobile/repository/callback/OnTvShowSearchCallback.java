package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;

import java.util.List;

public interface OnTvShowSearchCallback {
    void onSuccess(List<TvShow> movies, String msg, int page);
    void onFailure(String msg);
}
