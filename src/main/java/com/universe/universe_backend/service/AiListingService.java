package com.universe.universe_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.universe_backend.dto.ai.GenerateListingRequest;
import com.universe.universe_backend.dto.ai.GenerateListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AiListingService {

    private final GeminiService geminiService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GenerateListingResponse generate(GenerateListingRequest req) {
        String prompt = """
            You are helping a university student write a marketplace listing.
            Given this rough description: "%s"
            Category (if provided): "%s"

            Respond ONLY with valid JSON, no markdown, no code fences, in exactly this shape:
            {"suggestedTitle": "...", "polishedDescription": "...", "suggestedPrice": 0.00, "priceReasoning": "..."}

            The title should be short and clear. The description should be 2-3 honest sentences,
            no exaggeration. The suggestedPrice should be a realistic used-item price in USD for a
            student marketplace, as a plain number. priceReasoning should be one short sentence.
            """.formatted(req.getRoughDescription(), req.getCategory() != null ? req.getCategory() : "unspecified");

        String rawResponse = geminiService.generate(prompt);

        try {
            String cleaned = rawResponse.trim()
                    .replaceAll("^```json", "")
                    .replaceAll("^```", "")
                    .replaceAll("```$", "")
                    .trim();

            JsonNode json = mapper.readTree(cleaned);
            return new GenerateListingResponse(
                    json.path("suggestedTitle").asText(),
                    json.path("polishedDescription").asText(),
                    new BigDecimal(json.path("suggestedPrice").asText("0")),
                    json.path("priceReasoning").asText()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not parse AI response: " + rawResponse, e);
        }
    }
}