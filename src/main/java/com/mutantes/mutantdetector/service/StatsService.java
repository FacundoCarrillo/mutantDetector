package com.mutantes.mutantdetector.service;

import com.mutantes.mutantdetector.dto.StatsResponse;
import com.mutantes.mutantdetector.repository.DnaRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final DnaRepository dnaRepository;

    public StatsService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public StatsResponse getStats() {
        long countMutant = dnaRepository.countByIsMutant(true);
        long countHuman = dnaRepository.countByIsMutant(false); // Humanos son los que isMutant = false
        double ratio = 0;

        if (countHuman > 0) {
            ratio = (double) countMutant / countHuman;
        } else if (countMutant > 0) {
            // Si hay mutantes pero 0 humanos, el ratio matemáticamente es infinito,
            // pero por convención podemos ponerlo igual a la cantidad de mutantes o 1.
            // Aquí lo dejaremos como el total de mutantes para evitar división por cero.
            ratio = countMutant;
        }

        return new StatsResponse(countMutant, countHuman, ratio);
    }
}