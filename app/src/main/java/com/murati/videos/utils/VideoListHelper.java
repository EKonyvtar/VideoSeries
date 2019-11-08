package com.murati.videos.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.murati.videos.R;
import com.murati.videos.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class VideoListHelper {
    private final static String TAG = "OFFLINE";

    //JQ [.items[]|[.snippet.resourceId.videoId,.snippet.title,.snippet.thumbnails.standard.url]]
    public static List<Video> getVideoList(Context c, List<Video> VideoList) {
        //List<Video> VideoList = new ArrayList<>();
        try {
            JSONObject jsonObj = fetchJSON(c);
            if (jsonObj != null) {
                JSONArray jsonTracks = jsonObj.getJSONArray("playlist");

                if (jsonTracks != null) {
                    for (int j = 0; j < jsonTracks.length(); j++) {
                        VideoList.add(
                            new Video(
                                jsonTracks.getJSONArray(j).getString(1).
                                        replace(c.getString(R.string.content_strip),""),
                                jsonTracks.getJSONArray(j).getString(0)
                            )
                        );
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not retrieve videoList", e);
        }

        return VideoList;
    }

    private static JSONObject fetchJSON(Context c) throws JSONException {
        JSONObject json = null;

        try {
            String jsonStr = ConfigHelper.getConfig().getString("youtube_playlist_items");
            if (! TextUtils.isEmpty(jsonStr))
                json = new JSONObject(jsonStr);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        if (json != null)
            return json;


        //Else continue with written config
        //TODO: remove later
        BufferedReader reader = null;
        try {
            InputStream is = c.getResources().openRawResource(R.raw.playlist);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return new JSONObject(writer.toString());
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            Log.e("ListObject", "fetchJSON: ",e );
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
