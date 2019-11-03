package com.example.oppormap.listener;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.oppormap.MainActivity;
import com.example.oppormap.model.entity.User;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;

import java.lang.ref.WeakReference;

public class LocationEngineListener implements LocationEngineCallback<LocationEngineResult> {
    private final WeakReference<MainActivity> activityWeakReference;

    public LocationEngineListener(MainActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        User user = getCachedUser();
        if (user == null) {
            return;
        }
        user.setLocation(String.valueOf(result.getLastLocation().getLatitude()) + result.getLastLocation().getLongitude());
        updateCachedUser(user);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }

    private User getCachedUser() {
        SharedPreferences sharedPreferences = activityWeakReference.get().getSharedPreferences();
        String user = sharedPreferences.getString("user", null);
        if (user == null) {
            return null;
        }
        return activityWeakReference.get().getGson().fromJson(user, User.class);
    }

    private void updateCachedUser(User user) {
        MainActivity mainActivity = activityWeakReference.get();
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", mainActivity.getGson().toJson(user));
        editor.apply();
    }
}
