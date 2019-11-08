package com.murati.videos.utils;

import com.murati.videos.model.Video;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;


import java.util.List;


// TODO implement later
public class YoutubeHelper {

    //JQ [.items[]|[.snippet.resourceId.videoId,.snippet.title,.snippet.thumbnails.standard.url]]
    // https://developers.google.com/youtube/v3/docs/playlistItems/list?apix_params=%7B%22part%22%3A%22id%2Csnippet%22%2C%22playlistId%22%3A%22PLg_AyQoUGXhzMQC7weZTEHBlR6Mb98c89%22%7D
    public static List<Video> getVideosByPlaylist(String playlistId) {

        //curl \
        //'https://www.googleapis.com/youtube/v3/playlistItems?part=id%2Csnippet&maxResults=50&playlistId=PLg_AyQoUGXhzMQC7weZTEHBlR6Mb98c89&key=[YOUR_API_KEY]' \
        //--header 'Authorization: Bearer [YOUR_ACCESS_TOKEN]' \
        //--header 'Accept: application/json' \
        //--compressed


        //YouTube youtubeService = getService();
        // Define and execute the API request
        //YouTube.PlaylistItems.List request = youtubeService.playlistItems()
        //        .list("id,snippet");
        //PlaylistItemListResponse response = request.setMaxResults(50L)
        //        .setPlaylistId("PLg_AyQoUGXhzMQC7weZTEHBlR6Mb98c89")
        //        .execute();
        //System.out.println(response);
        return  null;
    }
}