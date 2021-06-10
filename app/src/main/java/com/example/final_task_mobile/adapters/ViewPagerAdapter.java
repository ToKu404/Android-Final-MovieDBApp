package com.example.final_task_mobile.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.final_task_mobile.fragments.FavoriteMovieFragment;
import com.example.final_task_mobile.fragments.FavoriteTvFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStateAdapter {
    // attribute
    private final Fragment[] fragments;

    public ViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        //instance fragments2 favorite
        fragments = new Fragment[]{
                new FavoriteTvFragment(),
                new FavoriteMovieFragment()
        };
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }


}
