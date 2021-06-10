package com.example.final_task_mobile.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.FavMovieAdapter;
import com.example.final_task_mobile.db.AppDatabase;
import com.example.final_task_mobile.db.table.FavoriteMovie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FavoriteMovieFragment extends Fragment {
    private RecyclerView recyclerView;
    private AppDatabase database;
    private LinearLayout llNoRecord;
    private List<FavoriteMovie> favoriteMovieList = new ArrayList<>();

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_page, container, false);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        loadData();
        llNoRecord = view.findViewById(R.id.ll_fav_empty);
        llNoRecord.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.rv_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setData();
        System.out.println("ON CREATE VIEW");
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ON RESUME");
        loadData();
        setData();

    }


    private void setData() {
//        if(favoriteMovieList.size()==0){
            llNoRecord.setVisibility(View.VISIBLE);
//        }else{
            llNoRecord.setVisibility(View.GONE);
            recyclerView.setAdapter(new FavMovieAdapter(favoriteMovieList));
//        }
    }

    private void loadData() {
        database.favoriteDao().getAllMovie().observe(getActivity(), new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                favoriteMovieList = favoriteMovies;
            }
        });

    }
}