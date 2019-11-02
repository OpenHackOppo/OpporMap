package com.example.oppormap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class DefaultOpporApi implements OpporApi {
    private static final String ENDPOINT = "https://jobsearch.api.jobtechdev.se/search?offset={0}&limit={1}&position={2},{3}&position.radius={4}";
    private static final String API_KEY = "YW50b24uZWhsZXJ0QGdtYWlsLmNvbQ";

    @Override
    public Map<String, String> getJobsNearby(double latitude, double longitude, int radius) {
        String endpoint = MessageFormat.format(ENDPOINT, 0, 30, latitude, longitude, radius);
        JSONObject json = performRequest(endpoint);
        Map<String, String> response = new HashMap<>();
        response.put("result", json.toString());
        return response;
    }

    private JSONObject performRequest(String endpoint) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("api-key", API_KEY);
            //connection.connect();
            InputStream inputStream = connection.getInputStream();

            InputStreamReader in = new InputStreamReader(inputStream);
            BufferedReader buff = new BufferedReader(in);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = buff.readLine()) != null) {
                line = buff.readLine();
                sb.append(line).append("\n");
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
