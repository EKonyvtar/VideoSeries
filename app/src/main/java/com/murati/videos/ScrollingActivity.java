package com.murati.videos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.murati.videos.adapter.VideoItemListener;
import com.murati.videos.adapter.VideoAdapter;
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

public class ScrollingActivity extends AppCompatActivity {

    private List<Video> VideoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VideoAdapter mAdapter;

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new VideoAdapter(VideoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new VideoItemListener(
                        getApplicationContext(),
                        recyclerView,
                        new VideoItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                System.out.println("On Click Item interface");
                                Video vid = VideoList.get(position);
                                String videoId = vid.getVideoId();

                                int startTime = 0;
                                boolean autoplay = true;
                                boolean lightboxMode = false;

                                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                                        (ScrollingActivity)v.getContext(),
                                        DeveloperKey.DEVELOPER_KEY,
                                        videoId, startTime, autoplay, lightboxMode);

                                startActivity(intent);
                            }

                            public void onLongClickItem(View v, int position) {
                                System.out.println("On Long Click Item interface");
                            }
                        }
                )
        );


        initVideoData(this.getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        checkYouTubeApi();
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void initVideoData(Context c) {
        //VideoList = new ArrayList<>();
        try {
            JSONObject jsonObj = fetchJSON(c);
            if (jsonObj != null) {
                JSONArray jsonTracks = jsonObj.getJSONArray("playlist");

                if (jsonTracks != null) {
                    for (int j = 0; j < jsonTracks.length(); j++) {
                        VideoList.add(
                                new Video(
                                        jsonTracks.getJSONArray(j).getString(1),
                                        jsonTracks.getJSONArray(j).getString(0)
                                )
                        );
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not retrieve videoList", e);
        }

        mAdapter.notifyDataSetChanged();
    }

    private  JSONObject fetchJSON(Context c) throws JSONException {
        //TODO: pass as config
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
