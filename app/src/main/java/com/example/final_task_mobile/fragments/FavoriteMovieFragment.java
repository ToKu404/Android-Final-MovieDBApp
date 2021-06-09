package com.example.final_task_mobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.FavMovieAdapter;
import com.example.final_task_mobile.db.AppDatabase;
import com.example.final_task_mobile.db.table.FavoriteMovie;

import java.util.List;


public class FavoriteMovieFragment extends Fragment {
    private RecyclerView recyclerView;
    private AppDatabase database;
    private LinearLayout llNoRecord;

    private List<FavoriteMovie> favoriteMovieList;

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_page, container, false);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        llNoRecord = view.findViewById(R.id.ll_fav_empty);
        llNoRecord.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.rv_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();
        return inflater.inflate(R.layout.fragment_favorite_page, container, false);
    }

    private void loadData() {
        llNoRecord.setVisibility(View.GONE);
        database.favoriteDao().getAllMovie().observe(getActivity(), new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                favoriteMovieList = favoriteMovies;
                recyclerView.setAdapter(new FavMovieAdapter(favoriteMovies));
                if(favoriteMovies.size()==0){
                    llNoRecord.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}