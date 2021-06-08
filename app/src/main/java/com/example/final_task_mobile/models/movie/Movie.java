package com.example.final_task_mobile.models.movie;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private String id;
    private String title;

    @SerializedName("poster_path")
    private String imgUrl;

    @SerializedName("vote_average")
    private String voteAverage;

    public String getVoteAverage() {
        return voteAverage;
    }

    public Movie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
