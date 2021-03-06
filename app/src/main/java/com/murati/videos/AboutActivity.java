/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.murati.videos;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.murati.videos.utils.AdHelper;

/**
 * Placeholder activity for features that are not implemented in this sample, but
 * are in the navigation drawer.
 */
public class AboutActivity extends AppCompatActivity {
    static final String TAG = "ABOUT";
    private AdView mAdView;

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Version info
        final TextView versionText = findViewById(R.id.version);
        versionText.setText(getVersion());

        final Button privacy = findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { openBrowser(getString(R.string.about_privacy)); }
        });
        
        // Button
        final Button patreon = findViewById(R.id.patreon);
        patreon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { openBrowser("https://www.patreon.com/murati");}
        });

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

    public String getVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //TODO: capture log
        }
        return "";
    }
}
