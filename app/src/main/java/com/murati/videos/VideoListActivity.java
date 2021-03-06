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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.perf.metrics.AddTrace;
import com.murati.videos.adapter.VideoItemListener;
import com.murati.videos.adapter.VideoAdapter;
import com.murati.videos.model.Video;
import com.murati.videos.utils.AdHelper;
import com.murati.videos.utils.DeveloperKey;
import com.murati.videos.utils.VideoListHelper;

import java.util.ArrayList;
import java.util.List;

import static com.murati.videos.utils.ConfigHelper.UpdateConfig;

public class VideoListActivity extends AppCompatActivity {

    private List<Video> VideoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VideoAdapter mAdapter;

    static final String TAG = "VIDMAIN";
    private AdView mAdView;

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    @AddTrace(name = "onCreateVideoList", enabled = true /* optional */)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Update remote config
        UpdateConfig(this);

        //Start inflating
        setContentView(R.layout.activity_videolist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new VideoAdapter(VideoList);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

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

                                int startTime = vid.getStartAt() * 1000;
                                boolean autoplay = true;
                                boolean lightboxMode = false;

                                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                                        (VideoListActivity)v.getContext(),
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


        //Advertisement
        try {
            mAdView = findViewById(R.id.adView);
            AdHelper.InitializeAd(
                    this.getApplicationContext(),
                    getString(R.string.admob_app_id),
                    mAdView,
                    TAG);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
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
       VideoList = VideoListHelper.getVideoList(c, VideoList);
       mAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_about) {
            Intent i = new Intent(VideoListActivity.this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
