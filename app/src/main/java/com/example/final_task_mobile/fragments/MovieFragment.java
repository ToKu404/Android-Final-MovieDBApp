package com.example.final_task_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.final_task_mobile.activities.DetailActivity;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.MovieAdapter;
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;
import com.example.final_task_mobile.repository.MovieRepository;
import com.example.final_task_mobile.repository.callback.OnMovieCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSearchCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieFragment extends Fragment implements OnItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    //widget
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar tvProgressBar;
    private LinearLayout llNoRecord;

    //extras
    private static final String TAG = "movie";
    private static final String SORT_BY = "popular";

    //movie attribut
    private RecyclerView recyclerView;
    private MovieRepository repository;
    private MovieAdapter adapter;

    //variable
    private int currentPage = 1;
    private boolean isFetching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);

        //set layout of widget
        refreshLayout = v.findViewById(R.id.swl_movie);
        recyclerView = v.findViewById(R.id.rv_movie);
        tvProgressBar = v.findViewById(R.id.pb_movie);
        llNoRecord = v.findViewById(R.id.ll_movie_empty);

        //db instance
        repository = MovieRepository.getRetrofit();

        //recycleview setting
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        //check onscroll and make rv load all page
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItem = layoutManager.getItemCount();
                int visibleItem = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItem >= totalItem / 2) {
                    if (!isFetching) {
                        isFetching = true;
                        currentPage++;
                        loadData("", currentPage);
                        isFetching = false;
                    }
                }
            }
        });

        //refreshlayout
        refreshLayout.setOnRefreshListener(this);

        //load all movie
        loadData("", currentPage);
        return v;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //set layout and clean it value
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.action_bar_fragment, menu);
        MenuItem item = menu.findItem(R.id.item_search);

        // searchview setting
        SearchView searchView = (SearchView) item.getActionView();
        searchView.requestFocus();
        searchView.setQueryHint("Cari Movie");
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        //search action if query not null
        if (newText.length() > 0) {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
            loadData(newText, currentPage);
        //search all
        } else {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
            loadData("", currentPage);
        }
        return true;
    }

    private void loadData(String query, int page) {
        isFetching = true;

        //show all movie data
        if(query.equals("")){
            //getmovie request to moviedb
            repository.getMovie(SORT_BY, page, new OnMovieCallback() {
                @Override
                public void onSuccess(int page, List<Movie> movieList) {
                    //if adapter actualy null
                    if(adapter == null){
                        adapter = new MovieAdapter(movieList);
                        adapter.setClickListener(MovieFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }
                    //if adapter not null, append movielist
                    else{
                        adapter.appendList(movieList);
                    }

                    //setting ui success
                    tvProgressBar.setVisibility(View.GONE);
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(String message) {
                    //setting ui unsuccess
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            tvProgressBar.setVisibility(View.GONE);
                            llNoRecord.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
                    Log.d(TAG, "onFailure: " + message);
                    Toast.makeText(getActivity(), "Failed " + message, Toast.LENGTH_LONG).show();
                }
            });
        }
        //show movie like in query
        else{
            repository.searchMovie(query, page, new OnMovieSearchCallback() {
                @Override
                public void onSuccess(List<Movie> movies, String msg, int page) {
                    //if adapter null, make new adapter
                    if(adapter == null){
                        adapter = new MovieAdapter(movies);
                        adapter.setClickListener(MovieFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }else{
                        //if adapter has been created, appeng movie list
                        adapter.appendList(movies);
                    }
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(String msg) {
                    tvProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        //so show all movie when refresh
        adapter = null;
        currentPage =1;
        tvProgressBar.setVisibility(View.VISIBLE);
        loadData("", currentPage);
    }


    @Override
    public void onItemClick(Movie movie) {
        //intent to detail
        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TYPE", TAG);
        startActivity(detailActivity);
    }

    @Override
    public void onItemClick(FavoriteMovie favoriteMovie) {}
    @Override
    public void onItemClick(FavoriteTv favoriteTv) {}
    @Override
    public void onItemClick(TvShow tv) {}
}