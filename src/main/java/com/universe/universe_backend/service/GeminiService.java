package com.universe.universe_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class GeminiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.model}")
    private String model;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public String generate(String prompt) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model + ":generateContent?key=" + apiKey;

            String requestBody = mapper.writeValueAsString(Map.of(
                    "contents", java.util.List.of(
                            Map.of("parts", java.util.List.of(Map.of("text", prompt)))
                    )
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(20))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Gemini API error: " + response.statusCode() + " - " + response.body());
            }

            JsonNode root = mapper.readTree(response.body());
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage(), e);
        }
    }
}