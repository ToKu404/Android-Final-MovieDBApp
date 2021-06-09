package com.example.final_task_mobile.db.table;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorites_tv")
public class FavoriteTv implements Serializable {

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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @PrimaryKey
    private int id;


    private String title;

    private String imgPath;

    public FavoriteTv(){}

    public FavoriteTv(int id){
        this.id = id;
    }
    public FavoriteTv(int id, String title, String imgPath){
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
    }


}
