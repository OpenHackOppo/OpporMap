package com.example.oppormap.service.impl;

import com.example.oppormap.model.entity.Employer;
import com.example.oppormap.model.entity.JobPosting;
import com.example.oppormap.service.JobService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class JobTechJobService implements JobService {
    private static final String ENDPOINT = "https://jobsearch.api.jobtechdev.se/search";
    private static final String API_KEY = "ZXIueW91c2VmaUBnbWFpbC5jb20";

    @Override
    public Set<JobPosting> findJobsNearby(double latitude, double longitude, int radius, Set<String> skills) {
        String location = String.valueOf(latitude) + ',' + longitude;
        HttpResponse<JsonNode> response = Unirest
                .get(ENDPOINT)
                .queryString("offset", 0)
                .queryString("limit", 30)
                .queryString("position", location)
                .queryString("position.radius", radius)
                .header("api-key", API_KEY)
                .asJson();
        return buildResult(response.getBody());
    }

    private Set<JobPosting> buildResult(JsonNode response) {
        try {
            Set<JobPosting> postings = new HashSet<>();
            JSONArray hits = response.getObject().getJSONArray("hits");
            for (Object hit : hits) {
                JobPosting posting = buildPosting((JSONObject) hit);
                if (posting != null) {
                    postings.add(posting);
                }
            }
            return postings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private JobPosting buildPosting(JSONObject posting) {
        try {
            Employer employer = new Employer();
            employer.setName(posting.getJSONObject("employer").optString("name"));
            employer.setWebsite(posting.getJSONObject("employer").optString("url"));
            double lat = posting.getJSONObject("workplace_address").getJSONArray("coordinates").optDouble(0);
            double lon = posting.getJSONObject("workplace_address").getJSONArray("coordinates").optDouble(1);
            employer.setLatitude(lat);
            employer.setLongitude(lon);
            employer.setAddress(posting.getJSONObject("workplace_address").optString("street_address"));
            JobPosting jobPosting = new JobPosting();
            jobPosting.setEmployer(employer);
            jobPosting.setDescription(posting.getJSONObject("description").optString("text"));
            jobPosting.setId(posting.optString("id"));
            jobPosting.setUrl(posting.optString("webpage_url"));
            return jobPosting;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
