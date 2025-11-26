package com.mutantes.mutantdetector.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class DnaRequest implements Serializable {
    private String[] dna;
}
