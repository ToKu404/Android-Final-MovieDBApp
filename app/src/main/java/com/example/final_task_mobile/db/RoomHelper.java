package com.example.final_task_mobile.db;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.FavMovieAdapter;
import com.example.final_task_mobile.db.table.FavoriteMovie;
import com.example.final_task_mobile.db.table.FavoriteTv;

import java.util.ArrayList;
import java.util.List;

public class RoomHelper {
    private AppCompatActivity context;
    private AppDatabase roomDb;
    public List<FavoriteMovie> favoriteMovieList;
    private List<FavoriteTv> favoriteTvs;
    private FavoriteMovie favoriteMovie;
    private LifecycleOwner owner;
    private boolean status;

    public RoomHelper(Context context){
        roomDb = AppDatabase.getInstance(context);
        status = false;
    }
    public boolean checkFavMovie(int id){
        return roomDb.favoriteDao().isMovieExists(id);
    }
    public boolean checkFavTv(int id){
        return roomDb.favoriteDao().isTvExists(id);
    }
    public  boolean insertFavMovie(int id, String title, String imgPath, Float rate){
        FavoriteMovie favoriteMovie = new FavoriteMovie(id, title, imgPath,rate);
        roomDb.favoriteDao().addFavoriteMovie(favoriteMovie).subscribe(()->{
            status = true;
        },throwable->{
            status = false;
        });
        return status;
    }
    public boolean deleteFavMovie(int id){
        FavoriteMovie favoriteMovie = roomDb.favoriteDao().findByMovieId(id);
        roomDb.favoriteDao().deleteFavoriteMovie(favoriteMovie).subscribe(()->{
            status = true;
        },throwable->{
            status = false;
        });
        return status;
    }

    public boolean insertFavTv(int id, String title, String imgPath, Float rate){
        FavoriteTv favoriteTv = new FavoriteTv(id, title, imgPath,rate);
        roomDb.favoriteDao().addFavoriteTvShow(favoriteTv).subscribe(()->{
            status = true;
        },throwable->{
            status = false;
        });
        return status;
    }

    public boolean deleteFavTv(int id){
        FavoriteTv favoriteTv = roomDb.favoriteDao().findByTvId(id);
        roomDb.favoriteDao().deleteFavoriteTv(favoriteTv).subscribe(()->{
            status = true;
        },throwable->{
            status = false;
        });
        return status;
    }




}
