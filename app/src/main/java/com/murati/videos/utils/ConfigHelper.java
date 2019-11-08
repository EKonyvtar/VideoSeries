package com.murati.videos.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.murati.videos.R;

public class ConfigHelper {
    private static String TAG = "RCONFIG";

    private static FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static FirebaseRemoteConfig getConfig() {
        return  mFirebaseRemoteConfig;
    }

    public static void UpdateConfig(final Activity a) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1800)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(a, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);

                        } else {
                            Log.d(TAG, "Config update failed.");
                        }
                        //displayWelcomeMessage();
                    }
                });
    }
}
