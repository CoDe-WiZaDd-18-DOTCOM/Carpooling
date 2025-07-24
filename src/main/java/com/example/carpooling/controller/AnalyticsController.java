package com.example.carpooling.controller;

import com.example.carpooling.entities.Ride;
import com.example.carpooling.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Endpoints for Analysis.")
public class AnalyticsController {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);
    @Autowired
    private AnalyticsService analyticsService;

    @Operation(summary = "Get Analytics", description = "Get Analytics")
    @GetMapping
    public ResponseEntity<?> getAnalytics() {
        try {
            return new ResponseEntity<>(analyticsService.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
