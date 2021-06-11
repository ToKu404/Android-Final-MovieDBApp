package com.example.final_task_mobile.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("results")
    @Expose
    private List<Videos> videoResult;


    public List<Videos> getVideoResult() {
        return videoResult;
    }
}
