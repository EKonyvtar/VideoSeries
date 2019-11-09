package com.murati.videos.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.murati.videos.R;

public class AdHelper {
    public static void InitializeAd(Context c, String AppId, AdView mAdView, String SourceTag) {
        try {
            Bundle extras = new Bundle();
            MobileAds.initialize(c, AppId);
            extras.putString("max_ad_content_rating", "G");
            AdRequest adRequest = new AdRequest
                    .Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .tagForChildDirectedTreatment(true)
                    .build();
            mAdView.loadAd(adRequest);
        } catch (Exception ex) {
            Log.e(SourceTag, ex.getMessage());
        }
    }
}
