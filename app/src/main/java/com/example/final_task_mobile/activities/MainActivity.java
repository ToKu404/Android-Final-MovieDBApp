package com.example.final_task_mobile.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.example.final_task_mobile.R;
import com.example.final_task_mobile.fragments.FavoriteFragment;
import com.example.final_task_mobile.fragments.MovieFragment;
import com.example.final_task_mobile.fragments.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationItemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationItemView = findViewById(R.id.bottom_nav);

        if(getSupportActionBar()!=null){
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1A212F"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        bottomNavigationItemView.setOnNavigationItemSelectedListener(this);
        bottomNavigationItemView.setSelectedItemId(R.id.menu_item_tv);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.menu_item_movie:
                setActionBarTitle(getString(R.string.item_movie));
                fragment = new MovieFragment();
                break;
            case R.id.menu_item_tv:
                setActionBarTitle(getString(R.string.item_tv_show));
                fragment = new TvShowFragment();
                break;
            case R.id.menu_item_favorite:
                setActionBarTitle(getString(R.string.item_favorite));
                fragment = new FavoriteFragment();
                break;
        }
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, fragment)
                    .commit();
            return true;
        }
        return true;
    }
    private void setActionBarTitle(String title) {
        title = title.replaceAll("\\s","");
        title += ".";
        String first = title.substring(0,title.length()/2);
        String end = title.substring(title.length()/2,title.length());
        getSupportActionBar().setTitle(Html.fromHtml(first+"<font color=\"#2C83F5\">"+end+"</font>"));
    }

    public void nothing_happen(View view) {}
}