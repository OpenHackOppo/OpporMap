package com.example.oppormap.listener;

import android.content.SharedPreferences;
import android.location.Location;

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
        user.setLatitude(result.getLastLocation().getLatitude());
        user.setLongitude(result.getLastLocation().getLongitude());
        updateCachedUser(user);
        MainActivity activity = activityWeakReference.get();

        if (activity != null) {
            Location location = result.getLastLocation();

            if (location == null) {
                return;
            }

/*// Create a Toast which displays the new location's coordinates
                Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
                        String.valueOf(result.getLastLocation().getLatitude()),
                        String.valueOf(result.getLastLocation().getLongitude())),
                        Toast.LENGTH_SHORT).show();*/

// Pass the new location to the Maps SDK's LocationComponent
            if (activity.getMapboxMap() != null && result.getLastLocation() != null) {
                activity.getMapboxMap().getLocationComponent().forceLocationUpdate(result.getLastLocation());
            }
        }
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
