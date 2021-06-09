package com.example.final_task_mobile.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        ViewPager2 viewPager = rootView.findViewById(R.id.vp_fav);
        viewPager.setAdapter(new ViewPagerAdapter(getActivity()));

        TabLayout tabLayout = rootView.findViewById(R.id.tab_fav);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();


        (tabLayout.getTabAt(0)).setText("Movie");
        (tabLayout.getTabAt(1)).setText("TV Show");

        return rootView;
    }
}