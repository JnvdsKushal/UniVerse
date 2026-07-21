package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.ai.GenerateListingRequest;
import com.universe.universe_backend.dto.ai.GenerateListingResponse;
import com.universe.universe_backend.service.AiListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.universe.universe_backend.dto.ai.AiSearchResponse;
import com.universe.universe_backend.dto.ai.AiSearchRequest;
import com.universe.universe_backend.service.AiSearchService;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiListingService aiListingService;

    @PostMapping("/generate-listing")
    public ResponseEntity<GenerateListingResponse> generateListing(@Valid @RequestBody GenerateListingRequest req) {
        return ResponseEntity.ok(aiListingService.generate(req));
    }
    
    private final AiSearchService aiSearchService;
    
    @PostMapping("/search-products")
    public ResponseEntity<AiSearchResponse> aiSearch(@Valid @RequestBody AiSearchRequest req) {
        return ResponseEntity.ok(aiSearchService.search(req));
    }
}