package com.example.final_task_mobile.models.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    @Expose
    private List<Movie> nowPlayings;

    public List<Movie> getNowPlayings() {
        return nowPlayings;
    }
}
