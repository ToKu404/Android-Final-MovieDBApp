package com.example.final_task_mobile.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.final_task_mobile.db.table.FavoriteMovie;
import com.example.final_task_mobile.db.table.FavoriteTv;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorites_movie")
    LiveData<List<FavoriteMovie>> getAllMovie();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavoriteMovie(FavoriteMovie favoriteMovies);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM favorites_tv")
    LiveData<List<FavoriteTv>> getAllTvSow();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavoriteTvShow(FavoriteTv favoritetv);

    @Delete
    void deleteFavoriteMovie(FavoriteTv favoriteTv);
}
