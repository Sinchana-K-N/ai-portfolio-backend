package com.sinchana.ai_portfolio_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService {

    private final String apiKey = System.getenv("GEMINI_API_KEY");

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String askGemini(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Gemini API key not configured");
        }

        String payload = """
        {
          "contents": [
            {
              "parts": [
                { "text": "%s" }
              ]
            }
          ],
          "generationConfig": {
            "temperature": 0.7,
            "maxOutputTokens": 150
          }
        }
        """.formatted(prompt.replace("\"", "\\\""));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return extractAnswer(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API", e);
        }
    }

    private String extractAnswer(String jsonResponse) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            return root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText("No response from AI");

        } catch (Exception e) {
            return "Error parsing AI response";
        }
    }
}
