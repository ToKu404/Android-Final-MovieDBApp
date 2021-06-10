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
//    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
//        super(fragmentManager, lifecycle);
//    }


    private final Fragment[] fragments;

    public ViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{
                new FavoriteMovieFragment(),
                new FavoriteTvFragment()
        };
    }



    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
//        switch (position){
//            case 0:
//                return new FavoriteMovieFragment();
//            case 1:
//                return new FavoriteTvFragment();
//        }
//       return new FavoriteMovieFragment();
    }

    @Override
    public int getItemCount() {
//        return 2;
        return fragments.length;
    }


}
