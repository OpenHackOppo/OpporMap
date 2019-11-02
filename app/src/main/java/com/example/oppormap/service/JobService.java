package com.example.oppormap.service;

import com.example.oppormap.model.entity.JobPosting;

import java.util.Set;

public interface JobService {
    /**
     * Find jobs nearby
     * @param latitude Latitude
     * @param longitude Longitude
     * @param radius Radius in KM
     * @param skills Skills to search for
     * @return A collection of found jobs nearby
     */
    Set<JobPosting> findJobsNearby(double latitude, double longitude, int radius, Set<String> skills);
}
