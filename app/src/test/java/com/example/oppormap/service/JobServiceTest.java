package com.example.oppormap.service;

import com.example.oppormap.service.impl.JobTechJobService;

import org.junit.Test;

import java.util.Collections;

public class JobServiceTest {
    @Test
    public void test() {
        JobService jobService = new JobTechJobService();
        System.out.println(jobService.findJobsNearby(59.3293, 18.0686, 20, Collections.emptySet()));
    }
}
