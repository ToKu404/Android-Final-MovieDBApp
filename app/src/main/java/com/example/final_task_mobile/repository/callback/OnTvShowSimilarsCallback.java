package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;

import java.util.List;

public interface OnTvShowSimilarsCallback {
    void onSuccess(List<TvShow> similarsTv);
    void onFailure(String message);
}
