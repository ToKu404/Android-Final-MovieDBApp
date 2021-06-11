package com.example.final_task_mobile.repository;

import com.example.final_task_mobile.models.CreditModel;
import com.example.final_task_mobile.models.DetailModel;
import com.example.final_task_mobile.models.Videos;
import com.example.final_task_mobile.models.VideosResponse;
import com.example.final_task_mobile.models.movie.MovieResponse;
import com.example.final_task_mobile.models.movie.MovieSimilarResponse;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.networks.MovieApiInterface;
import com.example.final_task_mobile.repository.callback.OnMovieCallback;
import com.example.final_task_mobile.repository.callback.OnCastCallback;
import com.example.final_task_mobile.repository.callback.OnDetailCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSearchCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSimilarsCallback;
import com.example.final_task_mobile.repository.callback.OnVideosCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    //attribute
    private static MovieRepository movieRepository;
    private final MovieApiInterface movieService;

    //container
    private MovieRepository(MovieApiInterface movieService){
        this.movieService = movieService;
    }

    //instance
    public static MovieRepository getRetrofit() {
        if(movieRepository==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieRepository = new MovieRepository(retrofit.create(MovieApiInterface.class));
        }
        return movieRepository;
    }

    //get movie
    public void getMovie(String sortBy, int page, final OnMovieCallback callback){
        movieService.getResult(sortBy, Const.API_KEY, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getMovieResult()!= null) {
                            callback.onSuccess(response.body().getPage(), response.body().getMovieResult());
                        } else {
                            callback.onFailure("response.body().getResults() is null");
                        }
                    } else {
                        callback.onFailure("response.body() is null");
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    //get movie detail
    public void getMovieDetail(int id, final OnDetailCallback callback) {
        movieService.getMovie(id, Const.API_KEY)
                .enqueue(new Callback<DetailModel>() {
                    @Override
                    public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                        System.out.println("URL :: "+ response.raw().request().url());
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onSuccess(response.body(), response.message());
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + ", Error Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailModel> call, Throwable t) {

                    }
                });
    }

    //get movie similar
    public void getMovieSimilar(int id, final OnMovieSimilarsCallback callback){
        movieService.getSimilarMovie(id, Const.API_KEY).enqueue(new Callback<MovieSimilarResponse>() {
            @Override
            public void onResponse(Call<MovieSimilarResponse> call, Response<MovieSimilarResponse> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                if(response.isSuccessful()) {
                if(response.body().getSimilarsMovie()!=null){
                    callback.onSuccess(response.body().getSimilarsMovie());
                }else {
                    callback.onFailure("NuLL");
                }
                }else {
                    callback.onFailure("null");
                }
            }
            @Override
            public void onFailure(Call<MovieSimilarResponse> call, Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    //get movie cast
    public void getMovieCast(int id, final OnCastCallback callback){
        movieService.getMovieCast(id, Const.API_KEY).enqueue(new Callback<CreditModel>() {
            @Override
            public void onResponse(Call<CreditModel> call, Response<CreditModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body(), response.message());
                    } else {
                        callback.onFailure("response.body() is null");
                    }
                } else {
                    callback.onFailure(response.message() + ", Error Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<CreditModel> call, Throwable t) {

            }
        });
    }

    //get search result
    public void searchMovie(String query, int page, final OnMovieSearchCallback callback) {
        movieService.search(Const.API_KEY, query, page)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        System.out.println("URL :: "+ response.raw().request().url());
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getMovieResult() != null) {
                                    callback.onSuccess(response.body().getMovieResult(), response.message(), response.body().getPage());
                                } else {
                                    callback.onFailure("No Results");
                                }
                            } else {
                                callback.onFailure("response.body() is null");
                            }
                        } else {
                            callback.onFailure(response.message() + " : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }

    //get movie trailer url
    public void getMovieTrailer(int id, final OnVideosCallback callback) {
        movieService.getTrailer(id, Const.API_KEY).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getVideoResult()!= null) {
                            callback.onSuccess(response.body().getVideoResult());
                        } else {
                            callback.onFailure("response.body().getResults() is null");
                        }
                    } else {
                        callback.onFailure("response.body() is null");
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {

            }
        });
    }
}
