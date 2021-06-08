package com.example.final_task_mobile.networks.movie;

import com.example.final_task_mobile.models.movie.MovieCreditModel;
import com.example.final_task_mobile.models.movie.MovieDetailModel;
import com.example.final_task_mobile.models.movie.MovieResponse;
import com.example.final_task_mobile.models.movie.MovieSimilarResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {
    @GET("now_playing")
    Call<MovieResponse> getNowPlaying(
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<MovieDetailModel> getMovie(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );
    @GET("{movie_id}/credits")
    Call<MovieCreditModel> getMovieCast (
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );

    @GET("{movie_id}/similar")
    Call<MovieSimilarResponse> getSimilarMovie (
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );
}

