package com.example.final_task_mobile.local;

import android.content.Context;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
import java.util.List;

public class RoomHelper {
    private final AppDatabase roomDb;
    private boolean status;

    public RoomHelper(Context context){
        roomDb = AppDatabase.getInstance(context);
        status = false;
    }
    //read favorite
    public List<FavoriteMovie> readFavMovie(){
        return roomDb.favoriteDao().getAllMovie();
    }
    public List<FavoriteTv> readFavTv() {
        return roomDb.favoriteDao().getAllTvSow();
    }

    //checker favorite
    public boolean checkFavMovie(int id){
        return roomDb.favoriteDao().isMovieExists(id);
    }
    public boolean checkFavTv(int id){
        return roomDb.favoriteDao().isTvExists(id);
    }

    //insert favorite
    public  boolean insertFavMovie(int id, String title, String imgPath, Float rate){
        FavoriteMovie favoriteMovie = new FavoriteMovie(id, title, imgPath,rate);
        roomDb.favoriteDao().addFavoriteMovie(favoriteMovie).subscribe(()->{
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

    //delete favorite
    public boolean deleteFavMovie(int id){
        FavoriteMovie favoriteMovie = roomDb.favoriteDao().findByMovieId(id);
        roomDb.favoriteDao().deleteFavoriteMovie(favoriteMovie).subscribe(()->{
            status = true;
        },throwable->{
            status = false;
        });
        return status;
    }
    public boolean deleteFavTv(int id) {
        FavoriteTv favoriteTv = roomDb.favoriteDao().findByTvId(id);
        roomDb.favoriteDao().deleteFavoriteTv(favoriteTv).subscribe(() -> {
            status = true;
        }, throwable -> {
            status = false;
        });
        return status;
    }
}
