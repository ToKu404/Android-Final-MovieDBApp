package com.example.final_task_mobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_task_mobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMovieFragment extends Fragment {

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }
}