package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;

import java.util.List;

public interface OnMovieSearchCallback {
    void onSuccess(List<Movie> movies, String msg, int page);

    void onFailure(String msg);
}
