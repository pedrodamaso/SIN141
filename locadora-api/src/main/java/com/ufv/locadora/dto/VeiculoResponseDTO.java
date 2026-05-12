package com.ufv.locadora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
