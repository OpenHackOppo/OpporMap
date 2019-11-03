package com.example.oppormap.service.impl;

import com.example.oppormap.model.entity.Employer;
import com.example.oppormap.model.entity.JobPosting;
import com.example.oppormap.service.JobService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mashape.unirest.http.Unirest;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;


public class JobTechJobService implements JobService {
    private static final String ENDPOINT = "https://jobsearch.api.jobtechdev.se/search";
    private static final String API_KEY = "ZXIueW91c2VmaUBnbWFpbC5jb20";
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public Future<Set<JobPosting>> findJobsNearbyAsync(double latitude, double longitude, int radius) {
        Future<Set<JobPosting>> postings = executorService.submit(() -> {
            try {
                String location = String.valueOf(latitude) + ',' + longitude;
                String path = MessageFormat.format("?offset={0}&limit={1}&position={2}&position.radius={3}", 0, 50, location, radius);
                String endpoint = ENDPOINT + path;
                String response = Unirest
                        .get(endpoint)
                        .header("api-key", API_KEY)
                        .asString()
                        .getBody();
                JsonElement json = new JsonParser().parse(response);
                return buildResult(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Collections.emptySet();
        });
        return postings;
    }


    private Set<JobPosting> buildResult(JsonElement jsonElement) {
        try {
            Set<JobPosting> postings = new HashSet<>();
            JsonArray hits = jsonElement.getAsJsonObject().getAsJsonArray("hits");
            for (JsonElement hit : hits) {
                JobPosting posting = buildPosting(hit.getAsJsonObject());
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

    private JobPosting buildPosting(JsonObject json) {
        try {
            JsonObject company = json.getAsJsonObject("employer");
            JsonObject workplace = json.getAsJsonObject("workplace_address");
            String name = nullSafeGet(company.get("name"), JsonPrimitive::getAsString);
            String url = nullSafeGet(company.get("url"), JsonPrimitive::getAsString);
            Double lat = nullSafeGet(workplace.getAsJsonArray("coordinates").get(1), JsonPrimitive::getAsDouble);
            Double lon = nullSafeGet(workplace.getAsJsonArray("coordinates").get(0), JsonPrimitive::getAsDouble);
            String address = nullSafeGet(workplace.get("workplace_address"), JsonPrimitive::getAsString);
            String description = nullSafeGet(json.get("description").getAsJsonObject().get("text"), JsonPrimitive::getAsString);
            String label = nullSafeGet(json.get("occupation").getAsJsonObject().get("label"), JsonPrimitive::getAsString);
            String id = nullSafeGet(json.get("id"), JsonPrimitive::getAsString);
            String web = nullSafeGet(json.get("webpage_url"), JsonPrimitive::getAsString);
            Employer employer = new Employer();
            employer.setName(name);
            employer.setWebsite(url);
            employer.setLatitude(lat);
            employer.setLongitude(lon);
            employer.setAddress(address);
            JobPosting jobPosting = new JobPosting();
            jobPosting.setEmployer(employer);
            jobPosting.setDescription(description);
            jobPosting.setTitle(label);
            jobPosting.setId(id);
            jobPosting.setUrl(web);
            return jobPosting;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> T nullSafeGet(JsonElement element, Function<JsonPrimitive, T> function) {
        if (element == null || element.isJsonNull()) {
            return null;
        } else {
            return function.apply(element.getAsJsonPrimitive());
        }
    }
}
