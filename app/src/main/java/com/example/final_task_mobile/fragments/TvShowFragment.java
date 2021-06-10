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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.activities.DetailActivity;

import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.adapters.TvShowAdapter;
import com.example.final_task_mobile.local.table.FavoriteMovie;
import com.example.final_task_mobile.local.table.FavoriteTv;
import com.example.final_task_mobile.models.movie.Movie;
import com.example.final_task_mobile.models.tvshow.TvShow;
import com.example.final_task_mobile.repository.TvShowRepository;
import com.example.final_task_mobile.repository.callback.OnTvShowCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowSearchCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TvShowFragment extends Fragment implements OnItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener{
    //extrass
    private static final String TAG = "tv";
    private static final String SORT_BY = "popular";

    //tv show attribut
    private RecyclerView recyclerView;
    private TvShowAdapter adapter;
    private TvShowRepository repository;

    //widget
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar tvProgressBar;
    private LinearLayout llNoRecord;

    //attribut
    private int currentPage = 1;
    private boolean isFetching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tv_show, container, false);

        //set layout for widget
        refreshLayout = v.findViewById(R.id.swl_tv_show);
        recyclerView = v.findViewById(R.id.rv_tv_show);
        tvProgressBar = v.findViewById(R.id.pb_tv_show);
        llNoRecord = v.findViewById(R.id.ll_tv_show_empty);

        //db instance
        repository = TvShowRepository.getRetrofit();

        //recycleview setting
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        //check onscroll and load new page
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
                        tvProgressBar.setVisibility(View.VISIBLE);
                        loadData("", currentPage);
                        isFetching = false;
                    }
                }
            }
        });

        //refresh layout instance
        refreshLayout.setOnRefreshListener(this);

        //load all tvshow data
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

        //set layout and clean old value
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.action_bar_fragment, menu);
        MenuItem item = menu.findItem(R.id.item_search);

        //searchview setting
        SearchView searchView = (SearchView) item.getActionView();
        searchView.requestFocus();
        searchView.setQueryHint("Cari TvShow");
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //load data by query
        if (newText.length() > 0) {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
            loadData(newText, currentPage);
        }
        //load all data
        else {
            adapter = null;
            tvProgressBar.setVisibility(View.VISIBLE);
            loadData("", currentPage);
        }
        return true;
    }

    private void loadData(String query, int page) {
        isFetching = true;

        //show all tv show data
        if(query.equals("")){
            repository.getTvShow(SORT_BY, page, new OnTvShowCallback() {
                @Override
                public void onSuccess(int page, List<TvShow> tvShowList) {
                    //make new adapter if adapter null
                    if(adapter == null){
                        adapter = new TvShowAdapter(tvShowList);
                        adapter.setClickListener(TvShowFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }
                    //append to list, if adapter not null
                    else{
                        adapter.appendList(tvShowList);
                    }

                    //setting ui success load
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
        //show tv show by query, in search
        else{
            repository.searchTvShow(query, page, new OnTvShowSearchCallback() {
                @Override
                public void onSuccess(List<TvShow> tvShowList, String msg, int page) {
                    //if adapter null
                    if(adapter == null){
                        adapter = new TvShowAdapter(tvShowList);
                        adapter.setClickListener(TvShowFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }else{
                        //if adapter hasbean created, append mtv list
                        adapter.appendList(tvShowList);
                    }
                    //success ui setting
                    currentPage = page;
                    isFetching = false;
                    refreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(String msg) {
                    llNoRecord.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        //show all tv show when refersh
        adapter = null;
        currentPage =1;
        tvProgressBar.setVisibility(View.VISIBLE);
        loadData("", currentPage);
    }

    @Override
    public void onItemClick(FavoriteMovie favoriteMovie) {}

    @Override
    public void onItemClick(FavoriteTv favoriteTv) {}

    @Override
    public void onItemClick(Movie movie) {}

    @Override
    //intent to detail
    public void onItemClick(TvShow tvShow) {
        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
        detailActivity.putExtra("ID", tvShow.getId());
        detailActivity.putExtra("TYPE", TAG);
        startActivity(detailActivity);
    }
}