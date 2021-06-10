package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;

import java.util.List;

public interface OnMovieCallback {
    void onSuccess(int page, List<Movie> movieList);
    void onFailure(String message);
}
