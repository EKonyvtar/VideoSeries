package com.murati.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

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
        holder.title.setText(VideoList.get(position).getTitle());
        //holder.author.setText(VideoList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return VideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;

        public VideoViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text);
            //author = (TextView) view.findViewById(R.id.author);
        }
    }
}
