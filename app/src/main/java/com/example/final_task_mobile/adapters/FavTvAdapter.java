package com.example.final_task_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.adapters.onclick.OnItemClickListener;
import com.example.final_task_mobile.local.table.FavoriteTv;
import com.example.final_task_mobile.networks.Const;

import java.util.List;

public class FavTvAdapter extends RecyclerView.Adapter<FavTvAdapter.ViewHolder> {
    //attribute adapter
    private final List<FavoriteTv> tvList;
    private OnItemClickListener clickListener;

    public FavTvAdapter(List<FavoriteTv> tvList){
        this.tvList = tvList;
    }

    public void setClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindItemView(tvList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //attribute holder
        FavoriteTv tv;
        ImageView ivPoster;
        TextView tvTitle;
        RatingBar rbFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //set layout
            ivPoster = itemView.findViewById(R.id.iv_poster_fav);
            tvTitle = itemView.findViewById(R.id.tv_title_fav);
            rbFavorite = itemView.findViewById(R.id.rb_rate_fav);
        }
        void onBindItemView(FavoriteTv tv) {
            //set value
            this.tv = tv;
            Glide.with(itemView.getContext()).load(Const.IMG_URL_200 + tv.getImgPath()).into(ivPoster);
            tvTitle.setText(tv.getTitle());
            rbFavorite.setRating(tv.getRate());
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(tv);
        }
    }

}
