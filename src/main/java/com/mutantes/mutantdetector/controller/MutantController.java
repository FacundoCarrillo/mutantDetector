package com.mutantes.mutantdetector.controller;

import com.mutantes.mutantdetector.dto.DnaRequest;
import com.mutantes.mutantdetector.dto.StatsResponse;
import com.mutantes.mutantdetector.service.MutantService;
import com.mutantes.mutantdetector.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/") // La ruta base puede ser raíz
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    // NIVEL 2: Endpoint /mutant/
    @PostMapping("/mutant/")
    public ResponseEntity<Void> detectMutant(@RequestBody DnaRequest dnaRequest) {
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());

        if (isMutant) {
            // Si es mutante -> HTTP 200 OK
            return ResponseEntity.ok().build();
        } else {
            // Si NO es mutante -> HTTP 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // NIVEL 3: Endpoint /stats
    @GetMapping("/stats")
    public StatsResponse getStats() {
        return statsService.getStats();
    }
}
