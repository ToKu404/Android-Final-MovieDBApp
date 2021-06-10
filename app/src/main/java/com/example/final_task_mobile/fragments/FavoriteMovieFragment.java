package com.example.final_task_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.activities.DetailActivity;
import com.example.final_task_mobile.adapters.FavMovieAdapter;
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.local.RoomHelper;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;

import java.util.List;


public class FavoriteMovieFragment extends Fragment implements OnItemClickListener {
    //widget
    private RecyclerView recyclerView;
    private LinearLayout llNoRecord;

    //atribute
    private static final String TAG = "movie";
    private RoomHelper roomHelper;
    private List<FavoriteMovie> favoriteMovieList;
    private FavMovieAdapter favMovieAdapter;

    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_page, container, false);

        //set layout
        llNoRecord = view.findViewById(R.id.ll_fav_empty);
        llNoRecord.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.rv_favorite);

        //recycler view and data instance
        roomHelper = new RoomHelper(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        //load data from local repositoru
        favoriteMovieList = roomHelper.readFavMovie();

        //set adapter
        favMovieAdapter = new FavMovieAdapter(favoriteMovieList);
        favMovieAdapter.setClickListener(this);
        recyclerView.setAdapter(favMovieAdapter);
        if(favoriteMovieList.size()==0){
            llNoRecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(FavoriteMovie favoriteMovie) {
        //intent to detail
        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
        detailActivity.putExtra("ID", favoriteMovie.getId());
        detailActivity.putExtra("TYPE", TAG);
        startActivity(detailActivity);
    }

    @Override
    public void onItemClick(FavoriteTv favoriteTv) {

    }

    @Override
    public void onItemClick(Movie movie) {

    }

    @Override
    public void onItemClick(TvShow tv) {

    }
}