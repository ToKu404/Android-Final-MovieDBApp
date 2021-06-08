package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.MovieDetailModel;

public interface OnMovieDetailCallback {
    void onSuccess(MovieDetailModel movie, String message);

    void onFailure(String message);
}
