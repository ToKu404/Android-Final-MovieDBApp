package com.example.final_task_mobile.models.movie;


import com.example.final_task_mobile.models.Cast;

import java.util.List;

public class MovieCreditModel {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    private int id;
    private List<Cast> cast;
}