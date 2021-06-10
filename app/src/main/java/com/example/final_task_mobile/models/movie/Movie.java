package com.example.final_task_mobile.models.movie;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
