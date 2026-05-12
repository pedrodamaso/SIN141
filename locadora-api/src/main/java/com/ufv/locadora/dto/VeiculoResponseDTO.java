package com.ufv.locadora.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VeiculoResponseDTO {
    private Long id;
    private String tipo;
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private BigDecimal valorDiaria;
    private boolean disponivel;
    // Carro
    private Integer numeroPortas;
    private String tipoCombustivel;
    // Moto
    private Integer cilindradas;
}
