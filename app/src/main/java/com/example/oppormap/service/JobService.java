package com.example.oppormap.service;

import com.example.oppormap.model.entity.JobPosting;

import java.util.Set;
import java.util.concurrent.Future;

public interface JobService {

    Future<Set<JobPosting>> findJobsNearbyAsync(double latitude, double longitude, int radius);
}
