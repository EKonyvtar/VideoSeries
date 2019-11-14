package com.murati.videos.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.perf.metrics.AddTrace;
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
    public static List<Video> convertVideoList(Context c, JSONObject jsonObj, List<Video> VideoList) {
        //List<Video> VideoList = new ArrayList<>();
        try {
            if (jsonObj != null) {
                JSONArray jsonTracks = jsonObj.getJSONArray("playlist");

                if (jsonTracks != null) {
                    for (int j = 0; j < jsonTracks.length(); j++) {
                        //Shitty array-schema --> TODO: fix new schema
                        try {
                            Video v = new Video(
                                    jsonTracks.getJSONArray(j).getString(1).
                                            replace(c.getString(R.string.content_strip), ""),
                                    jsonTracks.getJSONArray(j).getString(0),
                                    jsonTracks.getJSONArray(j).getString(2)
                            );
                            try {
                                v.setStartTime(Integer.parseInt(jsonTracks.getJSONArray(j).getString(3)));
                            } catch (Exception ex) {
                                //TODO
                            }
                            VideoList.add(v);


                        } catch (Exception ex) {
                            Log.e(TAG, "Error parsing item " + j);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse videolist", e);
        }

        return VideoList;
    }

    @AddTrace(name = "getVideoList", enabled = true /* optional */)
    public static List<Video> getVideoList(Context c, List<Video> VideoList) {
        try {
            convertVideoList(c, getRemoteJSON(c), VideoList);
        } catch (Exception ex) {
            Log.e(TAG, "Failed to fetch remote config" + ex.getMessage());
        }

        if (!VideoList.isEmpty()) return VideoList;

        try {
            convertVideoList(c, getDefaultJSON(c), VideoList);
        } catch (Exception ex) {
            Log.e(TAG, "Fatal.. Unable to fetch default config" + ex.getMessage());
        }
        return VideoList;
    }


    private static JSONObject getRemoteJSON(Context c) throws JSONException {
        return new JSONObject(
                ConfigHelper.getConfig().
                        getString("youtube_playlist_items")
        );
    }

    private static JSONObject getDefaultJSON(Context c) throws JSONException {
        //TODO: replace with default values
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
        } catch (Exception e) {
            Log.e("ListObject", "fetchJSON: ", e);
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
