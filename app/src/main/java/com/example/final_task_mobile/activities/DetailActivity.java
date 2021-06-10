package com.example.final_task_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.adapters.TvShowAdapter;
import com.example.final_task_mobile.local.RoomHelper;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
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

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener{
    //movie
    private MovieRepository movieRepository;
    private MovieAdapter mvAdapter;

    //tv show
    private TvShowRepository tvShowRepository;
    private TvShowAdapter tvAdapter;

    //widget
    private ImageView ivPoster, ivBackdrop;
    private TextView tvTitle, tvYear, tvDuration, tvSinopsis, tvRating, tvMore, tvHeaderSimilar;
    private RatingBar ratingBar;
    private RecyclerView rvGenre, rvCast, rvSimilar;

    //attribut
    private int EXTRAS_ID;
    private String EXTRAS_TYPE;
    private List<String> listGenre;
    private List<Cast> listCast;

    //local db attribut
    private RoomHelper roomHelper;
    private String favTitle, favImg;
    private Float favRate;
    private boolean isFavorite;

    //string for toast
    private final String EXTRAS_DELETE_SUCCESS = "Data Berhasil Dihapus Dari Favorite";
    private final String EXTRAS_DELETE_FAILED = "Data Gagal Dihapus Dari Favorite";
    private final String EXTRAS_INSERT_SUCCESS = "Data Berhasil Ditambahkan Ke Favorite";
    private final String EXTRAS_INSERT_FAILED = "Data Gagal Ditambahkan Ke Favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        //get data id from extras
        if (getIntent() != null) {
            EXTRAS_ID = getIntent().getIntExtra("ID", 0);
        }

        //set widget layout
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

        //local db instance
        roomHelper = new RoomHelper(this);
        listGenre = new ArrayList<>();
        listCast = new ArrayList<>();

        //repository retrofit instance
        movieRepository = MovieRepository.getRetrofit();
        tvShowRepository = TvShowRepository.getRetrofit();

        //load data and set extras data
        if (getIntent() != null) {
            EXTRAS_ID = getIntent().getIntExtra("ID", 0);
            EXTRAS_TYPE = getIntent().getStringExtra("TYPE");
            loadData();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inizialisation of favorite item
        getMenuInflater().inflate(R.menu.action_bar_detail_activity, menu);
        updateFavoriteItem(menu.getItem(0));
        return super.onCreateOptionsMenu(menu);
    }


    private void updateFavoriteItem(MenuItem item) {
        //check what is favorite item
        if(EXTRAS_TYPE.equals("movie")){
            isFavorite = roomHelper.checkFavMovie(EXTRAS_ID);
        }else{
            isFavorite = roomHelper.checkFavTv(EXTRAS_ID);
        }

        //icon setting if and not afavorite item
        if(!isFavorite){
            item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_favorite_border_24));
        }else{
            item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_favorite_24));
            item.getIcon().setColorFilter(getResources().getColor(R.color.favRed), PorterDuff.Mode.SRC_ATOP);
        }
    }


    private void onClickFavoriteItem(MenuItem item){
        String textStatus = "";

        //Movie onclick favorite action
        if(EXTRAS_TYPE.equals("movie")){
            if (!isFavorite) {
                textStatus = roomHelper.insertFavMovie(EXTRAS_ID, favTitle, favImg, favRate) == true? EXTRAS_INSERT_SUCCESS:EXTRAS_INSERT_FAILED;
            } else {
                textStatus = roomHelper.deleteFavMovie(EXTRAS_ID)==true?EXTRAS_DELETE_SUCCESS:EXTRAS_DELETE_FAILED;
            }
        }

        //Tv show onclick favorite action
        if(EXTRAS_TYPE.equals("tv")){
            if (!isFavorite) {
                textStatus = roomHelper.insertFavTv(EXTRAS_ID, favTitle, favImg, favRate) == true? EXTRAS_INSERT_SUCCESS:EXTRAS_INSERT_FAILED;
            } else {
                textStatus = roomHelper.deleteFavTv(EXTRAS_ID)==true?EXTRAS_DELETE_SUCCESS:EXTRAS_DELETE_FAILED;
            }
        }
        //make toast status
        Toast.makeText(this, textStatus, Toast.LENGTH_SHORT).show();

        //update favorite icon
        updateFavoriteItem(item);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //click favorite item
        switch (item.getItemId()) {
            case R.id.item_favorite:
                onClickFavoriteItem(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadData() {
        //movie load data
        if(EXTRAS_TYPE.equals("movie")){
            movieRepository.getMovieDetail(EXTRAS_ID, new OnDetailCallback() {
                @Override
                public void onSuccess(DetailModel movie, String message) {
                    //add movie value on success
                    addValue(movie);
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }

        //tv load data
        else if(EXTRAS_TYPE.equals("tv")){
            tvShowRepository.getTvShowDetail(EXTRAS_ID, new OnDetailCallback() {
                @Override
                public void onSuccess(DetailModel tv, String message) {
                    //add tv value on success
                    addValue(tv);
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }

    }

    private void addValue(DetailModel detailModel) {
        //setting actionbar
        configureActionBar(detailModel.getTitle());

        //setting widget value
        configureWidgetValue(detailModel);

        //setting favorite attribut value
        configureFavoriteValue(detailModel);

        //setting genre value
        configureGenreValue(detailModel.getGenres());

        //setting cast value
        configureCastValue();

        //setting similar movie value
        configureSimilarValue();
    }

    private void configureFavoriteValue(DetailModel detailModel) {
        // add value for favorite attribut
        favTitle = detailModel.getTitle();
        favImg = detailModel.getPoster();
        favRate = detailModel.getRating();
    }

    private void configureWidgetValue(DetailModel detailModel) {
        // add value for widget
        tvTitle.setText(detailModel.getTitle());
        tvSinopsis.setText(detailModel.getOverview());
        Glide.with(DetailActivity.this).load(Const.IMG_URL_300+ detailModel.getPoster()).into(ivPoster);
        Glide.with(DetailActivity.this).load(Const.IMG_URL_300+ detailModel.getBackdrop()).into(ivBackdrop);
        ratingBar.setRating(detailModel.getRating());
        tvRating.setText(detailModel.getVoteAverage());
        tvSinopsis.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        if(EXTRAS_TYPE.equals("movie")){
            tvYear.setText(detailModel.getYear());
            tvDuration.setText(detailModel.getDuration());
        }
        if(EXTRAS_TYPE.equals("tv")){
            tvYear.setText(detailModel.getEps());
            tvDuration.setText(detailModel.getStatus());
        }
    }

    private void configureActionBar(String title) {
        //setting action bar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1A212F"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configureSimilarValue() {
        //check similar movie and show it
        if(EXTRAS_TYPE.equals("movie")) {
            movieRepository.getMovieSimilar(EXTRAS_ID, new OnMovieSimilarsCallback() {
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
        }
        //check similar tv shoe and show it
        else if(EXTRAS_TYPE.equals("tv")){
            tvShowRepository.getTvShowSimilar(EXTRAS_ID, new OnTvShowSimilarsCallback() {
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

    private void configureCastValue() {
        //check movie cast and show it
        if(EXTRAS_TYPE.equals("movie")){
            movieRepository.getMovieCast(EXTRAS_ID, new OnCastCallback() {
                @Override
                public void onSuccess(CreditModel creditModel, String message) {
                    listCast = creditModel.getCast();
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL,false));
                    rvCast.setAdapter(new CastAdapter(listCast));
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }
        //check tv cast and show it
        else if(EXTRAS_TYPE.equals("tv")){
            tvShowRepository.getTvShowCast(EXTRAS_ID, new OnCastCallback() {
                @Override
                public void onSuccess(CreditModel creditModel, String message) {
                    listCast = creditModel.getCast();
                    rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, RecyclerView.HORIZONTAL,false));
                    rvCast.setAdapter(new CastAdapter(listCast));
                }
                @Override
                public void onFailure(String message) {

                }
            });
        }

    }

    private void configureGenreValue(List<Genre> genresList){
        //add all genre name, to list
        for(int i = 0; i< genresList.size(); i++){
            listGenre.add(genresList.get(i).getName());
        }
        //recycleview genre settings
        rvGenre.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvGenre.setAdapter(new GenreAdapter(listGenre));
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_detail_synopsis || v.getId()==R.id.tv_detail_more){
            actionExpand();
        }
    }

    private void actionExpand() {
        //code for expand and hide synopsis by manipulate maxlines
        if (tvMore.getText().toString().equalsIgnoreCase("less")){
            tvSinopsis.setMaxLines(2);
            tvMore.setText("more");
        } else {
            tvSinopsis.setMaxLines(Integer.MAX_VALUE);
            tvMore.setText("less");
        }
    }

    @Override
    public void onItemClick(FavoriteMovie favoriteMovie) {}

    @Override
    public void onItemClick(FavoriteTv favoriteTv) {}

    @Override
    public void onItemClick(Movie movie) {
        //intent to movie
        Intent detailActivity = new Intent(this, DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TYPE", EXTRAS_TYPE);
        startActivity(detailActivity);
        finish();
    }

    @Override
    public void onItemClick(TvShow tvShow) {
        //intent to tvshow
        Intent detailActivity = new Intent(this, DetailActivity.class);
        detailActivity.putExtra("ID", tvShow.getId());
        detailActivity.putExtra("TYPE", EXTRAS_TYPE);
        startActivity(detailActivity);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}