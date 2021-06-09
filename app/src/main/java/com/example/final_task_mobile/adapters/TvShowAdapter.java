package com.example.final_task_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_task_mobile.R;
import com.example.final_task_mobile.models.tvshow.TvShow;
import com.example.final_task_mobile.networks.Const;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.GridViewHolder> {
    private List<TvShow> tvShowList;
    private OnTvShowItemClickListener clickListener;

    public TvShowAdapter(List<TvShow> tvShowList){
        this.tvShowList = tvShowList;
    }
    public void setClickListener(OnTvShowItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card_recycler, parent, false);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.onBindItemView(tvShowList.get(position));
    }

    public void appendList(List<TvShow> listToAppend) {
        tvShowList.addAll(listToAppend);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TvShow tvShow;
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvVoteAverage;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivPoster = itemView.findViewById(R.id.iv_main_card_poster);
            tvTitle = itemView.findViewById(R.id.tv_main_card_title);
            tvVoteAverage = itemView.findViewById(R.id.tv_main_card_vote);
        }
        void onBindItemView(TvShow tvShow) {
            this.tvShow = tvShow;
            Glide.with(itemView.getContext()).load(Const.IMG_URL_200 + tvShow.getImgUrl()).into(ivPoster);
            tvTitle.setText(tvShow.getTitle());
            tvVoteAverage.setText(tvShow.getVoteAverage());
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(tvShow);
        }
    }

}
