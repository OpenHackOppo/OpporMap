package com.example.oppormap;

import java.util.Map;

public interface OpporApi {
    Map<String, String> getJobsNearby(double latitude, double longitude, int radius);
}
