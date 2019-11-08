package com.murati.videos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.murati.videos.utils.DeveloperKey;
import com.murati.videos.R;
import com.murati.videos.model.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private List<Video> VideoList;

    public VideoAdapter(List<Video> VideoList) {
        this.VideoList = VideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);

        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video v = VideoList.get(position);
        holder.title.setText(VideoList.get(position).getTitle());

        Glide
            .with(holder.itemView)
            .load(v.getThumbnailImage())
            //.placeholder(placeholder)
            .fitCenter()
            .into(holder.thumbnail2);

        holder.thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(final YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(v.getVideoId());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView childYouTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        youTubeThumbnailLoader.release();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //do nohing.. ada error, tambahin method ini jalan, error-nya lupa...
                //readyForLoadingYoutubeThumbnail = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return VideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public YouTubeThumbnailView thumbnail;
        public ImageView thumbnail2;


        public VideoViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text);
            thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
            thumbnail2 = (ImageView) view.findViewById(R.id.thumbnail2);
        }
    }
}
