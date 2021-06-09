package com.example.final_task_mobile.fragments;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.activities.DetailActivity;

import com.example.final_task_mobile.adapters.OnTvShowItemClickListener;
import com.example.final_task_mobile.adapters.TvShowAdapter;
import com.example.final_task_mobile.models.tvshow.TvShow;
import com.example.final_task_mobile.repository.TvShowRepository;
import com.example.final_task_mobile.repository.callback.OnTvShowCallback;
import com.example.final_task_mobile.repository.callback.OnTvShowSearchCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TvShowFragment extends Fragment implements OnTvShowItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "tv";
    private static final String SORT_BY = "popular";
    private RecyclerView recyclerView;
    private TvShowAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar tvProgressBar;
    private LinearLayout llNoRecord;
    private TvShowRepository repository;
    private int currentPage = 1;
    private boolean isFetching;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tv_show, container, false);
        refreshLayout = v.findViewById(R.id.swl_tv_show);
        recyclerView = v.findViewById(R.id.rv_tv_show);
        tvProgressBar = v.findViewById(R.id.pb_tv_show);
        llNoRecord = v.findViewById(R.id.ll_tv_show_empty);
        repository = TvShowRepository.getRetrofit();
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
                        tvProgressBar.setVisibility(View.VISIBLE);
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
        }
        if(searchView!=null){
            searchView.setIconifiedByDefault(false);
            searchView.requestFocus();
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.setQueryHint(getString(R.string.hint));
            searchView.setOnQueryTextListener(this);
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(im!=null){
                            im.showSoftInput(v.findFocus(), 0);
                        }
                    }
                }
            });

        }
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
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
        return true;
    }

    private void loadData(String query, int page) {
        isFetching = true;
        if(query.equals("")){
            repository.getTvShow(SORT_BY, page, new OnTvShowCallback() {
                @Override
                public void onSuccess(int page, List<TvShow> tvShowList) {
                    if(adapter == null){
                        adapter = new TvShowAdapter(tvShowList);
                        adapter.setClickListener(TvShowFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }
                    else{
                        adapter.appendList(tvShowList);
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
            repository.search(query, page, new OnTvShowSearchCallback() {
                @Override
                public void onSuccess(List<TvShow> tvShowList, String msg, int page) {

                    if(adapter == null){
                        adapter = new TvShowAdapter(tvShowList);
                        adapter.setClickListener(TvShowFragment.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        llNoRecord.setVisibility(View.GONE);
                    }else{
                        adapter.appendList(tvShowList);
                    }
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
        adapter = null;
        currentPage =1;

        tvProgressBar.setVisibility(View.VISIBLE);
        loadData("", currentPage);
    }

    @Override
    public void onItemClick(TvShow tvShow) {
        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
        detailActivity.putExtra("ID", tvShow.getId());
        detailActivity.putExtra("TYPE", TAG);
        startActivity(detailActivity);
    }
}