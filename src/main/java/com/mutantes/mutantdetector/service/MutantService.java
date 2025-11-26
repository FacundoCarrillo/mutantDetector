package com.mutantes.mutantdetector.service;

import com.mutantes.mutantdetector.dto.DnaRequest;
import com.mutantes.mutantdetector.model.DnaRecord;
import com.mutantes.mutantdetector.repository.DnaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MutantService {

    private final DnaRepository dnaRepository;

    // Constante para la cantidad de letras iguales necesarias
    private static final int SEQUENCE_LENGTH = 4;

    public MutantService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    // --- METODO PRINCIPAL QUE LLAMARA EL CONTROLLER ---
    public boolean analyzeDna(String[] dna) {
        // 1. Convertimos el array a String para buscarlo en BD
        String dnaString = String.join(",", dna);

        // 2. Verificamos si ya existe en base de datos (Nivel 3)
        Optional<DnaRecord> existingDna = dnaRepository.findByDna(dnaString);
        if (existingDna.isPresent()) {
            // Si ya lo analizamos antes, devolvemos el resultado guardado
            return existingDna.get().isMutant();
        }

        // 3. Si es nuevo, corremos el algoritmo (Nivel 1)
        boolean isMutant = isMutant(dna);

        // 4. Guardamos el resultado en BD
        DnaRecord dnaRecord = new DnaRecord();
        dnaRecord.setDna(dnaString);
        dnaRecord.setMutant(isMutant);
        dnaRepository.save(dnaRecord);

        return isMutant;
    }

    // --- ALGORITMO DE DETECCION (NIVEL 1) ---
    public boolean isMutant(String[] dna) {
        // Validaciones básicas (N x N y caracteres válidos)
        if (dna == null || dna.length == 0) return false;
        int n = dna.length;

        // Validación rápida de tamaño (NxN) y caracteres
        for (String row : dna) {
            if (row.length() != n) return false; // No es cuadrada
            if (!row.matches("[ATCG]+")) return false; // Letras invalidas [cite: 81]
        }

        int sequenceCount = 0;

        // Recorremos la matriz
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Solo verificamos si no hemos encontrado ya más de 1 secuencia (Optimización)
                if (sequenceCount > 1) return true;

                char currentChar = dna[i].charAt(j);

                // 1. Horizontal (Solo si hay espacio a la derecha)
                if (j + SEQUENCE_LENGTH <= n) {
                    if (checkLine(dna, i, j, 0, 1, currentChar)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // 2. Vertical (Solo si hay espacio hacia abajo)
                if (i + SEQUENCE_LENGTH <= n) {
                    if (checkLine(dna, i, j, 1, 0, currentChar)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // 3. Diagonal Principal (\) (Solo si hay espacio abajo y derecha)
                if (i + SEQUENCE_LENGTH <= n && j + SEQUENCE_LENGTH <= n) {
                    if (checkLine(dna, i, j, 1, 1, currentChar)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // 4. Diagonal Secundaria (/) (Solo si hay espacio abajo y izquierda)
                if (i + SEQUENCE_LENGTH <= n && j - SEQUENCE_LENGTH + 1 >= 0) {
                    if (checkLine(dna, i, j, 1, -1, currentChar)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }

        // Es mutante si encontramos más de 1 secuencia
        return sequenceCount > 1;
    }

    // Metodo auxiliar genérico para verificar 4 letras consecutivas
    // deltaRow: cuánto me muevo en filas
    // deltaCol: cuánto me muevo en columnas
    private boolean checkLine(String[] dna, int row, int col, int deltaRow, int deltaCol, char match) {
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[row + k * deltaRow].charAt(col + k * deltaCol) != match) {
                return false;
            }
        }
        return true;
    }
}