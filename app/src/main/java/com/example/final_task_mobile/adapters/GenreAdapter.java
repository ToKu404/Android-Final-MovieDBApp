package com.example.final_task_mobile.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_task_mobile.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder>
{
    //attribute
    private List<String> genreList;


    public GenreAdapter(List<String> genreList){
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_genre_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        final String genre = genreList.get(position);
        holder.setGenre(genre);
    }

    @Override
    public int getItemCount() {
        return genreList == null ? 0 : genreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGenre;
        public ViewHolder(View itemView){
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tv_genre_detail);
        }

        public void setGenre(String genre){
            tvGenre.setText(genre);
        }
    }
}