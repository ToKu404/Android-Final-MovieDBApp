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
import com.example.final_task_mobile.adapters.OnMovieItemClickListener;
import com.example.final_task_mobile.adapters.OnTvShowItemClickListener;
import com.example.final_task_mobile.adapters.TvShowAdapter;
import com.example.final_task_mobile.db.AppDatabase;
import com.example.final_task_mobile.models.Cast;
import com.example.final_task_mobile.models.Genre;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.CreditModel;
import com.example.final_task_mobile.models.DetailModel;
import com.example.final_task_mobile.models.tvshow.TvShow;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.repository.MovieRepository;
import com.example.final_task_mobile.repository.TvShowRepository;
import com.example.final_task_mobile.repository.callback.OnCastCallback;
import com.example.final_task_mobile.repository.callback.OnDetailCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSimilarsCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowSimilarsCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, OnMovieItemClickListener, OnTvShowItemClickListener {
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private MovieAdapter mvAdapter;
    private TvShowAdapter tvAdapter;
    private List<Cast> listCast;
    private ImageView ivPoster, ivBackdrop;
    private TextView tvTitle, tvYear, tvDuration, tvSinopsis, tvRating, tvMore, tvHeaderSimilar;
    private RatingBar ratingBar;
    private RecyclerView rvGenre, rvCast, rvSimilar;
    private ArrayList<String> genres;
    private String selectedFragment;
    private int id;
    private MenuItem itemFav;
    private boolean isFavorite = false;
    private String favoriteTitle, favoriteImgPath;
    private AppDatabase roomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        genres = new ArrayList<>();
        listCast = new ArrayList<>();
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
        roomDatabase = AppDatabase.getInstance(getApplicationContext());
        movieRepository = MovieRepository.getRetrofit();
        tvShowRepository = TvShowRepository.getRetrofit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_detail_activity, menu);
        itemFav = menu.findItem(R.id.item_favorite);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        favoriteImgPath = "";
        favoriteTitle = "";
        if (getIntent() != null) {
            id = getIntent().getIntExtra("ID", 0);
            selectedFragment = getIntent().getStringExtra("TYPE");
            loadData(id, selectedFragment);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainActivity = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            case R.id.item_favorite:
                isFavorite = !isFavorite;
                if(isFavorite){
                    itemFav.setIcon(R.drawable.ic_baseline_favorite_24);
                }else{
                    itemFav.setIcon(R.drawable.ic_baseline_favorite_border_24);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData(int id, String type) {
        if(type.equals("movie")){
            movieRepository.getMovieDetail(id, new OnDetailCallback() {
                @Override
                public void onSuccess(DetailModel movie, String message) {
                    addValue(movie, type);
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }else if(type.equals("tv")){
            tvShowRepository.getTvShowDetail(id, new OnDetailCallback() {
                @Override
                public void onSuccess(DetailModel tv, String message) {
                    addValue(tv, type);
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }

    }

    private void addValue(DetailModel detailModel, String type) {
        configureActionBar(detailModel.getTitle());
        tvTitle.setText(detailModel.getTitle());
        tvSinopsis.setText(detailModel.getOverview());
        Glide.with(DetailActivity.this).load(Const.IMG_URL_300+ detailModel.getPoster()).into(ivPoster);
        Glide.with(DetailActivity.this).load(Const.IMG_URL_300+ detailModel.getBackdrop()).into(ivBackdrop);
        ratingBar.setRating(detailModel.getRating());
        tvRating.setText(detailModel.getVoteAverage());
        tvSinopsis.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        setGenres(detailModel.getGenres());
        rvGenre.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvGenre.setAdapter(new GenreAdapter(genres, this));

        if(type.equals("movie")){
            tvYear.setText(detailModel.getYear());
            tvDuration.setText(detailModel.getDuration());
        }
        if(type.equals("tv")){
            tvYear.setText(detailModel.getEps());
            tvDuration.setText(detailModel.getStatus());
        }

        loadCastData(id, type);
        loadSimilarData(id, type);
    }

    private void configureActionBar(String title) {
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1A212F"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadSimilarData(int id, String type) {
        if(type.equals("movie")) {
            movieRepository.getMovieSimilar(id, new OnMovieSimilarsCallback() {
                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onSuccess(List<Movie> similarsMovie) {
                    if (similarsMovie.size() == 0) {
                        tvHeaderSimilar.setVisibility(View.GONE);
                    }
                    mvAdapter = new MovieAdapter(similarsMovie);
                    mvAdapter.setClickListener(DetailActivity.this);
                    mvAdapter.notifyDataSetChanged();
                    rvSimilar.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvSimilar.setAdapter(mvAdapter);
                }
            });
        }else if(type.equals("tv")){
            tvShowRepository.getTvShowSimilar(id, new OnTvShowSimilarsCallback() {
                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onSuccess(List<TvShow> similarTv) {
                    if (similarTv.size() == 0) {
                        tvHeaderSimilar.setVisibility(View.GONE);
                    }
                    tvAdapter = new TvShowAdapter(similarTv);
                    tvAdapter.setClickListener(DetailActivity.this);
                    tvAdapter.notifyDataSetChanged();
                    rvSimilar.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL, false));
                    rvSimilar.setAdapter(tvAdapter);
                }
            });
        }
    }

    private void loadCastData(int id, String type) {
        if(type.equals("movie")){
            movieRepository.getMovieCast(id, new OnCastCallback() {
                @Override
                public void onSuccess(CreditModel creditModel, String message) {
                    listCast = creditModel.getCast();
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL,false));
                    rvCast.setAdapter(new CastAdapter(listCast, DetailActivity.this));
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }else if(type.equals("tv")){
            tvShowRepository.getTvShowCast(id, new OnCastCallback() {
                @Override
                public void onSuccess(CreditModel creditModel, String message) {
                    listCast = creditModel.getCast();
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL,false));
                    rvCast.setAdapter(new CastAdapter(listCast, DetailActivity.this));
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }

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
    public void onItemClick(Movie movie) {
        Intent detailActivity = new Intent(this, DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TYPE", selectedFragment);
        startActivity(detailActivity);
        finish();
    }

    @Override
    public void onItemClick(TvShow tvShow) {
        Intent detailActivity = new Intent(this, DetailActivity.class);
        detailActivity.putExtra("ID", tvShow.getId());
        detailActivity.putExtra("TYPE", selectedFragment);
        startActivity(detailActivity);
        finish();

    }
}