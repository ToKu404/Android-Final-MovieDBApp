package com.example.final_task_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.final_task_mobile.activities.DetailMovieActivity;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.MovieAdapter;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.movie.MovieResponse;
import com.example.final_task_mobile.networks.Const;
import com.example.final_task_mobile.networks.movie.MovieApiClient;
import com.example.final_task_mobile.networks.movie.MovieApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment implements MovieAdapter.OnItemClick{
    private static final String TAG = "MovieFragment";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> listMovie;
    private ProgressBar tvProgressBar;
    private LinearLayout tvNoRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = v.findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        tvProgressBar = v.findViewById(R.id.pb_movie);
        tvNoRecord = v.findViewById(R.id.ll_movie_empty);
        loadData();
        return v;
    }

    private void loadData() {
        MovieApiInterface movieApiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);

        retrofit2.Call<MovieResponse> nowPlayingCall = movieApiInterface.getNowPlaying(Const.API_KEY);
        nowPlayingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body().getNowPlayings() != null) {
                    listMovie = response.body().getNowPlayings();
                    adapter = new MovieAdapter(listMovie, MovieFragment.this);
                    recyclerView.setAdapter(adapter);
                    tvProgressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    tvNoRecord.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        tvProgressBar.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);
                    }
                }, 3000);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(getActivity(), "Failed " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(int pos) {
        Intent detailActivity = new Intent(getActivity(), DetailMovieActivity.class);
        detailActivity.putExtra("ID", listMovie.get(pos).getId());
        detailActivity.putExtra("TITLE", listMovie.get(pos).getTitle());
        startActivity(detailActivity);
    }
}