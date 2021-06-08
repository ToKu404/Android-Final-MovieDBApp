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
    @GET("movie/{sort_by}")
    Call<MovieResponse> getResult(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<MovieDetailModel> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );

    @GET("search/movie")
    Call<MovieResponse> search(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("movie/{movie_id}/credits")
    Call<MovieCreditModel> getMovieCast (
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/similar")
    Call<MovieSimilarResponse> getSimilarMovie (
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );


}

