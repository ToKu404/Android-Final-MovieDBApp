package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.Movie;

import java.util.List;

public interface OnMovieSimilarsCallback {

    void onFailure(String message);

    void onSuccess(List<Movie> similarsMovie);
}
