package com.example.final_task_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.CastAdapter;
import com.example.final_task_mobile.adapters.GenreAdapter;
import com.example.final_task_mobile.adapters.MovieAdapter;
import com.example.final_task_mobile.fragments.MovieFragment;
import com.example.final_task_mobile.models.Cast;
import com.example.final_task_mobile.models.Genre;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.movie.MovieCreditModel;
import com.example.final_task_mobile.models.movie.MovieDetailModel;
import com.example.final_task_mobile.models.movie.MovieResponse;
import com.example.final_task_mobile.models.movie.MovieSimilarResponse;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.networks.movie.MovieApiClient;
import com.example.final_task_mobile.networks.movie.MovieApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener, MovieAdapter.OnItemClick {
    private MovieDetailModel movieDetailModel;
    private MovieCreditModel creditModel;
    private List<Cast> listMovieCast;
    private List<Movie> listSimilarMovie;
    private ImageView ivPoster, ivBackdrop;
    private TextView tvTitle, tvYear, tvDuration, tvSinopsis, tvRating, tvMore, tvHeaderSimilar;
    private RatingBar ratingBar;
    private RecyclerView rvGenre, rvCast, rvSimilar;
    private ArrayList<String> genres;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1A212F"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        genres = new ArrayList<>();
        listMovieCast = new ArrayList<>();
        listSimilarMovie = new ArrayList<>();
        ivPoster = findViewById(R.id.iv_mv_poster);
        ivBackdrop = findViewById(R.id.iv_mv_backdrop);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvYear = findViewById(R.id.tv_detail_year);
        tvHeaderSimilar = findViewById(R.id.tv_similar_header);
        tvDuration = findViewById(R.id.tv_detail_duration);
        tvSinopsis = findViewById(R.id.tv_detail_synopsis);
        tvMore = findViewById(R.id.tv_detail_more);
        ratingBar = findViewById(R.id.rb_detail);
        tvRating = findViewById(R.id.tv_detail_score);
        rvGenre = findViewById(R.id.rv_genre);
        rvCast = findViewById(R.id.rv_cast);
        rvSimilar = findViewById(R.id.rv_similar_movie);
        id = getIntent().getStringExtra("ID");
        loadData();
    }

    private void loadData() {
        System.out.println("ID : "+id);
        MovieApiInterface movieApiInterface = MovieApiClient.getMovieDetail().create(MovieApiInterface.class);
        Call<MovieDetailModel> movieCall = movieApiInterface.getMovie(id, Const.API_KEY);
        movieCall.enqueue(new Callback<MovieDetailModel>() {
            @Override
            public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                if (response.isSuccessful()&& response.body() != null)
                {
                    movieDetailModel = response.body();
                    addValue();
                }
                else
                {
                    Toast.makeText(DetailMovieActivity.this,"Request Error :: " + response.errorBody(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetailModel> call, Throwable t) {
                Toast.makeText(DetailMovieActivity.this,"Network Error :: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addValue() {
        tvTitle.setText(movieDetailModel.getTitle());
        tvSinopsis.setText(movieDetailModel.getOverview());
        tvYear.setText(movieDetailModel.getYear());
        tvDuration.setText(movieDetailModel.getDuration());
        Glide.with(DetailMovieActivity.this).load(Const.IMG_URL_300+ movieDetailModel.getPoster()).into(ivPoster);
        Glide.with(DetailMovieActivity.this).load(Const.IMG_URL_300+ movieDetailModel.getBackdrop()).into(ivBackdrop);
        ratingBar.setRating(movieDetailModel.getRating());
        tvRating.setText(movieDetailModel.getVoteAverage());
        tvSinopsis.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        setGenres(movieDetailModel.getGenres());
        rvGenre.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvGenre.setAdapter(new GenreAdapter(genres, this));
        loadCastData();
        loadSimilarMovieData();


    }

    private void loadSimilarMovieData() {
        MovieApiInterface movieApiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
        Call<MovieSimilarResponse> similarCall = movieApiInterface.getSimilarMovie(id, Const.API_KEY);
        similarCall.enqueue(new Callback<MovieSimilarResponse>() {
            @Override
            public void onResponse(Call<MovieSimilarResponse> call, Response<MovieSimilarResponse> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                    listSimilarMovie = response.body().getSimilarsMovie();
                    if(listSimilarMovie.size()==0){
                        tvHeaderSimilar.setVisibility(View.GONE);
                    }
                    rvSimilar.setLayoutManager(new LinearLayoutManager(DetailMovieActivity.this, RecyclerView.HORIZONTAL,false));
                    rvSimilar.setAdapter(new MovieAdapter(listSimilarMovie, DetailMovieActivity.this));
            }

            @Override
            public void onFailure(Call<MovieSimilarResponse> call, Throwable t) {

            }
        });
    }

    private void loadCastData() {
        MovieApiInterface movieApiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
        Call<MovieCreditModel> castCall = movieApiInterface.getMovieCast(id, Const.API_KEY);
        castCall.enqueue(new Callback<MovieCreditModel>() {
            @Override
            public void onResponse(Call<MovieCreditModel> call, Response<MovieCreditModel> response) {
                System.out.println("URL :: "+ response.raw().request().url());
                creditModel = response.body();
                listMovieCast = creditModel.getCast();
                rvCast.setLayoutManager(new LinearLayoutManager(DetailMovieActivity.this, RecyclerView.HORIZONTAL,false));
                rvCast.setAdapter(new CastAdapter(listMovieCast, DetailMovieActivity.this));
            }

            @Override
            public void onFailure(Call<MovieCreditModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_detail_synopsis:
                actionExpand();
                break;
           case  R.id.tv_detail_more:
                actionExpand();
                break;
        }
    }

    private void actionExpand() {
        if (tvMore.getText().toString().equalsIgnoreCase("less")){
            tvSinopsis.setMaxLines(2);
            tvMore.setText("more");
        } else {
            tvSinopsis.setMaxLines(Integer.MAX_VALUE);
            tvMore.setText("less");
        }
    }
    private void setGenres(List<Genre> genresList){
        for(int i = 0; i< genresList.size(); i++){
            genres.add(genresList.get(i).getName());
        }
    }

    @Override
    public void onClick(int pos) {
        Intent detailActivity = new Intent(this, DetailMovieActivity.class);
        detailActivity.putExtra("ID", listSimilarMovie.get(pos).getId());
        detailActivity.putExtra("TITLE", listSimilarMovie.get(pos).getTitle());
        startActivity(detailActivity);
        finish();
    }
}