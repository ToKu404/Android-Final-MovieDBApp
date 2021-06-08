package com.example.final_task_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.CastAdapter;
import com.example.final_task_mobile.adapters.GenreAdapter;
import com.example.final_task_mobile.adapters.MovieAdapter;
import com.example.final_task_mobile.adapters.OnItemClickListener;
import com.example.final_task_mobile.models.Cast;
import com.example.final_task_mobile.models.Genre;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.movie.MovieCreditModel;
import com.example.final_task_mobile.models.movie.MovieDetailModel;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.repository.MovieRepository;
import com.example.final_task_mobile.repository.callback.OnMovieCastCallback;
import com.example.final_task_mobile.repository.callback.OnMovieDetailCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSimilarsCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {
    private MovieRepository movieRepository;
    private List<Cast> listMovieCast;
    private ImageView ivPoster, ivBackdrop;
    private TextView tvTitle, tvYear, tvDuration, tvSinopsis, tvRating, tvMore, tvHeaderSimilar;
    private RatingBar ratingBar;
    private RecyclerView rvGenre, rvCast, rvSimilar;
    private ArrayList<String> genres;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);


        genres = new ArrayList<>();
        listMovieCast = new ArrayList<>();
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
        if (getIntent() != null) {
            id = getIntent().getIntExtra("ID", 0);
        }
        movieRepository = MovieRepository.getRetrofit();
        loadData(id);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_detail_activity, menu);
        // TODO: switch favourite button state
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainActivity = new Intent(DetailMovieActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            case R.id.item_favorite:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData(int id) {

        movieRepository.getMovieDetail(id, new OnMovieDetailCallback() {
            @Override
            public void onSuccess(MovieDetailModel movie, String message) {
                addValue(movie);
            }
            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void addValue(MovieDetailModel movieDetailModel) {
        configureActionBar(movieDetailModel.getTitle());
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
        loadCastData(id);
        loadSimilarMovieData(id);


    }

    private void configureActionBar(String title) {
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1A212F"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadSimilarMovieData(int id) {
        movieRepository.getMovieSimilar(id, new OnMovieSimilarsCallback() {
            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onSuccess(List<Movie> similarsMovie) {
                if(similarsMovie.size()==0){
                    tvHeaderSimilar.setVisibility(View.GONE);
                }
                rvSimilar.setLayoutManager(new LinearLayoutManager(DetailMovieActivity.this, RecyclerView.HORIZONTAL,false));
                rvSimilar.setAdapter(new MovieAdapter(similarsMovie));
            }
        });
    }

    private void loadCastData(int id) {
        movieRepository.getMovieCast(id, new OnMovieCastCallback() {
            @Override
            public void onSuccess(MovieCreditModel creditModel, String message) {
                listMovieCast = creditModel.getCast();
                rvCast.setLayoutManager(new LinearLayoutManager(DetailMovieActivity.this, RecyclerView.HORIZONTAL,false));
                rvCast.setAdapter(new CastAdapter(listMovieCast, DetailMovieActivity.this));
            }

            @Override
            public void onFailure(String message) {

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
    public void onClick(Movie movie) {
        Intent detailActivity = new Intent(this, DetailMovieActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        startActivity(detailActivity);
        finish();
    }
}