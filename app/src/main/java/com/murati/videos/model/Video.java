package com.murati.videos.model;

public class Video {

    private String title;
    private String videoId;
    private String thumbnailImage;
    private int startAt = 0;

    public Video(String title, String videoId, String thumbnailImage) {
        this.title = title;
        this.videoId = videoId;
        this.thumbnailImage = thumbnailImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getVideoId() {
        return videoId;
    }

    public int getStartAt() {
        return startAt;
    }
    public void setStartAt(int startAt) {
         this.startAt = startAt;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}