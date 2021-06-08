package com.example.final_task_mobile.models.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSimilarResponse {
    @SerializedName("results")
    @Expose
    private List<Movie> similars;

    public List<Movie> getSimilarsMovie() {
        return similars;
    }
}
