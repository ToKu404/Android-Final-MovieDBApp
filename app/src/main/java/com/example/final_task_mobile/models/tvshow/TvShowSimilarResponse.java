package com.example.final_task_mobile.models.tvshow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowSimilarResponse {
    @SerializedName("results")
    @Expose
    private List<TvShow> similars;

    public List<TvShow> getSimilarsTvShow() {
        return similars;
    }
}
