package com.mutantes.mutantdetector.controller;

import com.mutantes.mutantdetector.dto.StatsResponse;
import com.mutantes.mutantdetector.service.MutantService;
import com.mutantes.mutantdetector.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    void testDetectMutantReturns200() throws Exception {
        // Simulamos que el servicio dice que SI es mutante
        when(mutantService.analyzeDna(any())).thenReturn(true);

        String jsonBody = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void testDetectHumanReturns403() throws Exception {
        // Simulamos que el servicio dice que NO es mutante
        when(mutantService.analyzeDna(any())).thenReturn(false);

        String jsonBody = "{\"dna\":[\"ATGCGA\"]}";

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void testStatsReturnsJson() throws Exception {
        // Simulamos la respuesta de estadísticas
        StatsResponse mockStats = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(mockStats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}