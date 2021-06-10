package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;

import java.util.List;

public interface OnTvShowCallback {
    void onSuccess(int page, List<TvShow> tvList);
    void onFailure(String message);
}
