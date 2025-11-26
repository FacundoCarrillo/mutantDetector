package com.mutantes.mutantdetector.service;

import com.mutantes.mutantdetector.model.DnaRecord;
import com.mutantes.mutantdetector.repository.DnaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void testMutantHorizontal() {
        String[] dna = {
                "AAAA",  // Secuencia horizontal
                "CCCC",  // Secuencia horizontal (ya van 2)
                "TCAG",
                "GGTC"
        };
        // Simulamos que no existe en BD para que corra el algoritmo
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());

        assertTrue(mutantService.analyzeDna(dna));
    }

    @Test
    void testMutantVertical() {
        String[] dna = {
                "ATCG",
                "ATCG",
                "ATCG",
                "ATCG"   // A, T, C y G verticales
        };
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());

        assertTrue(mutantService.analyzeDna(dna));
    }

    @Test
    void testMutantDiagonal() {
        String[] dna = {
                "ATCG",
                "GATC",
                "CGAT",
                "TCGA"   // Diagonal AAAA
        };
        // Nota: Necesitamos mas de 1 secuencia. Agreguemos otra diagonal inversa
        String[] dna2 = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());

        assertTrue(mutantService.analyzeDna(dna2));
    }

    @Test
    void testHuman() {
        String[] dna = {
                "AAAT",
                "AACC",
                "AAAC",
                "CGGG"
        };
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());

        assertFalse(mutantService.analyzeDna(dna));
    }

    @Test
    void testAlreadyExistingDna() {
        // Probamos que si ya existe en BD, devuelve el valor guardado sin recalcular
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);

        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.of(existingRecord));

        assertTrue(mutantService.analyzeDna(dna));
    }
}