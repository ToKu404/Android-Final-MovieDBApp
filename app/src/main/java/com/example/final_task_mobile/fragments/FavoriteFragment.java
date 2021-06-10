package com.example.final_task_mobile.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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
    //wdiget
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        //viewPager settings
        viewPager = rootView.findViewById(R.id.vp_fav);
        viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(0);

        //tablayout settings
        tabLayout = rootView.findViewById(R.id.tab_fav);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();
        //set text of tab layout
        (tabLayout.getTabAt(0)).setText("TV Show");
        (tabLayout.getTabAt(1)).setText("Movie");

        return rootView;
    }
}