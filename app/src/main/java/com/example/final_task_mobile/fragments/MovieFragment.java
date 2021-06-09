package com.example.final_task_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.final_task_mobile.activities.DetailActivity;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.MovieAdapter;
import com.example.final_task_mobile.adapters.OnMovieItemClickListener;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.repository.MovieRepository;
import com.example.final_task_mobile.repository.callback.OnMovieCallback;
import com.example.final_task_mobile.repository.callback.OnMovieSearchCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieFragment extends Fragment implements OnMovieItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "movie";
    private static final String SORT_BY = "popular";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar tvProgressBar;
    private LinearLayout llNoRecord;
    private MovieRepository repository;
    private int currentPage = 1;
    private boolean isFetching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        refreshLayout = v.findViewById(R.id.swl_movie);
        recyclerView = v.findViewById(R.id.rv_movies);
        tvProgressBar = v.findViewById(R.id.pb_movie);
        llNoRecord = v.findViewById(R.id.ll_movie_empty);
        repository = MovieRepository.getRetrofit();
        loadData("", currentPage);
        onScrollListener();
        refreshLayout.setOnRefreshListener(this);
        return v;
    }

    private void onScrollListener() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
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
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.action_bar_fragment, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        SearchView searchView = null;
        if(item!=null){
            searchView = (SearchView) item.getActionView();
            searchView.setIconifiedByDefault(false);
            searchView.requestFocus();
        }
        if(searchView!=null){
            searchView.setQueryHint(getString(R.string.hint));
            searchView.setOnQueryTextListener(this);

        }
        super.onPrepareOptionsMenu(menu);
    }



    private void loadData(String query, int page) {
        isFetching = true;
        if(query.equals("")){
            repository.getMovie(SORT_BY, page, new OnMovieCallback() {
                @Override
                public void onSuccess(int page, List<Movie> movieList) {
                    if(adapter == null){
                        adapter = new MovieAdapter(movieList);
                        adapter.setClickListener(MovieFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }
                    else{
                        adapter.appendList(movieList);
                    }
                    tvProgressBar.setVisibility(View.GONE);
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);

                }

                @Override
                public void onFailure(String message) {
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
        }else{
            repository.search(query, page, new OnMovieSearchCallback() {
                @Override
                public void onSuccess(List<Movie> movies, String msg, int page) {
                    if(adapter == null){
                        adapter = new MovieAdapter(movies);
                        adapter.setClickListener(MovieFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }else{
                        adapter.appendList(movies);
                    }
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);

                }
                @Override
                public void onFailure(String msg) {

                }
            });
        }

    }

    @Override
    public void onItemClick(Movie movie) {
        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
        detailActivity.putExtra("ID", movie.getId());
        detailActivity.putExtra("TYPE", TAG);
        startActivity(detailActivity);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setMenuVisibility(false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
            loadData(newText, currentPage);
        } else {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
           loadData("", currentPage);
        }

        if(!isVisible()){
            return true;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        adapter = null;
        currentPage =1;
        tvProgressBar.setVisibility(View.VISIBLE);
        loadData("", currentPage);
    }
}