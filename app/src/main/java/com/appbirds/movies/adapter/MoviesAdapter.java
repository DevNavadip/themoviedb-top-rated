package com.appbirds.movies.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.appbirds.movies.R;
import com.appbirds.movies.listener.ItemListener;
import com.appbirds.movies.models.Results;
import com.appbirds.movies.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private Context mContext;
    private List<Results> mResultsList;
    private ItemListener mListener;

    public MoviesAdapter(Context mContext, List<Results> resultsList) {
        this.mContext = mContext;
        this.mResultsList = resultsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_rated_movies, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.ViewHolder viewHolder, int i) {
        final Results results = mResultsList.get(viewHolder.getAdapterPosition());

        if (!TextUtils.isEmpty(results.getOriginal_title()))
            viewHolder.txtTitle.setText(results.getOriginal_title());
        if (!TextUtils.isEmpty(results.getOverview()))
            viewHolder.txtDesc.setText(results.getOverview());
        if (!TextUtils.isEmpty(results.getVote_average()))
            viewHolder.txtRateAverage.setText(results.getVote_average());
        if (!TextUtils.isEmpty(results.getRelease_date()))
            viewHolder.txtReleaseDate.setText(results.getRelease_date());

        if (!TextUtils.isEmpty(results.getPoster_path())) {
            viewHolder.progress_bar.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Constants.IMAGE_URL + results.getPoster_path())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            viewHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.imgMovie);
        } else
            viewHolder.progress_bar.setVisibility(View.GONE);

        //set start service button click
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(results, viewHolder.getAdapterPosition(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtDesc, txtRateAverage, txtReleaseDate;
        private RoundedImageView imgMovie;
        private ProgressBar progress_bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtMovieName);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtRateAverage = itemView.findViewById(R.id.txtRateAverage);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }
    }

    //set item click listener
    public void setOnItemClickListener(ItemListener mListener) {
        this.mListener = mListener;
    }
}
