package com.example.final_task_mobile.repository;

import com.example.final_task_mobile.models.CreditModel;
import com.example.final_task_mobile.models.DetailModel;
import com.example.final_task_mobile.models.tvshow.TvShowResponse;
import com.example.final_task_mobile.models.tvshow.TvShowSimilarResponse;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.networks.TvShowApiInterface;
import com.example.final_task_mobile.repository.callback.OnCastCallback;
import com.example.final_task_mobile.repository.callback.OnDetailCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowSearchCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowSimilarsCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowRepository {
    //attribute tvshow
    private static TvShowRepository tvRepository;
    private TvShowApiInterface tvService;

    //instance
    private TvShowRepository(TvShowApiInterface tvService){
        this.tvService = tvService;
    }
    public static TvShowRepository getRetrofit() {
        if(tvRepository==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tvRepository = new TvShowRepository(retrofit.create(TvShowApiInterface.class));
        }
        return tvRepository;
    }

    //get tvshow
    public void getTvShow(String sortBy, int page, final OnTvShowCallback callback){
        tvService.getResult(sortBy, Const.API_KEY, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getTvShowResult()!= null) {
                            callback.onSuccess(response.body().getPage(), response.body().getTvShowResult());
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
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    //get tv show detail
    public void getTvShowDetail(int id, final OnDetailCallback callback) {
        tvService.getTvShow(id, Const.API_KEY)
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

    //get tv show similar
    public void getTvShowSimilar(int id, final OnTvShowSimilarsCallback callback){
        tvService.getSimilarTvShow(id, Const.API_KEY).enqueue(new Callback<TvShowSimilarResponse>() {
            @Override
            public void onResponse(Call<TvShowSimilarResponse> call, Response<TvShowSimilarResponse> response) {

                if(response.isSuccessful()) {
                if(response.body().getSimilarsTvShow()!=null){
                    callback.onSuccess(response.body().getSimilarsTvShow());
                }else {
                    callback.onFailure("NuLL");
                }
                }else {
                    callback.onFailure("null");
                }
            }

            @Override
            public void onFailure(Call<TvShowSimilarResponse> call, Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    //get tv show cast
    public void getTvShowCast(int id, final OnCastCallback callback){
        tvService.getTvCast(id, Const.API_KEY).enqueue(new Callback<CreditModel>() {
            @Override
            public void onResponse(Call<CreditModel> call, Response<CreditModel> response) {
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
            public void onFailure(Call<CreditModel> call, Throwable t) {

            }
        });
    }

    //search tv show
    public void searchTvShow(String query, int page, final OnTvShowSearchCallback callback) {
        tvService.search(Const.API_KEY, query, page)
                .enqueue(new Callback<TvShowResponse>() {
                    @Override
                    public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getTvShowResult() != null) {
                                    callback.onSuccess(response.body().getTvShowResult(), response.message(), response.body().getPage());
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
                    public void onFailure(Call<TvShowResponse> call, Throwable t) {
                        callback.onFailure(t.getLocalizedMessage());
                    }
                });
    }
}
