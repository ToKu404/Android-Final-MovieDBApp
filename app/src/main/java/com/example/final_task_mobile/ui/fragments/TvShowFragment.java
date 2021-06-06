package com.example.final_task_mobile.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_task_mobile.R;
public class TvShowFragment extends Fragment {
    public TvShowFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static TvShowFragment newInstance(String param1, String param2) {
        TvShowFragment fragment = new TvShowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }
}