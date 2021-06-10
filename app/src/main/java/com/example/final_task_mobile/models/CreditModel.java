package com.example.final_task_mobile.models;


import com.example.final_task_mobile.models.Cast;

import java.util.List;

public class CreditModel {
    private int id;
    private List<Cast> cast;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }
}