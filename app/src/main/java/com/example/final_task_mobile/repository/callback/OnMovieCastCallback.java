package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.movie.MovieCreditModel;
import com.example.final_task_mobile.models.movie.MovieDetailModel;

public interface OnMovieCastCallback {
    void onSuccess(MovieCreditModel creditModel, String message);

    void onFailure(String message);
}
