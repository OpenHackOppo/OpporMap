package com.example.oppormap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oppormap.listener.LocationEngineListener;
import com.example.oppormap.model.entity.Employer;
import com.example.oppormap.model.entity.JobPosting;
import com.example.oppormap.model.entity.User;
import com.example.oppormap.service.JobService;
import com.example.oppormap.service.UserService;
import com.example.oppormap.service.impl.DefaultUserService;
import com.example.oppormap.service.impl.JobTechJobService;
import com.google.gson.Gson;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Use the Mapbox Core Library to receive updates when the device changes location.
 */
public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private final UserService userService = new DefaultUserService();
    private final JobService jobService = new JobTechJobService();
    private final Gson gson = new Gson();
    private final LocationEngineCallback<LocationEngineResult> listener = new LocationEngineListener(this);
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;

    private BuildingPlugin buildingPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button btn = (Button)findViewById(R.id.open_activity_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ARActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        setupFakeProfile();
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle("Tester");
        Employer employer = new Employer();
        employer.setName("OpenHack");
        employer.setLatitude(59.3413674);
        employer.setLongitude(18.0618729);
        jobPosting.setEmployer(employer);
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                style -> {
                    enableLocationComponent(style);
                    buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
                    buildingPlugin.setMinZoomLevel(15f);
                    buildingPlugin.setVisibility(true);
                    MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                    publishPosting(markerViewManager, jobPosting);
                    publishPostingsOnMap(markerViewManager);

                });
    }

    public void publishPostingsOnMap(MarkerViewManager markerViewManager) {
        User user = getCurrentUser();
        Future<Set<JobPosting>> postings = jobService.findJobsNearbyAsync(user.getLatitude(), user.getLongitude(), 20);
        try {
            Set<JobPosting> posts = postings.get();
            posts.forEach(p -> publishPosting(markerViewManager, p));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void publishPosting(MarkerViewManager markerViewManager, JobPosting posting) {
        View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_view_bubble, null);
        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        TextView textView = customView.findViewById(R.id.mytext);
        textView.setText(posting.getTitle() + " @ " + posting.getEmployer().getName());
        customView.setOnClickListener(v -> {
            if (posting.getUrl() != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(posting.getUrl()));
                startActivity(browserIntent);
            }
        });
        MarkerView markerView = new MarkerView(new LatLng(posting.getEmployer().getLatitude(),posting.getEmployer().getLongitude()), customView);

        markerViewManager.addMarker(markerView);
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

// Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
        locationEngine.requestLocationUpdates(request, listener, getMainLooper());
        locationEngine.getLastLocation(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public JobService getJobService() {
        return jobService;
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(listener);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public User getCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String user = sharedPreferences.getString("user", null);
        if (user == null) {
            setupFakeProfile();
            return getCurrentUser();
        }
        User found = getGson().fromJson(user, User.class);
        found.setLatitude(59.3293);
        found.setLongitude(18.0686);
        return found;
    }

    public SharedPreferences getSharedPreferences() {
        return getApplication().getSharedPreferences("oppordb", MODE_PRIVATE);
    }

    private void setupFakeProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String user = sharedPreferences.getString("user", null);
        if (user != null) {
            return;
        }
        User fake = new User();
        fake.setName("Ruby Dumont");
        fake.setLatitude(59.3293);
        fake.setLongitude(18.0686);
        fake.setAddress("Hamngatan 10");
        //fake = userService.create(fake);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", getGson().toJson(fake));
        editor.apply();
    }
}