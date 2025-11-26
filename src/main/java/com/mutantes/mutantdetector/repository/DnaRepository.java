package com.mutantes.mutantdetector.repository;

import com.mutantes.mutantdetector.model.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Extiende JpaRepository para tener métodos como save(), findAll(), etc. [cite: 103]
public interface DnaRepository extends JpaRepository<DnaRecord, Long> {

    // Metodo mágico de Spring Data para buscar por la secuencia de ADN
    // Esto nos servirá para verificar si ya analizamos este ADN antes
    Optional<DnaRecord> findByDna(String dna);
    // AGREGAR ESTE METODO: Cuenta registros filtrando por si es mutante o no
    long countByIsMutant(boolean isMutant);
}