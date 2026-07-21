package com.universe.universe_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.universe_backend.dto.ai.AiSearchRequest;
import com.universe.universe_backend.dto.ai.AiSearchResponse;
import com.universe.universe_backend.dto.product.ProductResponse;
import com.universe.universe_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AiSearchService {

    private final GeminiService geminiService;
    private final ProductRepository productRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public AiSearchResponse search(AiSearchRequest request) {
        String prompt = """
            A student is searching a campus marketplace with this query: "%s"

            Extract search filters as JSON only, no markdown, no code fences, in exactly this shape:
            {"keyword": "string or null", "minPrice": number or null, "maxPrice": number or null, "interpretedAs": "one sentence summary"}

            Rules:
            - keyword should be the core product/item being searched for (e.g. "textbook", "bike", "calculator"), or null if too vague.
            - minPrice/maxPrice should be null unless the query implies a price constraint (e.g. "under $30" means maxPrice=30, "cheap" implies no specific number so leave null).
            - interpretedAs should read naturally, e.g. "Searching for textbooks under $30".
            """.formatted(request.getQuery());

        String rawResponse = geminiService.generate(prompt);

        try {
            String cleaned = rawResponse.trim()
                    .replaceAll("^```json", "")
                    .replaceAll("^```", "")
                    .replaceAll("```$", "")
                    .trim();

            JsonNode json = mapper.readTree(cleaned);

            String keyword = json.path("keyword").isNull() ? null : json.path("keyword").asText(null);
            BigDecimal minPrice = json.path("minPrice").isNull() ? null : new BigDecimal(json.path("minPrice").asText());
            BigDecimal maxPrice = json.path("maxPrice").isNull() ? null : new BigDecimal(json.path("maxPrice").asText());
            String interpretedAs = json.path("interpretedAs").asText("Searching for: " + request.getQuery());

            Page<ProductResponse> results = productRepository
                    .search(null, minPrice, maxPrice, keyword, PageRequest.of(0, 20))
                    .map(p -> new ProductResponse(
                            p.getId(), p.getTitle(), p.getDescription(), p.getPrice(),
                            p.getCondition(), p.getListingType(), p.getStatus(),
                            p.getCategory() != null ? p.getCategory().getName() : null,
                            p.getSeller().getFullName(),
                            p.getImages().stream().map(com.universe.universe_backend.entity.ProductImage::getUrl)
                                    .collect(java.util.stream.Collectors.toList()),
                            p.getCreatedAt()
                    ));

            return new AiSearchResponse(interpretedAs, results);

        } catch (Exception e) {
            throw new RuntimeException("Could not parse AI search response: " + rawResponse, e);
        }
    }
}